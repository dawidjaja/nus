package urlcrawler.crawler.jsoup;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import urlcrawler.crawler.CrawlerRunnable;
import urlcrawler.data.BufferedUrlList;
import urlcrawler.data.Url;
import urlcrawler.data.UrlSupplier;
import urlcrawler.indexer.IndexedUrlTree;

public class Crawler implements CrawlerRunnable {
    private final static int MAX_QUEUE_SIZE = 250000;

    private final static Logger logger = LogManager.getLogger(Crawler.class);

    private UrlSupplier urlSupplier;
    private BufferedUrlList bufferedUrlList;
    private IndexedUrlTree indexedUrlTree;
    private Queue<Url> urlQueue = new LinkedList<>();

    private boolean stopped = false;

    public Crawler(UrlSupplier urlSupplier, BufferedUrlList bufferedUrlList,
                   IndexedUrlTree indexedUrlTree) {
        this.urlSupplier = urlSupplier;
        this.bufferedUrlList = bufferedUrlList;
        this.indexedUrlTree = indexedUrlTree;
    }

    // in milli seconds
    // note: this value may be overwritten by config file
    private int politenessDelay = 0;

    @Override
    public void setPolitenessDelay(int politenessDelay) {
        this.politenessDelay = politenessDelay;
    }

    @Override
    public void run() {
        try {
            while (!stopped) {
                Url nextUrl = urlSupplier.consume();
                if (nextUrl == null) {
                    break;
                }

                urlQueue.add(nextUrl);
                while (!urlQueue.isEmpty() && !stopped) {
                    Url url = urlQueue.poll();
                    if (!indexedUrlTree.checkAndMarkUrl(url)) {
                        continue;
                    }

                    Thread.sleep(politenessDelay);
                    visit(url);
                }
            }
            if (stopped) {
                logger.error("Stop signal received!");
            }
        } catch (Exception e) {
            logger.error("There is an error while running crawler", e);
        }
    }

    /**
     * Initiates shutdown of the current crawling session.
     */
    @Override
    public void stop() {
        stopped = true;
    }

    private void visit(Url url) {
        logger.info("Crawling page " + url.getSpec());

        Document document;

        try {
            document = Jsoup.connect(url.getSpec())
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 " +
                            "Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .ignoreHttpErrors(true)
                    .timeout(3000)
                    .get();
        } catch (Exception e) {
            logger.error("Error retrieving (" + url.getSpec() + ")", e);
            url.markAsDeadUrl();

            // Keep adding to the buffered URL List to be marked as broken URL
            addToBufferedUrlList(url);
            return;
        }

        Elements linkElements = document.select("a[href]");
        Collections.shuffle(linkElements);

        for (Element link : linkElements) {
            if (urlQueue.size() >= MAX_QUEUE_SIZE) {
                break;
            }

            String targetUrlSpec = link.attr("abs:href");
            if (Url.isValid(targetUrlSpec)) {
                urlQueue.offer(new Url(targetUrlSpec, url.getSpec(), null));
            }
        }

        if (url.getSlug().length() <= IndexedUrlTree.MAX_LENGTH_FILENAME && Url.getPermissionToSaveContent()) {
            // avoid storing unnecessary content in memory
            url.setContent(document.toString());
        }

        addToBufferedUrlList(url);
    }

    private void addToBufferedUrlList(Url url) {
        try {
            bufferedUrlList.add(url);
        } catch (InterruptedException e) {
            logger.error("Something went wrong while adding data to the buffer", e);
        }
    }
}
