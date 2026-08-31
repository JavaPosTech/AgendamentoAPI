package br.com.fiap.agendamentoapi.controller.recepcionista;

import br.com.fiap.agendamentoapi.model.dto.recepcionista.RecepcionistaDTO;
import br.com.fiap.agendamentoapi.model.request.recepcionista.AtualizarRecepcionistaRequest;
import br.com.fiap.agendamentoapi.model.request.recepcionista.SalvarRecepcionistaRequest;
import br.com.fiap.agendamentoapi.model.response.page.PageResponse;
import br.com.fiap.agendamentoapi.model.response.sucesso.MensagemSucessoResponse;
import br.com.fiap.agendamentoapi.service.recepcionista.RecepcionistaService;
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
@RequestMapping("/v1/recepcionista")
@Tag(name = "Recepcionista", description = "Endpoints relacionados ao gerenciamento de Recepcionistas")
public class RecepcionistaController {

    private final RecepcionistaService recepcionistaService;

    @GetMapping
    public ResponseEntity<PageResponse<RecepcionistaDTO>> listar(@Parameter(hidden = true) @PageableDefault(size = 100, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(recepcionistaService.getRecepcionistas(pageable));
    }

    @PostMapping
    public ResponseEntity<MensagemSucessoResponse> salvar(@RequestBody @Valid SalvarRecepcionistaRequest salvarRecepcionistaRequest) {
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(recepcionistaService.salvar(salvarRecepcionistaRequest));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MensagemSucessoResponse> atualizar(@PathVariable Integer id, @RequestBody @Valid AtualizarRecepcionistaRequest atualizarRecepcionistaRequest) {
        return ResponseEntity.status(HttpStatus.OK.value()).body(recepcionistaService.atualizar(id, atualizarRecepcionistaRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        recepcionistaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
