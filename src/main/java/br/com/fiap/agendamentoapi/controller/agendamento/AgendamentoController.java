package br.com.fiap.agendamentoapi.controller.agendamento;

import br.com.fiap.agendamentoapi.service.agendamento.AgendamentoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/agendamento")
@Tag(name = "Agendamento", description = "Endpoints para realizar agendamentos de consultas")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;


}