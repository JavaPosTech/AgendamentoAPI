package br.com.fiap.agendamentoapi.model.dto.enfermeiro;

import br.com.fiap.agendamentoapi.model.entity.enfermeiro.Enfermeiro;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Representa o modelo de dados de um Enfermeiro.")
public record EnfermeiroDTO(

        @Schema(description = "Id do enfermeiro.", example = "1")
        Integer id,

        @Schema(description = "Id do usuário vinculado ao enfermeiro.", example = "4")
        Integer usuarioId,

        @Schema(description = "Nome do enfermeiro.", example = "CARLOS")
        String nome,

        @Schema(description = "Sobrenome do enfermeiro.", example = "SANTOS")
        String sobrenome,

        @Schema(description = "Registro no Conselho Regional de Enfermagem.", example = "COREN-SP-123456")
        String coren,

        @Schema(type = "string", format = "dd/MM/yyyy - HH:mm:ss", description = "Data e hora do cadastro.", example = "10/08/2026 - 14:32:05")
        @JsonFormat(pattern = "dd/MM/yyyy - HH:mm:ss")
        LocalDateTime dataCadastro,

        @Schema(description = "Situação do cadastro.", allowableValues = {"ATIVO", "EXCLUIDO"}, example = "ATIVO")
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