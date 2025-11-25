package br.com.mackenzie.ppads.sgaapo.dto;

import lombok.Data;

/**
 * DTO para receber os dados de uma nova submissão de APO.
 */
@Data
public class ApoSubmissaoDTO {
    
    // Dados que vêm do front-end (JSON)
    private Long alunoId;
    private Long orientadorId;
    private String titulo;
    private String descricao;
    
    // Não precisamos do Documento aqui ainda,
    // podemos criar um endpoint de upload separado depois.
}