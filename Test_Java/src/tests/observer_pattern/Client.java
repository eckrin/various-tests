package tests.observer_pattern;

public class Client {

    private String name;

    public Client(String name) {
        this.name = name;
    }

    public void inform() {
        System.out.println("notice to "+name);
    }
}
