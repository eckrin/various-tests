package com.eckrin.test.common;

import com.eckrin.test.deserialization.FoodDeserializationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Slf4j
public class FoodController {

    private final FoodService foodService;

    @GetMapping("async_transaction")
    public List<FoodDto> getFoodsAsync() {
        try {
            return foodService.getFoodsAsync();
        } catch(Exception e) {
            log.info("exception handled in getFoodsController");
            return null;
        }
    }

    @GetMapping("deserialization")
    public List<FoodDeserializationDto> getFoods() {
        return foodService.getFoods();
    }

    @PutMapping("deserialization")
    public FoodDeserializationDto saveFood(@RequestBody FoodDeserializationDto dto) {
        return foodService.saveFood(dto);
    }

    @PutMapping("dynamic_field")
    public Map<String, String> saveDynamicFood(@RequestBody Map<String, String> dynamicFood) {
        return foodService.saveDynamicFood(dynamicFood);
    }
}
