package urlcrawler.indexer;

import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import urlcrawler.DelayManager;
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
                // [EVENT 07] ibt_readFromBUL_success.0 && [EVENT 13] ibt_readFromBUL_success.1
                DelayManager.delayForBothIBTEvents(7, 13);

                Url[] bufferContents = bufferedUrlList.getAll();
                logger.info(String.format("Just obtained %d HTML contents from BUL", bufferContents.length));

                logger.info(String.format("Writing %d HTML Contents", bufferContents.length));
                Stream.of(bufferContents).forEach(x -> {
                    // [EVENT 08] ibt_writeNext.0 && [EVENT 14] ibt_writeNext.1
                    // Note that BUL size == 1, Thus, event 08 and event 14 delay will only be executed once.
                    DelayManager.delayForBothIBTEvents(8, 14);

                    indexedUrlTree.writeContents(x);
                });
                logger.info(String.format("Finish writing %d HTML Contents", bufferContents.length));

                // Each IBT only need to perform once for this project.
                stopped = true;
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
