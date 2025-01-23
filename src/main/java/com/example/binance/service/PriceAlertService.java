package com.example.binance.service;

import com.example.binance.model.Alert;
import com.example.binance.repository.AlertRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceAlertService {

    private final AlertRepository alertRepository;
    private final BinanceService binanceService;
    private static final Logger logger = LoggerFactory.getLogger(PriceAlertService.class);

    public PriceAlertService(AlertRepository alertRepository, BinanceService binanceService) {
        this.alertRepository = alertRepository;
        this.binanceService = binanceService;
    }

    public String addPriceAlert(String currency, double alertPrice) {
        Alert alert = new Alert(currency, alertPrice);
        alertRepository.save(alert);
        return "Alert for " + currency + " set at price: " + alertPrice;
    }

    @Scheduled(fixedRate = 500) // Co 0,5 sekundy
    public void checkAlerts() {
        logger.info("Checking price alerts...");
        List<Alert> alerts = alertRepository.findAll();

        for (Alert alert : alerts) {
            try {
                double currentPrice = binanceService.getCurrencyPrice(alert.getCurrency());
                if (currentPrice >= alert.getAlertPrice()) {
                    logger.info("Alert triggered! {} has reached the price of {}", alert.getCurrency(), alert.getAlertPrice());
                    alertRepository.delete(alert); // Usuwamy alert po jego wywołaniu
                }
            } catch (Exception e) {
                logger.error("Error while checking alert for {}: {}", alert.getCurrency(), e.getMessage());
            }
        }
    }
}
