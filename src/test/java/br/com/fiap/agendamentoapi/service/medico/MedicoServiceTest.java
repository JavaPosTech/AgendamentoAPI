package br.com.fiap.agendamentoapi.service.medico;

import br.com.fiap.agendamentoapi.config.AbstractTest;
import br.com.fiap.agendamentoapi.exceptions.UsuarioNaoEncontradoException;
import br.com.fiap.agendamentoapi.model.request.medico.AtualizarMedicoRequest;
import br.com.fiap.agendamentoapi.model.request.medico.CriarMedicoRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

@SpringBootTest
class MedicoServiceTest extends AbstractTest {

    @Autowired
    private MedicoService medicoService;

    @Test
    void getMedicosTest() {
        var medicos = Assertions.assertDoesNotThrow(() -> medicoService.getMedicos(Pageable.unpaged()));
        Assertions.assertNotNull(medicos);
    }

    @Test
    void salvarTest() {
        Assertions.assertDoesNotThrow(() -> medicoService.salvar(new CriarMedicoRequest(
                "Login Teste",
                "Senha Teste",
                "Teste",
                "Teste",
                "CRM-TESTE",
                "TESTE",
                "Rua Teste, 1234"
        )));
    }

    @Test
    void atualizarTest() {
        Assertions.assertDoesNotThrow(() -> medicoService.atualizar(1, new AtualizarMedicoRequest(
                "Login Atualizado",
                "Senha Atualizada",
                "Nome Atualizado",
                "TESTE",
                "TESTE"
        )));
    }

    @Test
    void atualizarTestComIdInexistente() {
        Assertions.assertThrows(UsuarioNaoEncontradoException.class, () -> medicoService.atualizar(Integer.MAX_VALUE, new AtualizarMedicoRequest(
                "Login Atualizado",
                "Senha Atualizada",
                "Nome Atualizado",
                "TESTE",
                "TESTE"
        )));
    }

    @Test
    void deletarTest() {
        Assertions.assertDoesNotThrow(() -> medicoService.deletar(1));
    }

    @Test
    void deletarTestComIdInexistente() {
        Assertions.assertThrows(UsuarioNaoEncontradoException.class, () -> medicoService.deletar(Integer.MAX_VALUE));
    }
}