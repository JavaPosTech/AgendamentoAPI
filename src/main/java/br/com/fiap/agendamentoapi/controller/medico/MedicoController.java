package br.com.fiap.agendamentoapi.controller.medico;

import br.com.fiap.agendamentoapi.model.dto.medico.MedicoDTO;
import br.com.fiap.agendamentoapi.model.request.medico.AtualizarMedicoRequest;
import br.com.fiap.agendamentoapi.model.request.medico.SalvarMedicoRequest;
import br.com.fiap.agendamentoapi.model.response.page.PageResponse;
import br.com.fiap.agendamentoapi.model.response.sucesso.MensagemSucessoResponse;
import br.com.fiap.agendamentoapi.service.medico.MedicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/medico")
public class MedicoController implements MedicoDocs {

    private final MedicoService medicoService;

    @Override
    public ResponseEntity<PageResponse<MedicoDTO>> listarMedicos(Pageable pageable) {
        return ResponseEntity.ok(medicoService.getMedicos(pageable));
    }

    @Override
    public ResponseEntity<MensagemSucessoResponse> salvarMedico(SalvarMedicoRequest salvarMedicoRequest) {
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(medicoService.salvar(salvarMedicoRequest));
    }

    @Override
    public ResponseEntity<MensagemSucessoResponse> atualizarMedico(Integer id, AtualizarMedicoRequest atualizarMedicoRequest) {
        return ResponseEntity.status(HttpStatus.OK.value()).body(medicoService.atualizar(id, atualizarMedicoRequest));
    }

    @Override
    public ResponseEntity<Void> deletarMedico(Integer id) {
        medicoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}