package br.com.mackenzie.ppads.sgaapo.repository;

import br.com.mackenzie.ppads.sgaapo.entity.APO;
import br.com.mackenzie.ppads.sgaapo.entity.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório para a entidade APO.
 * JpaRepository<APO, Long> -> A entidade é APO, a Chave Primária é Long.
 * O Spring Data JPA cria automaticamente métodos como:
 * save(), findById(), findAll(), deleteById(), etc.
 */
@Repository
public interface ApoRepository extends JpaRepository<APO, Long> {

    // O Spring é inteligente. Se você definir um método como este,
    // ele automaticamente cria a query para buscar todos os APOs de um Aluno.
    List<APO> findByAluno(Aluno aluno);
    
}