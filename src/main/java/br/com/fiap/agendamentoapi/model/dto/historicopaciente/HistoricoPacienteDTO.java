package br.com.fiap.agendamentoapi.model.dto.historicopaciente;

import br.com.fiap.agendamentoapi.model.entity.historicopaciente.HistoricoPaciente;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representa o modelo de dados de um Histórico de Paciente.")
public record HistoricoPacienteDTO(

        Integer id,

        Integer pacienteId,

        String paciente,

        String queixaPrincipal,

        String historicoDoenca,

        String medicamentos,

        String alergias,

        String observacoes

) {
    public HistoricoPacienteDTO(HistoricoPaciente historicoPaciente) {
        this(historicoPaciente.getId(),
                historicoPaciente.getPaciente().getId(),
                historicoPaciente.getPaciente().getNome(),
                historicoPaciente.getQueixaPrincipal(),
                historicoPaciente.getHistoricoDoenca(),
                historicoPaciente.getMedicamentos(),
                historicoPaciente.getAlergias(),
                historicoPaciente.getObservacoes());
    }
}
