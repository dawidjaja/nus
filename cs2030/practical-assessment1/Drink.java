public class Drink extends Food {
    public Drink(int idx, String desc, int price) {
        super(idx, desc, price);
    }

    @Override
    public String toString() {
        return ("#" + idx + " Drink: " + desc + " (" + price + ")");
    }

    @Override
    public String getDrink() {
        return (this + "\n");
    }
}
