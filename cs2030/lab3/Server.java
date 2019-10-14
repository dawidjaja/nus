package cs2030.simulator;
import java.util.*;

public class Server {
	protected Event serve;
	protected Event wait;
	protected int serverNum;
    protected static int servedCount;
    protected static int leaveCount;
    protected static double waitTime;


	public Server(int serverNum) {
		serve = null;
		wait = null;
		this.serverNum = serverNum;
	}

    public static void addDoneCount() {
        servedCount++;
    }

    public static void addLeaveCount() {
        leaveCount++;
    }

	public boolean srvAva() {
		if (serve == null) return true;
		return false;
	}

	public boolean waitAva() {
		if (wait == null) return true;
		return false;
	}

	public void add(Event e) {
		if (serve == null) serve = e;
		else wait = e;
	}

    public static String stats() {
        String out = "";
        out += String.format("[%.3f %d %d]", waitTime / servedCount, servedCount, leaveCount);
        return out;
    }

	public Event process(double now) {
        if (wait == null) {
            serve = null;
            return null;
        }
        waitTime += now - wait.getTime();
		serve = new ServedEvent(wait.getUser(), now, serverNum);
		wait = null;
		return serve;
	}
}
