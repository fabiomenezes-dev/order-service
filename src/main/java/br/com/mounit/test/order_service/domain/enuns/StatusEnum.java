package br.com.mounit.test.order_service.domain.enuns;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusEnum {

    PENDING(1, "PENDING"),
    PROCESSING(2, "PROCESSING"),
    COMPLETED(3, "COMPLETED");

    private final int id;
    private final String description;
}
