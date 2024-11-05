package com.example.bouquetomat.repository;

import com.example.bouquetomat.model.BouquetOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<BouquetOrder, Long> {
}
