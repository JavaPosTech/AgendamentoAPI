package br.com.fiap.agendamentoapi.model.dto.historicopaciente;

import br.com.fiap.agendamentoapi.model.entity.historicopaciente.HistoricoPaciente;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representa o histórico clínico de um Paciente.")
public record HistoricoPacienteDTO(

        @Schema(description = "Id do histórico.", example = "1")
        Integer id,

        @Schema(description = "Id do paciente dono do histórico.", example = "1")
        Integer pacienteId,

        @Schema(description = "Nome completo do paciente.", example = "PEDRO ALMEIDA")
        String paciente,

        @Schema(description = "Motivo principal relatado pelo paciente.", example = "Dor no peito")
        String queixaPrincipal,

        @Schema(description = "Histórico da doença atual.", example = "Paciente relata dores no peito recorrentes há aproximadamente 2 meses.")
        String historicoDoenca,

        @Schema(description = "Medicamentos em uso.", example = "Losartana 50mg")
        String medicamentos,

        @Schema(description = "Alergias conhecidas.", example = "Nenhuma alergia conhecida.")
        String alergias,

        @Schema(description = "Observações livres sobre o paciente.", example = "Recomendada avaliação cardiológica.")
        String observacoes

) {
    public HistoricoPacienteDTO(HistoricoPaciente historicoPaciente) {
        this(historicoPaciente.getId(),
                historicoPaciente.getPaciente().getId(),
                historicoPaciente.getPaciente().getNome() + " " + historicoPaciente.getPaciente().getSobrenome(),
                historicoPaciente.getQueixaPrincipal(),
                historicoPaciente.getHistoricoDoenca(),
                historicoPaciente.getMedicamentos(),
                historicoPaciente.getAlergias(),
                historicoPaciente.getObservacoes());
    }
}