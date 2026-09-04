package br.com.fiap.agendamentoapi.controller.agendamento;

import br.com.fiap.agendamentoapi.model.dto.agendamento.AgendamentoDTO;
import br.com.fiap.agendamentoapi.model.request.agendamento.AtualizarAgendamentoRequest;
import br.com.fiap.agendamentoapi.model.request.agendamento.SalvarAgendamentoRequest;
import br.com.fiap.agendamentoapi.model.response.page.PageResponse;
import br.com.fiap.agendamentoapi.model.response.sucesso.MensagemSucessoResponse;
import br.com.fiap.agendamentoapi.service.agendamento.AgendamentoService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/agendamento")
@Tag(name = "Agendamento", description = "Endpoints para realizar agendamentos de consultas")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    @GetMapping
    public ResponseEntity<PageResponse<AgendamentoDTO>> listar(@Parameter(hidden = true) @PageableDefault(size = 100, sort = "id") Pageable pageable, @Parameter(hidden = true) Authentication authentication) {
        return ResponseEntity.ok(agendamentoService.getAgendamentos(pageable, authentication));
    }

    @PostMapping
    public ResponseEntity<MensagemSucessoResponse> salvar(@RequestBody @Valid SalvarAgendamentoRequest salvarAgendamentoRequest) {
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(agendamentoService.salvar(salvarAgendamentoRequest));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MensagemSucessoResponse> atualizar(@PathVariable Integer id, @RequestBody @Valid AtualizarAgendamentoRequest atualizarAgendamentoRequest) {
        return ResponseEntity.ok().body(agendamentoService.atualizar(id, atualizarAgendamentoRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Integer id) {
        agendamentoService.cancelar(id);
        return ResponseEntity.noContent().build();
    }
}