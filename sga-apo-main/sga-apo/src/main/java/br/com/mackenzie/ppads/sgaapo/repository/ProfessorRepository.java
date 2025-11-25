package br.com.mackenzie.ppads.sgaapo.repository;

import br.com.mackenzie.ppads.sgaapo.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório para a entidade Professor.
 */
@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    Optional<Professor> findByLogin(String login);
    
}