package br.com.fiap.agendamentoapi.exceptions;

public class UsuarioInativoException extends RuntimeException {

    public UsuarioInativoException(String message) {
        super(message);
    }
}
