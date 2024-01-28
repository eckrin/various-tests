package tests.strategy_pattern;

public class LogContext_Callback {

    public void execute(LogStrategy strategy) {
        strategy.call();
    }
}
