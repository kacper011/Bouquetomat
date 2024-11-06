package com.example.bouquetomat.repository;

import com.example.bouquetomat.model.Bouquet;
import com.example.bouquetomat.model.BouquetStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BouquetRepository extends JpaRepository<Bouquet, Long> {

    List<Bouquet> findByStatus(BouquetStatus status);
}
