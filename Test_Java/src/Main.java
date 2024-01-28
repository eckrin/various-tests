import tests.strategy_pattern.*;

public class Main {
    public static void main(String[] args) {
        LogContext context = new LogContext(new Strategy001());
        context.execute(); // 001
        context.execute(); // 001

        LogContext_Callback context_callback = new LogContext_Callback();
        context_callback.execute(new Strategy001()); // 001
        context_callback.execute(new Strategy002()); // 002
    }
}