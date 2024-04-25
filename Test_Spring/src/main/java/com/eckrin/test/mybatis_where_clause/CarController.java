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

    /**
     * 커스텀한 DatasourceConfig가 제대로 동작하는지 롤백 테스트
     */
    @PostMapping("/save")
    public CarDto saveFood_Rollback_MyBatis(@RequestBody CarDto dto) {
        return carService.saveCar_Rollback_MyBatis(dto);
    }
}
