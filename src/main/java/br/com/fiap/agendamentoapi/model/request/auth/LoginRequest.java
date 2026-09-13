package br.com.fiap.agendamentoapi.model.request.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Representa o modelo de requisição para autenticação de um Usuário.")
public record LoginRequest(

        @Schema(description = "Login do usuário.", example = "admin")
        @NotBlank(message = "O campo 'login' é obrigatório!")
        String login,

        @Schema(description = "Senha do usuário, em texto puro.")
        @NotBlank(message = "O campo 'senha' é obrigatório!")
        String senha

) {}