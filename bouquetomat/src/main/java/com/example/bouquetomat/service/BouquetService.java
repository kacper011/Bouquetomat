package com.example.bouquetomat.service;

import com.example.bouquetomat.model.Bouquet;
import com.example.bouquetomat.model.BouquetOrder;
import com.example.bouquetomat.repository.BouquetRepository;
import com.example.bouquetomat.repository.OrderRepository;
import org.springframework.stereotype.Service;

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


    public String buyBouquet(Long bouquetId) {
        Bouquet bouquet = bouquetRepository.findById(bouquetId)
                .orElseThrow(() -> new RuntimeException("Nie ma takiego bukietu"));

        if (!bouquet.getIsAvailable()) {
            return "Ten bukiet jest już niedostępny";
        }

        BouquetOrder order = new BouquetOrder(bouquet, bouquet.getPrice());
        orderRepository.save(order);

        bouquet.setIsAvailable(false);
        bouquetRepository.save(bouquet);

        notificationService.sendNotification(order);

        return "Zakupiono bukiet numer " + bouquet.getNumber();
    }
}
