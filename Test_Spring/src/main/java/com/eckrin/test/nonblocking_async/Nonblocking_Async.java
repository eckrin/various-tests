package com.eckrin.test.nonblocking_async;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class Nonblocking_Async {

    private static ExecutorService executor = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            // 비동기 작업 수행
            try {
                Thread.sleep(2000); // 작업 시뮬레이션
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return 42;
        }, executor);

        // 비동기 작업이 완료된 후 처리할 작업 등록
        future.thenApply(result -> {
            log.info("Result: {}", result);
            return result * 2;
        }).thenAccept(finalResult -> {
            log.info("Final Result: {}", finalResult);
        });

        // Main 스레드는 다른 작업을 계속 수행할 수 있습니다.
        log.info("Main thread is free to do other work");

        // Executor 서비스 종료
        executor.shutdown();
    }
}