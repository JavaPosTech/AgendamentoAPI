package br.com.fiap.agendamentoapi.model.dto.medico;

import br.com.fiap.agendamentoapi.model.entity.medico.Medico;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Representa o modelo de dados de um Médico.")
public record MedicoDTO(

        @Schema(description = "Id do médico.", example = "1")
        Integer id,

        @Schema(description = "Id do usuário vinculado ao médico.", example = "2")
        Integer usuarioId,

        @Schema(description = "Nome do médico.", example = "JOAO")
        String nome,

        @Schema(description = "Sobrenome do médico.", example = "SILVA")
        String sobrenome,

        @Schema(description = "Registro no Conselho Regional de Medicina.", example = "CRM-SP-123456")
        String crm,

        @Schema(description = "Especialidade médica.", example = "CARDIOLOGIA")
        String especialidade,

        @Schema(description = "Endereço de atendimento.", example = "Rua das Flores, 100 - Sao Paulo - SP")
        String endereco,

        @Schema(type = "string", format = "dd/MM/yyyy - HH:mm:ss", description = "Data e hora do cadastro.", example = "10/08/2026 - 14:32:05")
        @JsonFormat(pattern = "dd/MM/yyyy - HH:mm:ss")
        LocalDateTime dataCadastro,

        @Schema(description = "Situação do cadastro.", allowableValues = {"ATIVO", "EXCLUIDO"}, example = "ATIVO")
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