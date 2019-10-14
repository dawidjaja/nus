import java.util.Scanner;

public class Main {
    public static void main(String[] args) { 
        Scanner in = new Scanner(System.in);
        Customer cCur = new Customer();
        Customer cPrev = new Customer();
        Customer cNew = new Customer();
        Server server = new Server();
        int ctr = 0;
        while (in.hasNext()) {
            ctr++;
            cNew = new Customer(in.nextDouble(), ctr, "arrives");
            server.add(cNew);
        }
        while (server.head != null) {
            System.out.println(server.toString());
        }
        System.out.printf("[%.3f %d %d]\n", server.getAveWT(), server.getSC(), server.getLC());
    }
}
