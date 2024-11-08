package com.example.bouquetomat.service;

import com.example.bouquetomat.model.Bouquet;
import com.example.bouquetomat.model.BouquetOrder;
import com.example.bouquetomat.model.BouquetStatus;
import com.example.bouquetomat.model.Notification;
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
    public String buyBouquet(Integer slotNumber) {

        Bouquet bouquet = bouquetRepository.findBySlotNumber(slotNumber)
                .orElse(null);

        if (bouquet == null) {
            return "Okienko numer " + slotNumber + " nie istnieje.";
        }

        BouquetOrder order = new BouquetOrder(bouquet, bouquet.getPrice());
        orderRepository.save(order);

        if (bouquet.getIsAvailable() != null && bouquet.getIsAvailable()) {
            bouquet.setIsAvailable(false);
            bouquet.setStatus(BouquetStatus.SOLD);
            bouquetRepository.save(bouquet);
            notificationService.sendNotification(order);
            return "Bukiet z numerem " + slotNumber + " został zakupiony!";
        } else {
            return "Bukiet w okienku numer " + slotNumber + " jest już niedostępny.";
        }
    }

    public String addBouquetToSlot(Integer slotNumber, String name, Double price) {

        Bouquet bouquet = bouquetRepository.findBySlotNumber(slotNumber)
                .orElse(null);

        if (bouquet == null) {
            bouquet = new Bouquet();
            bouquet.setSlotNumber(slotNumber);
            bouquet.setName(name);
            bouquet.setPrice(price);
            bouquet.setIsAvailable(true);
            bouquet.setStatus(BouquetStatus.AVAILABLE);
            bouquetRepository.save(bouquet);
            return "Nowy bukiet został dodany do okienka numer " + slotNumber;
        }


        if (bouquet.getStatus() == BouquetStatus.AVAILABLE) {
            return "Okienko numer " + slotNumber + " jest już zajęte przez dostępny bukiet.";
        }


        bouquet.setIsAvailable(true);
        bouquet.setStatus(BouquetStatus.AVAILABLE);
        bouquet.setName(name);
        bouquet.setPrice(price);
        bouquetRepository.save(bouquet);

        return "Bukiet został ponownie dostępny w okienku numer " + slotNumber;
    }


    public List<Bouquet> getAllBouquets() {
        return bouquetRepository.findByStatus(BouquetStatus.AVAILABLE);
    }

    public Bouquet createBouquet(Bouquet bouquet) {
        bouquet.setStatus(BouquetStatus.AVAILABLE);
        return bouquetRepository.save(bouquet);
    }
}
