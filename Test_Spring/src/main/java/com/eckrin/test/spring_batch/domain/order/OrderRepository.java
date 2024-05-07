package com.eckrin.test.spring_batch.domain.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByIdBefore(Long id, PageRequest pageRequest);
}
