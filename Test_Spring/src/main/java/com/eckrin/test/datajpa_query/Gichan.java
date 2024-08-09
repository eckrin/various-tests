package com.eckrin.test.datajpa_query;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Gichan {

    @Id @GeneratedValue
    private Long id;

    private Long version;

    private String name;
}
