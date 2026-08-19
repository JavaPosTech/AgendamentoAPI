package br.com.fiap.agendamentoapi.model.dto.medico;

import java.time.LocalDateTime;

public record MedicoDTO(

        Integer id,

        Integer usuarioId,

        String nome,

        String sobrenome,

        String crm,

        String especialidade,

        String endereco,

        LocalDateTime dataCadastro,

        Integer situacaoCadastroId

) {}