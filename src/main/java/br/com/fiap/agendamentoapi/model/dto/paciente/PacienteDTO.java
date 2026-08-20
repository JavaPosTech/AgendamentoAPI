package br.com.fiap.agendamentoapi.model.dto.paciente;

import br.com.fiap.agendamentoapi.model.entity.paciente.Paciente;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Modelo de um Paciente.")
public record PacienteDTO(

        Integer id,

        Integer usuarioId,

        String nome,

        String sobrenome,

        String cpf,

        String endereco,

        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataNascimento,

        @JsonFormat(pattern = "dd/MM/yyyy - HH:mm:ss")
        LocalDateTime dataCadastro,

        String situacaoCadastro
) {
    public PacienteDTO(Paciente paciente) {
        this(paciente.getId(),
                paciente.getUsuario().getId(),
                paciente.getNome(),
                paciente.getSobrenome(),
                paciente.getCpf(),
                paciente.getEndereco(),
                paciente.getDataNascimento(),
                paciente.getDataCadastro(),
                paciente.getSituacaoCadastro().getDescricao()
        );
    }
}