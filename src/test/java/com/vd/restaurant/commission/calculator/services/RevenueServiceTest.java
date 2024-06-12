package com.vd.restaurant.commission.calculator.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.vd.restaurant.commission.calculator.entities.MenuItem;
import com.vd.restaurant.commission.calculator.entities.Order;
import com.vd.restaurant.commission.calculator.entities.OrderItem;
import com.vd.restaurant.commission.calculator.exceptions.OrderNotFoundException;

@SpringBootTest
public class RevenueServiceTest {

    @InjectMocks
    private RevenueService revenueService;

    @Mock
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCalculateTotalRevenue() {
        MenuItem menuItem1 = new MenuItem();
        menuItem1.setPrice(10.5);
        OrderItem orderItem1 = new OrderItem();
        orderItem1.setMenuItem(menuItem1);
        orderItem1.setQuantity(2);

        MenuItem menuItem2 = new MenuItem();
        menuItem2.setPrice(8.0);
        OrderItem orderItem2 = new OrderItem();
        orderItem2.setMenuItem(menuItem2);
        orderItem2.setQuantity(3);

        Order order = new Order();
        order.setItems(Arrays.asList(orderItem1, orderItem2));

        when(orderService.getAllOrders()).thenReturn(Arrays.asList(order));

        double result = revenueService.calculateTotalRevenue();
        assertEquals(45.0, result);
    }

    @Test
    public void testCalculateTotalCommissionedRevenue() {
        RevenueService spyService = spy(revenueService);
        doReturn(100.0).when(spyService).calculateTotalRevenue();

        double result = spyService.calculateTotalCommissionedRevenue();
        assertEquals(12.0, result);
    }
    
    @Test
    public void testCalculateRevenueOfOrder() {
        MenuItem menuItem1 = new MenuItem();
        menuItem1.setPrice(10.5);
        OrderItem orderItem1 = new OrderItem();
        orderItem1.setMenuItem(menuItem1);
        orderItem1.setQuantity(2);

        MenuItem menuItem2 = new MenuItem();
        menuItem2.setPrice(8.0);
        OrderItem orderItem2 = new OrderItem();
        orderItem2.setMenuItem(menuItem2);
        orderItem2.setQuantity(3);

        Order order = new Order();
        order.setItems(Arrays.asList(orderItem1, orderItem2));

        when(orderService.getOrderById(1L)).thenReturn(Optional.of(order));

        double result = revenueService.calculateRevenueOfOrder(1L);
        assertEquals(45.0, result);
    }

    @Test
    public void testCalculateRevenueOfOrder_OrderNotFound() {
        when(orderService.getOrderById(1L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> revenueService.calculateRevenueOfOrder(1L));
    }

    @Test
    public void testCalculateCommissionedOfOrder() {
        RevenueService spyService = spy(revenueService);
        doReturn(100.0).when(spyService).calculateRevenueOfOrder(1L);

        double result = spyService.calculateCommissionedOfOrder(1L);
        assertEquals(12.0, result);
    }

    @Test
    public void testCalculateCommissionedOfOrder_OrderNotFound() {
        when(orderService.getOrderById(1L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> revenueService.calculateCommissionedOfOrder(1L));
    }
}