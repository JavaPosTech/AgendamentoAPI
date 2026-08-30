package br.com.fiap.agendamentoapi.model.dto.recepcionista;

import br.com.fiap.agendamentoapi.model.entity.recepcionista.Recepcionista;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Representa o modelo de dados de um Recepcionista.")
public record RecepcionistaDTO(

        Integer id,

        Integer usuarioId,

        String nome,

        String sobrenome,

        @JsonFormat(pattern = "dd/MM/yyyy - HH:mm:ss")
        LocalDateTime dataCadastro,

        String situacaoCadastro
) {
    public RecepcionistaDTO(Recepcionista recepcionista) {
        this(recepcionista.getId(),
                recepcionista.getUsuario().getId(),
                recepcionista.getNome(),
                recepcionista.getSobrenome(),
                recepcionista.getDataCadastro(),
                recepcionista.getSituacaoCadastro().getDescricao());
    }
}
