package br.com.fiap.agendamentoapi.model.dto.usuario;

public record UsuarioDTO(

        Integer id,

        String login,

        Integer tipoUsuarioId

) {}