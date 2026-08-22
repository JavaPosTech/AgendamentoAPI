package br.com.fiap.agendamentoapi.exceptions;

public class ConsultaNaoEncontradaException extends RuntimeException {

    public ConsultaNaoEncontradaException(String message) {
        super(message);
    }
}