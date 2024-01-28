package tests.strategy_pattern;

public class LogContext {

    private final LogStrategy strategy;

    public LogContext(LogStrategy strategy) {
        this.strategy = strategy;
    }

    public void execute() {
        strategy.call();
    }
}
