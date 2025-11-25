package br.com.mackenzie.ppads.sgaapo.entity;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Representa um membro da Secretaria no sistema,
 * que é um tipo de Usuário.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Secretaria extends Usuario {

    private String setor;
}