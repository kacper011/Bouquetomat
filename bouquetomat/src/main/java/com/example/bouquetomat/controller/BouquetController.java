package com.example.bouquetomat.controller;

import com.example.bouquetomat.model.Bouquet;
import com.example.bouquetomat.service.BouquetService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bouquets")
@Validated
public class BouquetController {

    private final BouquetService bouquetService;

    public BouquetController(BouquetService bouquetService) {
        this.bouquetService = bouquetService;
    }

    @GetMapping
    public ResponseEntity<List<Bouquet>> getAllBouquets() {
        List<Bouquet> bouquets = bouquetService.getAllBouquets();
        return ResponseEntity.ok(bouquets);
    }

    @PostMapping
    public ResponseEntity<Bouquet> createBouquet(@Valid @RequestBody Bouquet bouquet) {
        Bouquet createdBouquet = bouquetService.createBouquet(bouquet);
        return ResponseEntity.ok(createdBouquet);
    }

    @PostMapping("/buy/{bouquetId}")
    public ResponseEntity<String> buyBouquet(@PathVariable Long bouquetId) {
        String result = bouquetService.buyBouquet(bouquetId);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete/{bouquetId}")
    public ResponseEntity<String> deleteBouquet(@PathVariable Long bouquetId) {
        bouquetService.deleteBouquet(bouquetId);
        return ResponseEntity.ok("The bouquet with ID " + bouquetId + " has been deleted.");
    }
}
