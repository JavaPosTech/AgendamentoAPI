package br.com.fiap.agendamentoapi.model.request.medico;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Representa o modelo de requisição para atualizar um Médico.")
public record AtualizarMedicoRequest(

        @NotBlank(message = "O campo 'nome' é obrigatório!")
        String nome,

        @NotBlank(message = "O campo 'sobrenome' é obrigatório!")
        String sobrenome,

        @NotBlank(message = "O campo 'crm' é obrigatório!")
        String crm,

        @NotBlank(message = "O campo 'especialidade' é obrigatório!")
        String especialidade,

        @NotBlank(message = "O campo 'endereco' é obrigatório!")
        String endereco

) {}