package br.com.fiap.agendamentoapi.controller.agendamento;

import br.com.fiap.agendamentoapi.config.AbstractControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(roles = "RECEPCIONISTA")
@SpringBootTest
class AgendamentoControllerTest extends AbstractControllerTest {

    private String salvarAgendamentoRequest;

    private String salvarAgendamentoMedicoIndisponivelRequest;

    private String atualizarAgendamentoRequest;

    private String atualizarAgendamentoParcialRequest;

    @BeforeEach
    void setUp() throws IOException {
        if (salvarAgendamentoRequest == null && atualizarAgendamentoRequest == null) {
            salvarAgendamentoRequest = new String(Files.readAllBytes(Paths.get("src/test/resources/agendamento/salvarAgendamentoRequest.json")));
            salvarAgendamentoMedicoIndisponivelRequest = new String(Files.readAllBytes(Paths.get("src/test/resources/agendamento/salvarAgendamentoMedicoIndisponivelRequest.json")));
            atualizarAgendamentoRequest = new String(Files.readAllBytes(Paths.get("src/test/resources/agendamento/atualizarAgendamentoRequest.json")));
            atualizarAgendamentoParcialRequest = new String(Files.readAllBytes(Paths.get("src/test/resources/agendamento/atualizarAgendamentoParcialRequest.json")));
        }
    }

    @Test
    void listarTest() throws Exception {
        testGet("/v1/agendamento");
    }

    @Test
    @WithMockUser(username = "pedro.almeida", roles = "PACIENTE")
    void listarComoPacienteFiltraPelasPropriasTest() throws Exception {
        mockMvc.perform(get("/v1/agendamento"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.content[*].paciente", everyItem(is("PEDRO"))));
    }

    @Test
    @WithMockUser(roles = "PACIENTE")
    void listarComoPacienteSemCadastroRetornaVazioTest() throws Exception {
        mockMvc.perform(get("/v1/agendamento"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    void salvarTest() throws Exception {
        testPost("/v1/agendamento", salvarAgendamentoRequest);
    }

    @Test
    void salvarComMedicoIndisponivelTest() throws Exception {
        testPostStatusConflict("/v1/agendamento", salvarAgendamentoMedicoIndisponivelRequest);
    }

    @Test
    void atualizarTest() throws Exception {
        testPatch("/v1/agendamento/1", atualizarAgendamentoRequest);
    }

    @Test
    void atualizarParcialTest() throws Exception {
        testPatch("/v1/agendamento/1", atualizarAgendamentoParcialRequest);
    }

    @Test
    void salvarComHorarioJaAgendadoRetornaConflitoTest() throws Exception {
        testPost("/v1/agendamento", salvarAgendamentoRequest);

        mockMvc.perform(post("/v1/agendamento")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(salvarAgendamentoRequest))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.title").value("Horário de Consulta Indisponível!"));
    }

    @Test
    void salvarSemDataHoraConsultaRetornaBadRequestTest() throws Exception {
        mockMvc.perform(post("/v1/agendamento")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"medicoId\":1,\"pacienteId\":1}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void cancelarTest() throws Exception {
        testDelete("/v1/agendamento/1");
    }
}
