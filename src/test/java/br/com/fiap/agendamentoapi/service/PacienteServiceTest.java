package br.com.fiap.agendamentoapi.service;

import br.com.fiap.agendamentoapi.config.AbstractTest;
import br.com.fiap.agendamentoapi.model.request.AtualizarPacienteRequest;
import br.com.fiap.agendamentoapi.model.request.CriarPacienteRequest;
import br.com.fiap.agendamentoapi.service.paciente.PacienteService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

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
    void salvarTest() {
        Assertions.assertDoesNotThrow(() -> pacienteService.salvar(new CriarPacienteRequest(
                "Teste",
                "123456",
                "Teste",
                "Teste",
                "12345678901",
                "Teste",
                LocalDate.now())
        ));
    }

    @Test
    void atualizarTest() {
        Assertions.assertDoesNotThrow(() -> pacienteService.atualizar(1, new AtualizarPacienteRequest(
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
}