package br.com.fiap.agendamentoapi.model.request.recepcionista;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representa o modelo de requisição para atualizar um Recepcionista. Os campos são opcionais: os que forem omitidos mantêm o valor atual.")
public record AtualizarRecepcionistaRequest(

        String nome,

        String sobrenome

) {}
