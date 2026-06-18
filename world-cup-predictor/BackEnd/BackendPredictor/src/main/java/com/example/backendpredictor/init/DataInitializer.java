package com.example.backendpredictor.init;

import com.example.backendpredictor.entity.Prediction;
import com.example.backendpredictor.entity.Role;
import com.example.backendpredictor.entity.Team;
import com.example.backendpredictor.entity.User;
import com.example.backendpredictor.repository.PredictionRepository;
import com.example.backendpredictor.repository.RoleRepository;
import com.example.backendpredictor.repository.TeamRepository;
import com.example.backendpredictor.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

@Component
@Profile("!test")
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PredictionRepository predictionRepository;
    private final TeamRepository teamRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository, UserRepository userRepository, PredictionRepository predictionRepository, TeamRepository teamRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.predictionRepository = predictionRepository;
        this.teamRepository = teamRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Crear roles si no existen
        Role roleUser = roleRepository.findByName("ROLE_USER").orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));
        Role roleAdmin = roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));

        // Crear usuarios por defecto si no existen
        if (!userRepository.existsByUsername("usuario")) {
            User u = new User("Usuario Normal", "usuario", passwordEncoder.encode("usuario123"));
            u.getRoles().add(roleUser);
            userRepository.save(u);
        }

        if (!userRepository.existsByUsername("administrador")) {
            User a = new User("Administrador", "administrador", passwordEncoder.encode("admin123"));
            a.getRoles().add(roleAdmin);
            a.getRoles().add(roleUser);
            userRepository.save(a);
        }

        System.out.println("[DataInitializer] Usuarios creados: ");
        userRepository.findAll().forEach(us -> System.out.println(" - " + us.getUsername() + " roles=" + us.getRoles().stream().map(Role::getName).toList()));

        // Cargar equipos por región
        loadTeams();

        // Ejemplo: crear una predicción de muestra para el usuario "usuario"
        userRepository.findByUsername("usuario").ifPresent(u -> {
            Prediction p = new Prediction();
            p.setUser(u);
            p.setHomeTeam("Colombia");
            p.setAwayTeam("Argentina");
            p.setPredictedHomeScore(2);
            p.setPredictedAwayScore(1);
            p.setAiAnalysis("Análisis de ejemplo: Colombia con ligera ventaja por localía.");
            predictionRepository.save(p);
        });
    }

    private void loadTeams() {
        // CONCACAF - América del Norte (Región 1)
        String[] concacaf = {"Canadá", "Estados Unidos", "México"};
        loadTeamsByRegion("CONCACAF", concacaf);

        // UEFA - Europa
        String[] uefa = {"Alemania", "Austria", "Bélgica", "Bosnia y Herzegovina", "Croacia", 
                         "Escocia", "España", "Francia", "Inglaterra", "Noruega", 
                         "Países Bajos", "Portugal", "República Checa", "Suecia", "Suiza", "Turquía"};
        loadTeamsByRegion("UEFA", uefa);

        // CONCACAF - Caribe (Región 2)
        String[] caribbean = {"Curazao", "Haití", "Panamá"};
        loadTeamsByRegion("CONCACAF-CARIBBEAN", caribbean);

        // CAF - África
        String[] caf = {"Argelia", "Cabo Verde", "Costa de Marfil", "Egipto", "Ghana", 
                        "Marruecos", "RD Congo", "Senegal", "Sudáfrica", "Túnez"};
        loadTeamsByRegion("CAF", caf);

        // AFC - Asia y Oceanía
        String[] afc = {"Arabia Saudí", "Australia", "Corea del Sur", "Emiratos Árabes Unidos", 
                        "Irak", "Irán", "Japón", "Jordania", "Uzbekistán", "Nueva Zelanda"};
        loadTeamsByRegion("AFC", afc);

        System.out.println("[DataInitializer] Equipos cargados: " + teamRepository.count() + " equipos en total");
    }

    private void loadTeamsByRegion(String region, String[] teams) {
        for (String teamName : teams) {
            if (!teamRepository.existsByName(teamName)) {
                Team team = new Team(teamName, region);
                teamRepository.save(team);
            }
        }
    }
}

