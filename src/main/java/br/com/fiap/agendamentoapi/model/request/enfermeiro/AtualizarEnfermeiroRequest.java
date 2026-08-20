package br.com.fiap.agendamentoapi.model.request.enfermeiro;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Representa o modelo de requisição para atualizar um enfermeiro.")
public record AtualizarEnfermeiroRequest(

        @NotBlank(message = "O campo 'nome' é obrigatório!")
        String nome,

        @NotBlank(message = "O campo 'sobrenome' é obrigatório!")
        String sobrenome,

        @NotBlank(message = "O campo 'coren' é obrigatório!")
        String coren

) {}