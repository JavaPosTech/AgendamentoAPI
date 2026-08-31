package br.com.fiap.agendamentoapi.controller.auth;

import br.com.fiap.agendamentoapi.exceptions.SenhaIncorretaException;
import br.com.fiap.agendamentoapi.exceptions.UsuarioInativoException;
import br.com.fiap.agendamentoapi.exceptions.UsuarioNaoEncontradoException;
import br.com.fiap.agendamentoapi.exceptions.handler.GlobalExceptionHandler;
import br.com.fiap.agendamentoapi.model.request.auth.LoginRequest;
import br.com.fiap.agendamentoapi.service.auth.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    private String loginRequest;

    @Mock
    private AuthService authService;

    private String loginSemLoginRequest;

    private String loginSemSenhaRequest;

    private String loginSenhaIncorretaRequest;

    private String loginUsuarioInexistenteRequest;

    @BeforeEach
    void setUp() throws Exception {
        var authController = new AuthController(authService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(authController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        loginRequest = new String(Files.readAllBytes(Paths.get("src/test/resources/auth/loginRequest.json")));
        loginSemLoginRequest = new String(Files.readAllBytes(Paths.get("src/test/resources/auth/loginSemLoginRequest.json")));
        loginSemSenhaRequest = new String(Files.readAllBytes(Paths.get("src/test/resources/auth/loginSemSenhaRequest.json")));
        loginUsuarioInexistenteRequest = new String(Files.readAllBytes(Paths.get("src/test/resources/auth/loginUsuarioInexistenteRequest.json")));
        loginSenhaIncorretaRequest = new String(Files.readAllBytes(Paths.get("src/test/resources/auth/loginSenhaIncorretaRequest.json")));
    }

    @Test
    void loginTest() throws Exception {
        when(authService.login(any(LoginRequest.class))).thenReturn("token-jwt-teste");

        mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token-jwt-teste"))
                .andExpect(jsonPath("$.tipo").value("Bearer"));
    }

    @Test
    void loginSemLoginTest() throws Exception {
        mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginSemLoginRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void loginSemSenhaTest() throws Exception {
        mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginSemSenhaRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void loginComUsuarioInexistenteTest() throws Exception {
        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new UsuarioNaoEncontradoException("Usuário com login 'inexistente' não encontrado!"));

        mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginUsuarioInexistenteRequest))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.title").value("Usuário não encontrado!"));
    }

    @Test
    void loginComSenhaIncorretaTest() throws Exception {
        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new SenhaIncorretaException("A senha informada está incorreta!"));

        mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginSenhaIncorretaRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.title").value("Senha Incorreta!"));
    }

    @Test
    void loginComUsuarioInativoTest() throws Exception {

        when(authService.login(any(LoginRequest.class)))
                .thenThrow(
                        new UsuarioInativoException(
                                "O usuário com login 'usuario.teste' está inativo!"
                        )
                );

        mockMvc.perform(
                        post("/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(loginRequest)
                )
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.title")
                        .value("Usuário Inativo!"));
    }
}
