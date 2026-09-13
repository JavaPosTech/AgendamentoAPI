package br.com.fiap.agendamentoapi.model.request.enfermeiro;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Representa o modelo de requisição para criar um Enfermeiro.")
public record SalvarEnfermeiroRequest(

        @Schema(description = "Login de acesso do enfermeiro. Precisa ser único.", example = "patricia.gomes")
        @NotBlank(message = "O campo 'login' é obrigatório!")
        String login,

        @Schema(description = "Senha de acesso. É gravada criptografada.", example = "Senha@123")
        @NotBlank(message = "O campo 'senha' é obrigatório!")
        String senha,

        @Schema(description = "Nome do enfermeiro.", example = "Patricia")
        @NotBlank(message = "O campo 'nome' é obrigatório!")
        String nome,

        @Schema(description = "Sobrenome do enfermeiro.", example = "Gomes")
        @NotBlank(message = "O campo 'sobrenome' é obrigatório!")
        String sobrenome,

        @Schema(description = "Registro no Conselho Regional de Enfermagem. Precisa ser único.", example = "COREN-SP-789012")
        @NotBlank(message = "O campo 'coren' é obrigatório!")
        String coren

) {}