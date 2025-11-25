package br.com.mackenzie.ppads.sgaapo.controller;

import br.com.mackenzie.ppads.sgaapo.dto.LoginRequestDTO;
import br.com.mackenzie.ppads.sgaapo.dto.LoginResponseDTO;
import br.com.mackenzie.ppads.sgaapo.service.CustomUserDetailsService;
import br.com.mackenzie.ppads.sgaapo.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtService jwtService;

    /**
     * Endpoint para autenticar um usuário e retornar um token JWT.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        // 1. O AuthenticationManager usa o CustomUserDetailsService
        // e o PasswordEncoder para validar o login e senha.
        // Se estiver errado, ele lança uma exceção (que o Spring trata como 401).
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword())
        );

        // 2. Se a autenticação passou, buscamos o usuário
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getLogin());

        // 3. Geramos o token
        final String token = jwtService.generateToken(userDetails);

        // 4. Retornamos o token
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
}