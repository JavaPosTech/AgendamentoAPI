package br.com.fiap.agendamentoapi.security;

import br.com.fiap.agendamentoapi.config.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AutorizacaoConsultaHistoricoTest extends AbstractControllerTest {

    private static final String HISTORICO = "/v1/historico-paciente";

    private static final String AGENDAMENTO = "/v1/agendamento";

    @Test
    @WithMockUser(roles = "MEDICO")
    void medicoCriaEditaEVeHistoricoMasNaoRemoveTest() throws Exception {
        criar(HISTORICO).andExpect(status().isBadRequest());
        editar(HISTORICO).andExpect(status().isOk());
        listar(HISTORICO).andExpect(status().isOk());
        remover(HISTORICO).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "RECEPCIONISTA")
    void recepcionistaCriaEditaEVeHistoricoMasNaoRemoveTest() throws Exception {
        criar(HISTORICO).andExpect(status().isBadRequest());
        editar(HISTORICO).andExpect(status().isOk());
        listar(HISTORICO).andExpect(status().isOk());
        remover(HISTORICO).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ENFERMEIRO")
    void enfermeiroApenasVeHistoricoTest() throws Exception {
        listar(HISTORICO).andExpect(status().isOk());
        criar(HISTORICO).andExpect(status().isForbidden());
        editar(HISTORICO).andExpect(status().isForbidden());
        remover(HISTORICO).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    void administradorApenasRemoveHistoricoTest() throws Exception {
        remover(HISTORICO).andExpect(status().isNoContent());
        listar(HISTORICO).andExpect(status().isForbidden());
        criar(HISTORICO).andExpect(status().isForbidden());
        editar(HISTORICO).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "PACIENTE")
    void pacienteNaoAcessaHistoricoTest() throws Exception {
        listar(HISTORICO).andExpect(status().isForbidden());
        criar(HISTORICO).andExpect(status().isForbidden());
        editar(HISTORICO).andExpect(status().isForbidden());
        remover(HISTORICO).andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void anonimoNaoAcessaHistoricoTest() throws Exception {
        listar(HISTORICO).andExpect(status().isUnauthorized());
        criar(HISTORICO).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "MEDICO")
    void medicoMarcaEVeConsultaMasNaoRemarcaNemCancelaTest() throws Exception {
        criar(AGENDAMENTO).andExpect(status().isBadRequest());
        listar(AGENDAMENTO).andExpect(status().isOk());
        editar(AGENDAMENTO).andExpect(status().isForbidden());
        remover(AGENDAMENTO).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ENFERMEIRO")
    void enfermeiroMarcaEVeConsultaMasNaoRemarcaNemCancelaTest() throws Exception {
        criar(AGENDAMENTO).andExpect(status().isBadRequest());
        listar(AGENDAMENTO).andExpect(status().isOk());
        editar(AGENDAMENTO).andExpect(status().isForbidden());
        remover(AGENDAMENTO).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "RECEPCIONISTA")
    void recepcionistaMarcaVeRemarcaECancelaConsultaTest() throws Exception {
        criar(AGENDAMENTO).andExpect(status().isBadRequest());
        listar(AGENDAMENTO).andExpect(status().isOk());
        editar(AGENDAMENTO).andExpect(status().isOk());
        remover(AGENDAMENTO).andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "PACIENTE")
    void pacienteApenasVeConsultaTest() throws Exception {
        listar(AGENDAMENTO).andExpect(status().isOk());
        criar(AGENDAMENTO).andExpect(status().isForbidden());
        editar(AGENDAMENTO).andExpect(status().isForbidden());
        remover(AGENDAMENTO).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    void administradorNaoAcessaConsultaTest() throws Exception {
        listar(AGENDAMENTO).andExpect(status().isForbidden());
        criar(AGENDAMENTO).andExpect(status().isForbidden());
        editar(AGENDAMENTO).andExpect(status().isForbidden());
        remover(AGENDAMENTO).andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void anonimoNaoAcessaConsultaTest() throws Exception {
        listar(AGENDAMENTO).andExpect(status().isUnauthorized());
        criar(AGENDAMENTO).andExpect(status().isUnauthorized());
    }

    private ResultActions criar(String url) throws Exception {
        return mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"));
    }

    private ResultActions editar(String url) throws Exception {
        return mockMvc.perform(patch(url + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"));
    }

    private ResultActions listar(String url) throws Exception {
        return mockMvc.perform(get(url));
    }

    private ResultActions remover(String url) throws Exception {
        return mockMvc.perform(delete(url + "/1"));
    }
}
