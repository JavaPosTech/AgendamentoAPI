package br.com.fiap.agendamentoapi.model.request.recepcionista;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Representa o modelo de requisição para criar um Recepcionista.")
public record SalvarRecepcionistaRequest(

        @NotBlank(message = "O campo 'login' é obrigatório!")
        String login,

        @NotBlank(message = "O campo 'senha' é obrigatório!")
        String senha,

        @NotBlank(message = "O campo 'nome' é obrigatório!")
        String nome,

        @NotBlank(message = "O campo 'sobrenome' é obrigatório!")
        String sobrenome

) {}
