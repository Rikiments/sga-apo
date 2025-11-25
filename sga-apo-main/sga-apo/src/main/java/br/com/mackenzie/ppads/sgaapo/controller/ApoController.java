package br.com.mackenzie.ppads.sgaapo.controller;

import br.com.mackenzie.ppads.sgaapo.entity.APO;
import br.com.mackenzie.ppads.sgaapo.service.ApoService;
import br.com.mackenzie.ppads.sgaapo.dto.ApoSubmissaoDTO;
import br.com.mackenzie.ppads.sgaapo.dto.AvaliacaoDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * Controla os endpoints da API REST relacionados aos APOs.
 * @RestController = Combina @Controller e @ResponseBody. Diz ao Spring que esta
 * classe vai lidar com requisições web e retornar JSON.
 * @RequestMapping = Define a URL base para todos os métodos desta classe.
 */
@RestController
@RequestMapping("/api/v1/apos")
public class ApoController {

    @Autowired
    private ApoService apoService;

    /**
     * Endpoint para o Caso de Uso UC01: Submeter APO.
     * @PostMapping = Mapeia este método para requisições HTTP POST.
     * @RequestBody = Diz ao Spring para converter o JSON do corpo da requisição
     * em um objeto ApoSubmissaoDTO.
     */
    @PostMapping
    public ResponseEntity<APO> submeterApo(@RequestBody ApoSubmissaoDTO submissaoDTO) {
        
        APO novoApo = apoService.submeterApo(
                submissaoDTO.getAlunoId(),
                submissaoDTO.getOrientadorId(),
                submissaoDTO.getTitulo(),
                submissaoDTO.getDescricao()
        );

        // Retorna um status HTTP 201 (Created) com a localização do novo recurso
        URI location = URI.create("/api/v1/apos/" + novoApo.getId());
        return ResponseEntity.created(location).body(novoApo);
    }

    /**
     * Endpoint para o Caso de Uso UC02: Avaliar APO.
     * (Atualizado para incluir a Etapa)
     * @PathVariable = Pega o {apoId} da URL e o passa como um parâmetro.
     */
    @PostMapping("/{apoId}/avaliar")
    public ResponseEntity<APO> avaliarApo(@PathVariable Long apoId, @RequestBody AvaliacaoDTO avaliacaoDTO) {
        
        APO apoAtualizado = apoService.avaliarApo(
                apoId,
                avaliacaoDTO.getProfessorAvaliadorId(),
                avaliacaoDTO.getParecer(),
                avaliacaoDTO.getResultado(),
                avaliacaoDTO.getEtapa() // Passa a nova informação
        );

        // Retorna um status HTTP 200 (OK) com o objeto atualizado
        return ResponseEntity.ok(apoAtualizado);
    }

    // (Você pode adicionar outros endpoints aqui, como buscar todos os APOs)
    // @GetMapping
    // public ResponseEntity<List<APO>> listarTodosApos() { ... }
}