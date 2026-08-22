package br.com.fiap.agendamentoapi.model.dto.medico;

import br.com.fiap.agendamentoapi.model.entity.medico.Medico;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Representa o modelo de dados de um Médico.")
public record MedicoDTO(

        Integer id,

        Integer usuarioId,

        String nome,

        String sobrenome,

        String crm,

        String especialidade,

        String endereco,

        @JsonFormat(pattern = "dd/MM/yyyy - HH:mm:ss")
        LocalDateTime dataCadastro,

        String situacaoCadastro

) {
    public MedicoDTO(Medico medico) {
        this(medico.getId(),
                medico.getUsuario().getId(),
                medico.getNome(),
                medico.getSobrenome(),
                medico.getCrm(),
                medico.getEspecialidade(),
                medico.getEndereco(),
                medico.getDataCadastro(),
                medico.getSituacaoCadastro().getDescricao());
    }
}