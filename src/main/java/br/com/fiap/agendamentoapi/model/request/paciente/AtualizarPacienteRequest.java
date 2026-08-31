package br.com.fiap.agendamentoapi.model.request.paciente;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(description = "Representa o modelo de requisição para atualizar um Paciente.")
public record AtualizarPacienteRequest(

        @NotBlank(message = "O campo 'nome' é obrigatório!")
        String nome,

        @NotBlank(message = "O campo 'sobrenome' é obrigatório!")
        String sobrenome,

        @NotBlank(message = "O campo 'cpf' é obrigatório!")
        @Size(min = 11, max = 11, message = "O campo 'cpf' deve ter exatamente 11 caracteres!")
        String cpf,

        @NotBlank(message = "O campo 'email' é obrigatório!")
        @Email(message = "O campo 'email' deve conter um e-mail válido!")
        @Size(max = 100, message = "O campo 'email' deve ter no máximo 100 caracteres!")
        String email,

        @NotBlank(message = "O campo 'telefone' é obrigatório!")
        @Size(max = 15, message = "O campo 'telefone' deve ter no máximo 15 caracteres!")
        String telefone,

        @NotBlank(message = "O campo 'endereco' é obrigatório!")
        String endereco,

        @JsonFormat(pattern = "dd/MM/yyyy")
        @NotNull(message = "O campo 'dataNascimento' é obrigatório!")
        LocalDate dataNascimento

) {}
