package br.com.fiap.agendamentoapi.exceptions;

public class HorarioConsultaIndisponivelException extends RuntimeException {

    public HorarioConsultaIndisponivelException(String message) {
        super(message);
    }
}
