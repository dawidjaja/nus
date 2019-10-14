public class Burger extends Food {
    public Burger(int idx, String desc, int price) {
        super(idx, desc, price);
    }

    @Override
    public String toString() {
        return ("#" + idx + " Burger: " + desc + " (" + price + ")");
    }

    @Override
    public String getBurger() {
        return (this + "\n");
    }
}
