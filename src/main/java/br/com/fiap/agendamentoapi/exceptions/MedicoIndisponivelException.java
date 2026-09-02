package br.com.fiap.agendamentoapi.exceptions;

public class MedicoIndisponivelException extends RuntimeException {

    public MedicoIndisponivelException(String message) {
        super(message);
    }
}
