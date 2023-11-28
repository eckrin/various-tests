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
    public List<FoodDto> getFoods() {
        foodRepository.save(new Food(null, "엽떡"));

        try {
            foodReleaseService.saveDontGas(new Food(null, "돈가스"));
        } catch (Exception e) {
            log.error("exception caused: {}", e.getMessage());
        }

        return foodRepository.findAll()
                .stream()
                .map(f -> new FoodDto(f.getId(), f.getName()))
                .collect(Collectors.toList());
    }
}
