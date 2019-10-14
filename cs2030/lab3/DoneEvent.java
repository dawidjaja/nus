package cs2030.simulator;
import java.util.*;

public class DoneEvent extends Event {
	private int server;

	public DoneEvent(int user, double time, int server) {
		super(user, time);
		this.server = server;
	}

	@Override
	public void process(Server[] server, PriorityQueue<Event> pq) {
		Event tmp = server[this.server].process(time);
		if (tmp != null) {
			pq.add(tmp);
		}
	}

	public int getServer() {
		return server;
	}

	@Override
	public String toString() {
		String out = "";
		out += String.format("%.3f %d done serving by %d", this.getTime(), this.getUser(), server);
		return out;
	}
}

