package br.com.fiap.agendamentoapi.service.agendamento;

import br.com.fiap.agendamentoapi.config.AbstractTest;
import br.com.fiap.agendamentoapi.exceptions.ConsultaNaoEncontradaException;
import br.com.fiap.agendamentoapi.model.request.agendamento.AtualizarAgendamentoRequest;
import br.com.fiap.agendamentoapi.model.request.agendamento.SalvarAgendamentoRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

@SpringBootTest
class AgendamentoServiceTest extends AbstractTest {

    @Autowired
    private AgendamentoService agendamentoService;

    @Test
    void getAgendamentosTest() {
        var agendamentos = Assertions.assertDoesNotThrow(() -> agendamentoService.getAgendamentos(Pageable.unpaged()));
        Assertions.assertNotNull(agendamentos);
    }

    @Test
    void salvarTest() {
        Assertions.assertDoesNotThrow(() -> agendamentoService.salvar(new SalvarAgendamentoRequest(
                1,
                1,
                LocalDateTime.now()
        )));
    }

    @Test
    void atualizarTest() {
        Assertions.assertDoesNotThrow(() -> agendamentoService.atualizar(1, new AtualizarAgendamentoRequest(
                LocalDateTime.now()
        )));
    }

    @Test
    void atualizarTestComIdInexistente() {
        Assertions.assertThrows(ConsultaNaoEncontradaException.class, () -> agendamentoService.atualizar(Integer.MAX_VALUE, new AtualizarAgendamentoRequest(
                LocalDateTime.now()
        )));
    }
}
