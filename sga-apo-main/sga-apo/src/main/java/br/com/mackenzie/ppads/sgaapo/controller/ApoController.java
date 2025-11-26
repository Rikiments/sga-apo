package br.com.mackenzie.ppads.sgaapo.controller;

import br.com.mackenzie.ppads.sgaapo.entity.APO;
import br.com.mackenzie.ppads.sgaapo.enus.StatusAPO;
import br.com.mackenzie.ppads.sgaapo.service.ApoService;
import br.com.mackenzie.ppads.sgaapo.dto.ApoResponseDTO;
import br.com.mackenzie.ppads.sgaapo.dto.ApoSubmissaoDTO;
import br.com.mackenzie.ppads.sgaapo.dto.AvaliacaoDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/apos")
public class ApoController {

    @Autowired
    private ApoService apoService;

    @PostMapping
    public ResponseEntity<APO> submeterApo(@RequestBody ApoSubmissaoDTO submissaoDTO) {
        APO novoApo = apoService.submeterApo(
                submissaoDTO.getAlunoId(),
                submissaoDTO.getOrientadorId(),
                submissaoDTO.getTitulo(),
                submissaoDTO.getDescricao()
        );
        URI location = URI.create("/api/v1/apos/" + novoApo.getId());
        return ResponseEntity.created(location).body(novoApo);
    }

    @PostMapping("/{apoId}/avaliar")
    public ResponseEntity<APO> avaliarApo(@PathVariable Long apoId, @RequestBody AvaliacaoDTO avaliacaoDTO) {
        APO apoAtualizado = apoService.avaliarApo(
                apoId,
                avaliacaoDTO.getProfessorAvaliadorId(),
                avaliacaoDTO.getParecer(),
                avaliacaoDTO.getResultado(),
                avaliacaoDTO.getEtapa()
        );
        return ResponseEntity.ok(apoAtualizado);
    }

    // Endpoint para o Dashboard do Aluno
    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<ApoResponseDTO>> listarApos(@PathVariable Long alunoId) {
        List<ApoResponseDTO> apos = apoService.listarAposPorAluno(alunoId);
        return ResponseEntity.ok(apos);
    }

    // Endpoint para o Dashboard do Professor (Orientador)
    @GetMapping("/orientador/{orientadorId}/pendentes")
    public ResponseEntity<List<ApoResponseDTO>> listarPendenciasOrientador(@PathVariable Long orientadorId) {
        List<ApoResponseDTO> apos = apoService.listarPendenciasOrientador(orientadorId);
        return ResponseEntity.ok(apos);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ApoResponseDTO>> listarPorStatus(@PathVariable StatusAPO status) {
        return ResponseEntity.ok(apoService.listarPorStatus(status));
    }

    @PostMapping("/{apoId}/finalizar")
    public ResponseEntity<Void> finalizarApo(@PathVariable Long apoId) {
        apoService.finalizarApo(apoId);
        return ResponseEntity.noContent().build();
    }
}