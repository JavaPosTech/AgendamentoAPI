package br.com.fiap.agendamentoapi.security;

import br.com.fiap.agendamentoapi.config.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AutorizacaoCadastroTest extends AbstractControllerTest {

    private static final String MEDICO = "/v1/medico";

    private static final String ENFERMEIRO = "/v1/enfermeiro";

    private static final String RECEPCIONISTA = "/v1/recepcionista";

    private static final String PACIENTE = "/v1/paciente";

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    void administradorCriaMedicoEnfermeiroERecepcionistaTest() throws Exception {
        criar(MEDICO).andExpect(status().isBadRequest());
        criar(ENFERMEIRO).andExpect(status().isBadRequest());
        criar(RECEPCIONISTA).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    void administradorNaoCriaPacienteTest() throws Exception {
        criar(PACIENTE).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "RECEPCIONISTA")
    void recepcionistaCriaApenasPacienteTest() throws Exception {
        criar(PACIENTE).andExpect(status().isBadRequest());
        criar(MEDICO).andExpect(status().isForbidden());
        criar(ENFERMEIRO).andExpect(status().isForbidden());
        criar(RECEPCIONISTA).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "MEDICO")
    void medicoNaoCriaNenhumCadastroTest() throws Exception {
        criar(MEDICO).andExpect(status().isForbidden());
        criar(ENFERMEIRO).andExpect(status().isForbidden());
        criar(RECEPCIONISTA).andExpect(status().isForbidden());
        criar(PACIENTE).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "PACIENTE")
    void pacienteNaoCriaNenhumCadastroTest() throws Exception {
        criar(MEDICO).andExpect(status().isForbidden());
        criar(ENFERMEIRO).andExpect(status().isForbidden());
        criar(RECEPCIONISTA).andExpect(status().isForbidden());
        criar(PACIENTE).andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void anonimoNaoCriaNenhumCadastroTest() throws Exception {
        criar(MEDICO).andExpect(status().isUnauthorized());
        criar(ENFERMEIRO).andExpect(status().isUnauthorized());
        criar(RECEPCIONISTA).andExpect(status().isUnauthorized());
        criar(PACIENTE).andExpect(status().isUnauthorized());
    }

    private ResultActions criar(String url) throws Exception {
        return mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"));
    }
}
