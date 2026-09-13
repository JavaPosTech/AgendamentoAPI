package br.com.fiap.agendamentoapi.controller.agendamento;

import br.com.fiap.agendamentoapi.model.dto.agendamento.AgendamentoDTO;
import br.com.fiap.agendamentoapi.model.request.agendamento.AtualizarAgendamentoRequest;
import br.com.fiap.agendamentoapi.model.request.agendamento.SalvarAgendamentoRequest;
import br.com.fiap.agendamentoapi.model.response.page.PageResponse;
import br.com.fiap.agendamentoapi.model.response.sucesso.MensagemSucessoResponse;
import br.com.fiap.agendamentoapi.service.agendamento.AgendamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/agendamento")
public class AgendamentoController implements AgendamentoDocs {

    private final AgendamentoService agendamentoService;

    @Override
    public ResponseEntity<PageResponse<AgendamentoDTO>> listarAgendamentos(Pageable pageable, Authentication authentication) {
        return ResponseEntity.ok(agendamentoService.getAgendamentos(pageable, authentication));
    }

    @Override
    public ResponseEntity<MensagemSucessoResponse> salvarAgendamento(SalvarAgendamentoRequest salvarAgendamentoRequest) {
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(agendamentoService.salvar(salvarAgendamentoRequest));
    }

    @Override
    public ResponseEntity<MensagemSucessoResponse> atualizarAgendamento(Integer id, AtualizarAgendamentoRequest atualizarAgendamentoRequest) {
        return ResponseEntity.ok().body(agendamentoService.atualizar(id, atualizarAgendamentoRequest));
    }

    @Override
    public ResponseEntity<Void> cancelarAgendamento(Integer id) {
        agendamentoService.cancelar(id);
        return ResponseEntity.noContent().build();
    }
}