package br.com.mackenzie.ppads.sgaapo.entity;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Representa um Professor no sistema (Orientador, Avaliador),
 * que é um tipo de Usuário.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Professor extends Usuario {

    private String departamento;

    // Os relacionamentos (orientador, avaliador) serão mapeados
    // nas classes APO e Avaliacao (lado @ManyToOne)
}