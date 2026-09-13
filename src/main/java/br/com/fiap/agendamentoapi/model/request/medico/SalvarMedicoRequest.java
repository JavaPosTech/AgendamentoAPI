package br.com.fiap.agendamentoapi.model.request.medico;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Representa o modelo de requisição para criar um Médico.")
public record SalvarMedicoRequest(

        @Schema(description = "Login de acesso do médico. Precisa ser único.", example = "roberto.mendes")
        @NotBlank(message = "O campo 'login' é obrigatório!")
        String login,

        @Schema(description = "Senha de acesso. É gravada criptografada.", example = "Senha@123")
        @NotBlank(message = "O campo 'senha' é obrigatório!")
        String senha,

        @Schema(description = "Nome do médico.", example = "Roberto")
        @NotBlank(message = "O campo 'nome' é obrigatório!")
        String nome,

        @Schema(description = "Sobrenome do médico.", example = "Mendes")
        @NotBlank(message = "O campo 'sobrenome' é obrigatório!")
        String sobrenome,

        @Schema(description = "Registro no Conselho Regional de Medicina. Precisa ser único.", example = "CRM-SP-789012")
        @NotBlank(message = "O campo 'crm' é obrigatório!")
        String crm,

        @Schema(description = "Especialidade médica.", example = "DERMATOLOGIA")
        @NotBlank(message = "O campo 'especialidade' é obrigatório!")
        String especialidade,

        @Schema(description = "Endereço de atendimento.", example = "Avenida Brasil, 500 - Campinas - SP")
        @NotBlank(message = "O campo 'endereco' é obrigatório!")
        String endereco

) {}