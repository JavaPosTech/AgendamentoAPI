package br.com.fiap.agendamentoapi.model.response.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder({"type", "expires_in", "token"})
@Schema(description = "Modelo de resposta contendo o token de acesso do Usuário autenticado.")
public record TokenResponse(

        @JsonProperty("type")
        @Schema(description = "Tipo do token.", example = "Bearer")
        String tipo,

        @JsonProperty("expires_in")
        @Schema(description = "Tempo de validade do token, em segundos, contado a partir da emissão.", example = "7200")
        long expiresIn,

        @Schema(description = "Token de acesso JWT. É o valor a informar em Authorize.", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiJ9.assinatura")
        String token

) {
    public TokenResponse(String pToken, long pExpiresIn) {
        this("Bearer", pExpiresIn, pToken);
    }
}