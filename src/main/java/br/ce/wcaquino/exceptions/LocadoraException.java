package br.ce.wcaquino.exceptions;

public class LocadoraException extends Exception{

    public LocadoraException(String message) {
        super(message);
    }

    public LocadoraException(String message, Throwable cause) {
        super(message, cause);
    }
}
