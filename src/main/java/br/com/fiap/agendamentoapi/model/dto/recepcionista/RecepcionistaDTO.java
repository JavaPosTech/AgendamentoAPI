package br.com.fiap.agendamentoapi.model.dto.recepcionista;

import br.com.fiap.agendamentoapi.model.entity.recepcionista.Recepcionista;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Representa o modelo de dados de um Recepcionista.")
public record RecepcionistaDTO(

        @Schema(description = "Id do recepcionista.", example = "1")
        Integer id,

        @Schema(description = "Id do usuário vinculado ao recepcionista.", example = "6")
        Integer usuarioId,

        @Schema(description = "Nome do recepcionista.", example = "FERNANDA")
        String nome,

        @Schema(description = "Sobrenome do recepcionista.", example = "LIMA")
        String sobrenome,

        @Schema(type = "string", format = "dd/MM/yyyy - HH:mm:ss", description = "Data e hora do cadastro.", example = "10/08/2026 - 14:32:05")
        @JsonFormat(pattern = "dd/MM/yyyy - HH:mm:ss")
        LocalDateTime dataCadastro,

        @Schema(description = "Situação do cadastro.", allowableValues = {"ATIVO", "EXCLUIDO"}, example = "ATIVO")
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