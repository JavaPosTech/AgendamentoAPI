package br.com.fiap.agendamentoapi.service.auth;

import br.com.fiap.agendamentoapi.exceptions.SenhaIncorretaException;
import br.com.fiap.agendamentoapi.exceptions.UsuarioNaoEncontradoException;
import br.com.fiap.agendamentoapi.model.entity.usuario.Usuario;
import br.com.fiap.agendamentoapi.model.request.auth.LoginRequest;
import br.com.fiap.agendamentoapi.repository.usuario.UsuarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private TokenService tokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Test
    void loginTest() {
        var request = new LoginRequest("usuario.teste", "Senha@123");

        var usuario = new Usuario();
        usuario.setId(1);
        usuario.setLogin("usuario.teste");
        usuario.setSenha("senha-criptografada");

        when(usuarioRepository.findByLogin("usuario.teste"))
                .thenReturn(Optional.of(usuario));

        when(passwordEncoder.matches(
                "Senha@123",
                "senha-criptografada"
        )).thenReturn(true);

        when(tokenService.gerarToken(usuario))
                .thenReturn("token-jwt-teste");

        var token = Assertions.assertDoesNotThrow(() -> authService.login(request));

        Assertions.assertNotNull(token);
        Assertions.assertEquals("token-jwt-teste", token);

        verify(usuarioRepository).findByLogin("usuario.teste");
        verify(passwordEncoder).matches("Senha@123", "senha-criptografada");
        verify(tokenService).gerarToken(usuario);
    }

    @Test
    void loginComUsuarioInexistenteTest() {
        var request = new LoginRequest("usuario.inexistente", "Senha@123");

        when(usuarioRepository.findByLogin("usuario.inexistente"))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(UsuarioNaoEncontradoException.class, () -> authService.login(request));
        verify(usuarioRepository).findByLogin("usuario.inexistente");
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(tokenService);
    }

    @Test
    void loginComSenhaIncorretaTest() {
        var request = new LoginRequest("usuario.teste", "senha-errada");

        var usuario = new Usuario();
        usuario.setId(1);
        usuario.setLogin("usuario.teste");
        usuario.setSenha("senha-criptografada");

        when(usuarioRepository.findByLogin("usuario.teste"))
                .thenReturn(Optional.of(usuario));

        when(passwordEncoder.matches(
                "senha-errada",
                "senha-criptografada"
        )).thenReturn(false);

        Assertions.assertThrows(SenhaIncorretaException.class, () -> authService.login(request));
        verify(usuarioRepository).findByLogin("usuario.teste");
        verify(passwordEncoder).matches("senha-errada", "senha-criptografada");
        verifyNoInteractions(tokenService);
    }
}