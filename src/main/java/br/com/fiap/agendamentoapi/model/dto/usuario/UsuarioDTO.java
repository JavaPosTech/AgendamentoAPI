package br.com.fiap.agendamentoapi.model.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(hidden = true)
public record UsuarioDTO(

        String login,

        String senha,

        Integer tipoUsuarioId

) {}