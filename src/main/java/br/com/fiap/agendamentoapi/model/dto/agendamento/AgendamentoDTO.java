package br.com.fiap.agendamentoapi.model.dto.agendamento;

import br.com.fiap.agendamentoapi.model.entity.agendamento.Agendamento;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Representa o modelo de dados de um Agendamento.")
public record AgendamentoDTO(

        Integer id,

        String medico,

        String paciente,

        @JsonFormat(pattern = "dd/MM/yyyy - HH:mm:ss")
        LocalDateTime dataHoraConsulta,

        @JsonFormat(pattern = "dd/MM/yyyy - HH:mm:ss")
        LocalDateTime dataCadastro

) {
    public AgendamentoDTO(Agendamento agendamento) {
        this(agendamento.getId(),
                agendamento.getMedico().getNome(),
                agendamento.getPaciente().getNome(),
                agendamento.getDataHoraConsulta(),
                agendamento.getDataCadastro());
    }
}