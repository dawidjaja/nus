package cs2030.simulator;
import java.util.*;

public abstract class Event {
    protected int user;
    protected double time;
    protected static RandomGenerator rg;

    public Event(int user, double time) {
        this.user = user;
        this.time = time;
    }

    public static void setRG(int seed, double arrivalRate, double serviceRate) {
        rg = new RandomGenerator(seed, arrivalRate, serviceRate);
    }

    public int getUser() {
        return user;
    }

    public double getTime() {
        return time;
    }

    public abstract void process(
            Server[] server, PriorityQueue<Event> pq);
}

