package br.com.fiap.agendamentoapi.controller.recepcionista;

import br.com.fiap.agendamentoapi.model.dto.recepcionista.RecepcionistaDTO;
import br.com.fiap.agendamentoapi.model.request.recepcionista.AtualizarRecepcionistaRequest;
import br.com.fiap.agendamentoapi.model.request.recepcionista.SalvarRecepcionistaRequest;
import br.com.fiap.agendamentoapi.model.response.page.PageResponse;
import br.com.fiap.agendamentoapi.model.response.sucesso.MensagemSucessoResponse;
import br.com.fiap.agendamentoapi.service.recepcionista.RecepcionistaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/recepcionista")
public class RecepcionistaController implements RecepcionistaDocs {

    private final RecepcionistaService recepcionistaService;

    @Override
    public ResponseEntity<PageResponse<RecepcionistaDTO>> listarRecepcionistas(Pageable pageable) {
        return ResponseEntity.ok(recepcionistaService.getRecepcionistas(pageable));
    }

    @Override
    public ResponseEntity<MensagemSucessoResponse> salvarRecepcionista(SalvarRecepcionistaRequest salvarRecepcionistaRequest) {
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(recepcionistaService.salvar(salvarRecepcionistaRequest));
    }

    @Override
    public ResponseEntity<MensagemSucessoResponse> atualizarRecepcionista(Integer id, AtualizarRecepcionistaRequest atualizarRecepcionistaRequest) {
        return ResponseEntity.status(HttpStatus.OK.value()).body(recepcionistaService.atualizar(id, atualizarRecepcionistaRequest));
    }

    @Override
    public ResponseEntity<Void> deletarRecepcionista(Integer id) {
        recepcionistaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}