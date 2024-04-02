package com.eckrin.test.async_future;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@SpringBootTest
public class AsyncFutureTest {

    @Test
    @DisplayName("runAsync는 반환값이 없는 경우 사용 가능")
    void testRunAsync() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            sleep(5000);
            log.info("1) Thread: {}", Thread.currentThread().getName());
        });
        log.info("2) Thread: {}", Thread.currentThread().getName());

        future.get(); // 받아올때까지 Test thread block
        log.info("3) Thread: {}", Thread.currentThread().getName());
    }

    @Test
    @DisplayName("supplyAsync는 반환값이 존재하는 경우 사용 가능")
    void runSupplyAsync() throws ExecutionException, InterruptedException {

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            sleep(5000);
            log.info("1) Thread: {}", Thread.currentThread().getName());
            return "Thread: " + Thread.currentThread().getName();
        });

        log.info("2) Thread: {}", Thread.currentThread().getName());
        log.info("3) future.get(): {}", future.get());
        log.info("4) Thread: {}", Thread.currentThread().getName());
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
