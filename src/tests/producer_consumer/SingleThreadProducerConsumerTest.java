package tests.producer_consumer;

public class SingleThreadProducerConsumerTest {

    public static void producerConsumerTest() {
        MyProducer mp = new MyProducer();
        MyConsumer mc = new MyConsumer();

        mc.consume(mp.produce());
    }

    static class MyProducer {
        int var = 0;

        public int produce() {
            return ++var;
        }
    }

    static class MyConsumer {
        void consume(int var) {
            System.out.println("consumer: " + var);
        }
    }
}
