package br.com.mackenzie.ppads.sgaapo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import java.time.LocalDate;

/**
 * Representa um documento (PDF) anexado ao APO.
 * Conforme o diagrama, um APO tem um único documento ativo por vez.
 */
@Entity
@Data
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeArquivo; // Ex: "artigo_ia.pdf"
    private String tipoArquivo; // Ex: "application/pdf"
    private LocalDate dataUpload;
    private String versao; // Ex: "inicial", "corrigido", "final_assinado"

    // O relacionamento reverso (OneToOne com APO) será
    // definido na classe APO, que é a dona da relação.
}