package br.com.fiap.agendamentoapi.controller.agendamento;

import br.com.fiap.agendamentoapi.config.AbstractControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootTest
class AgendamentoControllerTest extends AbstractControllerTest {

    private String salvarAgendamentoRequest;

    private String atualizarAgendamentoRequest;

    private String atualizarAgendamentoParcialRequest;

    @BeforeEach
    void setUp() throws IOException {
        if (salvarAgendamentoRequest == null && atualizarAgendamentoRequest == null) {
            salvarAgendamentoRequest = new String(Files.readAllBytes(Paths.get("src/test/resources/agendamento/salvarAgendamentoRequest.json")));
            atualizarAgendamentoRequest = new String(Files.readAllBytes(Paths.get("src/test/resources/agendamento/atualizarAgendamentoRequest.json")));
            atualizarAgendamentoParcialRequest = new String(Files.readAllBytes(Paths.get("src/test/resources/agendamento/atualizarAgendamentoParcialRequest.json")));
        }
    }

    @Test
    void listarTest() throws Exception {
        testGet("/v1/agendamento");
    }

    @Test
    void salvarTest() throws Exception {
        testPost("/v1/agendamento", salvarAgendamentoRequest);
    }

    @Test
    void atualizarTest() throws Exception {
        testPatch("/v1/agendamento/1", atualizarAgendamentoRequest);
    }

    @Test
    void atualizarParcialTest() throws Exception {
        testPatch("/v1/agendamento/1", atualizarAgendamentoParcialRequest);
    }
}