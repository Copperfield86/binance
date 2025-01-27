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

    @PostMapping("/get-current-currency-price")
    public ResponseEntity<CurrencyResponse> getCurrencyPrice(@RequestBody CurrencyRequest request) {
        try {
            CurrencyResponse response = binanceService.getCurrencyPrice(request);
            currencyRequestRepository.save(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new CurrencyResponse("Error: " + e.getMessage(), 0.0));
        }
    }

    @PostMapping(value = "/save", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> saveCurrencyRequest(@RequestBody CurrencyRequest request) {
        if (request.getCurrencySymbol() == null || request.getUserName() == null) {
            return ResponseEntity.badRequest().body("Invalid input data");
        }
        currencyRequestRepository.save(request);
        return ResponseEntity.ok("Request saved successfully");
    }

    @GetMapping("/requests")
    public ResponseEntity<List<CurrencyRequest>> getAllRequests() {
        return ResponseEntity.ok(currencyRequestRepository.findAll());
    }
}