package br.com.fiap.agendamentoapi.controller.enfermeiro;

import br.com.fiap.agendamentoapi.model.dto.enfermeiro.EnfermeiroDTO;
import br.com.fiap.agendamentoapi.model.request.enfermeiro.AtualizarEnfermeiroRequest;
import br.com.fiap.agendamentoapi.model.request.enfermeiro.SalvarEnfermeiroRequest;
import br.com.fiap.agendamentoapi.model.response.page.PageResponse;
import br.com.fiap.agendamentoapi.model.response.sucesso.MensagemSucessoResponse;
import br.com.fiap.agendamentoapi.service.enfermeiro.EnfermeiroService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/enfermeiro")
@Tag(name = "Enfermeiro", description = "Endpoints relacionados ao gerenciamento de Enfermeiros")
public class EnfermeiroController {

    private final EnfermeiroService enfermeiroService;

    @GetMapping
    public ResponseEntity<PageResponse<EnfermeiroDTO>> listar(@Parameter(hidden = true) @PageableDefault(size = 100, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(enfermeiroService.getEnfermeiros(pageable));
    }

    @PostMapping
    public ResponseEntity<MensagemSucessoResponse> salvar(@RequestBody @Valid SalvarEnfermeiroRequest salvarEnfermeiroRequest) {
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(enfermeiroService.salvar(salvarEnfermeiroRequest));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MensagemSucessoResponse> atualizar(@PathVariable Integer id, @RequestBody @Valid AtualizarEnfermeiroRequest atualizarEnfermeiroRequest) {
        return ResponseEntity.status(HttpStatus.OK.value()).body(enfermeiroService.atualizar(id, atualizarEnfermeiroRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        enfermeiroService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}