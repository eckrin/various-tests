package com.eckrin.test.mybatis_where_clause;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CarMapper {

    void deleteByName(String carName);
    Long insertCar(CarDto dto);
    Car selectCarById(Long id);
}
