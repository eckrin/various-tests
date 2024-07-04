package tests.synchronized_principal_test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class SyncronizedMain {
    private static int THREAD_COUNT = 200;
    public static Counter counter;

    public static void main(String[] args) throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(THREAD_COUNT);
        AtomicInteger cnt = new AtomicInteger(0);

        for(int j=0; j<10000; j++) {
            CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

            counter = new Counter();
            for (int i = 0; i < THREAD_COUNT; i++) {
                service.submit(() -> {
                    try {
                        counter.addOne();
                        counter.subOne();
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await();

//            System.out.println(counter.count);
            if(counter.count!=0) {
                cnt.compareAndSet(cnt.get(), cnt.get()+1);
            }
        }

        System.out.println("1만개 중 200이 아닌 개수 = " + cnt);
    }


    static class Counter {
        int count = 0;

        public synchronized void addOne() {
            count++;
        }

        public synchronized void subOne() {
            count--;
        }
    }
}

