package br.com.fiap.agendamentoapi.service.agendamento;

import br.com.fiap.agendamentoapi.config.AbstractTest;
import br.com.fiap.agendamentoapi.exceptions.ConsultaNaoEncontradaException;
import br.com.fiap.agendamentoapi.exceptions.HorarioConsultaIndisponivelException;
import br.com.fiap.agendamentoapi.model.request.agendamento.AtualizarAgendamentoRequest;
import br.com.fiap.agendamentoapi.model.request.agendamento.SalvarAgendamentoRequest;
import br.com.fiap.agendamentoapi.repository.agendamento.AgendamentoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class AgendamentoServiceTest extends AbstractTest {

    @Autowired
    private AgendamentoService agendamentoService;

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Test
    void getAgendamentosTest() {
        var agendamentos = Assertions.assertDoesNotThrow(() -> agendamentoService.getAgendamentos(Pageable.unpaged(), autenticacao("recepcionista", "ROLE_RECEPCIONISTA")));
        Assertions.assertNotNull(agendamentos);
    }

    @Test
    void getAgendamentosSemAutenticacaoRetornaTodasTest() {
        var agendamentos = agendamentoService.getAgendamentos(Pageable.unpaged(), null);
        Assertions.assertEquals(agendamentoRepository.count(), agendamentos.totalElements());
    }

    @Test
    void getAgendamentosComoPacienteRetornaSomenteAsPropriasTest() {
        var agendamentos = agendamentoService.getAgendamentos(Pageable.unpaged(), autenticacao("pedro.almeida", "ROLE_PACIENTE"));

        Assertions.assertEquals(3, agendamentos.totalElements());
        Assertions.assertTrue(agendamentos.content().stream().allMatch(agendamento -> "PEDRO".equals(agendamento.paciente())));
    }

    @Test
    void getAgendamentosComoPacienteSemCadastroRetornaVazioTest() {
        var agendamentos = agendamentoService.getAgendamentos(Pageable.unpaged(), autenticacao("fantasma", "ROLE_PACIENTE"));

        Assertions.assertEquals(0, agendamentos.totalElements());
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
    void salvarComMedicoOcupadoNoHorarioLancaExcecaoTest() {
        var dataHora = LocalDateTime.of(2027, 1, 10, 9, 0, 0);

        agendamentoService.salvar(new SalvarAgendamentoRequest(1, 1, dataHora, null));

        Assertions.assertThrows(HorarioConsultaIndisponivelException.class, () -> agendamentoService.salvar(
                new SalvarAgendamentoRequest(1, 2, dataHora, null)));
    }

    @Test
    void salvarComPacienteOcupadoNoHorarioLancaExcecaoTest() {
        var dataHora = LocalDateTime.of(2027, 1, 10, 10, 0, 0);

        agendamentoService.salvar(new SalvarAgendamentoRequest(1, 1, dataHora, null));

        Assertions.assertThrows(HorarioConsultaIndisponivelException.class, () -> agendamentoService.salvar(
                new SalvarAgendamentoRequest(2, 1, dataHora, null)));
    }

    @Test
    void salvarPermiteMesmoMedicoEPacienteEmHorariosDiferentesTest() {
        var dataHora = LocalDateTime.of(2027, 1, 11, 8, 0, 0);

        agendamentoService.salvar(new SalvarAgendamentoRequest(1, 1, dataHora, null));

        Assertions.assertDoesNotThrow(() -> agendamentoService.salvar(
                new SalvarAgendamentoRequest(1, 1, dataHora.plusHours(1), null)));
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

    @Test
    void cancelarTest() {
        Assertions.assertDoesNotThrow(() -> agendamentoService.cancelar(1));
        Assertions.assertFalse(agendamentoRepository.existsById(1));
    }

    @Test
    void cancelarComIdInexistenteTest() {
        Assertions.assertThrows(ConsultaNaoEncontradaException.class, () -> agendamentoService.cancelar(Integer.MAX_VALUE));
    }

    private Authentication autenticacao(String login, String role) {
        return new UsernamePasswordAuthenticationToken(login, null, List.of(new SimpleGrantedAuthority(role)));
    }
}
