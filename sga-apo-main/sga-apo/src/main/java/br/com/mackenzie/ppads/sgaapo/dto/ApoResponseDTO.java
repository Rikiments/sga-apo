package br.com.mackenzie.ppads.sgaapo.dto;

import br.com.mackenzie.ppads.sgaapo.entity.APO;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ApoResponseDTO {
    private Long id;
    private String titulo;
    private String descricao;
    private LocalDate dataSubmissao;
    private String status;
    private String nomeOrientador;

    // Construtor inteligente que converte Entidade -> DTO
    public ApoResponseDTO(APO apo) {
        this.id = apo.getId();
        this.titulo = apo.getTitulo();
        this.descricao = apo.getDescricao();
        this.dataSubmissao = apo.getDataSubmissao();
        this.status = apo.getStatus() != null ? apo.getStatus().name() : "PENDENTE";
        this.nomeOrientador = apo.getOrientador().getNome();
    }
}