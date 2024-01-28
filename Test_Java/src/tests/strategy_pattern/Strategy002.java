package tests.strategy_pattern;

public class Strategy002 implements LogStrategy{

    @Override
    public void call() {
        System.out.println("call 002");
    }
}
