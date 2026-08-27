package br.com.fiap.agendamentoapi.model.request.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Representa o modelo de requisição para autenticação de um Usuário.")
public record LoginRequest(

        @NotBlank(message = "O campo 'login' é obrigatório!")
        String login,

        @NotBlank(message = "O campo 'senha' é obrigatório!")
        String senha

) {}
