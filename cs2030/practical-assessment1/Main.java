import java.util.Scanner;
import java.util.ArrayList;

class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int idx = 0;
        ArrayList<Menu> menu = new ArrayList<>();
        while(sc.next().equals("add")) {
            String type = sc.next();
            if (type.equals("Combo")) {
                int a = sc.nextInt();
                int b = sc.nextInt();
                int c = sc.nextInt();
                if (a >= idx || b >= idx || c >= idx || 
                        !Combo.checkValid(menu.get(a), menu.get(b), menu.get(c))) {
                    System.out.printf("Error: Invalid combo input %d %d %d\n", a, b, c);
                    idx--;
                } else {
                    menu.add(new Combo(idx, (Burger)menu.get(a), (Snack)menu.get(b), (Drink)menu.get(c)));
                }
            } else {
                String desc = sc.next();
                int price = sc.nextInt();
                if (type.equals("Burger")) {
                    menu.add(new Burger(idx, desc, price));
                } else if (type.equals("Snack")) {
                    menu.add(new Snack(idx, desc, price));
                } else if (type.equals("Drink")) {
                    menu.add(new Drink(idx, desc, price));
                }
            }
            idx++;
        }
        for (int i = 0; i < idx; i++)
            System.out.printf(menu.get(i).getBurger());
        for (int i = 0; i < idx; i++)
            System.out.printf(menu.get(i).getSnack());
        for (int i = 0; i < idx; i++)
            System.out.printf(menu.get(i).getDrink());
        for (int i = 0; i < idx; i++)
            System.out.printf(menu.get(i).getCombo());

        //read order
        int totalPrice = 0;
        System.out.println("--- Order ---");
        while (sc.hasNext()) {
            int ord = sc.nextInt();
            System.out.println(menu.get(ord));
            totalPrice += menu.get(ord).getPrice();
        }
        System.out.println("Total: " + totalPrice);
    }
}
