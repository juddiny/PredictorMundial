package com.example.backendpredictor.controller;

import com.example.backendpredictor.entity.Team;
import com.example.backendpredictor.repository.TeamRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/teams")
@CrossOrigin(origins = "*")
public class TeamController {

    private final TeamRepository teamRepository;

    public TeamController(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    /**
     * Obtiene todos los equipos
     */
    @GetMapping
    public ResponseEntity<List<Team>> getAllTeams() {
        List<Team> teams = teamRepository.findAll();
        return ResponseEntity.ok(teams);
    }

    /**
     * Obtiene los equipos por región
     */
    @GetMapping("/region/{region}")
    public ResponseEntity<List<Team>> getTeamsByRegion(@PathVariable String region) {
        List<Team> teams = teamRepository.findByRegion(region);
        return ResponseEntity.ok(teams);
    }

    /**
     * Obtiene un equipo por nombre
     */
    @GetMapping("/by-name/{name}")
    public ResponseEntity<?> getTeamByName(@PathVariable String name) {
        return teamRepository.findByName(name)
                .map(team -> ResponseEntity.ok((Object) team))
                .orElse(ResponseEntity.badRequest().body(Map.of("message", "Equipo no encontrado")));
    }

    /**
     * Obtiene la cantidad total de equipos
     */
    @GetMapping("/count")
    public ResponseEntity<?> getTeamCount() {
        long count = teamRepository.count();
        return ResponseEntity.ok(Map.of("total", count));
    }

    /**
     * Obtiene las regiones disponibles
     */
    @GetMapping("/regions")
    public ResponseEntity<?> getRegions() {
        List<String> regions = List.of("CONCACAF", "UEFA", "CONCACAF-CARIBBEAN", "CAF", "AFC");
        return ResponseEntity.ok(Map.of("regions", regions));
    }
}

