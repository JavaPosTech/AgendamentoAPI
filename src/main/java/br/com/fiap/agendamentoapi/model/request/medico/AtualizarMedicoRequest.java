package br.com.fiap.agendamentoapi.model.request.medico;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Representa o modelo de requisição para atualizar um Médico. Os campos são opcionais: os que forem omitidos mantêm o valor atual.")
public record AtualizarMedicoRequest(

        @Pattern(regexp = ".*\\S.*", message = "O campo 'nome' não pode ser vazio!")
        String nome,

        @Pattern(regexp = ".*\\S.*", message = "O campo 'sobrenome' não pode ser vazio!")
        String sobrenome,

        @Pattern(regexp = ".*\\S.*", message = "O campo 'crm' não pode ser vazio!")
        String crm,

        @Pattern(regexp = ".*\\S.*", message = "O campo 'especialidade' não pode ser vazio!")
        String especialidade,

        @Pattern(regexp = ".*\\S.*", message = "O campo 'endereco' não pode ser vazio!")
        String endereco

) {}
