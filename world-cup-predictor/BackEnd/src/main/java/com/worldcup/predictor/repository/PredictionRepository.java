package com.worldcup.predictor.repository;

import com.worldcup.predictor.model.Prediction;
import com.worldcup.predictor.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PredictionRepository extends JpaRepository<Prediction, Long> {
    List<Prediction> findByUserOrderByTimestampDesc(User user);
}
