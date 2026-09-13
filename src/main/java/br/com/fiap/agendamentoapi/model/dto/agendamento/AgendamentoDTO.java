package br.com.fiap.agendamentoapi.model.dto.agendamento;

import br.com.fiap.agendamentoapi.model.entity.agendamento.Agendamento;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Representa o modelo de dados de um Agendamento.")
public record AgendamentoDTO(

        @Schema(description = "Id da consulta.", example = "1")
        Integer id,

        @Schema(description = "Nome do médico que atende a consulta.", example = "JOAO")
        String medico,

        @Schema(description = "Nome do paciente da consulta.", example = "PEDRO")
        String paciente,

        @Schema(type = "string", format = "dd/MM/yyyy - HH:mm:ss", description = "Data e hora da consulta.", example = "22/08/2026 - 08:00:00")
        @JsonFormat(pattern = "dd/MM/yyyy - HH:mm:ss")
        LocalDateTime dataHoraConsulta,

        @Schema(description = "Observação livre sobre a consulta.", example = "Primeira consulta cardiológica.")
        String observacao,

        @Schema(type = "string", format = "dd/MM/yyyy - HH:mm:ss", description = "Data e hora em que a consulta foi marcada.", example = "10/08/2026 - 14:32:05")
        @JsonFormat(pattern = "dd/MM/yyyy - HH:mm:ss")
        LocalDateTime dataCadastro

) {
    public AgendamentoDTO(Agendamento agendamento) {
        this(agendamento.getId(),
                agendamento.getMedico().getNome(),
                agendamento.getPaciente().getNome(),
                agendamento.getDataHoraConsulta(),
                agendamento.getObservacao(),
                agendamento.getDataCadastro());
    }
}