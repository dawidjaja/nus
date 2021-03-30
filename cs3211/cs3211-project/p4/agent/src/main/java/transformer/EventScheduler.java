package transformer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Function;

public class EventScheduler {
    public static final EventScheduler shared = new EventScheduler();

    private BufferedReader bufferedReader;
    private volatile LinkedList<String> cachedEvents;

    // currentEvent holds the event that is currently executing
    public volatile String currentEvent;

    private final Object notifier;
    private final ReadWriteLock readWriteLock;

    private EventScheduler() {
        readWriteLock = new ReentrantReadWriteLock();
        notifier = new Object();
        cachedEvents = new LinkedList<String>();
    }

    public void readEventSchedule(String filePath) {
        try {
            bufferedReader = new BufferedReader(new FileReader(filePath));
        } catch (IOException e) {
            System.err.println("Error when opening file");
            e.printStackTrace();
        }
    }

    public synchronized boolean hasNext() {
        // acquire read lock before checking the value of cachedEvent
        readWriteLock.readLock().lock();
        try {
            if (bufferedReader == null) {
                return false;
            } else if (!cachedEvents.isEmpty()) {
                return true;
            }
        } finally {
            readWriteLock.readLock().unlock();
        }

        // try to read the next event
        readWriteLock.writeLock().lock();
        try {
            String event = bufferedReader.readLine();
            if (event == null) {
                bufferedReader.close();
                bufferedReader = null;
            } else {
                cachedEvents.addLast(event);
            }

            synchronized (notifier) {
                notifier.notifyAll();
            }

            return event != null;

        } catch (IOException e) {
            System.err.println("Error when reading next event");
            e.printStackTrace();

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
                        System.out.println("Successfully obtain permission boolean");
                        break;
                    }

                    permissionAvailable.wait();
                }
            }
        } catch (InterruptedException e) {
            System.err.println("Scheduler interrupted");
            e.printStackTrace();
        }
    }

    // Return the permission boolean for other threads to obtain.
    private void releasePermissionBool() {
        System.out.println("Releasing permission boolean.");
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
                currentEvent = cachedEvents.getFirst();
                index = checker.apply(currentEvent);
            } else {
                System.out.println("No more events left unless some threads undo.");
            }

            if (index != -1) {
                readWriteLock.writeLock().lock();
                cachedEvents.pollFirst();
                onSuccess.accept(index);
                readWriteLock.writeLock().unlock();
                return index;
            }

            System.out.println("Current event does not match");

            try {
                synchronized (notifier) {
                    // After getting permission but the event does not match, we have to release the permission.
                    releasePermissionBool();

                    notifier.wait();
                }
            } catch (InterruptedException e) {
                System.err.println("Scheduler interrupted");
                e.printStackTrace();
            }
        }

        return -1;
    }

    // Call this function at the end of the event to let other events proceed.
    public void releasePermission() {
        System.out.println("Releasing permission.");

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
                currentEvent -> findIndex(eventDescriptors, currentEvent),
                index -> {
                    System.out.println("Executing event: " + eventDescriptors[index]);
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
     * If the suffix is not an integer, returns -2.
     */
    public int requestPermissionWithPrefix(String eventDescriptorPrefix) {
        return requestPermission(
                currentEvent -> getSuffixAsInt(eventDescriptorPrefix, currentEvent),
                index -> System.err.println("Executing event: " + eventDescriptorPrefix + index)
        );
    }

    /**
     * Finds the index matching a string in an array of strings.
     * If there is no such index, returns -1.
     */
    private static int findIndex(String[] array, String string) {
        System.out.println("Requesting for any of this/these event(s): " + String.join(", ", array));
        for (int i = 0; i < array.length; i++) {
            if (string.equals(array[i])) {
                return i;
            }
        }

        return -1;
    }

    private static int getSuffixAsInt(String prefix, String string) {
        System.out.println("Requesting for any event starting with: " + prefix);
        if (!string.startsWith(prefix)) {
            return -1;
        }

        String suffix = string.substring(prefix.length(), string.length());

        try {
            return Integer.parseInt(suffix);
        } catch (NumberFormatException e) {
            return -2;
        }
    }

    /**
     * Restores the current event and release permission.
     * Note: only use this function when you have obtained the permission.
     */
    public void undoEvent() {
        System.out.println("Undo current event");
        readWriteLock.writeLock().lock();
        cachedEvents.addFirst(currentEvent);
        readWriteLock.writeLock().unlock();

        releasePermission();
    }
}
