package com.example.bouquetomat.controller;

import com.example.bouquetomat.model.Bouquet;
import com.example.bouquetomat.service.BouquetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bouquets")
public class BouquetController {

    private final BouquetService bouquetService;
    @Autowired
    public BouquetController(BouquetService bouquetService) {
        this.bouquetService = bouquetService;
    }

    @GetMapping
    public ResponseEntity<List<Bouquet>> getAllBouquets() {
        List<Bouquet> bouquets = bouquetService.getAllBouquets();
        return ResponseEntity.ok(bouquets);
    }

    @PostMapping
    public ResponseEntity<Bouquet> createBouquet(@RequestBody Bouquet bouquet) {
        Bouquet createdBouquet = bouquetService.createBouquet(bouquet);
        return ResponseEntity.ok(createdBouquet);
    }

    @PostMapping("/{bouquetId}/buy")
    public ResponseEntity<String> buyBouquet(@PathVariable Long bouquetId) {
        String result = bouquetService.buyBouquet(bouquetId);
        return ResponseEntity.ok(result);
    }
}
