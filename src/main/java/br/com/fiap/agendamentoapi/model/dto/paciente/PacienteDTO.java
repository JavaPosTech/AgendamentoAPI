package br.com.fiap.agendamentoapi.model.dto.paciente;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PacienteDTO(

        Integer id,

        Integer usuarioId,

        String nome,

        String sobrenome,

        String cpf,

        String endereco,

        LocalDate dataNascimento,

        LocalDateTime dataCadastro,

        Integer situacaoCadastroId

) {}