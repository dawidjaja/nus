package urlcrawler.data;

public class BufferedUrlList {
    private Url[] storage;
    private int size;
    private int capacity;

    public BufferedUrlList(int capacity) {
        storage = new Url[capacity];
        size = 0;
        this.capacity = capacity;
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

    private boolean isFull() {
        return size == capacity;
    }

    public synchronized Url[] getAll() throws InterruptedException {
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
