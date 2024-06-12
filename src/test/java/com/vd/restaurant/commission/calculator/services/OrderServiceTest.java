package com.vd.restaurant.commission.calculator.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
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
import com.vd.restaurant.commission.calculator.repositories.OrderRepository;

@SpringBootTest
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private MenuItemService menuItemService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateOrder() {
        Order order = new Order();
        OrderItem orderItem = new OrderItem();
        MenuItem menuItem = new MenuItem();
        menuItem.setId(1L);
        menuItem.setStock(10);
        orderItem.setMenuItem(menuItem);
        orderItem.setQuantity(5);
        order.getItems().add(orderItem);

        when(menuItemService.updateMenuItemStock(1L, -5)).thenReturn(menuItem);
        when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.createOrder(order);
        assertEquals(order, result);
        verify(menuItemService).updateMenuItemStock(1L, -5);
    }

    @Test
    public void testGetAllOrders() {
        List<Order> orders = Arrays.asList(new Order(), new Order());
        when(orderRepository.findAll()).thenReturn(orders);

        List<Order> result = orderService.getAllOrders();
        assertEquals(orders, result);
    }

    @Test
    public void testGetOrderById() {
        Order order = new Order();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Optional<Order> result = orderService.getOrderById(1L);
        assertTrue(result.isPresent());
        assertEquals(order, result.get());
    }

    @Test
    public void testDeleteOrder() {
        Order order = new Order();
        OrderItem orderItem = new OrderItem();
        MenuItem menuItem = new MenuItem();
        menuItem.setId(1L);
        orderItem.setMenuItem(menuItem);
        orderItem.setQuantity(5);
        order.getItems().add(orderItem);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        doNothing().when(orderRepository).deleteById(1L);
        when(menuItemService.updateMenuItemStock(1L, 5)).thenReturn(menuItem);

        assertDoesNotThrow(() -> orderService.deleteOrder(1L));
        verify(menuItemService).updateMenuItemStock(1L, 5);
    }

    @Test
    public void testDeleteOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.deleteOrder(1L));
    }

    @Test
    public void testAddOrderItem() {
        Order order = new Order();
        OrderItem orderItem = new OrderItem();
        MenuItem menuItem = new MenuItem();
        menuItem.setId(1L);
        orderItem.setMenuItem(menuItem);
        orderItem.setQuantity(5);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);
        when(menuItemService.updateMenuItemStock(1L, -5)).thenReturn(menuItem);

        Order result = orderService.addOrderItem(1L, orderItem);
        assertTrue(result.getItems().contains(orderItem));
        verify(menuItemService).updateMenuItemStock(1L, -5);
    }

    @Test
    public void testRemoveOrderItem() {
        Order order = new Order();
        OrderItem orderItem = new OrderItem();
        MenuItem menuItem = new MenuItem();
        menuItem.setId(1L);
        orderItem.setId(1L);
        orderItem.setMenuItem(menuItem);
        orderItem.setQuantity(5);
        order.getItems().add(orderItem);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);
        when(menuItemService.updateMenuItemStock(1L, 5)).thenReturn(menuItem);

        Order result = orderService.removeOrderItem(1L, 1L);
        assertFalse(result.getItems().contains(orderItem));
        verify(menuItemService).updateMenuItemStock(1L, 5);
    }
}
