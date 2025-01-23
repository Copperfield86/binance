package com.example.binance.repository;

import com.example.binance.model.CurrencyRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRequestRepository extends JpaRepository<CurrencyRequest, Long> {
}
