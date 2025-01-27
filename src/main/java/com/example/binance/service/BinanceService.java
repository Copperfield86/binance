package com.example.binance.service;

import com.example.binance.model.CurrencyRequest;
import com.example.binance.model.CurrencyResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BinanceService {

    private static final String BINANCE_API_URL = "https://api.binance.com/api/v3/ticker/price?symbol=";

    public CurrencyResponse getCurrencyPrice(CurrencyRequest request) {
        String currencySymbol = request.getCurrencySymbol();
        double price = fetchPriceFromBinance(currencySymbol);

        return new CurrencyResponse("Price fetched successfully", price);
    }

    public double getCurrencyPrice(String currencySymbol) {
        return fetchPriceFromBinance(currencySymbol);
    }

    private double fetchPriceFromBinance(String currencySymbol) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            String url = BINANCE_API_URL + currencySymbol.toUpperCase();
            BinanceApiResponse response = restTemplate.getForObject(url, BinanceApiResponse.class);

            if (response != null && response.getPrice() != null) {
                return Double.parseDouble(response.getPrice());
            } else {
                throw new RuntimeException("Failed to fetch price for: " + currencySymbol);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching price for " + currencySymbol + ": " + e.getMessage());
        }
    }

    static class BinanceApiResponse {
        private String symbol;
        private String price;

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