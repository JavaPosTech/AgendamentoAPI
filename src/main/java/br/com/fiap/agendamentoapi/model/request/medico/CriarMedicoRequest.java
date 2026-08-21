package br.com.fiap.agendamentoapi.model.request.medico;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representa o modelo de requisição para criar um Médico.")
public record CriarMedicoRequest(

        String login,

        String senha,

        String nome,

        String sobrenome,

        String crm,

        String especialidade,

        String endereco

) {}