package tests.producer_consumer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 크기가 제한된 큐를 중심으로, Producer는 데이터를 넣고, Consumer는 데이터를 처리한다.
 */
public class MultiThreadProducerConsumerTest {

    public static void producerConsumerTest() {
        Producer producer = new Producer();
        producer.setName("Producer1");
        producer.start();

        Consumer consumer1 = new Consumer(producer);
        consumer1.setName("Consumer1");
        consumer1.start();

        Consumer consumer2 = new Consumer(producer);
        consumer2.setName("Consumer2");
        consumer2.start();

        Consumer consumer3 = new Consumer(producer);
        consumer3.setName("Consumer3");
        consumer3.start();
    }

    static class Producer extends Thread {
        private static final int MAX_SIZE = 3;
        private final List<String> queue = new ArrayList<>();

        @Override
        public void run() {
            try {
                while (true){
                    produce();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        // Produce Log
        private synchronized void produce() throws Exception {
            while (queue.size() == MAX_SIZE){
                System.out.println("Queue limit reached. Waiting for consumer");
                wait();
                System.out.println("Producer got notification from consumer");
            }
            String data = LocalDateTime.now().toString();
            queue.add(data);
            System.out.println("Producer produced data");
            notify();
        }

        // Consume Log
        public synchronized String consume() throws Exception {
            notify(); // wait()된 쓰레드들 중 하나 깨우기
            while (queue.isEmpty()){
                wait();
            }
            String data = queue.get(0);
            queue.remove(data);
            return data;
        }
    }

    static class Consumer extends Thread {
        private final Producer producer;

        public Consumer(Producer producer) {
            this.producer = producer;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String data = producer.consume();
                    System.out.println("Consumed by : " + Thread.currentThread().getName() + " data : " + data);
                }
            } catch (Exception e) {

            }
        }
    }
}
