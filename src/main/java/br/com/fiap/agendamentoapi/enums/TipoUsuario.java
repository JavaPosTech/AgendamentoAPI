package br.com.fiap.agendamentoapi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoUsuario {

    ADMINISTRADOR(1, "ADMINISTRADOR"),
    MEDICO(2, "MEDICO"),
    ENFERMEIRO(3, "ENFERMEIRO"),
    RECEPCIONISTA(4, "RECEPCIONISTA"),
    PACIENTE(5, "PACIENTE");

    private final Integer id;
    private final String descricao;

}