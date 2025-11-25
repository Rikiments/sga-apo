package br.com.mackenzie.ppads.sgaapo.repository;

import br.com.mackenzie.ppads.sgaapo.entity.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório para a entidade Avaliacao.
 * Usado para salvar o histórico de pareceres.
 */
@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {
    
}