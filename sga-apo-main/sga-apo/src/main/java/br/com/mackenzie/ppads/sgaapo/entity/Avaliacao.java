package br.com.mackenzie.ppads.sgaapo.entity;

import br.com.mackenzie.ppads.sgaapo.enus.EtapaAvaliacao;
import br.com.mackenzie.ppads.sgaapo.enus.ResultadoAvaliacao;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

/**
 * Representa um evento de avaliação (um parecer) dado a um APO.
 * A lista de avaliações de um APO forma seu histórico.
 */
@Entity
@Data
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate data;
    
    @Lob // Para textos longos (justificativa)
    @Column(columnDefinition = "TEXT")
    private String parecer;

    @Enumerated(EnumType.STRING) // Salva o nome do enum (APROVADO, DEVOLVIDO)
    private ResultadoAvaliacao resultado;

    @Enumerated(EnumType.STRING) // Salva a etapa (ORIENTADOR, COMISSAO)
    private EtapaAvaliacao etapa;

    // --- Relacionamentos ---

    /**
     * Muitas avaliações (histórico) pertencem a UM APO.
     */
    @ManyToOne(fetch = FetchType.LAZY) // LAZY = só carrega quando precisar
    @JoinColumn(name = "apo_id")
    private APO apo;

    /**
     * Muitas avaliações são feitas por UM Professor.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id")
    private Professor professorAvaliador;
}