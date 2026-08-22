package br.com.fiap.agendamentoapi.model.dto.agendamento;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Representa o modelo de dados de um Agendamento.")
public record AgendamentoDTO(

        Integer id,

        Integer medicoId,

        Integer pacienteId,

        String statusConsulta,

        @JsonFormat(pattern = "dd/MM/yyyy - HH:mm:ss")
        LocalDateTime dataHoraConsulta,

        @JsonFormat(pattern = "dd/MM/yyyy - HH:mm:ss")
        LocalDateTime dataCadastro

) {}