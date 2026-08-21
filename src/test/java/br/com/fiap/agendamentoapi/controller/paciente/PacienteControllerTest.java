package br.com.fiap.agendamentoapi.controller.paciente;

import br.com.fiap.agendamentoapi.config.AbstractControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootTest
class PacienteControllerTest extends AbstractControllerTest {

    private String criarPacienteRequest;

    private String atualizarPacienteRequest;

    @BeforeEach
    void setUp() throws IOException {
        if (criarPacienteRequest == null && atualizarPacienteRequest == null) {
            criarPacienteRequest = new String(Files.readAllBytes(Paths.get("src/test/resources/paciente/criarPacienteRequest.json")));
            atualizarPacienteRequest = new String(Files.readAllBytes(Paths.get("src/test/resources/paciente/atualizarPacienteRequest.json")));
        }
    }

    @Test
    void listarTest() throws Exception {
        testGet("/v1/paciente");
    }

    @Test
    void salvarTest() throws Exception {
        testPost("/v1/paciente", criarPacienteRequest);
    }

    @Test
    void atualizarTest() throws Exception {
        testPatch("/v1/paciente/1", atualizarPacienteRequest);
    }

    @Test
    void deletarTest() throws Exception {
        testDelete("/v1/paciente/1");
    }
}