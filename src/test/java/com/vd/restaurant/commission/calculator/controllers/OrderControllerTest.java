package com.vd.restaurant.commission.calculator.controllers;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vd.restaurant.commission.calculator.entities.Order;
import com.vd.restaurant.commission.calculator.entities.OrderItem;
import com.vd.restaurant.commission.calculator.services.OrderService;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @MockBean
    private OrderService orderService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new OrderController(orderService))
                .setControllerAdvice(new GenericExceptionHandler())
                .build();
    }

    @Test
    public void testCreateOrder() throws Exception {
        Order order = new Order();
        order.setId(1L);

        when(orderService.createOrder(any(Order.class))).thenReturn(order);

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testGetAllOrders() throws Exception {
        Order order1 = new Order();
        order1.setId(1L);
        
        Order order2 = new Order();
        order2.setId(2L);

        when(orderService.getAllOrders()).thenReturn(Arrays.asList(order1, order2));

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    public void testGetOrderById() throws Exception {
        Order order = new Order();
        order.setId(1L);

        when(orderService.getOrderById(1L)).thenReturn(Optional.of(order));

        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testDeleteOrder() throws Exception {
        when(orderService.getOrderById(1L)).thenReturn(Optional.of(new Order()));

        mockMvc.perform(delete("/orders/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testAddOrderItem() throws Exception {
        Order order = new Order();
        order.setId(1L);

        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);

        when(orderService.addOrderItem(anyLong(), any(OrderItem.class))).thenReturn(order);

        mockMvc.perform(put("/orders/1/add-item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderItem)))
                .andExpect(status().isOk());
    }

    @Test
    public void testRemoveOrderItem() throws Exception {
        Order order = new Order();
        order.setId(1L);

        when(orderService.removeOrderItem(anyLong(), anyLong())).thenReturn(order);

        mockMvc.perform(delete("/orders/1/remove-item/1"))
                .andExpect(status().isOk());
    }
}
