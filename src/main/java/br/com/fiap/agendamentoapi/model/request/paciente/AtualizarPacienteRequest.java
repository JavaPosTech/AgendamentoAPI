package br.com.fiap.agendamentoapi.model.request.paciente;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(description = "Representa o modelo de requisição para atualizar um Paciente. Os campos são opcionais: os que forem omitidos mantêm o valor atual.")
public record AtualizarPacienteRequest(

        String nome,

        String sobrenome,

        @Size(min = 11, max = 11, message = "O campo 'cpf' deve ter exatamente 11 caracteres!")
        String cpf,

        @Email(message = "O campo 'email' deve conter um e-mail válido!")
        @Size(max = 100, message = "O campo 'email' deve ter no máximo 100 caracteres!")
        String email,

        @Size(max = 15, message = "O campo 'telefone' deve ter no máximo 15 caracteres!")
        String telefone,

        String endereco,

        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataNascimento

) {}
