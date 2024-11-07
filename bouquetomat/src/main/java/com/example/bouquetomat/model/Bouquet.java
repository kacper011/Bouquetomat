package com.example.bouquetomat.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Bouquet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer slotNumber;

    private Double price;

    private Boolean isAvailable = true;

    @Enumerated(EnumType.STRING)
    private BouquetStatus status;

}
