package urlcrawler.data;

import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BufferedUrlList {
    private final static Logger logger = LogManager.getLogger(BufferedUrlList.class);

    private Url[] storage;
    private int size;
    private int capacity;

    private AtomicBoolean isEnd;
    private volatile int numOfActiveCrawlers;

    public BufferedUrlList(int capacity, int numOfCrawlers) {
        this.capacity = capacity;

        storage = new Url[capacity];
        size = 0;
        isEnd = new AtomicBoolean(false);
        numOfActiveCrawlers = numOfCrawlers;
    }

    public synchronized void notifyInactiveCrawler() {
        numOfActiveCrawlers--;
        if (numOfActiveCrawlers == 0) {
            isEnd.set(true);
            this.notifyAll(); // notify IBT that might be waiting
        }
    }

    public synchronized void add(Url url) throws InterruptedException {
        while (isFull()) {
            // The producer that calls add will be blocked.
            this.wait();
        }

        // push to the storage.
        storage[size] = url;
        size++;

        if (isFull()) {
            // notify the consumers that call method `getall`
            this.notifyAll();
        }
    }

    private synchronized boolean isFull() {
        return size == capacity;
    }

    public synchronized Url[] getAll() throws InterruptedException {
        // only get all if the storage is full.
        while (!isFull() && !isEnd.get()) {
            this.wait();
        }

        // get all elements in storage and empty the storage.
        Url[] allElements = replicateAndEmptyStorage();

        // now the storage is empty, notify all producers that calls method `add`.
        this.notifyAll();
        return allElements;
    }

    /**
     * replicateAndEmptyStorage create a copy of the current storage, empty the storage and then return the copied
     * storage object.
     *
     * @return the duplicated storage and empty the current storage.
     */
    private Url[] replicateAndEmptyStorage() {
        // create new storage
        Url[] oldStorage = storage;

        if (!isEnd.get()) {
            storage = new Url[capacity];
        } else {
            storage = null;
        }

        // empty the storage
        size = 0;

        // return the old storage
        return oldStorage;
    }
}
