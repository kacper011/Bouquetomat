package com.example.bouquetomat.service;

import com.example.bouquetomat.model.Bouquet;
import com.example.bouquetomat.model.BouquetOrder;
import com.example.bouquetomat.model.BouquetStatus;
import com.example.bouquetomat.repository.BouquetRepository;
import com.example.bouquetomat.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BouquetServiceTest {

    @Mock
    private BouquetRepository bouquetRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private BouquetService bouquetService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @DisplayName("Buy Bouquet Successful Purchase")
    @Test
    void testBuyBouquetSuccessfulPurchase() {

        //Given
        Long bouquetId = 1L;
        Bouquet bouquet = new Bouquet();
        bouquet.setId(bouquetId);
        bouquet.setPrice(100.0);
        bouquet.setIsAvailable(true);

        when(bouquetRepository.findById(bouquetId)).thenReturn(Optional.of(bouquet));

        //When
        String result = bouquetService.buyBouquet(bouquetId);

        //Then
        assertEquals("Bukiet z numerem 1 został zakupiony!", result);
        assertFalse(bouquet.getIsAvailable());
        assertEquals(BouquetStatus.SOLD, bouquet.getStatus());
        verify(bouquetRepository, times(1)).save(bouquet);
        verify(orderRepository, times(1)).save(any(BouquetOrder.class));
        verify(notificationService, times(1)).sendNotification(any(BouquetOrder.class));
    }

    @DisplayName("Buy Bouquet Bouquet Not Available")
    @Test
    void testBuyBouquetBouquetNotAvailable() {

        //Given
        Long bouquetId = 2L;
        Bouquet bouquet = new Bouquet();
        bouquet.setId(bouquetId);
        bouquet.setIsAvailable(false);

        when(bouquetRepository.findById(bouquetId)).thenReturn(Optional.of(bouquet));

        //When
        String result = bouquetService.buyBouquet(bouquetId);

        //Then
        assertEquals("Ten bukiet jest już niedostępny", result);
        verify(orderRepository, never()).save(any(BouquetOrder.class));
        verify(notificationService, never()).sendNotification(any(BouquetOrder.class));
        verify(bouquetRepository, never()).save(bouquet);
    }


}