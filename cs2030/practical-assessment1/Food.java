public class Food extends Menu {
    String desc;
    
    public Food(int idx, String desc, int price) {
        super(idx, price);
        this.desc = desc;
    }
}
