package br.com.fiap.agendamentoapi.controller.enfermeiro;

import br.com.fiap.agendamentoapi.model.dto.enfermeiro.EnfermeiroDTO;
import br.com.fiap.agendamentoapi.model.request.enfermeiro.AtualizarEnfermeiroRequest;
import br.com.fiap.agendamentoapi.model.request.enfermeiro.SalvarEnfermeiroRequest;
import br.com.fiap.agendamentoapi.model.response.page.PageResponse;
import br.com.fiap.agendamentoapi.model.response.sucesso.MensagemSucessoResponse;
import br.com.fiap.agendamentoapi.service.enfermeiro.EnfermeiroService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/enfermeiro")
public class EnfermeiroController implements EnfermeiroDocs {

    private final EnfermeiroService enfermeiroService;

    @Override
    public ResponseEntity<PageResponse<EnfermeiroDTO>> listarEnfermeiros(Pageable pageable) {
        return ResponseEntity.ok(enfermeiroService.getEnfermeiros(pageable));
    }

    @Override
    public ResponseEntity<MensagemSucessoResponse> salvarEnfermeiro(SalvarEnfermeiroRequest salvarEnfermeiroRequest) {
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(enfermeiroService.salvar(salvarEnfermeiroRequest));
    }

    @Override
    public ResponseEntity<MensagemSucessoResponse> atualizarEnfermeiro(Integer id, AtualizarEnfermeiroRequest atualizarEnfermeiroRequest) {
        return ResponseEntity.status(HttpStatus.OK.value()).body(enfermeiroService.atualizar(id, atualizarEnfermeiroRequest));
    }

    @Override
    public ResponseEntity<Void> deletarEnfermeiro(Integer id) {
        enfermeiroService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}