package br.com.fiap.agendamentoapi.security;

import br.com.fiap.agendamentoapi.config.AbstractControllerTest;
import br.com.fiap.agendamentoapi.repository.usuario.UsuarioRepository;
import br.com.fiap.agendamentoapi.service.auth.TokenService;
import br.com.fiap.agendamentoapi.service.recepcionista.RecepcionistaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WithAnonymousUser
class SecurityFilterTest extends AbstractControllerTest {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RecepcionistaService recepcionistaService;

    @Test
    void tokenDeAdministradorAcessaEndpointRestritoTest() throws Exception {
        mockMvc.perform(get("/v1/recepcionista").header("Authorization", "Bearer " + gerarTokenPara("admin")))
                .andExpect(status().isOk());
    }

    @Test
    void tokenDeRecepcionistaNaoAcessaEndpointRestritoTest() throws Exception {
        mockMvc.perform(delete("/v1/recepcionista/1").header("Authorization", "Bearer " + gerarTokenPara("fernanda.lima")))
                .andExpect(status().isForbidden());
    }

    @Test
    void tokenDeUsuarioAtivoAcessaEndpointAutenticadoTest() throws Exception {
        mockMvc.perform(get("/v1/medico").header("Authorization", "Bearer " + gerarTokenPara("fernanda.lima")))
                .andExpect(status().isOk());
    }

    @Test
    void tokenDeUsuarioExcluidoEhRejeitadoTest() throws Exception {
        var token = gerarTokenPara("fernanda.lima");

        recepcionistaService.deletar(1);

        mockMvc.perform(get("/v1/medico").header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.title").value("Não Autorizado!"));
    }

    @Test
    void tokenInvalidoEhRejeitadoTest() throws Exception {
        mockMvc.perform(get("/v1/medico").header("Authorization", "Bearer token-invalido"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void requisicaoSemTokenEhRejeitadaTest() throws Exception {
        mockMvc.perform(get("/v1/medico"))
                .andExpect(status().isUnauthorized());
    }

    private String gerarTokenPara(String login) {
        return tokenService.gerarToken(usuarioRepository.findByLogin(login).orElseThrow());
    }
}
