package br.com.mackenzie.ppads.sgaapo.service;

import br.com.mackenzie.ppads.sgaapo.entity.APO;
import br.com.mackenzie.ppads.sgaapo.entity.Aluno;
import br.com.mackenzie.ppads.sgaapo.entity.Avaliacao;
import br.com.mackenzie.ppads.sgaapo.entity.Professor;
import br.com.mackenzie.ppads.sgaapo.enus.EtapaAvaliacao;
import br.com.mackenzie.ppads.sgaapo.enus.ResultadoAvaliacao;
import br.com.mackenzie.ppads.sgaapo.enus.StatusAPO;
import br.com.mackenzie.ppads.sgaapo.repository.ApoRepository;
import br.com.mackenzie.ppads.sgaapo.repository.AlunoRepository;
import br.com.mackenzie.ppads.sgaapo.repository.AvaliacaoRepository;
import br.com.mackenzie.ppads.sgaapo.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class ApoService {

    @Autowired
    private ApoRepository apoRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    // Este repositório não é mais necessário aqui se usarmos Cascade
    // @Autowired
    // private AvaliacaoRepository avaliacaoRepository; 

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
        // TODO: Enviar notificação para o Orientador aqui

        return apoRepository.save(novoApo);
    }

    /**
     * Implementa o Caso de Uso UC02: Avaliar APO
     * (Corrigido para sincronizar a relação bidirecional)
     */
    @Transactional
    public APO avaliarApo(Long apoId, Long professorAvaliadorId, String parecer, ResultadoAvaliacao resultado, EtapaAvaliacao etapa) {
        
        APO apo = apoRepository.findById(apoId)
                .orElseThrow(() -> new RuntimeException("APO não encontrado"));
        
        Professor professor = professorRepository.findById(professorAvaliadorId)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        // 1. Criar o registro de Avaliação (o histórico)
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setApo(apo); // Seta o lado "filho" da relação
        avaliacao.setProfessorAvaliador(professor);
        avaliacao.setParecer(parecer);
        avaliacao.setResultado(resultado);
        avaliacao.setEtapa(etapa);
        avaliacao.setData(LocalDate.now());

        // --- CORREÇÃO CRÍTICA ---
        // 2. Sincroniza o lado "pai" da relação
        // Adiciona a nova avaliação na lista do APO (que já foi inicializada na entidade)
        apo.getHistoricoAvaliacoes().add(avaliacao);
        
        // 3. --- Lógica do 'alt/else' do Diagrama de Sequência ---
        if (resultado == ResultadoAvaliacao.DEVOLVIDO) {
            // 3.A. Lógica de Devolução
            apo.setStatus(StatusAPO.DEVOLVIDO);
            // TODO: Enviar notificação ao Aluno
            
        } else {
            // 3.B. Lógica de Aprovação (Máquina de Estados)
            switch (etapa) {
                case ORIENTADOR:
                    apo.setStatus(StatusAPO.EM_ANALISE_COMISSAO);
                    // TODO: Enviar notificação para a Comissão
                    break;
                case COMISSAO:
                    apo.setStatus(StatusAPO.EM_ANALISE_COORDENACAO);
                    // TODO: Enviar notificação para a Coordenação
                    break;
                case COORDENACAO:
                    apo.setStatus(StatusAPO.AGUARDANDO_SECRETARIA);
                    // TODO: Enviar notificação para a Secretaria
                    break;
            }
        }

        // 4. Salva o APO (e o Cascade.ALL salvará a nova Avaliação junto)
        // Não precisamos mais chamar o avaliacaoRepository.save()
        return apoRepository.save(apo);
    }
}