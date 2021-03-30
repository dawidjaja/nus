package urlcrawler.scheduler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EventScheduler {
    public static final EventScheduler shared = new EventScheduler();

    private static final Logger logger = LogManager.getLogger(EventScheduler.class);

    private BufferedReader bufferedReader;
    private volatile String cachedEvent;
    private final Object notifier;
    private final ReadWriteLock readWriteLock;

    private EventScheduler() {
        readWriteLock = new ReentrantReadWriteLock();
        notifier = new Object();
    }

    public void readEventSchedule(String filePath) {
        try {
            bufferedReader = new BufferedReader(new FileReader(filePath));
        } catch (IOException e) {
            logger.error("Error when opening file", e);
        }
    }

    public synchronized boolean hasNext() {
        // acquire read lock before checking the value of cachedEvent
        readWriteLock.readLock().lock();
        try {
            if (bufferedReader == null) {
                return false;
            } else if (cachedEvent != null) {
                return true;
            }
        } finally {
            readWriteLock.readLock().unlock();
        }

        // try to read the next event
        readWriteLock.writeLock().lock();
        try {
            cachedEvent = bufferedReader.readLine();
            if (cachedEvent == null) {
                bufferedReader.close();
                bufferedReader = null;
            }

            synchronized (notifier) {
                notifier.notifyAll();
            }

            return cachedEvent != null;

        } catch (IOException e) {
            logger.error("Error when reading next event", e);
            bufferedReader = null;

            synchronized (notifier) {
                notifier.notifyAll();
            }

            return false;

        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    // AtomicBoolean is chosen compared to Boolean because of Boolean's auto-boxing,
    // which is problematic if we are using it as monitor.
    private volatile AtomicBoolean permissionAvailable = new AtomicBoolean(true);

    // All threads will compete to obtain the permission boolean.
    // Thus, ensuring only one event will be processed at one point of time.
    private void requestPermissionBool() {
        try {
            while (true) {
                synchronized (permissionAvailable) {
                    if (permissionAvailable.get() == true) {
                        permissionAvailable.set(false);
                        logger.info("Successfully obtain permission boolean");
                        break;
                    }

                    permissionAvailable.wait();
                }
             }
        } catch (InterruptedException e) {
            logger.error("Scheduler interrupted", e);
        }
    }

    // Return the permission boolean for other threads to obtain.
    private void releasePermissionBool() {
        logger.info("Releasing permission boolean.");
        synchronized (permissionAvailable) {
            permissionAvailable.set(true);
            // Notify all process who is competing to get the permission boolean
            permissionAvailable.notifyAll();
        }
    }

    private int requestPermission(Function<String, Integer> checker, Consumer<Integer> onSuccess) {
        while (hasNext()) {
            requestPermissionBool();

            int index = -1;

            if (hasNext()) {
                index = checker.apply(cachedEvent);
            }

            if (index != -1) {
                readWriteLock.writeLock().lock();
                cachedEvent = null;
                onSuccess.accept(index);
                readWriteLock.writeLock().unlock();
                return index;
            }

            logger.info("Current event does not match");

            try {
                synchronized (notifier) {
                    // After getting permission but the event does not match, we have to release the permission.
                    releasePermissionBool();

                    notifier.wait();
                }
            } catch (InterruptedException ex) {
                logger.error("Scheduler interrupted", ex);
            }
        }
        return -1;
    }

    // Call this function at the end of the event to let other events proceed.
    public void releasePermission() {
        logger.info("Releasing permission.");

        // A process might wait() in 2 positions.
        // First is when they are trying to get permission boolean.
        // Second is when they have obtained the permission boolean, but realized that the event is unmatch.
        // Thus, we need to do notification in both notifier and permission boolean.
        synchronized (notifier) {
            notifier.notifyAll();
        }

        releasePermissionBool();
    }

    /**
     * Returns the index of the first event that is enabled in an array of event descriptor strings,
     * and atomically execute an operation upon acquiring the permission.
     * If none of the events are enabled, returns -1.
     */
    public int requestPermission(String[] eventDescriptors, Runnable onSuccess) {
        return requestPermission(
                cachedEvent -> findIndex(eventDescriptors, cachedEvent),
                index -> {
                    logger.info("Executing event: " + eventDescriptors[index]);
                    onSuccess.run();
                }
        );
    }

    /**
     * Returns true when the event is enabled, and atomically execute an operation upon acquiring the permission.
     * If the event is never enabled, returns false.
     */
    public boolean requestPermission(String eventDescriptor, Runnable onSuccess) {
        return requestPermission(new String[]{eventDescriptor}, onSuccess) == 0;
    }

    /**
     * Returns true when the event is enabled.
     * If the event is never enabled, returns false.
     */
    public boolean requestPermission(String eventDescriptor) {
        return requestPermission(new String[]{eventDescriptor}) == 0;
    }

    /**
     * Returns the index of the first event that is enabled in an array of event descriptor strings.
     * If none of the events are enabled, returns -1.
     */
    public int requestPermission(String[] eventDescriptors) {
        return requestPermission(eventDescriptors, () -> {});
    }

    /**
     * Returns the integer suffix of the first event that is enabled that matches a given prefix.
     * If such an event is never enabled, returns -1.
     */
    public int requestPermissionWithPrefix(String eventDescriptorPrefix) {
        return requestPermission(
                cachedEvent -> getSuffixAsInt(eventDescriptorPrefix, cachedEvent),
                index -> logger.info("Executing event: " + eventDescriptorPrefix + index)
        );
    }

    /**
     * Finds the index matching a string in an array of strings.
     * If there is no such index, returns -1.
     */
    private static int findIndex(String[] array, String string) {
        for (int i = 0; i < array.length; i++) {
            if (string.equals(array[i])) {
                return i;
            }
        }

        return -1;
    }

    private static int getSuffixAsInt(String prefix, String string) {
        if (!string.startsWith(prefix)) {
            return -1;
        }

        String suffix = string.substring(prefix.length(), string.length());

        try {
            return Integer.parseInt(suffix);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
