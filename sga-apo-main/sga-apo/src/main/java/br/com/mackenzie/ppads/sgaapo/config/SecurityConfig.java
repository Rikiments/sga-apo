package br.com.mackenzie.ppads.sgaapo.config;

import br.com.mackenzie.ppads.sgaapo.config.filter.JwtAuthenticationFilter;
import br.com.mackenzie.ppads.sgaapo.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    // Injeta o novo filtro que criamos
    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    /**
     * Define o Bean do PasswordEncoder para criptografar senhas.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura o AuthenticationManager para "ensinar" ao Spring Security
     * como encontrar usuários (usando nosso CustomUserDetailsService)
     * e como verificar senhas (usando nosso PasswordEncoder).
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder = 
            http.getSharedObject(AuthenticationManagerBuilder.class);
        
        authBuilder
            .userDetailsService(customUserDetailsService)
            .passwordEncoder(passwordEncoder());
            
        return authBuilder.build();
    }

    /**
     * O Filtro de Segurança principal.
     * AGORA configurado para JWT.
     */
    @Bean
    @Profile("dev") // Usamos o perfil "dev" por enquanto
    public SecurityFilterChain securityFilterChainDev(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Libera o H2 Console
                .requestMatchers("/h2-console/**").permitAll() 
                
                // Libera o novo endpoint de login
                .requestMatchers("/api/v1/auth/**").permitAll()
                
                // --- REGRAS DE ACESSO ---
                .requestMatchers(HttpMethod.POST, "/api/v1/apos").hasRole("ALUNO")
                .requestMatchers(HttpMethod.POST, "/api/v1/apos/*/avaliar").hasRole("PROFESSOR")
                
                // Bloqueia todo o resto que não for autenticado
                .anyRequest().authenticated() 
            )
            // Configuração para H2
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.sameOrigin())
            )
            // Desabilita CSRF
            .csrf(AbstractHttpConfigurer::disable)
            
            // API stateless (sem sessão)
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            // Adiciona o nosso filtro JWT antes do filtro de autenticação padrão
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}