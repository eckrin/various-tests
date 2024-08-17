package tests.blocking_queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BlockingQueueProducerConsumer {

    public static void main(String[] args) {
        // 크기가 5인 ArrayBlockingQueue 생성
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5);

        // 생산자 스레드
        Thread producer = new Thread(() -> {
            try {
                for (int i=0; i<10; i++) {
                    System.out.println("[Produce]: " + i);
                    queue.put(i);  // 큐가 가득 차면 대기
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // 소비자 스레드
        Thread consumer = new Thread(() -> {
            try {
                for (int i=0; i<10; i++) {
                    Integer value = queue.take();  // 큐가 비어 있으면 대기
                    System.out.println("[Consumed]: " + value);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();
    }
}
