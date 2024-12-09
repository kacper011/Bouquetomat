package com.example.bouquetomat.service;

import com.example.bouquetomat.model.Bouquet;
import com.example.bouquetomat.model.BouquetOrder;
import com.example.bouquetomat.model.BouquetStatus;
import com.example.bouquetomat.repository.BouquetRepository;
import com.example.bouquetomat.repository.NotificationRepository;
import com.example.bouquetomat.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
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
            return "This bouquet is no longer available.";
        }

        BouquetOrder order = new BouquetOrder(bouquet, bouquet.getPrice());
        orderRepository.save(order);

        if (bouquet.getIsAvailable() != null && bouquet.getIsAvailable()) {
            bouquet.setIsAvailable(false);
            bouquet.setStatus(BouquetStatus.SOLD);
            bouquetRepository.save(bouquet);
            notificationService.sendNotification(order);
            return "The bouquet with number " + bouquetID + " has been purchased!";
        } else {
            return "The bouquet with ID " + bouquetID + " is no longer available.";
        }
    }

    public List<Bouquet> getAllBouquets() {
        return bouquetRepository.findByStatus(BouquetStatus.AVAILABLE);
    }

    public Bouquet createBouquet(Bouquet bouquet) {

        if (bouquet.getSlotNumber() < 1 || bouquet.getSlotNumber() > 6) {
            throw new IllegalArgumentException("The slot number must be between 1 and 6.");
        }

        Bouquet existingBouquet = bouquetRepository.findBySlotNumberAndStatus(bouquet.getSlotNumber(), BouquetStatus.AVAILABLE)
                .orElse(null);

        if (existingBouquet != null) {
            throw new IllegalArgumentException("Slot number " + bouquet.getSlotNumber() + " is already occupied by an available bouquet.");
        }

        bouquet.setStatus(BouquetStatus.AVAILABLE);

        return bouquetRepository.save(bouquet);
    }


    public String deleteBouquet(Long bouquetId) {

        if (bouquetRepository.existsById(bouquetId)) {
            bouquetRepository.deleteById(bouquetId);
            return "The bouquet with ID " + bouquetId + " has been successfully deleted.";
        } else {
            throw new EntityNotFoundException("The bouquet with ID " + bouquetId + " was not found.");
        }
    }
}
