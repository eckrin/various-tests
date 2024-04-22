package com.eckrin.test.mybatis_where_clause;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
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
    private final EntityManager em;

    @PostConstruct
    public void init() {
        carRepository.deleteAllInBatch();
//        saveCars();
    }

    @Transactional
    public List<CarDto> deleteCarAndGetResult(String name) {
        // JPA를 사용하여 차 저장
        saveCars();
        em.flush();
        // MyBatis를 사용하여 차 삭제
        carMapper.deleteByName(name);
        em.flush();
        // JPA를 사용하여 차 조회후 반환
        return carRepository.findAll().stream()
                .map(c -> new CarDto(c.getId(), c.getName()))
                .collect(Collectors.toList());
//        return null;
    }

    private void saveCars() {
        carRepository.save(new Car(null, "포르쉐"));
        carRepository.save(new Car(null, "벤틀리"));
        carRepository.save(new Car(null, "애스던 마틴"));
    }

}
