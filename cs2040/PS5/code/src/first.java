
public class Main {
    public static void main(String args[]){
        if(args != null && args.length > 0) {
            int result = MySpeedDemon.speedMatch(args[0]);
            System.out.print(result);
        }else{
            throw new IllegalArgumentException();
        }
    }
}
