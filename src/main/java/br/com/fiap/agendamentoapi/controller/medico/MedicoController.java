package br.com.fiap.agendamentoapi.controller.medico;

import br.com.fiap.agendamentoapi.model.dto.medico.MedicoDTO;
import br.com.fiap.agendamentoapi.model.request.medico.AtualizarMedicoRequest;
import br.com.fiap.agendamentoapi.model.request.medico.CriarMedicoRequest;
import br.com.fiap.agendamentoapi.model.response.page.PageResponse;
import br.com.fiap.agendamentoapi.model.response.sucesso.MensagemSucessoResponse;
import br.com.fiap.agendamentoapi.service.medico.MedicoService;
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
@RequestMapping("/v1/medico")
@Tag(name = "Médico", description = "Endpoints relacionados ao gerenciamento de Médicos")
public class MedicoController {

    private final MedicoService medicoService;

    @GetMapping
    public ResponseEntity<PageResponse<MedicoDTO>> listar(@Parameter(hidden = true) @PageableDefault(size = 100, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(medicoService.getMedicos(pageable));
    }

    @PostMapping
    public ResponseEntity<MensagemSucessoResponse> salvar(@RequestBody @Valid CriarMedicoRequest criarMedicoRequest) {
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(medicoService.salvar(criarMedicoRequest));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MensagemSucessoResponse> atualizar(@PathVariable Integer id, @RequestBody @Valid AtualizarMedicoRequest atualizarMedicoRequest) {
        return ResponseEntity.status(HttpStatus.OK.value()).body(medicoService.atualizar(id, atualizarMedicoRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        medicoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}