package com.vd.restaurant.commission.calculator.controllers;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.vd.restaurant.commission.calculator.services.RevenueService;

@WebMvcTest(RevenueController.class)
public class RevenueControllerTest {

    @MockBean
    private RevenueService revenueService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new RevenueController(revenueService))
                .setControllerAdvice(new GenericExceptionHandler())
                .build();
    }

    @Test
    public void testCalculateTotalRevenue() throws Exception {
        when(revenueService.calculateTotalRevenue()).thenReturn(100.0);

        mockMvc.perform(get("/revenue/total"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("100.0"));
    }

    @Test
    public void testCalculateCommissionedRevenue() throws Exception {
        when(revenueService.calculateTotalCommissionedRevenue()).thenReturn(12.0);

        mockMvc.perform(get("/revenue/commission"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("12.0"));
    }

    @Test
    public void testCalculateRevenueOfOrder() throws Exception {
        when(revenueService.calculateRevenueOfOrder(anyLong())).thenReturn(45.0);

        mockMvc.perform(get("/revenue/order/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("45.0"));
    }

    @Test
    public void testCalculateCommissionedOfOrder() throws Exception {
        when(revenueService.calculateCommissionedOfOrder(anyLong())).thenReturn(5.4);

        mockMvc.perform(get("/revenue/order/1/commission"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("5.4"));
    }
}
