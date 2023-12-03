package com.eckrin.test.mybatis_where_clause;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/car")
public class CarController {

    private final CarService carService;

    @GetMapping("/delete/{name}")
    public List<CarDto> deleteCar(@PathVariable("name") String name) {
        return carService.deleteCarAndGetResult(name);
    }
}
