package urlcrawler.indexer;

import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import urlcrawler.data.BufferedUrlList;
import urlcrawler.data.Url;

public class IndexBuildingThread implements Runnable {
    final static Logger logger = LogManager.getLogger(IndexBuildingThread.class);

    private BufferedUrlList bufferedUrlList;
    private IndexedUrlTree indexedUrlTree;

    private volatile boolean stopped = false;

    public IndexBuildingThread(BufferedUrlList bufferedUrlList, IndexedUrlTree indexedUrlTree) {
        this.bufferedUrlList = bufferedUrlList;
        this.indexedUrlTree = indexedUrlTree;
    }

    @Override
    public void run() {
        try {
            while (!stopped) {
                Url[] bufferContents = bufferedUrlList.getAll();
                logger.info(String.format("Just obtained %d HTML contents from BUL", bufferContents.length));

                logger.info(String.format("Writing %d HTML Contents", bufferContents.length));

                for (Url x : bufferContents) {
                    indexedUrlTree.writeContents(x);
                }
                logger.info(String.format("Finish writing %d HTML Contents", bufferContents.length));
            }
            if (stopped) {
                logger.error("Stop signal received!");
            }
        } catch (InterruptedException ex) {
            logger.error("Something went wrong while running the thread", ex);
        }
    }

    /**
     * Initializes shutdown of the current thread.
     */
    public void stop() {
        stopped = true;
    }
}
