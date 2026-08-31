package br.com.fiap.agendamentoapi.model.request.historicopaciente;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Representa o modelo de requisição para atualizar um Histórico de Paciente.")
public record AtualizarHistoricoPacienteRequest(

        @Size(max = 500, message = "O campo 'queixaPrincipal' deve ter no máximo 500 caracteres!")
        String queixaPrincipal,

        @NotBlank(message = "O campo 'historicoDoenca' é obrigatório!")
        String historicoDoenca,

        @NotBlank(message = "O campo 'medicamentos' é obrigatório!")
        String medicamentos,

        @NotBlank(message = "O campo 'alergias' é obrigatório!")
        String alergias,

        String observacoes

) {}
