package br.com.fiap.agendamentoapi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SituacaoCadastro {

    ATIVO(1, "ATIVO"),
    EXCLUIDO(2, "EXCLUIDO");

    private final Integer id;
    private final String descricao;

}