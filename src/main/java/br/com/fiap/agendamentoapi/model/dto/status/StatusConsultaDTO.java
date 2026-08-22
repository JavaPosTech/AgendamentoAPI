package br.com.fiap.agendamentoapi.model.dto.status;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representa o modelo de dados do status de uma consulta.")
public record StatusConsultaDTO(

        Integer id,

        String descricao

) {}