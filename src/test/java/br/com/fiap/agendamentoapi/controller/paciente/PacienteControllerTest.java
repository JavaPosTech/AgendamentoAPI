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

    private String salvarPacienteRequest;

    private String atualizarPacienteRequest;

    private String atualizarPacienteParcialRequest;

    @BeforeEach
    void setUp() throws IOException {
        if (salvarPacienteRequest == null && atualizarPacienteRequest == null) {
            salvarPacienteRequest = new String(Files.readAllBytes(Paths.get("src/test/resources/paciente/salvarPacienteRequest.json")));
            atualizarPacienteRequest = new String(Files.readAllBytes(Paths.get("src/test/resources/paciente/atualizarPacienteRequest.json")));
            atualizarPacienteParcialRequest = new String(Files.readAllBytes(Paths.get("src/test/resources/paciente/atualizarPacienteParcialRequest.json")));
        }
    }

    @Test
    void listarTest() throws Exception {
        testGet("/v1/paciente");
    }

    @Test
    void salvarTest() throws Exception {
        testPost("/v1/paciente", salvarPacienteRequest);
    }

    @Test
    void atualizarTest() throws Exception {
        testPatch("/v1/paciente/1", atualizarPacienteRequest);
    }

    @Test
    void atualizarParcialTest() throws Exception {
        testPatch("/v1/paciente/1", atualizarPacienteParcialRequest);
    }

    @Test
    void deletarTest() throws Exception {
        testDelete("/v1/paciente/1");
    }
}