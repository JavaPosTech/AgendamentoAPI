package br.com.fiap.agendamentoapi.service.recepcionista;

import br.com.fiap.agendamentoapi.config.AbstractTest;
import br.com.fiap.agendamentoapi.exceptions.UsuarioNaoEncontradoException;
import br.com.fiap.agendamentoapi.model.request.recepcionista.AtualizarRecepcionistaRequest;
import br.com.fiap.agendamentoapi.model.request.recepcionista.SalvarRecepcionistaRequest;
import br.com.fiap.agendamentoapi.service.auth.UsuarioDetailsServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

@SpringBootTest
class RecepcionistaServiceTest extends AbstractTest {

    @Autowired
    private RecepcionistaService recepcionistaService;

    @Autowired
    private UsuarioDetailsServiceImpl usuarioDetailsService;

    @Test
    void getRecepcionistasTest() {
        var recepcionistas = Assertions.assertDoesNotThrow(() -> recepcionistaService.getRecepcionistas(Pageable.unpaged()));
        Assertions.assertNotNull(recepcionistas);
    }

    @Test
    void salvarTest() {
        Assertions.assertDoesNotThrow(() -> recepcionistaService.salvar(new SalvarRecepcionistaRequest(
                "recepcionista.teste",
                "Senha@123",
                "Recepcionista",
                "Teste"
        )));
    }

    @Test
    void atualizarTest() {
        Assertions.assertDoesNotThrow(() -> recepcionistaService.atualizar(1, new AtualizarRecepcionistaRequest(
                "Recepcionista Atualizado",
                "Teste Atualizado"
        )));
    }

    @Test
    void atualizarTestComIdInexistente() {
        Assertions.assertThrows(UsuarioNaoEncontradoException.class, () -> recepcionistaService.atualizar(
                Integer.MAX_VALUE,
                new AtualizarRecepcionistaRequest("Recepcionista Atualizado", "Teste Atualizado")
        ));
    }

    @Test
    void deletarTest() {
        Assertions.assertDoesNotThrow(() -> recepcionistaService.deletar(1));
    }

    @Test
    void deletarTestComIdInexistente() {
        Assertions.assertThrows(UsuarioNaoEncontradoException.class, () -> recepcionistaService.deletar(Integer.MAX_VALUE));
    }

    @Test
    void deletarDesativaAcessoDoUsuarioTest() {
        Assertions.assertTrue(usuarioDetailsService.loadUserByUsername("fernanda.lima").isEnabled());

        recepcionistaService.deletar(1);

        Assertions.assertFalse(usuarioDetailsService.loadUserByUsername("fernanda.lima").isEnabled());
    }
}
