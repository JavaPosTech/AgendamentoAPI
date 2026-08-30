package br.com.fiap.agendamentoapi.service.auth;

import br.com.fiap.agendamentoapi.model.entity.tipousuario.TipoUsuario;
import br.com.fiap.agendamentoapi.model.entity.usuario.Usuario;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class TokenServiceTest {

    private TokenService tokenService;

    @BeforeEach
    void setUp() {

        tokenService = new TokenService();

        ReflectionTestUtils.setField(
                tokenService,
                "secret",
                "agendamento-api-jwt-secret-utilizado-apenas-nos-testes-automatizados-2026"
        );

        ReflectionTestUtils.setField(
                tokenService,
                "expirationMs",
                60000L
        );
    }

    @Test
    void gerarEValidarTokenTest() {

        var usuario = criarUsuario();

        var token = tokenService.gerarToken(usuario);

        Assertions.assertNotNull(token);
        Assertions.assertFalse(token.isBlank());

        var login = tokenService.validarToken(token);

        Assertions.assertEquals(
                "usuario.teste",
                login
        );
    }

    @Test
    void validarTokenInvalidoTest() {

        Assertions.assertThrows(
                JwtException.class,
                () -> tokenService.validarToken(
                        "token-invalido"
                )
        );
    }

    @Test
    void validarTokenExpiradoTest() {

        ReflectionTestUtils.setField(
                tokenService,
                "expirationMs",
                -1000L
        );

        var usuario = criarUsuario();

        var token = tokenService.gerarToken(usuario);

        Assertions.assertThrows(
                JwtException.class,
                () -> tokenService.validarToken(token)
        );
    }

    private Usuario criarUsuario() {

        var tipoUsuario = new TipoUsuario();
        tipoUsuario.setId(1);
        tipoUsuario.setDescricao("ADMINISTRADOR");

        var usuario = new Usuario();
        usuario.setId(1);
        usuario.setLogin("usuario.teste");
        usuario.setSenha("senha-criptografada");
        usuario.setTipoUsuario(tipoUsuario);

        return usuario;
    }
}