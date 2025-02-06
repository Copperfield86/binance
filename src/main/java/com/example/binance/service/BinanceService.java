package com.example.binance.service;

import com.example.binance.model.PriceAlert;
import com.example.binance.repository.PriceAlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BinanceService {

    private final PriceAlertRepository priceAlertRepository;
    private final Map<String, Double> priceCache = new HashMap<>();
    private final RestTemplate restTemplate;

    @Autowired
    public BinanceService(PriceAlertRepository priceAlertRepository, RestTemplate restTemplate) {
        this.priceAlertRepository = priceAlertRepository;
        this.restTemplate = restTemplate;
    }

    public double fetchPriceFromBinance(String symbol) {
        String url = "https://api.binance.com/api/v3/ticker/price?symbol=" + symbol.toUpperCase();
        try {
            System.out.println("Fetching price from URL: " + url); // Logowanie URL
            BinanceApiResponse response = restTemplate.getForObject(url, BinanceApiResponse.class);
            if (response != null && response.getPrice() != null) {
                System.out.println("Fetched price for " + symbol + ": " + response.getPrice()); // Logowanie ceny
                return Double.parseDouble(response.getPrice());
            } else {
                System.err.println("No price data found for symbol: " + symbol);
            }
        } catch (Exception e) {
            System.err.println("Error fetching price from Binance API: " + e.getMessage());
        }
        return 0.0;
    }

    @Scheduled(fixedRateString = "${binance.api.interval}")
    public void updatePrices() {
        List<PriceAlert> activeAlerts = priceAlertRepository.findByActiveTrue();
        for (PriceAlert alert : activeAlerts) {
            String symbol = alert.getCurrencySymbol();
            double price = fetchPriceFromBinance(symbol);
            priceCache.put(symbol, price);
            System.out.println("Updated price for " + symbol + ": " + price); // Logowanie
            checkPriceAlerts();
        }
    }

    public double getCurrencyPrice(String symbol) {
        return fetchPriceFromBinance(symbol); // Pobierz aktualną cenę z API Binance
    }

    public void checkPriceAlerts() {
        List<PriceAlert> activeAlerts = priceAlertRepository.findByActiveTrue();
        for (PriceAlert alert : activeAlerts) {
            double currentPrice = getCurrencyPrice(alert.getCurrencySymbol());
            if (currentPrice >= alert.getTargetPrice()) {
                triggerAlert(alert, currentPrice);
                alert.setActive(false); // Dezaktywuj alert po uruchomieniu
                priceAlertRepository.save(alert);
            }
        }
    }

    private void triggerAlert(PriceAlert alert, double currentPrice) {
        System.out.println("ALERT: " + alert.getUserName() + ", " + alert.getCurrencySymbol() +
                " reached target price: " + alert.getTargetPrice() + ". Current price: " + currentPrice);
    }

    // Klasa do mapowania odpowiedzi z API Binance
    public static class BinanceApiResponse {
        private String symbol;
        private String price;

        // Gettery i settery
        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }
}