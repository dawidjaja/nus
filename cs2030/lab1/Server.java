public class Server {
    private double nextServe;
    Customer head;
    private int wait;
    private int servedCtr;
    private int leaveCtr;
    private double waitTime;

    public Server() {
        this.nextServe = 0.0;
        this.head = null;
        this.wait = 0;
        this.waitTime = 0.0;
        this.servedCtr = 0;
        this.leaveCtr = 0;
    }

    public double getNextServe() {
        return nextServe;
    }

    public void setNextServe(double x) {
        nextServe = x;
    }

    public double getAveWT() {
        return waitTime / servedCtr;
    }

    public int getSC() {
        return servedCtr;
    }

    public int getLC() {
        return leaveCtr;
    }
    
    public void add(Customer c) {
        if (head == null) {
            head = c;
        } else {
            Customer tmp = head;
            while (tmp.next != null) {
                tmp = tmp.next;
            }
            tmp.next = c;
        }
    }
    
    public String printInit() {
        String output = "# Adding arrivals\n";
        Customer tmp = head;
        while (tmp != null) {
            output += tmp.toString() + "\n";
            tmp = tmp.next;
        }
        return output;
    }

    private void process() {
        if (head.getStatus().equals("arrives")) {
           if (head.getTime() >= this.nextServe) {
               this.nextServe = head.getTime() + 1;
               head.setStatus("served");
           } else if (wait == 0) {
               wait = 1;
               head.setStatus("waits");
           } else {
               head.setStatus("leaves");
           }
        } else if (head.getStatus().equals("served")) {
            servedCtr++;
            Customer tmp = new Customer(head.getTime() + 1, head.getIdx(), "done");
            Customer cur = head;
            while (cur.next != null && cur.next.getTime() <= tmp.getTime()) {
                if (cur.next.getTime() == tmp.getTime() && 
                    cur.next.getIdx() > tmp.getIdx()) 
                        break;
                cur = cur.next;
            }
            tmp.next = cur.next;
            cur.next = tmp;
            head = head.next;
        } else if (head.getStatus().equals("waits")) {
            waitTime += nextServe - head.getTime();
            Customer tmp = new Customer(nextServe, head.getIdx(), "served");
            Customer cur = head;
            this.nextServe += 1;
            while (cur.next != null && cur.next.getTime() <= tmp.getTime()) {
                if (cur.next.getTime() == tmp.getTime() && 
                    cur.next.getIdx() > tmp.getIdx()) 
                        break;
                cur = cur.next;
            }
            tmp.next = cur.next;
            cur.next = tmp;
            head = head.next;
        } else if (head.getStatus().equals("leaves")) {
            leaveCtr++;
            head = head.next;
        } else if (head.getStatus().equals("done")) {
            wait = 0;
            head = head.next;
        }
    }

    public String toString() {
        String output = "";
        output += head.toString();
        this.process();
        return output;
    }
}

