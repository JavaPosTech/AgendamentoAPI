package br.com.fiap.agendamentoapi.model.request.enfermeiro;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representa o modelo de requisição para atualizar um Enfermeiro. Os campos são opcionais: os que forem omitidos mantêm o valor atual.")
public record AtualizarEnfermeiroRequest(

        String nome,

        String sobrenome,

        String coren

) {}
