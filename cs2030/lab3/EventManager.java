package cs2030.simulator;
import java.util.*;

public class EventManager {
    public void manage() {
        PriorityQueue<Event> pq = new PriorityQueue<Event>(new EventComparator());
        Scanner in = new Scanner(System.in);
        int seed = in.nextInt();
        int nServer = in.nextInt();
        int nUser = in.nextInt();
        double arrivalRate = in.nextDouble();
        double serviceRate = in.nextDouble();
        Event.setRG(seed, arrivalRate, serviceRate);
        Server[] server = new Server[nServer + 1];
        for (int i = 0; i <= nServer; i++) {
            server[i] = new Server(i);
        }
        int ctrUser = 0;
        double lastTime = 0;
        while (ctrUser < nUser) {
            ctrUser++;
            pq.add(new ArrivesEvent(ctrUser, lastTime));
            lastTime += Event.rg.genInterArrivalTime();
        }
        while (pq.size() > 0) {
            Event e = pq.poll();
            System.out.println(e);
            e.process(server, pq);
        }
        System.out.println(Server.stats());
    }
}
