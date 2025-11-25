package br.com.mackenzie.ppads.sgaapo.repository;

import br.com.mackenzie.ppads.sgaapo.entity.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório para a entidade Aluno.
 */
@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    // Exemplo de busca personalizada que será útil:
    Optional<Aluno> findByMatricula(String matricula);

    Optional<Aluno> findByLogin(String login);
}