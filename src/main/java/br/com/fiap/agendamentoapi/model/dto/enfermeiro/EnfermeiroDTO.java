package br.com.fiap.agendamentoapi.model.dto.enfermeiro;

import java.time.LocalDateTime;

public record EnfermeiroDTO(

        Integer id,

        Integer usuarioId,

        String nome,

        String sobrenome,

        String coren,

        LocalDateTime dataCadastro,

        Integer situacaoCadastroId

) {}