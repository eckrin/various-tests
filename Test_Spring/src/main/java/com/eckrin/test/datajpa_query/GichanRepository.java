package com.eckrin.test.datajpa_query;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GichanRepository extends JpaRepository<Gichan, Long> {
    List<Gichan> findTop3ByNameOrderByVersionDesc(String name);
}
