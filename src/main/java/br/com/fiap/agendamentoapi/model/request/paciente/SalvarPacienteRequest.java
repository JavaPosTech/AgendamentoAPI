package br.com.fiap.agendamentoapi.model.request.paciente;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(description = "Representa o modelo de requisição para criar um Paciente.")
public record SalvarPacienteRequest(

        @Schema(description = "Login de acesso do paciente. Precisa ser único.", example = "maria.souza")
        @NotBlank(message = "O campo 'login' é obrigatório!")
        String login,

        @Schema(description = "Senha de acesso. É gravada criptografada.", example = "Senha@123")
        @NotBlank(message = "O campo 'senha' é obrigatório!")
        String senha,

        @Schema(description = "Nome do paciente.", example = "Maria")
        @NotBlank(message = "O campo 'nome' é obrigatório!")
        String nome,

        @Schema(description = "Sobrenome do paciente.", example = "Souza")
        @NotBlank(message = "O campo 'sobrenome' é obrigatório!")
        String sobrenome,

        @Schema(description = "CPF com 11 dígitos, sem pontos nem traço. Precisa ser único.", example = "45678901234")
        @NotBlank(message = "O campo 'cpf' é obrigatório!")
        @Size(min = 11, max = 11, message = "O campo 'cpf' deve ter exatamente 11 caracteres!")
        String cpf,

        @Schema(description = "E-mail do paciente. Precisa ser único.", example = "maria.souza@email.com")
        @NotBlank(message = "O campo 'email' é obrigatório!")
        @Email(message = "O campo 'email' deve conter um e-mail válido!")
        @Size(max = 100, message = "O campo 'email' deve ter no máximo 100 caracteres!")
        String email,

        @Schema(description = "Telefone de contato.", example = "(19) 99999-1004")
        @NotBlank(message = "O campo 'telefone' é obrigatório!")
        @Size(max = 15, message = "O campo 'telefone' deve ter no máximo 15 caracteres!")
        String telefone,

        @Schema(description = "Endereço residencial.", example = "Rua dos Girassois, 80 - Limeira - SP")
        @NotBlank(message = "O campo 'endereco' é obrigatório!")
        String endereco,

        @Schema(type = "string", format = "dd/MM/yyyy", description = "Data de nascimento no formato dd/MM/yyyy.", example = "12/07/1995")
        @JsonFormat(pattern = "dd/MM/yyyy")
        @NotNull(message = "O campo 'dataNascimento' é obrigatório!")
        LocalDate dataNascimento

) {}