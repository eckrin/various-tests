package com.eckrin.test.mybatis_where_clause;

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
public class CarService {

    private final CarRepository carRepository;
    private final CarMapper carMapper;

    @PostConstruct
    public void init() {
        carRepository.deleteAllInBatch();
        carRepository.save(new Car(null, "포르쉐"));
        carRepository.save(new Car(null, "벤틀리"));
        carRepository.save(new Car(null, "애스던 마틴"));
    }

    @Transactional
    public List<CarDto> deleteCarAndGetResult(String name) {
        carMapper.deleteByName(name);
        return carRepository.findAll().stream()
                .map(c -> new CarDto(c.getId(), c.getName()))
                .collect(Collectors.toList());
//        return null;
    }


}
