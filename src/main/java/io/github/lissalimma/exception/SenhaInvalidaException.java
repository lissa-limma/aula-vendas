package io.github.lissalimma.exception;

public class SenhaInvalidaException extends RuntimeException{
    public SenhaInvalidaException() {
        super("Senha Inválida!");
    }
}
