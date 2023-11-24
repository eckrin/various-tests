package tests.producer_consumer;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.ObjIntConsumer;

public class ConsumerTest {

    public static void consumerTest() {
        Consumer<String> consumer = str -> System.out.println(str + " 8");
        consumer.accept("Java"); // accept 함수 구현

        BiConsumer<String, Integer> biConsumer = (str, num) -> System.out.println(str + " " + num);
        biConsumer.accept("Java", 8);

        DoubleConsumer doubleConsumer = d -> System.out.println("Java" + " " + d);
        doubleConsumer.accept(8.0);

        ObjIntConsumer<String> objIntConsumer = (t, i) -> System.out.println(t + " " + i);
        objIntConsumer.accept("Java", 8);
    }
}
