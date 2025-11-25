package br.com.mackenzie.ppads.sgaapo.entity;

import br.com.mackenzie.ppads.sgaapo.enus.Role; // Importe o novo Enum
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Classe abstrata que representa um usuário no sistema.
 * AGORA implementa UserDetails para integração com o Spring Security.
 */
@MappedSuperclass
@Data
public abstract class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    
    @Column(unique = true) // O email deve ser único
    private String email;
    
    @Column(unique = true) // O login deve ser único
    private String login;

    // --- CAMPOS ADICIONADOS PARA SEGURANÇA ---
    
    private String password; // Para armazenar a senha HASHED

    @Enumerated(EnumType.STRING) // Salva o nome do Enum (ex: "ROLE_ALUNO")
    private Role role;

    // --- MÉTODOS DO USERDETAILS ---
    // O Spring Security usará estes métodos para saber
    // quais são as permissões do usuário e se a conta é válida.

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Retorna o "perfil" (role) do usuário
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.login; // Vamos usar o login como "username"
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // A conta nunca expira
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // A conta nunca é bloqueada
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // As credenciais nunca expiram
    }

    @Override
    public boolean isEnabled() {
        return true; // A conta está sempre habilitada
    }
}