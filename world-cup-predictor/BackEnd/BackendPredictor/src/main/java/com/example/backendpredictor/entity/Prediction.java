package com.example.backendpredictor.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "predictions")
public class Prediction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String homeTeam;
    private String awayTeam;

    private Integer predictedHomeScore;
    private Integer predictedAwayScore;

    @Column(columnDefinition = "TEXT")
    private String aiAnalysis;

    private Instant createdAt = Instant.now();

    public Prediction() {}

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getHomeTeam() { return homeTeam; }
    public void setHomeTeam(String homeTeam) { this.homeTeam = homeTeam; }
    public String getAwayTeam() { return awayTeam; }
    public void setAwayTeam(String awayTeam) { this.awayTeam = awayTeam; }
    public Integer getPredictedHomeScore() { return predictedHomeScore; }
    public void setPredictedHomeScore(Integer predictedHomeScore) { this.predictedHomeScore = predictedHomeScore; }
    public Integer getPredictedAwayScore() { return predictedAwayScore; }
    public void setPredictedAwayScore(Integer predictedAwayScore) { this.predictedAwayScore = predictedAwayScore; }
    public String getAiAnalysis() { return aiAnalysis; }
    public void setAiAnalysis(String aiAnalysis) { this.aiAnalysis = aiAnalysis; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}

