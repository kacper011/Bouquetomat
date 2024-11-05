package com.example.bouquetomat.repository;

import com.example.bouquetomat.model.Bouquet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BouquetRepository extends JpaRepository<Bouquet, Long> {
}
