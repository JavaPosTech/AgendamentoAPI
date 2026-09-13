package br.com.fiap.agendamentoapi.model.request.medico;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representa o modelo de requisição para atualizar um Médico. Os campos são opcionais: os que forem omitidos mantêm o valor atual.")
public record AtualizarMedicoRequest(

        @Schema(description = "Nome do médico.", example = "Roberto")
        String nome,

        @Schema(description = "Sobrenome do médico.", example = "Mendes")
        String sobrenome,

        @Schema(description = "Registro no Conselho Regional de Medicina. Precisa ser único.", example = "CRM-SP-789012")
        String crm,

        @Schema(description = "Especialidade médica.", example = "DERMATOLOGIA")
        String especialidade,

        @Schema(description = "Endereço de atendimento.", example = "Avenida Brasil, 500 - Campinas - SP")
        String endereco

) {}