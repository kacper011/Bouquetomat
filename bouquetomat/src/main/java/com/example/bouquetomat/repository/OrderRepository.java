package com.example.bouquetomat.repository;

import com.example.bouquetomat.model.BouquetOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<BouquetOrder, Long> {
}
