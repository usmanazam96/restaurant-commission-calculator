package com.vd.restaurant.commission.calculator.controllers;

import com.vd.restaurant.commission.calculator.exceptions.InvalidStockException;
import com.vd.restaurant.commission.calculator.exceptions.MenuItemNotFoundException;
import com.vd.restaurant.commission.calculator.exceptions.OrderNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GenericExceptionHandlerTest {

    @InjectMocks
    private GenericExceptionHandler exceptionHandler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHandleInvalidStockException() {
        InvalidStockException ex = new InvalidStockException("Invalid stock message");
        ResponseEntity<String> response = exceptionHandler.handleInvalidStateException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid stock message", response.getBody());
    }

    @Test
    public void testHandleOrderNotFoundException() {
        OrderNotFoundException ex = new OrderNotFoundException("Order not found");
        ResponseEntity<String> response = exceptionHandler.handleOrderNotFoundException(ex);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Order not found", response.getBody());
    }

    @Test
    public void testHandleMenuItemNotFoundException() {
        MenuItemNotFoundException ex = new MenuItemNotFoundException("Menu item not found");
        ResponseEntity<String> response = exceptionHandler.handleMenuItemNotFoundException(ex);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Menu item not found", response.getBody());
    }

    @Test
    public void testHandleGenericException() {
        Exception ex = new Exception("Generic exception message");
        ResponseEntity<String> response = exceptionHandler.handleGenericException(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Generic exception message", response.getBody());
    }
}
