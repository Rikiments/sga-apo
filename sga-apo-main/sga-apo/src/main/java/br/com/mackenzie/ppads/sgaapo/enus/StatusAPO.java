package br.com.mackenzie.ppads.sgaapo.enus;

/**
 * Define o status ATUAL de um APO no fluxo de trabalho.
 */
public enum StatusAPO {
    EM_ANALISE_ORIENTADOR,
    EM_ANALISE_COMISSAO,
    EM_ANALISE_COORDENACAO,
    AGUARDANDO_SECRETARIA,
    FINALIZADO,
    DEVOLVIDO
}