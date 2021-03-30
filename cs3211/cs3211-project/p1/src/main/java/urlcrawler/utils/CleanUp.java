package urlcrawler.utils;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import urlcrawler.crawler.CrawlerRunnable;
import urlcrawler.indexer.IndexBuildingThread;

public class CleanUp extends Thread {
    private static final Logger logger = LogManager.getLogger(CleanUp.class);

    private List<IndexBuildingThread> indexBuildingThreads;
    private List<CrawlerRunnable> crawlers;
    private String outputFile;

    public CleanUp(List<IndexBuildingThread> indexBuildingThreads, List<CrawlerRunnable> crawlers, String outputFile) {
        this.indexBuildingThreads = indexBuildingThreads;
        this.crawlers = crawlers;
        this.outputFile = outputFile;
    }

    public void run() {
        logger.error("Interrupt received. Shutting down...");

        for (CrawlerRunnable crawler : crawlers) {
            crawler.markAsStopped();
        }

        for (IndexBuildingThread indexBuildingThread : indexBuildingThreads) {
            indexBuildingThread.markAsStopped();
        }

        logger.error(String.format("TODO: convert the IUT to %s.", outputFile));
        logger.error("Shutdown complete.");
    }
}
