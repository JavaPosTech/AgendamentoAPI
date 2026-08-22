package br.com.fiap.agendamentoapi.exceptions;

public class TipoUsuarioNaoEncontradoException extends RuntimeException {

    public TipoUsuarioNaoEncontradoException(String message) {
        super(message);
    }
}