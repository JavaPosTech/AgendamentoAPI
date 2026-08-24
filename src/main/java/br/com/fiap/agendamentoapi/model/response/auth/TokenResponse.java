package br.com.fiap.agendamentoapi.model.response.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Modelo de resposta contendo o token de acesso do Usuário autenticado.")
public record TokenResponse(

        @Schema(description = "Token de acesso JWT")
        String token,

        @Schema(description = "Tipo do token", example = "Bearer")
        String tipo

) {
    public TokenResponse(String pToken) {
        this(pToken, "Bearer");
    }
}
