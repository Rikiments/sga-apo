package br.com.mackenzie.ppads.sgaapo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import br.com.mackenzie.ppads.sgaapo.enus.StatusAPO;

/**
 * Entidade central que representa a Atividade Programada Obrigatória.
 */
@Entity
@Data
public class APO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String descricao;
    
    private LocalDate dataSubmissao;
    private Integer pontosAtribuidos;

    @Enumerated(EnumType.STRING)
    private StatusAPO status; // Campo para o status atual

    // --- Relacionamentos ---

    /**
     * Muitos APOs pertencem a UM Aluno.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;

    /**
     * Muitos APOs são orientados por UM Professor.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orientador_id")
    private Professor orientador;

    /**
     * UM APO possui UM Documento ativo.
     * cascade = CascadeType.ALL: Se o APO for salvo, salve o documento junto.
     * orphanRemoval = true: Se o documento for trocado, apague o antigo do DB.
     */
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "documento_id", referencedColumnName = "id")
    private Documento documento;

    /**
     * UM APO possui MUITAS Avaliações (histórico).
     * mappedBy = "apo": Diz ao JPA que a classe Avaliacao é a dona
     * deste relacionamento (ela tem o @JoinColumn 'apo_id').
     */
    @OneToMany(mappedBy = "apo", cascade = CascadeType.ALL)
    private List<Avaliacao> historicoAvaliacoes = new ArrayList<>();
}