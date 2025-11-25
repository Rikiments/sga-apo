package br.com.mackenzie.ppads.sgaapo.config.filter;

import br.com.mackenzie.ppads.sgaapo.service.CustomUserDetailsService;
import br.com.mackenzie.ppads.sgaapo.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userLogin;

        // 1. Se não houver cabeçalho ou não começar com "Bearer ", ignora o filtro.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extrai o token (ex: "Bearer eyJhbGciOi...")
        jwt = authHeader.substring(7); // Pula os 7 caracteres de "Bearer "

        // 3. Extrai o login do usuário de dentro do token
        userLogin = jwtService.extractUsername(jwt);

        // 4. Se o usuário não estiver autenticado na sessão...
        if (userLogin != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userLogin);

            // 5. Valida o token
            if (jwtService.isTokenValid(jwt, userDetails)) {
                // 6. Se for válido, cria a autenticação e a coloca no Contexto de Segurança
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // Credenciais (senha) são nulas para JWT
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // 7. Passa a requisição para o próximo filtro
        filterChain.doFilter(request, response);
    }
}