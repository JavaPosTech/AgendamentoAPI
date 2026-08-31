package br.com.fiap.agendamentoapi.exceptions;

public class HistoricoPacienteNaoEncontradoException extends RuntimeException {

    public HistoricoPacienteNaoEncontradoException(String message) {
        super(message);
    }
}
