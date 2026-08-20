package br.com.fiap.agendamentoapi.controller;

import br.com.fiap.agendamentoapi.model.dto.paciente.PacienteDTO;
import br.com.fiap.agendamentoapi.model.request.AtualizarPacienteRequest;
import br.com.fiap.agendamentoapi.model.request.PacienteRequest;
import br.com.fiap.agendamentoapi.model.response.MensagemSucessoResponse;
import br.com.fiap.agendamentoapi.model.response.page.PageResponse;
import br.com.fiap.agendamentoapi.service.paciente.PacienteService;
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
@RequestMapping("/v1/paciente")
@Tag(name = "Paciente", description = "Endpoints relacionados ao gerenciamento de Pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    @GetMapping
    public ResponseEntity<PageResponse<PacienteDTO>> getPacientes(@Parameter(hidden = true) @PageableDefault(size = 100, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(pacienteService.getPacientes(pageable));
    }

    @PostMapping
    public ResponseEntity<MensagemSucessoResponse> criar(@RequestBody @Valid PacienteRequest pacienteRequest) {
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(pacienteService.salvar(pacienteRequest));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MensagemSucessoResponse> atualizar(@PathVariable Integer id, @RequestBody @Valid AtualizarPacienteRequest atualizarPacienteRequest) {
        return ResponseEntity.status(HttpStatus.OK.value()).body(pacienteService.atualizar(id, atualizarPacienteRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        pacienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}