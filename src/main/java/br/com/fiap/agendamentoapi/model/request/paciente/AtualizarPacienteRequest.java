package br.com.fiap.agendamentoapi.model.request.paciente;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(description = "Representa o modelo de requisição para atualizar um Paciente. Os campos são opcionais: os que forem omitidos mantêm o valor atual.")
public record AtualizarPacienteRequest(

        @Schema(description = "Nome do paciente.", example = "Maria")
        String nome,

        @Schema(description = "Sobrenome do paciente.", example = "Souza")
        String sobrenome,

        @Schema(description = "CPF com 11 dígitos, sem pontos nem traço. Precisa ser único.", example = "45678901234")
        @Size(min = 11, max = 11, message = "O campo 'cpf' deve ter exatamente 11 caracteres!")
        String cpf,

        @Schema(description = "E-mail do paciente. Precisa ser único.", example = "maria.souza@email.com")
        @Email(message = "O campo 'email' deve conter um e-mail válido!")
        @Size(max = 100, message = "O campo 'email' deve ter no máximo 100 caracteres!")
        String email,

        @Schema(description = "Telefone de contato.", example = "(19) 99999-1004")
        @Size(max = 15, message = "O campo 'telefone' deve ter no máximo 15 caracteres!")
        String telefone,

        @Schema(description = "Endereço residencial.", example = "Rua dos Girassois, 80 - Limeira - SP")
        String endereco,

        @Schema(type = "string", format = "dd/MM/yyyy", description = "Data de nascimento no formato dd/MM/yyyy.", example = "12/07/1995")
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataNascimento

) {}