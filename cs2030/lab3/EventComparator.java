package cs2030.simulator;
import java.util.*;

public class EventComparator implements Comparator<Event> {
    public int compare(Event a, Event b) {
        if (a.getTime() == b.getTime()) {
            return a.getUser() - b.getUser();
        }
        return a.getTime() < b.getTime() ? -1 : 1;
    }
}

