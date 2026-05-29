package com.example.agent;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.Annotations.Schema;
import com.google.adk.tools.FunctionTool;

import java.util.Map;

public class FootballPredictorAgent {

    // Cambiamos el punto de acceso para que apunte a este nuevo agente
    public static BaseAgent ROOT_AGENT = initAgent();

    private static BaseAgent initAgent() {
        return LlmAgent.builder()
                .name("football-predictor-agent")
                .description("Predice resultados de partidos del mundial de fútbol 2026 basándose en historia y estadísticas.")
                .instruction("""
                Eres un analista deportivo experto en mundiales de fútbol e Inteligencia Artificial.
                Tu objetivo es predecir el resultado de un encuentro entre dos selecciones proporcionadas por el usuario.
                
                SIEMPRE debes utilizar la herramienta 'predictMatchResult' pasando los dos equipos.
                
                Cuando generes la respuesta final para el usuario, debes incluir obligatoriamente:
                1. El análisis breve del encuentro.
                2. Las probabilidades de ganar de cada equipo en porcentaje (ej. 43% - 57%).
                3. Un marcador probable exacto (ej. 0 - 2).
                
                Sé entusiasta pero mantén la objetividad estadística.
                """)
                .model("gemini-flash-latest") // Puedes cambiar a gemini-pro si buscas análisis más profundos
                .tools(FunctionTool.create(FootballPredictorAgent.class, "predictMatchResult"))
                .build();
    }

    /** * Herramienta que procesa la solicitud de predicción.
     * Al no tener un modelo analítico aún, la herramienta actúa como un puente estructurado.
     */
    @Schema(description = "Genera la estructura de análisis y predicción para un partido entre dos selecciones nacionales.")
    public static Map<String, String> predictMatchResult(
            @Schema(name = "homeTeam", description = "Nombre de la primera selección (Local)") String homeTeam,
            @Schema(name = "awayTeam", description = "Nombre de la segunda selección (Visitante)") String awayTeam) {

        // Esta información le regresa al LLM para que construya su respuesta final con base en estos campos.
        return Map.of(
                "homeTeam", homeTeam,
                "awayTeam", awayTeam,
                "contexto", "Calcula las probabilidades históricas en mundiales, rendimiento de plantilla y genera un marcador estimado realista."
        );
    }
}