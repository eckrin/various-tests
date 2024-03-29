package com.eckrin.test.async_transaction;

import com.eckrin.test.common.Food;
import com.eckrin.test.common.FoodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FoodReleaseService {

    private final FoodRepository foodRepository;

    @Async
    public void saveDontGas(Food food) throws InterruptedException {
        foodRepository.save(new Food(null, "돈가스"));
        log.info("비동기 쓰레드 id = {}", Thread.currentThread().getId());
        throw new InterruptedException("saveDontGas exception caused");
    }
}
