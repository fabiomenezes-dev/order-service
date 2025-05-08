package br.com.mounit.test.order_service.domain.exceptions;

public class OrderNotFoundException extends  Exception {

    public OrderNotFoundException(String message) {
        super(message);
    }

    public OrderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
