package transformer;

import java.util.HashSet;

// DelayManager contains implementation of all delays
public class DelayManager {


    public static HashSet<Integer> doneEvent = new HashSet<>();

    // Introduce an event delay based the crawler thread executing.
    public static void delayForBothCTEvents(int firstEvent, int secondEvent) {
        if (Thread.currentThread().getName().equals("0")) {
            if (!doneEvent.contains(firstEvent)) {
                delayForEvent(firstEvent);
            } else {
                delayForEvent(secondEvent);
            }
        }
    }

    // Introduce an event delay based on the IBT executing.
    public static void delayForBothIBTEvents(int firstEvent, int secondEvent) {
        if (Thread.currentThread().getName().equals("0")) {
            if (!doneEvent.contains(firstEvent)) {
                delayForEvent(firstEvent);
            } else {
                delayForEvent(secondEvent);
            }
        }
    }

    // Introduce a delay based on the event number
    public static void delayForEvent(int num) {
        try {
            if (doneEvent.contains(num)) {
                return;
            }
            doneEvent.add(num);
            int delay = determineEventDelay(num);
            System.out.println("Delay event " + num + " for " + (delay / oneSecond) + "s");
            Thread.sleep(delay);
            System.out.println("Event " + num + " wake up");
        } catch (InterruptedException ex) {
            System.out.println("Something went wrong while adding delay for event " + num);
        }
    }

    private static int oneSecond = 1000;
    // Event delays are tweaked manually to show data race
    private static int determineEventDelay(int num) {
        switch (num) {
        case 1:
            return 0;
        case 8:
            return 5 * oneSecond;
        case 11:
            return 10 * oneSecond;
        case 18:
            return 10 * oneSecond;
        case 22:
            return 20 * oneSecond;
        default:
            // The remaining are events that should happened immediately after the previous event.
            return 0;
        }
    }
}
