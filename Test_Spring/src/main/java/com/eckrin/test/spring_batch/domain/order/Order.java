package com.eckrin.test.spring_batch.domain.order;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@Getter
@ToString
@Entity
@Table(name = "orders")
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderItem;
    private Long price;
    private Date orderDate;
}
