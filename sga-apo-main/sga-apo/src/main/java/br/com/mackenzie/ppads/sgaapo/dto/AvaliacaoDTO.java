package br.com.mackenzie.ppads.sgaapo.dto;

import br.com.mackenzie.ppads.sgaapo.enus.ResultadoAvaliacao;
import lombok.Data;
import br.com.mackenzie.ppads.sgaapo.enus.EtapaAvaliacao;

/**
 * DTO para receber os dados de uma nova avaliação.
 */
@Data
public class AvaliacaoDTO {

    // Dados que vêm do front-end (JSON)
    private Long professorAvaliadorId;
    private String parecer;
    private ResultadoAvaliacao resultado;
    private EtapaAvaliacao etapa;

}