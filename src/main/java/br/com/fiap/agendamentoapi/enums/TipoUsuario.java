package br.com.fiap.agendamentoapi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoUsuario {

    ADMINISTRADOR(1, "ADMINISTRADOR"),
    MEDICO(2, "MEDICO"),
    ENFERMEIRO(3, "ENFERMEIRO"),
    PACIENTE(4, "PACIENTE");

    private final Integer id;
    private final String descricao;

}