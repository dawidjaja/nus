package urlcrawler.indexer;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import urlcrawler.data.BufferedUrlList;
import urlcrawler.data.Url;

public class IndexBuildingThread implements Runnable {
    private final static Logger logger = LogManager.getLogger(IndexBuildingThread.class);

    private BufferedUrlList bufferedUrlList;
    private IndexedUrlTree indexedUrlTree;

    private AtomicBoolean stopped;
    private final Object signal = new Object();

    public IndexBuildingThread(BufferedUrlList bufferedUrlList, IndexedUrlTree indexedUrlTree) {
        this.bufferedUrlList = bufferedUrlList;
        this.indexedUrlTree = indexedUrlTree;

        stopped = new AtomicBoolean(false);
    }

    @Override
    public void run() {
        try {
            while (!stopped.get()) {
                Url[] bufferContents = bufferedUrlList.getAll();
                if (bufferContents == null) {
                    break;
                }

                long numOfContents = Stream.of(bufferContents).filter(Objects::nonNull).count();
                logger.info(String.format("Writing %d HTML Contents", numOfContents));

                Stream.of(bufferContents).filter(Objects::nonNull).forEach(x -> indexedUrlTree.writeContents(x));

                logger.info(String.format("Finish writing %d HTML Contents", numOfContents));
            }

            if (stopped.getAndSet(true)) {
                logger.info("Stop signal for IBT received!");
                synchronized (signal) {
                    signal.notify(); // notify that the IBT is ready to stop
                }
            } else {
                logger.info("End of IBT");
            }
        } catch (InterruptedException ex) {
            logger.error("Something went wrong while running the thread", ex);
        }
    }

    /**
     * Initializes shutdown of the current thread.
     */
    public void markAsStopped() {
        if (stopped.getAndSet(true)) {
            // the IBT stopped on its own
            return;
        }

        try {
            synchronized (signal) {
                signal.wait();
            }
        } catch (InterruptedException e) {
            logger.error("Error when waiting for IBT to stop", e);
        }
    }
}
