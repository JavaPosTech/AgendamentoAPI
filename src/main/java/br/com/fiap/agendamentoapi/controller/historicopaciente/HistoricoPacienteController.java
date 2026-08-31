package br.com.fiap.agendamentoapi.controller.historicopaciente;

import br.com.fiap.agendamentoapi.model.dto.historicopaciente.HistoricoPacienteDTO;
import br.com.fiap.agendamentoapi.model.request.historicopaciente.AtualizarHistoricoPacienteRequest;
import br.com.fiap.agendamentoapi.model.request.historicopaciente.SalvarHistoricoPacienteRequest;
import br.com.fiap.agendamentoapi.model.response.page.PageResponse;
import br.com.fiap.agendamentoapi.model.response.sucesso.MensagemSucessoResponse;
import br.com.fiap.agendamentoapi.service.historicopaciente.HistoricoPacienteService;
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
@RequestMapping("/v1/historico-paciente")
@Tag(name = "Histórico do Paciente", description = "Endpoints relacionados ao gerenciamento do Histórico dos Pacientes")
public class HistoricoPacienteController {

    private final HistoricoPacienteService historicoPacienteService;

    @GetMapping
    public ResponseEntity<PageResponse<HistoricoPacienteDTO>> listar(@Parameter(hidden = true) @PageableDefault(size = 100, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(historicoPacienteService.getHistoricos(pageable));
    }

    @PostMapping
    public ResponseEntity<MensagemSucessoResponse> salvar(@RequestBody @Valid SalvarHistoricoPacienteRequest salvarHistoricoPacienteRequest) {
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(historicoPacienteService.salvar(salvarHistoricoPacienteRequest));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MensagemSucessoResponse> atualizar(@PathVariable Integer id, @RequestBody @Valid AtualizarHistoricoPacienteRequest atualizarHistoricoPacienteRequest) {
        return ResponseEntity.status(HttpStatus.OK.value()).body(historicoPacienteService.atualizar(id, atualizarHistoricoPacienteRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        historicoPacienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
