package br.com.fiap.agendamentoapi.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(description = "Representa o modelo de requisição para criar um paciente.")
public record CriarPacienteRequest(

        @NotBlank(message = "O campo 'login' é obrigatório!")
        String login,

        @NotBlank(message = "O campo 'senha' é obrigatório!")
        String senha,

        @NotBlank(message = "O campo 'nome' é obrigatório!")
        String nome,

        @NotBlank(message = "O campo 'sobrenome' é obrigatório!")
        String sobrenome,

        @NotBlank(message = "O campo 'cpf' é obrigatório!")
        @Size(min = 11, max = 11, message = "O campo 'cpf' deve ter exatamente 11 caracteres!")
        String cpf,

        @NotBlank(message = "O campo 'endereco' é obrigatório!")
        String endereco,

        @JsonFormat(pattern = "dd/MM/yyyy")
        @NotNull(message = "O campo 'dataNascimento' é obrigatório!")
        LocalDate dataNascimento

) {}