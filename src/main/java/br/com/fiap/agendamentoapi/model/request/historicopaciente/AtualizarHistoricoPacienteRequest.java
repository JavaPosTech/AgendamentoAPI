package br.com.fiap.agendamentoapi.model.request.historicopaciente;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "Representa o modelo de requisição para atualizar um Histórico de Paciente. Os campos são opcionais: os que forem omitidos mantêm o valor atual.")
public record AtualizarHistoricoPacienteRequest(

        @Size(max = 500, message = "O campo 'queixaPrincipal' deve ter no máximo 500 caracteres!")
        String queixaPrincipal,

        String historicoDoenca,

        String medicamentos,

        String alergias,

        String observacoes

) {}
