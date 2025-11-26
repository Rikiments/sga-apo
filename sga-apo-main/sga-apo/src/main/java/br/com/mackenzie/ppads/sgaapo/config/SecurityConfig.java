package br.com.mackenzie.ppads.sgaapo.config;

import br.com.mackenzie.ppads.sgaapo.config.filter.JwtAuthenticationFilter;
import br.com.mackenzie.ppads.sgaapo.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder = 
            http.getSharedObject(AuthenticationManagerBuilder.class);
        
        authBuilder
            .userDetailsService(customUserDetailsService)
            .passwordEncoder(passwordEncoder());
            
        return authBuilder.build();
    }

    @Bean
    @Profile("dev")
    public SecurityFilterChain securityFilterChainDev(HttpSecurity http) throws Exception {
        http
            // 1. Ativa o CORS (permite o Front-end acessar)
            .cors(Customizer.withDefaults())
            
            // 2. Configura as permissões de acesso
            .authorizeHttpRequests(auth -> auth
                // Libera H2 e Login
                .requestMatchers("/h2-console/**").permitAll() 
                .requestMatchers("/api/v1/auth/**").permitAll()
                
                // Regras de negócio
                .requestMatchers(HttpMethod.POST, "/api/v1/apos").hasRole("ALUNO")
                .requestMatchers(HttpMethod.POST, "/api/v1/apos/*/avaliar").hasRole("PROFESSOR")
                
                // Bloqueia o resto
                .anyRequest().authenticated() 
            )
            // 3. Configurações necessárias para o H2
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.sameOrigin())
            )
            // 4. Desabilita CSRF (não usado em APIs REST)
            .csrf(AbstractHttpConfigurer::disable)
            
            // 5. Define a sessão como Stateless (sem cookies, apenas Token)
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            // 6. Adiciona o nosso filtro de Token JWT
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configuração do CORS.
     * Define quem pode acessar a API (no caso, o localhost:5173 do React).
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Permite o Front-end React
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); 
        
        // Permite os métodos HTTP
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        // Permite os cabeçalhos (como o Authorization do Token)
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}