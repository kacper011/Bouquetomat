package com.example.bouquetomat.service;

import com.example.bouquetomat.model.Bouquet;
import com.example.bouquetomat.model.BouquetOrder;
import com.example.bouquetomat.model.BouquetStatus;
import com.example.bouquetomat.repository.BouquetRepository;
import com.example.bouquetomat.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
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

    //Unit Tests

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
        assertEquals("The bouquet with number 1 has been purchased!", result);
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
        assertEquals("This bouquet is no longer available.", result);
        verify(orderRepository, never()).save(any(BouquetOrder.class));
        verify(notificationService, never()).sendNotification(any(BouquetOrder.class));
        verify(bouquetRepository, never()).save(bouquet);
    }

    @DisplayName("Buy Bouquet Bouquet Not Found")
    @Test
    void testBuyBouquetBouquetNotFound() {

        //Given
        Long bouquetId = 2L;

        when(bouquetRepository.findById(bouquetId)).thenReturn(Optional.empty());

        //When
        Exception exception = assertThrows(NullPointerException.class, () -> {
            bouquetService.buyBouquet(bouquetId);
        });

        //Then
        assertNotNull(exception);
        verify(orderRepository, never()).save(any(BouquetOrder.class));
        verify(notificationService, never()).sendNotification(any(BouquetOrder.class));
        verify(bouquetRepository, never()).save(any(Bouquet.class));
    }

    @DisplayName("Get All Bouquets")
    @Test
    void testGetAllBouquets() {

        //Given
        Bouquet bouquet1 = new Bouquet();
        bouquet1.setId(1L);
        bouquet1.setName("Roses");
        bouquet1.setIsAvailable(true);

        Bouquet bouquet2 = new Bouquet();
        bouquet2.setId(2L);
        bouquet2.setName("Tulips");
        bouquet2.setIsAvailable(true);

        List<Bouquet> mockBouquets = Arrays.asList(bouquet1, bouquet2);
        when(bouquetRepository.findByStatus(BouquetStatus.AVAILABLE)).thenReturn(mockBouquets);

        //When
        List<Bouquet> result = bouquetService.getAllBouquets();

        //Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Roses", result.get(0).getName());
        assertEquals("Tulips", result.get(1).getName());

        verify(bouquetRepository, times(1)).findByStatus(BouquetStatus.AVAILABLE);
    }

    @DisplayName("Create Bouquet Success")
    @Test
    void testCreateBouquetSuccess() {

        //Given
        Bouquet bouquet = new Bouquet();
        bouquet.setSlotNumber(3);
        bouquet.setName("Roses");

        when(bouquetRepository.findBySlotNumberAndStatus(3, BouquetStatus.AVAILABLE)).thenReturn(Optional.empty());

        when(bouquetRepository.save(bouquet)).thenReturn(bouquet);

        //When
        Bouquet result = bouquetService.createBouquet(bouquet);

        //Then
        assertNotNull(result);
        assertEquals(3, result.getSlotNumber());
        assertEquals(BouquetStatus.AVAILABLE, result.getStatus());

        verify(bouquetRepository, times(1)).findBySlotNumberAndStatus(3, BouquetStatus.AVAILABLE);
        verify(bouquetRepository, times(1)).save(bouquet);
    }

    @DisplayName("Create Bouquet Invalid Slot Number")
    @Test
    void testCreateBouquetInvalidSlotNumber() {

        //Given
        Bouquet bouquet = new Bouquet();
        bouquet.setSlotNumber(7);

        //When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> bouquetService.createBouquet(bouquet));

        assertEquals("The slot number must be between 1 and 6.", exception.getMessage());
        verifyNoInteractions(bouquetRepository);
    }

    @DisplayName("Create Bouquet Slot Already Taken")
    @Test
    void testCreateBouquetSlotAlreadyTaken() {

        //Given
        Bouquet bouquet = new Bouquet();
        bouquet.setSlotNumber(3);
        bouquet.setName("Roses");

        Bouquet existingBouquet = new Bouquet();
        existingBouquet.setSlotNumber(3);
        existingBouquet.setStatus(BouquetStatus.AVAILABLE);

        when(bouquetRepository.findBySlotNumberAndStatus(3, BouquetStatus.AVAILABLE))
                .thenReturn(Optional.of(existingBouquet));

        //When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> bouquetService.createBouquet(bouquet));

        assertEquals(
                "Slot number 3 is already occupied by an available bouquet.", exception.getMessage());
        verify(bouquetRepository, times(1)).findBySlotNumberAndStatus(3, BouquetStatus.AVAILABLE);

    }

    @DisplayName("Delete Bouquet Success")
    @Test
    void testDeleteBouquetSuccess() {

        //Given
        Long bouquetId = 1L;
        when(bouquetRepository.existsById(bouquetId)).thenReturn(true);

        //When
        String result = bouquetService.deleteBouquet(bouquetId);

        //Then
        verify(bouquetRepository).deleteById(bouquetId);
        assertEquals("The bouquet with ID " + bouquetId + " has been successfully deleted.", result);
    }

    @DisplayName("Delete Bouquet Not Found")
    @Test
    void testDeleteBouquetNotFound() {

        //Given
        Long bouquetId = 1L;
        when(bouquetRepository.existsById(bouquetId)).thenReturn(false);

        //When & Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
                bouquetService.deleteBouquet(bouquetId);
        });
        assertEquals("The bouquet with ID " + bouquetId + " was not found.", exception.getMessage());

        verify(bouquetRepository, never()).deleteById(anyLong());

    }
}