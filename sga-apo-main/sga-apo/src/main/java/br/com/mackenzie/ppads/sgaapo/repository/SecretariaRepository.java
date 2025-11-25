package br.com.mackenzie.ppads.sgaapo.repository;

import br.com.mackenzie.ppads.sgaapo.entity.Secretaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório para a entidade Secretaria.
 */
@Repository
public interface SecretariaRepository extends JpaRepository<Secretaria, Long> {

    Optional<Secretaria> findByLogin(String login);
    
}