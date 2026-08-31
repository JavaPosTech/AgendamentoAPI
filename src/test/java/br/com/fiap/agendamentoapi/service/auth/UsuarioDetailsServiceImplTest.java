package br.com.fiap.agendamentoapi.service.auth;

import br.com.fiap.agendamentoapi.config.AbstractTest;
import br.com.fiap.agendamentoapi.exceptions.UsuarioNaoEncontradoException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UsuarioDetailsServiceImplTest extends AbstractTest {

    @Autowired
    private UsuarioDetailsServiceImpl usuarioDetailsService;

    @Test
    void loadUserByUsernameTest() {
        var usuarioDetails = Assertions.assertDoesNotThrow(() -> usuarioDetailsService.loadUserByUsername("admin"));

        Assertions.assertEquals("admin", usuarioDetails.getUsername());
        Assertions.assertTrue(usuarioDetails.isEnabled());
        Assertions.assertTrue(usuarioDetails.getAuthorities()
                .stream()
                .anyMatch(authority -> "ROLE_ADMINISTRADOR".equals(authority.getAuthority())));
    }

    @Test
    void loadUserByUsernameComLoginInexistenteTest() {
        Assertions.assertThrows(UsuarioNaoEncontradoException.class,
                () -> usuarioDetailsService.loadUserByUsername("usuario.inexistente"));
    }
}
