package urlcrawler.data;

import urlcrawler.scheduler.EventScheduler;

public class BufferedUrlList {
    private static int numOfBULs = 0;

    private Url[] storage;
    private int size;
    private int capacity;
    private int id;

    public BufferedUrlList(int capacity) {
        storage = new Url[capacity];
        size = 0;
        this.capacity = capacity;
        id = getAndIncNumOfBULs();
    }

    private synchronized static int getAndIncNumOfBULs() {
        int result = numOfBULs;
        numOfBULs++;
        return result;
    }

    public int getId() {
        return id;
    }

    public synchronized void add(Url url, int enabled) throws InterruptedException {        
        assert enabled == (isFull() ? 0 : 1) : "Bad event detected!";
        
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

    private boolean isFull() {
        return size == capacity;
    }

    public synchronized Url[] getAll(int enabled) throws InterruptedException {
        assert enabled == (isFull() ? 1 : 0) : "Bad event detected!";
        
        // only get all if the storage is full.
        while (!isFull()) {
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
        // replicate the current storage content
        Url[] copyOfStorage = storage.clone();

        // empty the storage
        size = 0;

        // return the copy
        return copyOfStorage;
    }
}
