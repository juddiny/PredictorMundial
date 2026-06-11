package com.worldcup.predictor.controller;

import com.worldcup.predictor.config.CustomUserDetails;
import com.worldcup.predictor.model.Prediction;
import com.worldcup.predictor.service.PredictionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MatchController {

    private final PredictionService predictionService;

    private static final List<String> TEAMS = List.of(
            "Argentina", "Brazil", "Germany", "France", "Spain", "England", "Portugal", "Belgium",
            "Netherlands", "Croatia", "Uruguay", "Colombia", "Mexico", "USA", "Japan", "South Korea",
            "Italy", "Switzerland", "Denmark", "Sweden", "Poland", "Senegal", "Morocco", "Turkey",
            "Chile", "Australia", "Argentina", "Canada", "Russia", "Egypt", "Ghana", "Costa Rica"
    );

    public MatchController(PredictionService predictionService) {
        this.predictionService = predictionService;
    }

    @GetMapping("/teams/list")
    public ResponseEntity<List<String>> listTeams() {
        return ResponseEntity.ok(new ArrayList<>(TEAMS));
    }

    @GetMapping("/teams/search")
    public ResponseEntity<List<String>> searchTeams(@RequestParam(name = "q", required = false) String query) {
        if (query == null || query.isBlank()) {
            return ResponseEntity.ok(new ArrayList<>(TEAMS));
        }
        String normalized = query.toLowerCase().trim();
        List<String> filtered = TEAMS.stream()
                .filter(team -> team.toLowerCase().contains(normalized))
                .collect(Collectors.toList());
        return ResponseEntity.ok(filtered);
    }

    @PostMapping("/matches/predict")
    public ResponseEntity<PredictionResponse> predictMatch(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                           @Valid @RequestBody PredictionRequest request) {
        Prediction prediction = predictionService.createPrediction(userDetails.getUser(), request.getHomeTeam(), request.getAwayTeam());
        return ResponseEntity.ok(PredictionResponse.from(prediction));
    }

    @GetMapping("/matches/history")
    public ResponseEntity<List<PredictionResponse>> history(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Prediction> history = predictionService.getHistory(userDetails.getUser());
        return ResponseEntity.ok(history.stream().map(PredictionResponse::from).collect(Collectors.toList()));
    }

    public static class PredictionRequest {
        @NotBlank
        private String homeTeam;

        @NotBlank
        private String awayTeam;

        public PredictionRequest() {
        }

        public String getHomeTeam() {
            return homeTeam;
        }

        public void setHomeTeam(String homeTeam) {
            this.homeTeam = homeTeam;
        }

        public String getAwayTeam() {
            return awayTeam;
        }

        public void setAwayTeam(String awayTeam) {
            this.awayTeam = awayTeam;
        }
    }

    public static class PredictionResponse {
        private Long id;
        private String homeTeamName;
        private String awayTeamName;
        private Integer homeScore;
        private Integer awayScore;
        private String aiAnalysis;
        private String timestamp;
        private Double homeWinProbability;
        private Double drawProbability;
        private Double awayWinProbability;
        private Double confidence;

        public PredictionResponse() {
        }

        public PredictionResponse(Long id, String homeTeamName, String awayTeamName, Integer homeScore, Integer awayScore, String aiAnalysis, String timestamp, Double homeWinProbability, Double drawProbability, Double awayWinProbability, Double confidence) {
            this.id = id;
            this.homeTeamName = homeTeamName;
            this.awayTeamName = awayTeamName;
            this.homeScore = homeScore;
            this.awayScore = awayScore;
            this.aiAnalysis = aiAnalysis;
            this.timestamp = timestamp;
            this.homeWinProbability = homeWinProbability;
            this.drawProbability = drawProbability;
            this.awayWinProbability = awayWinProbability;
            this.confidence = confidence;
        }

        public static PredictionResponse from(Prediction prediction) {
            double[] values = calculateProbabilities(prediction.getHomeScore(), prediction.getAwayScore());
            return new PredictionResponse(
                    prediction.getId(),
                    prediction.getHomeTeamName(),
                    prediction.getAwayTeamName(),
                    prediction.getHomeScore(),
                    prediction.getAwayScore(),
                    prediction.getAiAnalysis(),
                    prediction.getTimestamp().toString(),
                    values[0],
                    values[1],
                    values[2],
                    calculateConfidence(prediction.getHomeScore(), prediction.getAwayScore())
            );
        }

        private static double[] calculateProbabilities(int homeScore, int awayScore) {
            double difference = homeScore - awayScore;
            double home = 0.45 + Math.max(-0.2, Math.min(0.2, difference * 0.05));
            double away = 0.35 - Math.max(-0.15, Math.min(0.15, difference * 0.05));
            double draw = 1.0 - home - away;
            if (draw < 0) {
                draw = 0.05;
                double total = home + away + draw;
                home /= total;
                away /= total;
                draw /= total;
            }
            return new double[]{home, draw, away};
        }

        private static double calculateConfidence(int homeScore, int awayScore) {
            int diff = Math.abs(homeScore - awayScore);
            return Math.min(1.0, 0.5 + diff * 0.15);
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getHomeTeamName() {
            return homeTeamName;
        }

        public void setHomeTeamName(String homeTeamName) {
            this.homeTeamName = homeTeamName;
        }

        public String getAwayTeamName() {
            return awayTeamName;
        }

        public void setAwayTeamName(String awayTeamName) {
            this.awayTeamName = awayTeamName;
        }

        public Integer getHomeScore() {
            return homeScore;
        }

        public void setHomeScore(Integer homeScore) {
            this.homeScore = homeScore;
        }

        public Integer getAwayScore() {
            return awayScore;
        }

        public void setAwayScore(Integer awayScore) {
            this.awayScore = awayScore;
        }

        public String getAiAnalysis() {
            return aiAnalysis;
        }

        public void setAiAnalysis(String aiAnalysis) {
            this.aiAnalysis = aiAnalysis;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public Double getHomeWinProbability() {
            return homeWinProbability;
        }

        public void setHomeWinProbability(Double homeWinProbability) {
            this.homeWinProbability = homeWinProbability;
        }

        public Double getDrawProbability() {
            return drawProbability;
        }

        public void setDrawProbability(Double drawProbability) {
            this.drawProbability = drawProbability;
        }

        public Double getAwayWinProbability() {
            return awayWinProbability;
        }

        public void setAwayWinProbability(Double awayWinProbability) {
            this.awayWinProbability = awayWinProbability;
        }

        public Double getConfidence() {
            return confidence;
        }

        public void setConfidence(Double confidence) {
            this.confidence = confidence;
        }
    }
}
