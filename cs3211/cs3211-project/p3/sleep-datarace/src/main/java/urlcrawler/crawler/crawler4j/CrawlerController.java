package urlcrawler.crawler.crawler4j;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import urlcrawler.crawler.CrawlerRunnable;
import urlcrawler.data.BufferedUrlList;
import urlcrawler.data.UrlSupplier;
import urlcrawler.indexer.IndexedUrlTree;

public class CrawlerController implements CrawlerRunnable {
    final static Logger logger = LogManager.getLogger(Crawler.class);
    static int totalController = 0;

    private int id;
    private UrlSupplier urlSupplier;
    private BufferedUrlList bufferedUrlList;
    private IndexedUrlTree indexedUrlTree;
    private String crawlerIntermediateStorageFolder;

    private CrawlController currentController;
    private volatile boolean stopped = false;

    public CrawlerController(UrlSupplier urlSupplier, BufferedUrlList bufferedUrlList,
                             IndexedUrlTree indexedUrlTree) {
        id = totalController++;

        this.urlSupplier = urlSupplier;
        this.bufferedUrlList = bufferedUrlList;
        this.indexedUrlTree = indexedUrlTree;
        crawlerIntermediateStorageFolder = "temp/crawler" + totalController + "/";

        logger.debug("Intermediate storage location: " + crawlerIntermediateStorageFolder);
    }

    // Politeness delay is set to 100 ms to not overload testing server
    private int politenessDelay = 100;

    @Override
    public void setPolitenessDelay(int politenessDelay) {
        this.politenessDelay = politenessDelay;
    }

    @Override
    public void run() {
        try {
            String nextUrl;
            while (!stopped) {
                nextUrl = urlSupplier.consume();
                if (nextUrl == null) {
                    break;
                }

                startCrawler(nextUrl);
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

        synchronized (this) {
            if (currentController != null && !currentController.isFinished()) {
                currentController.shutdown();

                //Uncomment for complete shutdown
                //currentController.waitUntilFinish();
            }
        }
    }

    private static boolean doesIncludeBinaryContentInCrawling = false;

    private void startCrawler(String seed) throws Exception {
        logger.info("Initializing crawler...");

        int numberOfCrawlers = 1;

        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlerIntermediateStorageFolder);

        config.setPolitenessDelay(politenessDelay);
        config.setIncludeBinaryContentInCrawling(doesIncludeBinaryContentInCrawling);

        // Instantiate the controller for this crawl.
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);

        // Synchronize block to ensure new crawler is not created after shutdown
        synchronized (this) {
            if (stopped) {
                return;
            }

            currentController = new CrawlController(config, pageFetcher, robotstxtServer);
        }

        // Adding the first URLS that are going to be fetched.
        // After fetching this, crawler will start following links which are found inside
        // the page recursively.
        currentController.addSeed(seed);

        // The factory which creates instances of crawlers.
        CrawlController.WebCrawlerFactory<Crawler> factory = (() -> new Crawler(bufferedUrlList, indexedUrlTree));

        // Start the crawl. This is a blocking operation, meaning that this code
        // will reach the line after this, only when crawling is finished.
        currentController.start(factory, numberOfCrawlers);
    }
}
