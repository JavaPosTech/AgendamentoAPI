package br.com.fiap.agendamentoapi.service.enfermeiro;

import br.com.fiap.agendamentoapi.config.AbstractTest;
import br.com.fiap.agendamentoapi.exceptions.UsuarioNaoEncontradoException;
import br.com.fiap.agendamentoapi.model.request.enfermeiro.AtualizarEnfermeiroRequest;
import br.com.fiap.agendamentoapi.model.request.enfermeiro.SalvarEnfermeiroRequest;
import br.com.fiap.agendamentoapi.repository.enfermeiro.EnfermeiroRepository;
import br.com.fiap.agendamentoapi.service.auth.UsuarioDetailsServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

@SpringBootTest
class EnfermeiroServiceTest extends AbstractTest {

    @Autowired
    private EnfermeiroService enfermeiroService;

    @Autowired
    private UsuarioDetailsServiceImpl usuarioDetailsService;

    @Autowired
    private EnfermeiroRepository enfermeiroRepository;

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
    void atualizarMantemCamposOmitidosOuEmBrancoTest() {
        var enfermeiro = enfermeiroRepository.findById(1).orElseThrow();

        var nomeOriginal = enfermeiro.getNome();
        var sobrenomeOriginal = enfermeiro.getSobrenome();

        enfermeiroService.atualizar(1, new AtualizarEnfermeiroRequest(null, "   ", "654321-SP"));

        var enfermeiroAtualizado = enfermeiroRepository.findById(1).orElseThrow();

        Assertions.assertEquals("654321-SP", enfermeiroAtualizado.getCoren());
        Assertions.assertEquals(nomeOriginal, enfermeiroAtualizado.getNome());
        Assertions.assertEquals(sobrenomeOriginal, enfermeiroAtualizado.getSobrenome());
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

    @Test
    void deletarDesativaAcessoDoUsuarioTest() {
        Assertions.assertTrue(usuarioDetailsService.loadUserByUsername("carlos.santos").isEnabled());

        enfermeiroService.deletar(1);

        Assertions.assertFalse(usuarioDetailsService.loadUserByUsername("carlos.santos").isEnabled());
    }
}
