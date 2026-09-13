package br.com.fiap.agendamentoapi.model.request.historicopaciente;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "Representa o modelo de requisição para atualizar um Histórico de Paciente. Os campos são opcionais: os que forem omitidos mantêm o valor atual.")
public record AtualizarHistoricoPacienteRequest(

        @Schema(description = "Motivo principal relatado pelo paciente.", example = "Dor no peito")
        @Size(max = 500, message = "O campo 'queixaPrincipal' deve ter no máximo 500 caracteres!")
        String queixaPrincipal,

        @Schema(description = "Histórico da doença atual.", example = "Paciente relata dores no peito recorrentes há aproximadamente 2 meses.")
        String historicoDoenca,

        @Schema(description = "Medicamentos em uso.", example = "Losartana 50mg")
        String medicamentos,

        @Schema(description = "Alergias conhecidas.", example = "Nenhuma alergia conhecida.")
        String alergias,

        @Schema(description = "Observações livres sobre o paciente.", example = "Recomendada avaliação cardiológica.")
        String observacoes

) {}