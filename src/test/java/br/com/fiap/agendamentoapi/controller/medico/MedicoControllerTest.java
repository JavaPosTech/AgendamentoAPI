package br.com.fiap.agendamentoapi.controller.medico;

import br.com.fiap.agendamentoapi.config.AbstractControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@WithMockUser(roles = "ADMINISTRADOR")
@SpringBootTest
class MedicoControllerTest extends AbstractControllerTest {

    private String salvarMedicoRequest;

    private String atualizarMedicoRequest;

    private String atualizarMedicoParcialRequest;

    @BeforeEach
    void setUp() throws IOException {
        if (salvarMedicoRequest == null && atualizarMedicoRequest == null) {
            salvarMedicoRequest = new String(Files.readAllBytes(Paths.get("src/test/resources/medico/salvarMedicoRequest.json")));
            atualizarMedicoRequest = new String(Files.readAllBytes(Paths.get("src/test/resources/medico/atualizarMedicoRequest.json")));
            atualizarMedicoParcialRequest = new String(Files.readAllBytes(Paths.get("src/test/resources/medico/atualizarMedicoParcialRequest.json")));
        }
    }

    @Test
    void listarTest() throws Exception {
        testGet("/v1/medico");
    }

    @Test
    void salvarTest() throws Exception {
        testPost("/v1/medico", salvarMedicoRequest);
    }

    @Test
    void atualizarTest() throws Exception {
        testPatch("/v1/medico/1", atualizarMedicoRequest);
    }

    @Test
    void atualizarParcialTest() throws Exception {
        testPatch("/v1/medico/1", atualizarMedicoParcialRequest);
    }

    @Test
    void deletarTest() throws Exception {
        testDelete("/v1/medico/1");
    }
}