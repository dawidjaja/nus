public class Snack extends Food {
    public Snack(int idx, String desc, int price) {
        super(idx, desc, price);
    }

    @Override
    public String toString() {
        return ("#" + idx + " Snack: " + desc + " (" + price + ")");
    }

    @Override
    public String getSnack() {
        return (this + "\n");
    }
}
