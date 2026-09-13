package br.com.fiap.agendamentoapi.model.request.historicopaciente;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Representa o modelo de requisição para criar um Histórico de Paciente.")
public record SalvarHistoricoPacienteRequest(

        @Schema(description = "Id do paciente dono do histórico.", example = "1")
        @NotNull(message = "O campo 'pacienteId' é obrigatório!")
        Integer pacienteId,

        @Schema(description = "Motivo principal relatado pelo paciente.", example = "Dor no peito")
        @Size(max = 500, message = "O campo 'queixaPrincipal' deve ter no máximo 500 caracteres!")
        String queixaPrincipal,

        @Schema(description = "Histórico da doença atual.", example = "Paciente relata dores no peito recorrentes há aproximadamente 2 meses.")
        @NotBlank(message = "O campo 'historicoDoenca' é obrigatório!")
        String historicoDoenca,

        @Schema(description = "Medicamentos em uso.", example = "Losartana 50mg")
        @NotBlank(message = "O campo 'medicamentos' é obrigatório!")
        String medicamentos,

        @Schema(description = "Alergias conhecidas.", example = "Nenhuma alergia conhecida.")
        @NotBlank(message = "O campo 'alergias' é obrigatório!")
        String alergias,

        @Schema(description = "Observações livres sobre o paciente.", example = "Recomendada avaliação cardiológica.")
        String observacoes

) {}