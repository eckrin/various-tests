package com.eckrin.test.async_transaction;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodReleaseService foodReleaseService;
    private final FoodRepository foodRepository;

    @PostConstruct
    public void init() {
        foodRepository.deleteAllInBatch();
    }

    @Transactional
    public List<FoodDto> getFoods() throws InterruptedException{
        // 데이터 1개 저장
        foodRepository.save(new Food(null, "엽떡"));
        log.info("트랜잭션 쓰레드 id = {}", Thread.currentThread().getId());

        // Async 동작중 오류 발생
        foodReleaseService.saveDontGas(new Food(null, "돈가스"));

        // db에 남아있는 리스트 반환
        return foodRepository.findAll()
                .stream()
                .map(f -> new FoodDto(f.getId(), f.getName()))
                .collect(Collectors.toList());
    }
}
