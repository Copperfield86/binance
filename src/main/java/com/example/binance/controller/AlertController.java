package com.example.binance.controller;

import com.example.binance.service.PriceAlertService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/alerts")
public class AlertController {

    private final PriceAlertService priceAlertService;

    public AlertController(PriceAlertService priceAlertService) {
        this.priceAlertService = priceAlertService;
    }

    @PostMapping(value = "/add-alert", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Map<String, Object>> addAlert(@RequestBody Map<String, Object> payload) {
        String currency = (String) payload.get("currency");
        Double alertPrice = (Double) payload.get("alertPrice");

        Map<String, Object> response = new HashMap<>();
        if (currency == null || alertPrice == null || alertPrice <= 0) {
            response.put("status", 400);
            response.put("error", "Invalid request");
            return ResponseEntity.badRequest().body(response);
        }

        String message = priceAlertService.addPriceAlert(currency, alertPrice);
        response.put("status", 200);
        response.put("message", message);
        return ResponseEntity.ok(response);
    }
}
