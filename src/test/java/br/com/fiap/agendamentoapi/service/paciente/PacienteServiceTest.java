package br.com.fiap.agendamentoapi.service.paciente;

import br.com.fiap.agendamentoapi.config.AbstractTest;
import br.com.fiap.agendamentoapi.exceptions.UsuarioNaoEncontradoException;
import br.com.fiap.agendamentoapi.model.request.paciente.AtualizarPacienteRequest;
import br.com.fiap.agendamentoapi.model.request.paciente.SalvarPacienteRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@SpringBootTest
class PacienteServiceTest extends AbstractTest {

    @Autowired
    private PacienteService pacienteService;

    @Test
    void getPacientesTest() {
        var pacientes = Assertions.assertDoesNotThrow(() -> pacienteService.getPacientes(Pageable.unpaged()));
        Assertions.assertNotNull(pacientes);
    }

    @Test
    @Transactional
    void salvarTest() {
        Assertions.assertDoesNotThrow(() -> pacienteService.salvar(new SalvarPacienteRequest(
                "Teste",
                "123456",
                "Teste",
                "Teste",
                "12908734011",
                "Teste",
                LocalDate.now())
        ));
    }

    @Test
    void atualizarTest() {
        Assertions.assertDoesNotThrow(() -> pacienteService.atualizar(1, new AtualizarPacienteRequest(
                "Nome Atualizado",
                "Sobrenome Atualizado",
                "09876543210",
                "Teste Atualizado",
                LocalDate.now())
        ));
    }

    @Test
    void atualizarTestComIdInexistente() {
        Assertions.assertThrows(UsuarioNaoEncontradoException.class, () -> pacienteService.atualizar(Integer.MAX_VALUE, new AtualizarPacienteRequest(
                "Nome Atualizado",
                "Sobrenome Atualizado",
                "0987654321",
                "Teste Atualizado",
                LocalDate.now())
        ));
    }

    @Test
    void deletarTest() {
        Assertions.assertDoesNotThrow(() -> pacienteService.deletar(1));
    }

    @Test
    void deletarTestComIdInexistente() {
        Assertions.assertThrows(UsuarioNaoEncontradoException.class, () -> pacienteService.deletar(Integer.MAX_VALUE));
    }
}