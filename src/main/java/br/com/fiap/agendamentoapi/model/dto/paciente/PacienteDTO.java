package br.com.fiap.agendamentoapi.model.dto.paciente;

import br.com.fiap.agendamentoapi.model.entity.paciente.Paciente;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Representa o modelo de dados de um Paciente.")
public record PacienteDTO(

        @Schema(description = "Id do paciente.", example = "1")
        Integer id,

        @Schema(description = "Id do usuário vinculado ao paciente.", example = "7")
        Integer usuarioId,

        @Schema(description = "Nome do paciente.", example = "PEDRO")
        String nome,

        @Schema(description = "Sobrenome do paciente.", example = "ALMEIDA")
        String sobrenome,

        @Schema(description = "CPF com 11 dígitos, sem pontos nem traço.", example = "12345678901")
        String cpf,

        @Schema(description = "E-mail do paciente.", example = "pedro.almeida@email.com")
        String email,

        @Schema(description = "Telefone de contato.", example = "(19) 99999-1001")
        String telefone,

        @Schema(description = "Endereço residencial.", example = "Rua das Palmeiras, 50 - Limeira - SP")
        String endereco,

        @Schema(type = "string", format = "dd/MM/yyyy", description = "Data de nascimento.", example = "15/05/1990")
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataNascimento,

        @Schema(type = "string", format = "dd/MM/yyyy - HH:mm:ss", description = "Data e hora do cadastro.", example = "10/08/2026 - 14:32:05")
        @JsonFormat(pattern = "dd/MM/yyyy - HH:mm:ss")
        LocalDateTime dataCadastro,

        @Schema(description = "Situação do cadastro.", allowableValues = {"ATIVO", "EXCLUIDO"}, example = "ATIVO")
        String situacaoCadastro

) {
    public PacienteDTO(Paciente paciente) {
        this(paciente.getId(),
                paciente.getUsuario().getId(),
                paciente.getNome(),
                paciente.getSobrenome(),
                paciente.getCpf(),
                paciente.getEmail(),
                paciente.getTelefone(),
                paciente.getEndereco(),
                paciente.getDataNascimento(),
                paciente.getDataCadastro(),
                paciente.getSituacaoCadastro().getDescricao()
        );
    }
}