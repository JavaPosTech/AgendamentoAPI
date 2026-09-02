package br.com.fiap.agendamentoapi.controller.enfermeiro;

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
class EnfermeiroControllerTest extends AbstractControllerTest {

    private String salvarEnfermeiroRequest;

    private String atualizarEnfermeiroRequest;

    private String atualizarEnfermeiroParcialRequest;

    @BeforeEach
    void setUp() throws IOException {
        if (salvarEnfermeiroRequest == null && atualizarEnfermeiroRequest == null) {
            salvarEnfermeiroRequest = new String(Files.readAllBytes(Paths.get("src/test/resources/enfermeiro/salvarEnfermeiroRequest.json")));
            atualizarEnfermeiroRequest = new String(Files.readAllBytes(Paths.get("src/test/resources/enfermeiro/atualizarEnfermeiroRequest.json")));
            atualizarEnfermeiroParcialRequest = new String(Files.readAllBytes(Paths.get("src/test/resources/enfermeiro/atualizarEnfermeiroParcialRequest.json")));
        }
    }

    @Test
    void listarTest() throws Exception {
        testGet("/v1/enfermeiro");
    }

    @Test
    void salvarTest() throws Exception {
        testPost("/v1/enfermeiro", salvarEnfermeiroRequest);
    }

    @Test
    void atualizarTest() throws Exception {
        testPatch("/v1/enfermeiro/1", atualizarEnfermeiroRequest);
    }

    @Test
    void atualizarParcialTest() throws Exception {
        testPatch("/v1/enfermeiro/1", atualizarEnfermeiroParcialRequest);
    }

    @Test
    void deletarTest() throws Exception {
        testDelete("/v1/enfermeiro/1");
    }
}