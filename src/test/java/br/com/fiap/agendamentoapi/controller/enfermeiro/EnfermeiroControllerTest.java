package br.com.fiap.agendamentoapi.controller.enfermeiro;

import br.com.fiap.agendamentoapi.config.AbstractControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootTest
class EnfermeiroControllerTest extends AbstractControllerTest {

    private String criarEnfermeiroRequest;

    private String atualizarEnfermeiroRequest;

    @BeforeEach
    void setUp() throws IOException {
        if (criarEnfermeiroRequest == null && atualizarEnfermeiroRequest == null) {
            criarEnfermeiroRequest = new String(Files.readAllBytes(Paths.get("src/test/resources/enfermeiro/criarEnfermeiroRequest.json")));
            atualizarEnfermeiroRequest = new String(Files.readAllBytes(Paths.get("src/test/resources/enfermeiro/atualizarEnfermeiroRequest.json")));
        }
    }

    @Test
    void listarTest() throws Exception {
        testGet("/v1/enfermeiro");
    }

    @Test
    void salvarTest() throws Exception {
        testPost("/v1/enfermeiro", criarEnfermeiroRequest);
    }

    @Test
    void atualizarTest() throws Exception {
        testPatch("/v1/enfermeiro/1", atualizarEnfermeiroRequest);
    }

    @Test
    void deletarTest() throws Exception {
        testDelete("/v1/enfermeiro/1");
    }
}