package com.worldcup.predictor.service;

import com.worldcup.predictor.model.Prediction;
import com.worldcup.predictor.model.User;
import com.worldcup.predictor.repository.PredictionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PredictionService {

    private final PredictionRepository predictionRepository;
    private final FootballPredictorAgent predictorAgent;

    public PredictionService(PredictionRepository predictionRepository, FootballPredictorAgent predictorAgent) {
        this.predictionRepository = predictionRepository;
        this.predictorAgent = predictorAgent;
    }

    public Prediction createPrediction(User user, String homeTeamName, String awayTeamName) {
        FootballPredictorAgent.PredictionData result = predictorAgent.predict(homeTeamName, awayTeamName);

        Prediction prediction = new Prediction();
        prediction.setUser(user);
        prediction.setHomeTeamName(homeTeamName);
        prediction.setAwayTeamName(awayTeamName);
        prediction.setHomeScore(result.getHomeScore());
        prediction.setAwayScore(result.getAwayScore());
        prediction.setAiAnalysis(result.getAnalysis());
        prediction.setTimestamp(LocalDateTime.now());

        return predictionRepository.save(prediction);
    }

    public List<Prediction> getHistory(User user) {
        return predictionRepository.findByUserOrderByTimestampDesc(user);
    }
}
