package br.com.fiap.agendamentoapi.controller.historicopaciente;

import br.com.fiap.agendamentoapi.model.dto.historicopaciente.HistoricoPacienteDTO;
import br.com.fiap.agendamentoapi.model.request.historicopaciente.AtualizarHistoricoPacienteRequest;
import br.com.fiap.agendamentoapi.model.request.historicopaciente.SalvarHistoricoPacienteRequest;
import br.com.fiap.agendamentoapi.model.response.page.PageResponse;
import br.com.fiap.agendamentoapi.model.response.sucesso.MensagemSucessoResponse;
import br.com.fiap.agendamentoapi.service.historicopaciente.HistoricoPacienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/historico-paciente")
public class HistoricoPacienteController implements HistoricoPacienteDocs {

    private final HistoricoPacienteService historicoPacienteService;

    @Override
    public ResponseEntity<PageResponse<HistoricoPacienteDTO>> listarHistoricosPaciente(Pageable pageable) {
        return ResponseEntity.ok(historicoPacienteService.getHistoricos(pageable));
    }

    @Override
    public ResponseEntity<MensagemSucessoResponse> salvarHistoricoPaciente(SalvarHistoricoPacienteRequest salvarHistoricoPacienteRequest) {
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(historicoPacienteService.salvar(salvarHistoricoPacienteRequest));
    }

    @Override
    public ResponseEntity<MensagemSucessoResponse> atualizarHistoricoPaciente(Integer id, AtualizarHistoricoPacienteRequest atualizarHistoricoPacienteRequest) {
        return ResponseEntity.status(HttpStatus.OK.value()).body(historicoPacienteService.atualizar(id, atualizarHistoricoPacienteRequest));
    }

    @Override
    public ResponseEntity<Void> deletarHistoricoPaciente(Integer id) {
        historicoPacienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}