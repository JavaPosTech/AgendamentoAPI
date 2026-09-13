package br.com.fiap.agendamentoapi.controller.paciente;

import br.com.fiap.agendamentoapi.model.dto.paciente.PacienteDTO;
import br.com.fiap.agendamentoapi.model.request.paciente.AtualizarPacienteRequest;
import br.com.fiap.agendamentoapi.model.request.paciente.SalvarPacienteRequest;
import br.com.fiap.agendamentoapi.model.response.page.PageResponse;
import br.com.fiap.agendamentoapi.model.response.sucesso.MensagemSucessoResponse;
import br.com.fiap.agendamentoapi.service.paciente.PacienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/paciente")
public class PacienteController implements PacienteDocs {

    private final PacienteService pacienteService;

    @Override
    public ResponseEntity<PageResponse<PacienteDTO>> listarPacientes(Pageable pageable) {
        return ResponseEntity.ok(pacienteService.getPacientes(pageable));
    }

    @Override
    public ResponseEntity<MensagemSucessoResponse> salvarPaciente(SalvarPacienteRequest salvarPacienteRequest) {
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(pacienteService.salvar(salvarPacienteRequest));
    }

    @Override
    public ResponseEntity<MensagemSucessoResponse> atualizarPaciente(Integer id, AtualizarPacienteRequest atualizarPacienteRequest) {
        return ResponseEntity.status(HttpStatus.OK.value()).body(pacienteService.atualizar(id, atualizarPacienteRequest));
    }

    @Override
    public ResponseEntity<Void> deletarPaciente(Integer id) {
        pacienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}