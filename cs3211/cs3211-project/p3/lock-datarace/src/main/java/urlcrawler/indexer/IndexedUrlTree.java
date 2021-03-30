package urlcrawler.indexer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import urlcrawler.data.Url;
import urlcrawler.scheduler.EventScheduler;

public class IndexedUrlTree {
    private enum UrlContentType {
        OK, BROKEN, IGNORED;
    }

    private final static Logger logger = LogManager.getLogger(IndexedUrlTree.class);

    private final static int MAX_LENGTH_FILENAME = 200;

    private File indexDirectory;
    private File contentDirectory;

    private Map<String, ReadWriteLock> lockCache;

    // Stores the slugs of in-progress URLs
    private final Set<String> inProgressWriting;

    public IndexedUrlTree(String indexDirPath, String contentDirPath, String outputFilePath) {
        indexDirectory = new File(indexDirPath);
        if (!indexDirectory.isDirectory()) {
            indexDirectory.mkdirs();
        }

        contentDirectory = new File(contentDirPath);
        if (!contentDirectory.isDirectory()) {
            contentDirectory.mkdirs();
        }

        inProgressWriting = new HashSet<>();
        lockCache = new HashMap<>();
    }

    /**
     * Write HTML content to correct location in disk. Note that: url must have been marked on the tree as being crawled
     * by checkAndMark()
     */
    void writeContents(Url url, int ibtId) {
        String fileName = url.getSlug();
        String dirName = url.getHashValue();

        File indexFile = Paths.get(indexDirectory.getAbsolutePath(), dirName).toFile();
        File contentFile = Paths.get(contentDirectory.getAbsolutePath(), dirName, fileName).toFile();

        UrlContentType type;
        if (url.isContentCrawled()) {
            if (fileName.length() > MAX_LENGTH_FILENAME || Url.getAndIncNumOfStoredPages() >= Url.maxNumOfStoredPages) {
                type = UrlContentType.IGNORED;
            } else {
                type = UrlContentType.OK;
                writeUrlContentToFile(url, contentFile);
            }
        } else {
            type = UrlContentType.BROKEN;
        }

        if (!EventScheduler.shared.requestPermission(
                "ibt_writeToIUT_success." + ibtId + "." + url.getId(),
                () -> writeUrlIndexToFile(url, indexFile, type)
        )) {
            logger.error("Unexpected event: ibt_writeToIUT_fail");
            return;
        }

        EventScheduler.shared.releasePermission();

        EventScheduler.shared.requestPermission(
                "ibt_removeFromIPW_success." + ibtId + "." + url.getId(),
                () -> inProgressWriting.remove(fileName)
        );

        EventScheduler.shared.releasePermission();
    }

    private void writeUrlContentToFile(Url url, File file) {
        if (!url.isContentCrawled()) {
            // no need to write anything if the content has not been crawled.
            // this happens when the url turns out to be a broken link.
            return;
        }

        try {
            // Create parent directory if it does not exist
            File parentDir = file.getParentFile();
            if (!file.getParentFile().isDirectory()) {
                parentDir.mkdirs();
            }

            // open file
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(url.getContent());

            // close file
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            logger.error("Error in writing HTML content", e);
        }
    }

    private synchronized ReadWriteLock getLock(String s) {
        if (lockCache.containsKey(s)) {
            return lockCache.get(s);
        }

        ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        lockCache.put(s, readWriteLock);
        return readWriteLock;
    }

    private void writeUrlIndexToFile(Url url, File file, UrlContentType type) {
        try {
            // open file
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter printWriter = new PrintWriter(bufferedWriter);

            switch (type) {
            case OK:
                printWriter.println(String.join(" ", url.getSlug(), url.getSpec(), url.getParentSpec()));
                break;
            case IGNORED:
                printWriter.println(String.join(" ", url.getSlug(), url.getSpec(), url.getParentSpec(), "IGNORED"));
                break;
            case BROKEN:
                printWriter.println(String.join(" ", url.getSlug(), url.getSpec(), url.getParentSpec(), "BROKEN"));
                break;
            }

            // close file
            printWriter.close();
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            logger.error("Error in writing index", e);
        }
    }

    /**
     * Check whether the url needs to be crawled or not. It returns true if the url has not been crawled and marked it
     * on this data structure that this url is being crawled. Otherwise, if it has ever been crawled, returns false.
     */
    public boolean checkAndMarkUrl(Url url, int crawlerId) {
        String fileName = url.getSlug();
        String dirName = url.getHashValue();
        File file = Paths.get(indexDirectory.getAbsolutePath(), dirName).toFile();

        AtomicBoolean urlExistInFile = new AtomicBoolean(false);

        int enabled = EventScheduler.shared.requestPermission(
                new String[]{
                        "ct_urlInIUT." + crawlerId + "." + url.getId(),
                        "ct_urlNotInIUT." + crawlerId + "." + url.getId()
                },
                () -> urlExistInFile.set(existsInFile(url, file))
        );

        if (enabled == -1) {
            return false; // terminate if never enabled
        }

        assert enabled == (urlExistInFile.get() ? 0 : 1) : "Bad event detected!";

        if (enabled == 0) {
            return false; // go back to crawling
        }

        EventScheduler.shared.releasePermission();

        AtomicBoolean needToBeCrawled = new AtomicBoolean(false);

        enabled = EventScheduler.shared.requestPermission(
                new String[]{
                        "ct_addToIPW_success." + crawlerId + "." + url.getId(),
                        "ct_addToIPW_duplicate." + crawlerId + "." + url.getId()
                },
                () -> needToBeCrawled.set(inProgressWriting.add(fileName))
        );

        if (enabled == -1) {
            return false; // terminate if never enabled
        }

        // Check whether the url is about to be fetched
        assert enabled == (needToBeCrawled.get() ? 0 : 1) : "Bad event detected!";

        EventScheduler.shared.releasePermission();

        return needToBeCrawled.get();
    }

    private boolean existsInFile(Url url, File file) {
        // Check whether the index file exists.
        if (!file.isFile()) {
            return false;
        }

        // If the index file exists, read from it to find out whether
        // the URL has already been crawled.
        boolean found = false;
        try {
            // Open file
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            // Iterate through all (key, value)-pairs in the index file.
            String line;
            while (!found && (line = bufferedReader.readLine()) != null) {
                found = line.split(" ", 2)[0].equals(url.getSlug());
            }

            // Close file
            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            logger.error("Error in reading index", e);
        }
        return found;
    }
}