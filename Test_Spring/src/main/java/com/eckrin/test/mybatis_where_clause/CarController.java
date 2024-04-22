package com.eckrin.test.mybatis_where_clause;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/car")
public class CarController {

    private final CarService carService;

    @DeleteMapping
    public List<CarDto> deleteCar(
            @RequestParam(required = false) String name
    ) {
        return carService.deleteCarAndGetResult(name);
    }
}
