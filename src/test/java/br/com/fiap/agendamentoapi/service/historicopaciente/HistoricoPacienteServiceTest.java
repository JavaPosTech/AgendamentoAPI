package br.com.fiap.agendamentoapi.service.historicopaciente;

import br.com.fiap.agendamentoapi.config.AbstractTest;
import br.com.fiap.agendamentoapi.exceptions.HistoricoPacienteNaoEncontradoException;
import br.com.fiap.agendamentoapi.model.request.historicopaciente.AtualizarHistoricoPacienteRequest;
import br.com.fiap.agendamentoapi.model.request.historicopaciente.SalvarHistoricoPacienteRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

@SpringBootTest
class HistoricoPacienteServiceTest extends AbstractTest {

    @Autowired
    private HistoricoPacienteService historicoPacienteService;

    @Test
    void getHistoricosTest() {
        var historicos = Assertions.assertDoesNotThrow(() -> historicoPacienteService.getHistoricos(Pageable.unpaged()));
        Assertions.assertNotNull(historicos);
    }

    @Test
    void salvarTest() {
        Assertions.assertDoesNotThrow(() -> historicoPacienteService.salvar(new SalvarHistoricoPacienteRequest(
                1,
                "Dor de cabeça",
                "Paciente relata dor de cabeça recorrente.",
                "Paracetamol 750mg",
                "Nenhuma alergia conhecida.",
                "Acompanhar evolução dos sintomas."
        )));
    }

    @Test
    void atualizarTest() {
        Assertions.assertDoesNotThrow(() -> historicoPacienteService.atualizar(1, new AtualizarHistoricoPacienteRequest(
                "Dor de cabeça persistente",
                "Paciente relata aumento da frequência das dores.",
                "Paracetamol 750mg",
                "Nenhuma alergia conhecida.",
                "Solicitada nova avaliação clínica."
        )));
    }

    @Test
    void atualizarTestComIdInexistente() {
        Assertions.assertThrows(HistoricoPacienteNaoEncontradoException.class, () -> historicoPacienteService.atualizar(
                Integer.MAX_VALUE,
                new AtualizarHistoricoPacienteRequest(
                        "Dor de cabeça persistente",
                        "Paciente relata aumento da frequência das dores.",
                        "Paracetamol 750mg",
                        "Nenhuma alergia conhecida.",
                        "Solicitada nova avaliação clínica."
                )
        ));
    }

    @Test
    void deletarTest() {
        Assertions.assertDoesNotThrow(() -> historicoPacienteService.deletar(1));
    }

    @Test
    void deletarTestComIdInexistente() {
        Assertions.assertThrows(HistoricoPacienteNaoEncontradoException.class, () -> historicoPacienteService.deletar(Integer.MAX_VALUE));
    }
}
