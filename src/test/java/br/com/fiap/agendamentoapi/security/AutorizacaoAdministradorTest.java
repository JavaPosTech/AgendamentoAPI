package br.com.fiap.agendamentoapi.security;

import br.com.fiap.agendamentoapi.config.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AutorizacaoAdministradorTest extends AbstractControllerTest {

    private static final String RECEPCIONISTA = "/v1/recepcionista";

    private static final String HISTORICO_PACIENTE = "/v1/historico-paciente";

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    void administradorAcessaRecepcionistaTest() throws Exception {
        mockMvc.perform(get(RECEPCIONISTA)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    void administradorAcessaHistoricoPacienteTest() throws Exception {
        mockMvc.perform(get(HISTORICO_PACIENTE)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "MEDICO")
    void medicoNaoAcessaRecepcionistaTest() throws Exception {
        assertTodosOsMetodosNegados(RECEPCIONISTA);
    }

    @Test
    @WithMockUser(roles = "MEDICO")
    void medicoNaoAcessaHistoricoPacienteTest() throws Exception {
        assertTodosOsMetodosNegados(HISTORICO_PACIENTE);
    }

    @Test
    @WithMockUser(roles = "ENFERMEIRO")
    void enfermeiroNaoAcessaHistoricoPacienteTest() throws Exception {
        assertTodosOsMetodosNegados(HISTORICO_PACIENTE);
    }

    @Test
    @WithMockUser(roles = "RECEPCIONISTA")
    void recepcionistaNaoAcessaProprioCadastroTest() throws Exception {
        assertTodosOsMetodosNegados(RECEPCIONISTA);
    }

    @Test
    @WithMockUser(roles = "PACIENTE")
    void pacienteNaoAcessaHistoricoPacienteTest() throws Exception {
        assertTodosOsMetodosNegados(HISTORICO_PACIENTE);
    }

    @Test
    @WithMockUser(roles = "MEDICO")
    void acessoNegadoRetornaCorpoPadronizadoTest() throws Exception {
        mockMvc.perform(get(RECEPCIONISTA))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.title").value("Acesso Negado!"))
                .andExpect(jsonPath("$.instance").value("/v1/recepcionista"));
    }

    @Test
    @WithAnonymousUser
    void semAutenticacaoRecebeNaoAutorizadoTest() throws Exception {
        mockMvc.perform(get(RECEPCIONISTA))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.title").value("Não Autorizado!"));

        mockMvc.perform(get(HISTORICO_PACIENTE))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "MEDICO")
    void regraDeAdministradorNaoAfetaDemaisEndpointsTest() throws Exception {
        mockMvc.perform(get("/v1/medico")).andExpect(status().isOk());
        mockMvc.perform(get("/v1/paciente")).andExpect(status().isOk());
        mockMvc.perform(get("/v1/enfermeiro")).andExpect(status().isOk());
    }

    private void assertTodosOsMetodosNegados(String url) throws Exception {
        mockMvc.perform(get(url))
                .andExpect(status().isForbidden());

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());

        mockMvc.perform(patch(url + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());

        mockMvc.perform(delete(url + "/1"))
                .andExpect(status().isForbidden());
    }
}
