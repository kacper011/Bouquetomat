package com.example.bouquetomat.controller;

import com.example.bouquetomat.model.Bouquet;
import com.example.bouquetomat.model.BouquetStatus;
import com.example.bouquetomat.service.BouquetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import com.example.bouquetomat.controller.BouquetController;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.*;


@WebMvcTest(BouquetController.class)
public class BouquetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BouquetService bouquetService;

    @InjectMocks
    private BouquetController bouquetController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

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

    @DisplayName("Create Bouquet Should Return Created Bouquet")
    @Test
    public void testCreateBouquetShouldReturnCreatedBouquet() throws Exception {

        //Given
        Bouquet bouquet = new Bouquet(1L, "Rose Bouquet", 2, 29.99, true, BouquetStatus.AVAILABLE);

        //When
        when(bouquetService.createBouquet(any(Bouquet.class))).thenReturn(bouquet);

        //Then
        mockMvc.perform(post("/api/bouquets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bouquet)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Rose Bouquet"))
                .andExpect(jsonPath("$.price").value(29.99));

    }

    @DisplayName("Buy Bouquet Should Return Ok With Result")
    @Test
    public void testBuyBouquetShouldReturnOkWithResult() throws Exception {

        //Given
        Long bouquetId = 1L;
        String expectedResponse = "Bouquet purchased successfully!";

        //When
        when(bouquetService.buyBouquet(bouquetId)).thenReturn(expectedResponse);

        //Then
        mockMvc.perform(post("/api/bouquets/buy/{bouquetId}", bouquetId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));

        verify(bouquetService, times(1)).buyBouquet(bouquetId);
    }

    @DisplayName("Delete Bouquet Should Return Success Message")
    @Test
    public void testDeleteBouquetShouldReturnSuccessMessage() throws Exception {

        //Given
        Long bouquetId = 1L;
        String expectedMessage = "Bukiet o ID " + bouquetId + " został usunięty.";

        //When
        when(bouquetService.deleteBouquet(bouquetId)).thenReturn(expectedMessage);

        //Then
        mockMvc.perform(delete("/api/bouquets/delete/{bouquetId}", bouquetId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedMessage));

        verify(bouquetService, times(1)).deleteBouquet(bouquetId);
    }

}