package org.example.exception;

public class NotEnoughMoneyException extends RuntimeException {
    public NotEnoughMoneyException(String s) {
        super(s);
    }
}
