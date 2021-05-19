package br.ce.wcaquino.exceptions;

public class NaoPodeDividirPorZeroException extends Exception{

    public NaoPodeDividirPorZeroException(String message, Throwable cause) {
        super(message, cause);
    }

    public NaoPodeDividirPorZeroException(String message) {
        super(message);
    }
}
