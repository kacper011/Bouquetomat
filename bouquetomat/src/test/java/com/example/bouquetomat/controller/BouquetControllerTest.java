package com.example.bouquetomat.controller;

import com.example.bouquetomat.model.Bouquet;
import com.example.bouquetomat.model.BouquetStatus;
import com.example.bouquetomat.service.BouquetService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest
class BouquetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BouquetService bouquetService;

    @DisplayName("Get All Bouquets Should Return Bouquet List")
    @Test
    public void getAllBouquetsShouldReturnBouquetList() throws Exception {

        //Given
        Bouquet bouquet1 = new Bouquet(1L, "Rose Bouquet", 1, 29.99, true, BouquetStatus.AVAILABLE);
        Bouquet bouquet2 = new Bouquet(2L, "Tulip Bouquet", 2, 19.99, true, BouquetStatus.AVAILABLE);
        List<Bouquet> mockBouquets = Arrays.asList(bouquet1, bouquet2);

        //When
        when(bouquetService.getAllBouquets()).thenReturn(mockBouquets);

        //Then
        mockMvc.perform(get("/api/bouquets")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Status jako ResultMatcher
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Rose Bouquet"))
                .andExpect(jsonPath("$[0].price").value(29.99))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Tulip Bouquet"))
                .andExpect(jsonPath("$[1].price").value(19.99));

    }

}