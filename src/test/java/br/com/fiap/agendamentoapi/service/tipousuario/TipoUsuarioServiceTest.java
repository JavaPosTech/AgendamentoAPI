package br.com.fiap.agendamentoapi.service.tipousuario;

import br.com.fiap.agendamentoapi.config.AbstractTest;
import br.com.fiap.agendamentoapi.enums.TipoUsuario;
import br.com.fiap.agendamentoapi.exceptions.TipoUsuarioNaoEncontradoException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TipoUsuarioServiceTest extends AbstractTest {

    @Autowired
    private TipoUsuarioService tipoUsuarioService;

    @Test
    void buscarPorIdTest() {
        Assertions.assertDoesNotThrow(() -> tipoUsuarioService.buscarPorId(TipoUsuario.ADMINISTRADOR.getId()));
    }

    @Test
    void buscarPorIdInexistenteTest() {
        Assertions.assertThrows(TipoUsuarioNaoEncontradoException.class, () -> tipoUsuarioService.buscarPorId(Integer.MAX_VALUE));
    }
}