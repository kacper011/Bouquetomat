package com.example.bouquetomat.service;

import com.example.bouquetomat.model.Bouquet;
import com.example.bouquetomat.model.BouquetOrder;
import com.example.bouquetomat.model.BouquetStatus;
import com.example.bouquetomat.repository.BouquetRepository;
import com.example.bouquetomat.repository.NotificationRepository;
import com.example.bouquetomat.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BouquetService {

    private final BouquetRepository bouquetRepository;
    private final OrderRepository orderRepository;
    private final NotificationService notificationService;
    private final NotificationRepository notificationRepository;


    public BouquetService(BouquetRepository bouquetRepository, OrderRepository orderRepository, NotificationService notificationService, NotificationRepository notificationRepository) {
        this.bouquetRepository = bouquetRepository;
        this.orderRepository = orderRepository;
        this.notificationService = notificationService;
        this.notificationRepository = notificationRepository;
    }

    @Transactional
    public String buyBouquet(Long bouquetID) {

        Bouquet bouquet = bouquetRepository.findById(bouquetID)
                .orElse(null);

        if (!bouquet.getIsAvailable()) {
            return "Ten bukiet jest już niedostępny";
        }

        BouquetOrder order = new BouquetOrder(bouquet, bouquet.getPrice());
        orderRepository.save(order);

        if (bouquet.getIsAvailable() != null && bouquet.getIsAvailable()) {
            bouquet.setIsAvailable(false);
            bouquet.setStatus(BouquetStatus.SOLD);
            bouquetRepository.save(bouquet);
            notificationService.sendNotification(order);
            return "Bukiet z numerem " + bouquetID + " został zakupiony!";
        } else {
            return "Bukiet z id " + bouquetID + " jest już niedostępny.";
        }
    }

    public List<Bouquet> getAllBouquets() {
        return bouquetRepository.findByStatus(BouquetStatus.AVAILABLE);
    }

    public Bouquet createBouquet(Bouquet bouquet) {

        if (bouquet.getSlotNumber() < 1 || bouquet.getSlotNumber() > 6) {
            throw new IllegalArgumentException("Numer slotu musi być pomiędzy 1 a 6.");
        }

        Bouquet existingBouquet = bouquetRepository.findBySlotNumberAndStatus(bouquet.getSlotNumber(), BouquetStatus.AVAILABLE)
                .orElse(null);

        if (existingBouquet != null) {
            throw new IllegalArgumentException("Okienko numer " + bouquet.getSlotNumber() + " jest już zajęte przez dostępny bukiet.");
        }

        bouquet.setStatus(BouquetStatus.AVAILABLE);

        return bouquetRepository.save(bouquet);
    }


}
