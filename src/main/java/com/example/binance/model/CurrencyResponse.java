package com.example.binance.model;

public class CurrencyResponse {

    private String message;
    private double price;

    // Konstruktor
    public CurrencyResponse(String message, double price) {
        this.message = message;
        this.price = price;
    }

    // Gettery i settery
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}