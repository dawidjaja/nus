package urlcrawler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// DelayManager contains implementation of all delays
public class DelayManager {
    private final static Logger logger = LogManager.getLogger(DelayManager.class);

    // Introduce an event delay based the crawler thread executing.
    // These 2 events happening at the same position but executed by different crawler thread.
    public static void delayForBothCTEvents(int firstEvent, int secondEvent) {
        if (Thread.currentThread().getName().equals("CT0")) {
            delayForEvent(firstEvent);
        }

        if (Thread.currentThread().getName().equals("CT1")) {
            delayForEvent(secondEvent);
        }
    }

    // Introduce an event delay based on the IBT executing.
    // These 2 events happening at the same position but executed by different IBT.
    public static void delayForBothIBTEvents(int firstEvent, int secondEvent) {
        if (Thread.currentThread().getName().equals("IBT0")) {
            delayForEvent(firstEvent);
        }

        if (Thread.currentThread().getName().equals("IBT1")) {
            delayForEvent(secondEvent);
        }
    }

    // Introduce a delay based on the event number
    public static void delayForEvent(int num) {
        try {
            int delay = determineEventDelay(num);
            logger.info("Delay event " + num + " for " + (delay / oneSecond) + "s");
            Thread.sleep(delay);

            logger.info("Event " + num + " wake up");
        } catch (InterruptedException ex) {
            logger.error("Something went wrong while adding delay for event " + num, ex);
        }
    }

    private static int oneSecond = 1000;
    // Event delays are tweaked manually to show data race
    private static int determineEventDelay(int num) {
        switch (num) {
        case 1:
            return 0;
        case 5:
            return 10 * oneSecond;
        case 7:
            return 20 * oneSecond;
        case 11:
            return 20 * oneSecond;
        case 13:
            return 40 * oneSecond;
        default:
            // The remaining are events that should happened immediately after the previous event.
            return 0;
        }
    }
}
