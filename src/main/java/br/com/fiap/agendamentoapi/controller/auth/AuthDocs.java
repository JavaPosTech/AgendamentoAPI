package br.com.fiap.agendamentoapi.controller.auth;

import br.com.fiap.agendamentoapi.exceptions.dto.ErrorResponseDTO;
import br.com.fiap.agendamentoapi.model.request.auth.LoginRequest;
import br.com.fiap.agendamentoapi.model.response.auth.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Autenticação", description = "Endpoints relacionados à autenticação de Usuários")
public interface AuthDocs {

    @SecurityRequirements
    @Operation(
            summary = "Realiza o login",
            description = """
                    Autentica o usuário com login e senha e devolve um token JWT, válido por 2 horas \
                    por padrão. O campo expires_in informa essa validade em segundos, contada a partir \
                    da emissão. O perfil do usuário vai dentro do token e define quais rotas ele acessa.

                    Para usar o token nesta página, clique em **Authorize** e cole apenas o valor do \
                    campo token. Fora do Swagger, envie o cabeçalho Authorization: Bearer seguido do token.

                    Esta é a única rota pública da API.""")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login realizado com sucesso!"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Login ou senha ausentes, ou senha incorreta!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(
                    responseCode = "403",
                    description = "Usuário inativo!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor!",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping("/login")
    ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest loginRequest);
}