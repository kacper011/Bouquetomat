package com.example.bouquetomat.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BouquetOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bouquet_id", nullable = false)
    private Bouquet bouquet;

    private LocalDateTime orderDate;

    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public BouquetOrder(Bouquet bouquet, Double totalPrice) {
        this.bouquet = bouquet;
        this.orderDate = LocalDateTime.now();
        this.totalPrice = totalPrice;
        this.status = OrderStatus.COMPLETED;
    }
}
