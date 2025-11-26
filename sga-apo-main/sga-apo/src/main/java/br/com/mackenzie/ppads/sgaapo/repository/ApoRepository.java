package br.com.mackenzie.ppads.sgaapo.repository;

import br.com.mackenzie.ppads.sgaapo.entity.APO;
import br.com.mackenzie.ppads.sgaapo.entity.Aluno;
import br.com.mackenzie.ppads.sgaapo.entity.Professor;
import br.com.mackenzie.ppads.sgaapo.enus.StatusAPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApoRepository extends JpaRepository<APO, Long> {

    // Busca os APOs de um aluno específico
    List<APO> findByAluno(Aluno aluno);

    // Busca os APOs de um orientador específico
    List<APO> findByOrientador(Professor orientador);
    
    // Busca os APOs de um orientador filtrando pelo status
    // Ex: O professor quer ver apenas os que estão "EM_ANALISE_ORIENTADOR"
    List<APO> findByOrientadorAndStatus(Professor orientador, StatusAPO status);

    // ... outros métodos
    List<APO> findByStatus(StatusAPO status);
}