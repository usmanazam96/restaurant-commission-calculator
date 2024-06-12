package com.vd.restaurant.commission.calculator.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vd.restaurant.commission.calculator.entities.Order;
import com.vd.restaurant.commission.calculator.exceptions.OrderNotFoundException;

@Service
public class RevenueService {
	
	@Autowired
    private OrderService orderService;	
	
    public double calculateTotalRevenue() {
        List<Order> orders = orderService.getAllOrders();
        double totalRevenue = 0.0;
        for (Order order : orders) {
            totalRevenue += order.getItems().stream()
                .mapToDouble(item -> item.getMenuItem().getPrice() * item.getQuantity())
                .sum();
        }
        return totalRevenue;
    }

    public double calculateTotalCommissionedRevenue() {
        double totalRevenue = calculateTotalRevenue();
        return totalRevenue * 0.12; 
    }
    
    public double calculateRevenueOfOrder(long orderId) {
    	Order order = orderService.getOrderById(orderId)
				.orElseThrow(() -> new OrderNotFoundException("Order with id " + orderId + " not found"));
        double orderRevenue =  order.getItems().stream()
                .mapToDouble(item -> item.getMenuItem().getPrice() * item.getQuantity())
                .sum();
        return orderRevenue;
    }
    
    public double calculateCommissionedOfOrder(long orderId) {
        double orderRevenue = calculateRevenueOfOrder(orderId);
        return orderRevenue * 0.12; 
    }
    
    
    
}
