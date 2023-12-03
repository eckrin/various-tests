package com.eckrin.test.mybatis_where_clause;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
}
