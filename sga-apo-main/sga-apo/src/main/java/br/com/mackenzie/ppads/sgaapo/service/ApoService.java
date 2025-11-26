package br.com.mackenzie.ppads.sgaapo.service;

import br.com.mackenzie.ppads.sgaapo.dto.ApoResponseDTO;
import br.com.mackenzie.ppads.sgaapo.entity.APO;
import br.com.mackenzie.ppads.sgaapo.entity.Aluno;
import br.com.mackenzie.ppads.sgaapo.entity.Avaliacao;
import br.com.mackenzie.ppads.sgaapo.entity.Professor;
import br.com.mackenzie.ppads.sgaapo.enus.EtapaAvaliacao;
import br.com.mackenzie.ppads.sgaapo.enus.ResultadoAvaliacao;
import br.com.mackenzie.ppads.sgaapo.enus.StatusAPO;
import br.com.mackenzie.ppads.sgaapo.repository.ApoRepository;
import br.com.mackenzie.ppads.sgaapo.repository.AlunoRepository;
import br.com.mackenzie.ppads.sgaapo.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApoService {

    @Autowired
    private ApoRepository apoRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    /**
     * Implementa o Caso de Uso UC01: Submeter APO.
     * Define o status inicial do fluxo.
     */
    @Transactional
    public APO submeterApo(Long alunoId, Long orientadorId, String titulo, String descricao) {
        
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        
        Professor orientador = professorRepository.findById(orientadorId)
                .orElseThrow(() -> new RuntimeException("Professor orientador não encontrado"));

        APO novoApo = new APO();
        novoApo.setAluno(aluno);
        novoApo.setOrientador(orientador);
        novoApo.setTitulo(titulo);
        novoApo.setDescricao(descricao);
        novoApo.setDataSubmissao(LocalDate.now());
        
        // Define o status inicial do fluxo
        novoApo.setStatus(StatusAPO.EM_ANALISE_ORIENTADOR); 
        
        return apoRepository.save(novoApo);
    }

    /**
     * Implementa o Caso de Uso UC02: Avaliar APO
     */
    @Transactional
    public APO avaliarApo(Long apoId, Long professorAvaliadorId, String parecer, ResultadoAvaliacao resultado, EtapaAvaliacao etapa) {
        
        APO apo = apoRepository.findById(apoId)
                .orElseThrow(() -> new RuntimeException("APO não encontrado"));
        
        Professor professor = professorRepository.findById(professorAvaliadorId)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        // 1. Criar o registro de Avaliação (o histórico)
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setApo(apo); 
        avaliacao.setProfessorAvaliador(professor);
        avaliacao.setParecer(parecer);
        avaliacao.setResultado(resultado);
        avaliacao.setEtapa(etapa);
        avaliacao.setData(LocalDate.now());

        // 2. Sincroniza o lado "pai" da relação
        apo.getHistoricoAvaliacoes().add(avaliacao);
        
        // 3. Lógica de Máquina de Estados
        if (resultado == ResultadoAvaliacao.DEVOLVIDO) {
            apo.setStatus(StatusAPO.DEVOLVIDO);
            
        } else {
            switch (etapa) {
                case ORIENTADOR:
                    apo.setStatus(StatusAPO.EM_ANALISE_COMISSAO);
                    break;
                case COMISSAO:
                    apo.setStatus(StatusAPO.EM_ANALISE_COORDENACAO);
                    break;
                case COORDENACAO:
                    apo.setStatus(StatusAPO.AGUARDANDO_SECRETARIA);
                    break;
            }
        }

        return apoRepository.save(apo);
    }

    /**
     * Lista os APOs de um aluno (Para o Dashboard do Aluno).
     */
    public List<ApoResponseDTO> listarAposPorAluno(Long alunoId) {
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        
        return apoRepository.findByAluno(aluno).stream()
                .map(ApoResponseDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Lista os APOs pendentes para um Orientador (Para o Dashboard do Professor).
     * Filtra apenas os que estão com status EM_ANALISE_ORIENTADOR.
     */
    public List<ApoResponseDTO> listarPendenciasOrientador(Long orientadorId) {
        Professor orientador = professorRepository.findById(orientadorId)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        return apoRepository.findByOrientador(orientador)
                .stream()
                .map(ApoResponseDTO::new)
                .collect(Collectors.toList());
    }

    // Adicione ao ApoService.java

    // Lista APOs por status (Usado pela Secretaria)
    public List<ApoResponseDTO> listarPorStatus(StatusAPO status) {
        return apoRepository.findByStatus(status).stream()
                .map(ApoResponseDTO::new)
                .collect(Collectors.toList());
    }

    // Ação final da Secretaria
    public void finalizarApo(Long apoId) {
        APO apo = apoRepository.findById(apoId)
                .orElseThrow(() -> new RuntimeException("APO não encontrado"));
        
        // Aqui poderíamos validar se tem o documento assinado, etc.
        apo.setStatus(StatusAPO.FINALIZADO);
        apoRepository.save(apo);
    }
}