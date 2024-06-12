package com.vd.restaurant.commission.calculator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vd.restaurant.commission.calculator.services.RevenueService;

@RestController
@RequestMapping("/revenue")
public class RevenueController {

    @Autowired
    private RevenueService revenueService;

    @GetMapping("/total")
    public ResponseEntity<Double> calculateTotalRevenue() {
        double totalRevenue= revenueService.calculateTotalRevenue();
        return new ResponseEntity<>(totalRevenue, HttpStatus.OK);
    }

    @GetMapping("/commission")
    public  ResponseEntity<Double>  calculateCommissionedRevenue() {
        double commission = revenueService.calculateTotalCommissionedRevenue();
        return new ResponseEntity<>(commission, HttpStatus.OK);
    }
    
    @GetMapping("/order/{orderId}")
    public ResponseEntity<Double> calculateRevenueOfOrder(@PathVariable long orderId) {
        double orderRevenue = revenueService.calculateRevenueOfOrder(orderId);
        return new ResponseEntity<>(orderRevenue, HttpStatus.OK);
    }

    @GetMapping("/order/{orderId}/commission")
    public ResponseEntity<Double> calculateCommissionedOfOrder(@PathVariable long orderId) {
        double orderCommission = revenueService.calculateCommissionedOfOrder(orderId);
        return new ResponseEntity<>(orderCommission, HttpStatus.OK);
    }
}