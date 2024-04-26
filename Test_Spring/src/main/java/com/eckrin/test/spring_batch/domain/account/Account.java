package com.eckrin.test.spring_batch.domain.account;

import com.eckrin.test.spring_batch.domain.order.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Getter
@ToString
@Entity
@Table(name = "accounts")
@NoArgsConstructor
public class Account {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderItem;
    private Long price;
    private Date orderDate;
    private Date accountDate;

    public Account(Order order) {
        this.id = order.getId();
        this.orderItem = order.getOrderItem();
        this.price = order.getPrice();
        this.orderDate = order.getOrderDate();
        this.accountDate = new Date();
    }
}
