package app;

public class App {
    public void doSomething() {
        System.out.println("[App] Begin");
        System.out.println("[App] Logic");
        toBeReplaced();
        someComplicatedLogic();
        System.out.println("[App] End");
    }

    public void someComplicatedLogic() {


        System.out.println("[App] Complicated Logic");


    }

    public void toBeReplaced() {
        System.out.println("[App: To be Replaced] This message should not appear");
        System.out.println("[App: To be Replaced] This message is to be replaced by Agent");
    }
}
