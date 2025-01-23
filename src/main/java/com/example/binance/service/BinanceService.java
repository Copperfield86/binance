package com.example.binance.service;

import com.example.binance.model.CurrencyRequest;
import com.example.binance.model.CurrencyResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class BinanceService {

    public CurrencyResponse getCurrencyPrice(CurrencyRequest request) {
        String symbol = request.getCurrencySymbol();
        String url = "https://api.binance.com/api/v3/ticker/price?symbol=" + symbol;

        RestTemplate restTemplate = new RestTemplate();
        try {
            // Wysyłanie zapytania do Binance API
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            // Pobieranie ceny z odpowiedzi API
            double price = Double.parseDouble((String) response.get("price"));
            return new CurrencyResponse("Price fetched successfully", price);
        } catch (Exception e) {
            // Obsługa błędów, np. gdy symbol waluty jest nieprawidłowy
            return new CurrencyResponse("Error fetching price: " + e.getMessage(), 0.0);
        }
    }
}
