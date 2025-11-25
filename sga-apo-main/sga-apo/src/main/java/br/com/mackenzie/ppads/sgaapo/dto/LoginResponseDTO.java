package br.com.mackenzie.ppads.sgaapo.dto;

import lombok.Data;

/**
 * DTO para enviar o token JWT como resposta ao login.
 */
@Data
public class LoginResponseDTO {
    private String token;

    public LoginResponseDTO(String token) {
        this.token = token;
    }
}