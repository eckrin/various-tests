import tests.template_method_pattern.LogTemplate;

public class Main {
    public static void main(String[] args) {
        orderItem();
    }

    private static void orderItem() {

        LogTemplate<Void> template = new LogTemplate<>() {
            @Override
            protected Void call() { // 실제 비즈니스 로직
                System.out.println("this is core business pattern");
                return null;
            }
        };

        template.log();
    }
}