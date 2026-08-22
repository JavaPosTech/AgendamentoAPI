package br.com.fiap.agendamentoapi.exceptions;

public class SenhaIncorretaException extends RuntimeException {

    public SenhaIncorretaException(String message) {
        super(message);
    }
}