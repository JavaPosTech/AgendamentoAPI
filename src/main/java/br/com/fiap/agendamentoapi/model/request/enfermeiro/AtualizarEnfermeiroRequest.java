package br.com.fiap.agendamentoapi.model.request.enfermeiro;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representa o modelo de requisição para atualizar um Enfermeiro. Os campos são opcionais: os que forem omitidos mantêm o valor atual.")
public record AtualizarEnfermeiroRequest(

        @Schema(description = "Nome do enfermeiro.", example = "Patricia")
        String nome,

        @Schema(description = "Sobrenome do enfermeiro.", example = "Gomes")
        String sobrenome,

        @Schema(description = "Registro no Conselho Regional de Enfermagem. Precisa ser único.", example = "COREN-SP-789012")
        String coren

) {}