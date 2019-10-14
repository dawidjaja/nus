public class Combo extends Menu {
    Burger burger;
    Snack snack;
    Drink drink;

    public Combo(int idx, Burger burger, Snack snack, Drink drink) {
        super(idx, burger.getPrice() + snack.getPrice() + drink.getPrice() - 50);
        this.burger = burger;
        this.snack = snack;
        this.drink = drink;
    }

    public static boolean checkValid(Menu a, Menu b, Menu c) {
        if (!(a instanceof Burger) || 
        !(b instanceof Snack) || 
        !(c instanceof Drink)) {
            return false;
        } 
        return true;
    }

    @Override
    public String toString() {
        String out = "";
        out += ("#" + idx + " Combo (" + price + ")\n");
        out += "   " + burger + "\n";
        out += "   " + snack + "\n";
        out += "   " + drink;
        return out;
    }

    public String getCombo() {
        return (this + "\n");
    }
}
