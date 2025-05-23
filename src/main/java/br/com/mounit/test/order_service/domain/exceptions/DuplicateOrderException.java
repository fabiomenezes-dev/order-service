package br.com.mounit.test.order_service.domain.exceptions;

public class DuplicateOrderException  extends Exception{
    public DuplicateOrderException(String message) {
        super(message);
    }

    public DuplicateOrderException(String message, Throwable cause) {
        super(message, cause);
    }
}
