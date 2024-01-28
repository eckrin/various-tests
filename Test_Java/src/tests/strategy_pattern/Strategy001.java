package tests.strategy_pattern;

public class Strategy001 implements LogStrategy{

    @Override
    public void call() {
        System.out.println("call 001");
    }
}
