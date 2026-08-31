package br.com.fiap.agendamentoapi.service.historicopaciente;

import br.com.fiap.agendamentoapi.config.AbstractTest;
import br.com.fiap.agendamentoapi.exceptions.HistoricoPacienteNaoEncontradoException;
import br.com.fiap.agendamentoapi.model.request.historicopaciente.AtualizarHistoricoPacienteRequest;
import br.com.fiap.agendamentoapi.model.request.historicopaciente.SalvarHistoricoPacienteRequest;
import br.com.fiap.agendamentoapi.repository.historicopaciente.HistoricoPacienteRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

@SpringBootTest
class HistoricoPacienteServiceTest extends AbstractTest {

    @Autowired
    private HistoricoPacienteService historicoPacienteService;

    @Autowired
    private HistoricoPacienteRepository historicoPacienteRepository;

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

        var historico = historicoPacienteRepository.findById(1).orElseThrow();

        Assertions.assertEquals("Dor de cabeça persistente", historico.getQueixaPrincipal());
        Assertions.assertEquals("Paciente relata aumento da frequência das dores.", historico.getHistoricoDoenca());
        Assertions.assertEquals("Solicitada nova avaliação clínica.", historico.getObservacoes());
    }

    @Test
    void atualizarMantemCamposOpcionaisOmitidosTest() {
        var historicoAntes = historicoPacienteRepository.findById(1).orElseThrow();

        var queixaOriginal = historicoAntes.getQueixaPrincipal();
        var observacoesOriginais = historicoAntes.getObservacoes();

        Assertions.assertNotNull(queixaOriginal);
        Assertions.assertNotNull(observacoesOriginais);

        historicoPacienteService.atualizar(1, new AtualizarHistoricoPacienteRequest(
                null,
                "Paciente relata melhora do quadro.",
                "Dipirona 500mg",
                "Nenhuma alergia conhecida.",
                null
        ));

        var historicoDepois = historicoPacienteRepository.findById(1).orElseThrow();

        Assertions.assertEquals(queixaOriginal, historicoDepois.getQueixaPrincipal());
        Assertions.assertEquals(observacoesOriginais, historicoDepois.getObservacoes());
        Assertions.assertEquals("Paciente relata melhora do quadro.", historicoDepois.getHistoricoDoenca());
        Assertions.assertEquals("Dipirona 500mg", historicoDepois.getMedicamentos());
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
        Assertions.assertFalse(historicoPacienteRepository.existsById(1));
    }

    @Test
    void deletarTestComIdInexistente() {
        Assertions.assertThrows(HistoricoPacienteNaoEncontradoException.class, () -> historicoPacienteService.deletar(Integer.MAX_VALUE));
    }
}
