package br.com.fiap.agendamentoapi.security;

import br.com.fiap.agendamentoapi.config.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AutorizacaoLeituraCadastrosTest extends AbstractControllerTest {

    private static final List<String> CADASTROS = List.of("/v1/medico", "/v1/enfermeiro", "/v1/paciente", "/v1/recepcionista");

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    void administradorListaTodosOsCadastrosTest() throws Exception {
        assertListaTodos();
    }

    @Test
    @WithMockUser(roles = "MEDICO")
    void medicoListaTodosOsCadastrosTest() throws Exception {
        assertListaTodos();
    }

    @Test
    @WithMockUser(roles = "ENFERMEIRO")
    void enfermeiroListaTodosOsCadastrosTest() throws Exception {
        assertListaTodos();
    }

    @Test
    @WithMockUser(roles = "RECEPCIONISTA")
    void recepcionistaListaTodosOsCadastrosTest() throws Exception {
        assertListaTodos();
    }

    @Test
    @WithMockUser(roles = "PACIENTE")
    void pacienteNaoListaNenhumCadastroTest() throws Exception {
        for (String url : CADASTROS) {
            mockMvc.perform(get(url)).andExpect(status().isForbidden());
        }
    }

    @Test
    @WithMockUser(roles = "PACIENTE")
    void pacienteSoEnxergaAsPropriasConsultasTest() throws Exception {
        mockMvc.perform(get("/v1/agendamento")).andExpect(status().isOk());

        mockMvc.perform(get("/v1/historico-paciente")).andExpect(status().isForbidden());
        mockMvc.perform(post("/v1/agendamento").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isForbidden());
        mockMvc.perform(patch("/v1/agendamento/1").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isForbidden());
    }

    private void assertListaTodos() throws Exception {
        for (String url : CADASTROS) {
            mockMvc.perform(get(url)).andExpect(status().isOk());
        }
    }
}
