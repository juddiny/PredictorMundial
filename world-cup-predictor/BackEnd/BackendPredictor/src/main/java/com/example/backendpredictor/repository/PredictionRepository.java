package com.example.backendpredictor.repository;

import com.example.backendpredictor.entity.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PredictionRepository extends JpaRepository<Prediction, Long> {
    List<Prediction> findByUserId(Long userId);
}

