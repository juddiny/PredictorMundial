package com.worldcup.predictor.service;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Random;
import org.springframework.stereotype.Component;

@Component
public class FootballPredictorAgent {

    private final Random random = new Random();

    public PredictionData predict(String homeTeam, String awayTeam) {
        int homeBase = Math.max(0, homeTeam.length() - awayTeam.length());
        int awayBase = Math.max(0, awayTeam.length() - homeTeam.length());

        int homeScore = Math.min(5, Math.max(0, random.nextInt(4) + homeBase / 5));
        int awayScore = Math.min(5, Math.max(0, random.nextInt(4) + awayBase / 5));

        // Intentar delegar al predictor IA si está presente en el classpath (com.example.agent.FootballPredictorAgent)
        String iaContext = null;
        try {
            Class<?> iaClass = Class.forName("com.example.agent.FootballPredictorAgent");
            Method m = iaClass.getMethod("predictMatchResult", String.class, String.class);
            Object result = m.invoke(null, homeTeam, awayTeam);
            if (result instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, String> map = (Map<String, String>) result;
                iaContext = map.getOrDefault("contexto", null);
            }
        } catch (ClassNotFoundException e) {
            // Intentar cargar dinámicamente desde ../IA/my_agent/target/classes
            try {
                java.nio.file.Path iaPath = java.nio.file.Paths.get("..", "IA", "my_agent", "target", "classes").toAbsolutePath();
                java.io.File iaDir = iaPath.toFile();
                if (iaDir.exists()) {
                    java.net.URL iaUrl = iaDir.toURI().toURL();
                    try (java.net.URLClassLoader loader = new java.net.URLClassLoader(new java.net.URL[]{iaUrl}, this.getClass().getClassLoader())) {
                        Class<?> iaClass = Class.forName("com.example.agent.FootballPredictorAgent", true, loader);
                        Method m = iaClass.getMethod("predictMatchResult", String.class, String.class);
                        Object result = m.invoke(null, homeTeam, awayTeam);
                        if (result instanceof Map) {
                            @SuppressWarnings("unchecked")
                            Map<String, String> map = (Map<String, String>) result;
                            iaContext = map.getOrDefault("contexto", null);
                        }
                    }
                }
            } catch (Throwable ex) {
                // Ignorar y seguir con la heurística local (captura NoClassDefFoundError y similares)
            }
        } catch (Throwable e) {
            // Si ocurre cualquier otro error al invocar el agente IA, lo ignoramos y usamos la lógica local
        }

        String analysis = buildAnalysis(homeTeam, awayTeam, homeScore, awayScore, iaContext);

        return new PredictionData(homeScore, awayScore, analysis);
    }

    private String buildAnalysis(String homeTeam, String awayTeam, int homeScore, int awayScore, String iaContext) {
        String base = String.format("Predicción: %s %d - %d %s. ",
                homeTeam,
                homeScore,
                awayScore,
                awayTeam);

        String heur = String.format("Análisis heurístico: %s muestra mayor presión ofensiva que %s. " +
                        "Se espera un partido con transiciones rápidas y una fase defensiva determinante.",
                homeTeam,
                awayTeam);

        if (iaContext != null && !iaContext.isEmpty()) {
            return "IA Contexto: " + iaContext + " -- " + base + " " + heur;
        }

        return base + " " + heur;
    }

    public static class PredictionData {
        private final int homeScore;
        private final int awayScore;
        private final String analysis;

        public PredictionData(int homeScore, int awayScore, String analysis) {
            this.homeScore = homeScore;
            this.awayScore = awayScore;
            this.analysis = analysis;
        }

        public int getHomeScore() {
            return homeScore;
        }

        public int getAwayScore() {
            return awayScore;
        }

        public String getAnalysis() {
            return analysis;
        }
    }
}
