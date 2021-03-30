package urlcrawler.indexer;

import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import urlcrawler.data.BufferedUrlList;
import urlcrawler.data.Url;
import urlcrawler.scheduler.EventScheduler;

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
                if (!EventScheduler.shared.hasNext()) {
                    stopped = true;
                    break;
                }
                
                int enabled = EventScheduler.shared.requestPermission(new String[] {
                    "ibt_waitInBUL." + bufferedUrlList.getId(), "ibt_readFromBUL_success." + bufferedUrlList.getId()
                });

                if (enabled == -1) {
                    return; // return if never enabled
                }
                
                Url[] bufferContents = bufferedUrlList.getAll(enabled); // processed outside to avoid deadlock

                EventScheduler.shared.releasePermission();
                
                logger.info(String.format("Just obtained %d HTML contents from BUL", bufferContents.length));

                logger.info(String.format("Writing %d HTML Contents", bufferContents.length));
                Stream.of(bufferContents).forEach(x -> {
                    if (!EventScheduler.shared.requestPermission("ibt_writeNext." + bufferedUrlList.getId())) {
                        return; // return if never enabled
                    }

                    // write next event is simply to represent looping. Thus, we can release it immediately.
                    EventScheduler.shared.releasePermission();

                    indexedUrlTree.writeContents(x, bufferedUrlList.getId()); // ibt and bul have same id
                });
                
                if (!EventScheduler.shared.requestPermission("ibt_exitForLoop." + bufferedUrlList.getId())) {
                    return; // return if never enabled
                }

                // release exit for loop
                EventScheduler.shared.releasePermission();

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
