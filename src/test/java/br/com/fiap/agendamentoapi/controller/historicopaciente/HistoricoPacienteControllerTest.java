package br.com.fiap.agendamentoapi.controller.historicopaciente;

import br.com.fiap.agendamentoapi.config.AbstractControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@WithMockUser(roles = "RECEPCIONISTA")
@SpringBootTest
class HistoricoPacienteControllerTest extends AbstractControllerTest {

    private String salvarHistoricoPacienteRequest;

    private String atualizarHistoricoPacienteRequest;

    private String atualizarHistoricoPacienteParcialRequest;

    @BeforeEach
    void setUp() throws IOException {
        salvarHistoricoPacienteRequest = new String(Files.readAllBytes(Paths.get("src/test/resources/historicopaciente/salvarHistoricoPacienteRequest.json")));
        atualizarHistoricoPacienteRequest = new String(Files.readAllBytes(Paths.get("src/test/resources/historicopaciente/atualizarHistoricoPacienteRequest.json")));
        atualizarHistoricoPacienteParcialRequest = new String(Files.readAllBytes(Paths.get("src/test/resources/historicopaciente/atualizarHistoricoPacienteParcialRequest.json")));
    }

    @Test
    void listarTest() throws Exception {
        testGet("/v1/historico-paciente");
    }

    @Test
    void salvarTest() throws Exception {
        testPost("/v1/historico-paciente", salvarHistoricoPacienteRequest);
    }

    @Test
    void atualizarTest() throws Exception {
        testPatch("/v1/historico-paciente/1", atualizarHistoricoPacienteRequest);
    }

    @Test
    void atualizarParcialTest() throws Exception {
        testPatch("/v1/historico-paciente/1", atualizarHistoricoPacienteParcialRequest);
    }

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    void deletarTest() throws Exception {
        testDelete("/v1/historico-paciente/1");
    }
}
