package com.example.binance.controller;

import com.example.binance.model.CurrencyRequest;
import com.example.binance.model.CurrencyResponse;
import com.example.binance.repository.CurrencyRequestRepository;
import com.example.binance.service.BinanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/currencies")
public class CurrencyPriceController {

    private final BinanceService binanceService;
    private final CurrencyRequestRepository currencyRequestRepository;

    public CurrencyPriceController(BinanceService binanceService, CurrencyRequestRepository currencyRequestRepository) {
        this.binanceService = binanceService;
        this.currencyRequestRepository = currencyRequestRepository;
    }

    @PostMapping(value = "/get-current-currency-price", consumes = "application/json", produces = "application/json")
    public ResponseEntity<CurrencyResponse> getCurrencyPrice(@RequestBody CurrencyRequest request) {
        if (request.getCurrencySymbol() == null || request.getCurrencySymbol().isEmpty()) {
            return ResponseEntity.badRequest().body(new CurrencyResponse("Currency symbol is required", 0.0));
        }
        try {
            CurrencyResponse response = binanceService.getCurrencyPrice(request);
            currencyRequestRepository.save(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new CurrencyResponse("Error: " + e.getMessage(), 0.0));
        }
    }

    @GetMapping("/requests")
    public ResponseEntity<List<CurrencyRequest>> getAllRequests() {
        return ResponseEntity.ok(currencyRequestRepository.findAll());
    }
}
