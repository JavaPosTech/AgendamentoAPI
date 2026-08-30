package br.com.fiap.agendamentoapi.model.request.agendamento;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Representa o modelo de requisição para atualizar um Agendamento.")
public record AtualizarAgendamentoRequest(

        @JsonFormat(pattern = "dd/MM/yyyy - HH:mm:ss")
        LocalDateTime dataHoraConsulta,

        String observacao

) {}