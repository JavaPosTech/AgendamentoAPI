package br.com.fiap.agendamentoapi.model.request.recepcionista;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Representa o modelo de requisição para criar um Recepcionista.")
public record SalvarRecepcionistaRequest(

        @Schema(description = "Login de acesso do recepcionista. Precisa ser único.", example = "beatriz.souza")
        @NotBlank(message = "O campo 'login' é obrigatório!")
        String login,

        @Schema(description = "Senha de acesso. É gravada criptografada.", example = "Senha@123")
        @NotBlank(message = "O campo 'senha' é obrigatório!")
        String senha,

        @Schema(description = "Nome do recepcionista.", example = "Beatriz")
        @NotBlank(message = "O campo 'nome' é obrigatório!")
        String nome,

        @Schema(description = "Sobrenome do recepcionista.", example = "Souza")
        @NotBlank(message = "O campo 'sobrenome' é obrigatório!")
        String sobrenome

) {}