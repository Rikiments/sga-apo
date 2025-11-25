package br.com.mackenzie.ppads.sgaapo.dto;

import lombok.Data;

/**
 * DTO para receber os dados de uma requisição de login.
 */
@Data
public class LoginRequestDTO {
    private String login;
    private String password;
}