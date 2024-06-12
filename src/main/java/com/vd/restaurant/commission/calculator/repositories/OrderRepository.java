package com.vd.restaurant.commission.calculator.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vd.restaurant.commission.calculator.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}