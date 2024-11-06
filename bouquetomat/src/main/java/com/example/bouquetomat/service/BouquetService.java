package com.example.bouquetomat.service;

import com.example.bouquetomat.model.Bouquet;
import com.example.bouquetomat.model.BouquetOrder;
import com.example.bouquetomat.model.BouquetStatus;
import com.example.bouquetomat.repository.BouquetRepository;
import com.example.bouquetomat.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BouquetService {

    private final BouquetRepository bouquetRepository;
    private final OrderRepository orderRepository;
    private final NotificationService notificationService;

    public BouquetService(BouquetRepository bouquetRepository, OrderRepository orderRepository, NotificationService notificationService) {
        this.bouquetRepository = bouquetRepository;
        this.orderRepository = orderRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public String buyBouquet(Long bouquetId) {
        Bouquet bouquet = bouquetRepository.findById(bouquetId)
                .orElseThrow(() -> new RuntimeException("Nie ma takiego bukietu"));

        if (bouquet.getStatus() == BouquetStatus.SOLD) {
            return "Ten bukiet jest już niedostępny";
        }

        BouquetOrder order = new BouquetOrder(bouquet, bouquet.getPrice());
        orderRepository.save(order);

        bouquet.setStatus(BouquetStatus.SOLD);
        bouquetRepository.save(bouquet);

        notificationService.sendNotification(order);

        return "Zakupiono bukiet o numerze " + bouquet.getNumber();
    }

    public List<Bouquet> getAllBouquets() {
        return bouquetRepository.findByStatus(BouquetStatus.AVAILABLE);
    }

    public Bouquet createBouquet(Bouquet bouquet) {
        bouquet.setStatus(BouquetStatus.AVAILABLE);
        return bouquetRepository.save(bouquet);
    }
}
