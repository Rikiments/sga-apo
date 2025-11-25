package br.com.mackenzie.ppads.sgaapo.entity;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Representa um Aluno no sistema, que é um tipo de Usuário.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true) // Necessário ao herdar de classes com @Data
public class Aluno extends Usuario {

    private String matricula;
    private Integer totalPontosAcumulados;

    // O relacionamento com APO será mapeado na classe APO (lado @ManyToOne)
}