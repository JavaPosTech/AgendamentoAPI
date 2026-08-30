package br.com.fiap.agendamentoapi.model.request.enfermeiro;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Representa o modelo de requisição para atualizar um Enfermeiro. Os campos são opcionais: os que forem omitidos mantêm o valor atual.")
public record AtualizarEnfermeiroRequest(

        @Pattern(regexp = ".*\\S.*", message = "O campo 'nome' não pode ser vazio!")
        String nome,

        @Pattern(regexp = ".*\\S.*", message = "O campo 'sobrenome' não pode ser vazio!")
        String sobrenome,

        @Pattern(regexp = ".*\\S.*", message = "O campo 'coren' não pode ser vazio!")
        String coren

) {}
