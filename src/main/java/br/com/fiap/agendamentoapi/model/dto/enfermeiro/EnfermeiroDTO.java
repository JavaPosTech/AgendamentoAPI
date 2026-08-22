package br.com.fiap.agendamentoapi.model.dto.enfermeiro;

import br.com.fiap.agendamentoapi.model.entity.enfermeiro.Enfermeiro;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Representa o modelo de dados de um Enfermeiro.")
public record EnfermeiroDTO(

        Integer id,

        Integer usuarioId,

        String nome,

        String sobrenome,

        String coren,

        @JsonFormat(pattern = "dd/MM/yyyy - HH:mm:ss")
        LocalDateTime dataCadastro,

        String situacaoCadastro
) {
    public EnfermeiroDTO(Enfermeiro enfermeiro) {
        this(enfermeiro.getId(),
             enfermeiro.getUsuario().getId(),
             enfermeiro.getNome(),
             enfermeiro.getSobrenome(),
             enfermeiro.getCoren(),
             enfermeiro.getDataCadastro(),
             enfermeiro.getSituacaoCadastro().getDescricao());
    }
}