package br.com.fiap.agendamentoapi.model.request.medico;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representa o modelo de requisição para atualizar um Médico. Os campos são opcionais: os que forem omitidos mantêm o valor atual.")
public record AtualizarMedicoRequest(

        String nome,

        String sobrenome,

        String crm,

        String especialidade,

        String endereco

) {}
