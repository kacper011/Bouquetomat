package com.example.bouquetomat.repository;

import com.example.bouquetomat.model.Bouquet;
import com.example.bouquetomat.model.BouquetStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BouquetRepository extends JpaRepository<Bouquet, Long> {

    List<Bouquet> findByStatus(BouquetStatus status);
    Optional<Bouquet> findBySlotNumber(Integer slotNumber);
}
