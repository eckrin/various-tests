package com.eckrin.test.async_transaction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/food")
@Slf4j
public class FoodController {

    private final FoodService foodService;

    @GetMapping("/test")
    public List<FoodDto> getFoods() {
        try {
            return foodService.getFoods();
        } catch(Exception e) {
            log.info("exception handled in getFoodsController");
            return null;
        }
    }
}
