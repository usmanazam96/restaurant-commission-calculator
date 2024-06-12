package com.vd.restaurant.commission.calculator.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vd.restaurant.commission.calculator.entities.MenuItem;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
}
