package br.com.fiap.agendamentoapi.service.agendamento;

import br.com.fiap.agendamentoapi.config.AbstractTest;
import br.com.fiap.agendamentoapi.exceptions.ConsultaNaoEncontradaException;
import br.com.fiap.agendamentoapi.model.request.agendamento.AtualizarAgendamentoRequest;
import br.com.fiap.agendamentoapi.model.request.agendamento.SalvarAgendamentoRequest;
import br.com.fiap.agendamentoapi.repository.agendamento.AgendamentoRepository;
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

    @Autowired
    private AgendamentoRepository agendamentoRepository;

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
                LocalDateTime.now(),
                "Primeira consulta do paciente."
        )));
    }

    @Test
    void atualizarTest() {
        Assertions.assertDoesNotThrow(() -> agendamentoService.atualizar(1, new AtualizarAgendamentoRequest(
                LocalDateTime.now(),
                "Consulta remarcada."
        )));
    }

    @Test
    void atualizarMantemCamposOmitidosTest() {
        var agendamento = agendamentoRepository.findById(1).orElseThrow();

        var dataHoraOriginal = agendamento.getDataHoraConsulta();
        var observacaoOriginal = agendamento.getObservacao();

        agendamentoService.atualizar(1, new AtualizarAgendamentoRequest(null, null));

        var agendamentoAtualizado = agendamentoRepository.findById(1).orElseThrow();

        Assertions.assertEquals(dataHoraOriginal, agendamentoAtualizado.getDataHoraConsulta());
        Assertions.assertEquals(observacaoOriginal, agendamentoAtualizado.getObservacao());
    }

    @Test
    void atualizarTestComIdInexistente() {
        Assertions.assertThrows(ConsultaNaoEncontradaException.class, () -> agendamentoService.atualizar(Integer.MAX_VALUE, new AtualizarAgendamentoRequest(
                LocalDateTime.now(),
                "Consulta remarcada."
        )));
    }
}
