package cs2030.simulator;
import java.util.*;

public class ServedEvent extends Event {
    private int server;

    public ServedEvent(int user, double time, int server) {
        super(user, time);
        this.server = server;
    }

    @Override
    public void process(Server[] server, PriorityQueue<Event> pq) {
        pq.add(new DoneEvent(this.getUser(), this.getTime() + rg.genServiceTime(), this.server));
        Server.addDoneCount();
    }

    public int getServer() {
        return server;
    }

    @Override
    public String toString() {
        String out = "";
        out += String.format("%.3f %d served by %d", this.getTime(), this.getUser(), server);
        return out;
    }
}

