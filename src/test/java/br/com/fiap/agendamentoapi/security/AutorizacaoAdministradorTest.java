package br.com.fiap.agendamentoapi.security;

import br.com.fiap.agendamentoapi.config.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AutorizacaoAdministradorTest extends AbstractControllerTest {

    private static final List<String> GESTAO_PESSOAL = List.of("/v1/medico", "/v1/enfermeiro", "/v1/recepcionista");

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    void administradorGerenciaMedicoEnfermeiroERecepcionistaTest() throws Exception {
        for (String url : GESTAO_PESSOAL) {
            mockMvc.perform(get(url)).andExpect(status().isOk());
            criar(url).andExpect(status().isBadRequest());
        }
    }

    @Test
    @WithMockUser(roles = "MEDICO")
    void medicoListaMasNaoGerenciaTest() throws Exception {
        assertListaMasNaoGerencia();
    }

    @Test
    @WithMockUser(roles = "ENFERMEIRO")
    void enfermeiroListaMasNaoGerenciaTest() throws Exception {
        assertListaMasNaoGerencia();
    }

    @Test
    @WithMockUser(roles = "RECEPCIONISTA")
    void recepcionistaListaMasNaoGerenciaTest() throws Exception {
        assertListaMasNaoGerencia();
    }

    @Test
    @WithMockUser(roles = "PACIENTE")
    void pacienteNaoListaNemGerenciaTest() throws Exception {
        for (String url : GESTAO_PESSOAL) {
            mockMvc.perform(get(url)).andExpect(status().isForbidden());
            assertGerenciaNegada(url);
        }
    }

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    void administradorExcluiPacienteTest() throws Exception {
        mockMvc.perform(delete("/v1/paciente/1")).andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "RECEPCIONISTA")
    void recepcionistaNaoExcluiPacienteTest() throws Exception {
        mockMvc.perform(delete("/v1/paciente/1")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "MEDICO")
    void medicoNaoExcluiPacienteTest() throws Exception {
        mockMvc.perform(delete("/v1/paciente/1")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "MEDICO")
    void gerenciaNegadaRetornaCorpoPadronizadoTest() throws Exception {
        criar("/v1/medico")
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.title").value("Acesso Negado!"))
                .andExpect(jsonPath("$.instance").value("/v1/medico"));
    }

    @Test
    @WithAnonymousUser
    void semAutenticacaoRecebeNaoAutorizadoTest() throws Exception {
        mockMvc.perform(get("/v1/recepcionista"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.title").value("Não Autorizado!"));
    }

    private void assertListaMasNaoGerencia() throws Exception {
        for (String url : GESTAO_PESSOAL) {
            mockMvc.perform(get(url)).andExpect(status().isOk());
            assertGerenciaNegada(url);
        }
    }

    private ResultActions criar(String url) throws Exception {
        return mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"));
    }

    private void assertGerenciaNegada(String url) throws Exception {
        criar(url).andExpect(status().isForbidden());

        mockMvc.perform(patch(url + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());

        mockMvc.perform(delete(url + "/1"))
                .andExpect(status().isForbidden());
    }
}
