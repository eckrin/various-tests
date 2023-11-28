package com.eckrin.test.async_transaction;

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
        throw new InterruptedException("exception caused");
//        foodRepository.save(new Food(null, "돈가스"));
    }
}
