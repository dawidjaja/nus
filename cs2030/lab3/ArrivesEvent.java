package cs2030.simulator;
import java.util.*;

public class ArrivesEvent extends Event {
    public ArrivesEvent(int user, double time) {
        super(user, time);
    }

    @Override
    public void process(Server[] server, PriorityQueue<Event> pq) {
        int vis = 0;
        for (int i = 1; i < server.length; i++) {
            if (server[i].srvAva()) {
                Event tmp = new ServedEvent(this.getUser(), this.getTime(), i);
                pq.add(tmp);
                server[i].add(tmp);
                vis = 1;
                break;
            }
        }
        if (vis == 0) {
            for (int i = 1; i < server.length; i++) {
                if (server[i].waitAva()) {
                    Event tmp = new WaitEvent(this.getUser(), this.getTime(), i);
                    pq.add(tmp);
                    server[i].add(tmp);
                    vis = 1;
                    break;
                }
            }
        }
        if (vis == 0) {
            pq.add(new LeaveEvent(this.getUser(), this.getTime()));
        }
    }

    @Override
    public String toString() {
        String out = "";
        out += String.format("%.3f %d arrives", this.getTime(), this.getUser());
        return out;
    }
}

