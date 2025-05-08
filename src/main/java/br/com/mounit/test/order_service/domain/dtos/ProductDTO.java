package br.com.mounit.test.order_service.domain.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDTO {

    private String name;
    private Double valueUnit;
    private int units;
}
