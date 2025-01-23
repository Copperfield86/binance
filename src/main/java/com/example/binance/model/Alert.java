package com.example.binance.model;

import jakarta.persistence.*;

@Entity
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String currency;
    private double alertPrice;

    public Alert() {
    }

    public Alert(String currency, double alertPrice) {
        this.currency = currency;
        this.alertPrice = alertPrice;
    }

    public Long getId() {
        return id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getAlertPrice() {
        return alertPrice;
    }

    public void setAlertPrice(double alertPrice) {
        this.alertPrice = alertPrice;
    }
}
