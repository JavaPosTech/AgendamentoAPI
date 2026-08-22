package br.com.fiap.agendamentoapi.service.enfermeiro;

import br.com.fiap.agendamentoapi.config.AbstractTest;
import br.com.fiap.agendamentoapi.exceptions.UsuarioNaoEncontradoException;
import br.com.fiap.agendamentoapi.model.request.enfermeiro.AtualizarEnfermeiroRequest;
import br.com.fiap.agendamentoapi.model.request.enfermeiro.SalvarEnfermeiroRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

@SpringBootTest
class EnfermeiroServiceTest extends AbstractTest {

    @Autowired
    private EnfermeiroService enfermeiroService;

    @Test
    void getEnfermeirosTest() {
        var enfermeiros = Assertions.assertDoesNotThrow(() -> enfermeiroService.getEnfermeiros(Pageable.unpaged()));
        Assertions.assertNotNull(enfermeiros);
    }

    @Test
    void salvarTest() {
        Assertions.assertDoesNotThrow(() -> enfermeiroService.salvar(new SalvarEnfermeiroRequest(
                "Login Teste",
                "Senha Teste",
                "Teste",
                "Teste",
                "COREN-TESTE"
        )));
    }

    @Test
    void atualizarTest() {
        Assertions.assertDoesNotThrow(() -> enfermeiroService.atualizar(1, new AtualizarEnfermeiroRequest(
                "Login Atualizado",
                "Senha Atualizada",
                "Nome Atualizado"
        )));
    }

    @Test
    void atualizarTestComIdInexistente() {
        Assertions.assertThrows(UsuarioNaoEncontradoException.class, () -> enfermeiroService.atualizar(Integer.MAX_VALUE, new AtualizarEnfermeiroRequest(
                "Login Atualizado",
                "Senha Atualizada",
                "Nome Atualizado"
        )));
    }

    @Test
    void deletarTest() {
        Assertions.assertDoesNotThrow(() -> enfermeiroService.deletar(1));
    }

    @Test
    void deletarTestComIdInexistente() {
        Assertions.assertThrows(UsuarioNaoEncontradoException.class, () -> enfermeiroService.deletar(Integer.MAX_VALUE));
    }
}