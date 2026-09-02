package br.com.fiap.agendamentoapi.model.request.agendamento;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "Representa o modelo de requisição para criar um Agendamento.")
public record SalvarAgendamentoRequest(

        @NotNull(message = "O campo 'medicoId' é obrigatório!")
        Integer medicoId,

        @NotNull(message = "O campo 'pacienteId' é obrigatório!")
        Integer pacienteId,

        @NotNull(message = "O campo 'dataHoraConsulta' é obrigatório!")
        @JsonFormat(pattern = "dd/MM/yyyy - HH:mm:ss")
        LocalDateTime dataHoraConsulta,

        String observacao

) {}