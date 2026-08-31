package br.com.fiap.agendamentoapi.controller.recepcionista;

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
class RecepcionistaControllerTest extends AbstractControllerTest {

    private String salvarRecepcionistaRequest;

    private String atualizarRecepcionistaRequest;

    @BeforeEach
    void setUp() throws IOException {
        salvarRecepcionistaRequest = new String(Files.readAllBytes(Paths.get("src/test/resources/recepcionista/salvarRecepcionistaRequest.json")));
        atualizarRecepcionistaRequest = new String(Files.readAllBytes(Paths.get("src/test/resources/recepcionista/atualizarRecepcionistaRequest.json")));
    }

    @Test
    void listarTest() throws Exception {
        testGet("/v1/recepcionista");
    }

    @Test
    void salvarTest() throws Exception {
        testPost("/v1/recepcionista", salvarRecepcionistaRequest);
    }

    @Test
    void atualizarTest() throws Exception {
        testPatch("/v1/recepcionista/1", atualizarRecepcionistaRequest);
    }

    @Test
    void deletarTest() throws Exception {
        testDelete("/v1/recepcionista/1");
    }
}
