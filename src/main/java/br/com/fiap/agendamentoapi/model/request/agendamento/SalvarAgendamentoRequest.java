package br.com.fiap.agendamentoapi.model.request.agendamento;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "Representa o modelo de requisição para criar um Agendamento. O médico informado precisa ter um intervalo mínimo de 1 hora entre as consultas.")
public record SalvarAgendamentoRequest(

        @Schema(description = "Id do médico que vai atender a consulta.", example = "1")
        @NotNull(message = "O campo 'medicoId' é obrigatório!")
        Integer medicoId,

        @Schema(description = "Id do paciente da consulta.", example = "1")
        @NotNull(message = "O campo 'pacienteId' é obrigatório!")
        Integer pacienteId,

        @Schema(type = "string", format = "dd/MM/yyyy - HH:mm:ss", description = "Data e hora da consulta no formato dd/MM/yyyy - HH:mm:ss.", example = "15/10/2026 - 09:00:00")
        @NotNull(message = "O campo 'dataHoraConsulta' é obrigatório!")
        @JsonFormat(pattern = "dd/MM/yyyy - HH:mm:ss")
        LocalDateTime dataHoraConsulta,

        @Schema(description = "Observação livre sobre a consulta.", example = "Primeira consulta cardiológica.")
        String observacao

) {}