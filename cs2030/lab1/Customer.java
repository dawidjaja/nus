public class Customer {
    private double time;
    private int idx;
    private String status;
    Customer next;

    public Customer() {};

    public Customer(double time, int idx, String status) {
        this.time = time;
        this.idx = idx;
        this.status = status;
    }

    public int getIdx() {
        return idx;
    }

    public void setTime(double x) {
        time = x;
    }

    public double getTime() {
        return time;
    }

    public void setStatus(String s) {
        this.status = s;
    }

    public String getStatus() {
        return status;
    }

    public String toString() {
        String output = "";
        output += String.format("%.3f %d ", time, idx) + status;
        return output;
    }
}
