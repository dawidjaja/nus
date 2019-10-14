package cs2030.simulator;
import java.util.*;

public class LeaveEvent extends Event {
	public LeaveEvent(int user, double time) {
		super(user, time);
	}

	@Override
	public void process(Server[] server, PriorityQueue<Event> pq) {
        Server.addLeaveCount();
    }

	@Override
	public String toString() {
		String out = "";
		out += String.format("%.3f %d leaves", this.getTime(), this.getUser());
		return out;
	}
}

