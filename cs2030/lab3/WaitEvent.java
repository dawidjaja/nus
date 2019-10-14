package cs2030.simulator;
import java.util.*;

public class WaitEvent extends Event {
	private int server;

	public WaitEvent(int user, double time, int server) {
		super(user, time);
		this.server = server;
	}

	public int getServer() {
		return server;
	}

	public void process(Server[] server, PriorityQueue<Event> pq) {}

	@Override
	public String toString() {
		String out = "";
		out += String.format("%.3f %d waits to be served by %d", this.getTime(), this.getUser(), server);
		return out;
	}
}

