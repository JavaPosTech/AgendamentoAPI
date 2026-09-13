package br.com.fiap.agendamentoapi.model.request.agendamento;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Representa o modelo de requisição para atualizar um Agendamento. Os campos são opcionais: os que forem omitidos mantêm o valor atual. O novo horário precisa respeitar o intervalo mínimo de 1 hora entre as consultas do médico.")
public record AtualizarAgendamentoRequest(

        @Schema(type = "string", format = "dd/MM/yyyy - HH:mm:ss", description = "Novo horário da consulta no formato dd/MM/yyyy - HH:mm:ss.", example = "15/10/2026 - 11:00:00")
        @JsonFormat(pattern = "dd/MM/yyyy - HH:mm:ss")
        LocalDateTime dataHoraConsulta,

        @Schema(description = "Observação livre sobre a consulta.", example = "Paciente pediu para remarcar.")
        String observacao

) {}