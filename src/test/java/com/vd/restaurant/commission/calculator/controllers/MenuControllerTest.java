package com.vd.restaurant.commission.calculator.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import com.vd.restaurant.commission.calculator.entities.MenuItem;
import com.vd.restaurant.commission.calculator.services.MenuItemService;

@WebMvcTest(MenuController.class)
public class MenuControllerTest {

    @MockBean
    private MenuItemService menuItemService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new MenuController(menuItemService))
                .setControllerAdvice(new GenericExceptionHandler())
                .build();
    }

    @Test
    public void testAddMenuItem() throws Exception {
        MenuItem menuItem = new MenuItem();
        menuItem.setId(1L);
        menuItem.setName("Pizza");
        menuItem.setStock(10);
        menuItem.setPrice(9.99);

        when(menuItemService.addMenuItem(any(MenuItem.class))).thenReturn(menuItem);

        mockMvc.perform(post("/menu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menuItem)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Pizza"));
    }

    @Test
    public void testGetAllMenuItems() throws Exception {
        MenuItem menuItem1 = new MenuItem();
        menuItem1.setId(1L);
        menuItem1.setName("Pizza");
        
        MenuItem menuItem2 = new MenuItem();
        menuItem2.setId(2L);
        menuItem2.setName("Burger");

        when(menuItemService.getAllMenuItems()).thenReturn(Arrays.asList(menuItem1, menuItem2));

        mockMvc.perform(get("/menu"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Pizza"))
                .andExpect(jsonPath("$[1].name").value("Burger"));
    }

    @Test
    public void testGetMenuItemById() throws Exception {
        MenuItem menuItem = new MenuItem();
        menuItem.setId(1L);
        menuItem.setName("Pizza");

        when(menuItemService.getMenuItemById(1L)).thenReturn(Optional.of(menuItem));

        mockMvc.perform(get("/menu/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pizza"));
    }

    @Test
    public void testDeleteMenuItem() throws Exception {
        when(menuItemService.existsById(1L)).thenReturn(true);

        mockMvc.perform(delete("/menu/1"))
                .andExpect(status().isNoContent());
    }
}