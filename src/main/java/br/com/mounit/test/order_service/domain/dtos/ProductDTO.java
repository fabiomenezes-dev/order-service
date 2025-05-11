package br.com.mounit.test.order_service.domain.dtos;

import br.com.mounit.test.order_service.domain.data.entities.OrderEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDTO {

    @NotBlank(message = "O nome do produto é obrigatório")
    private String name;

    @NotNull(message = "O valor unitário é obrigatório")
    @DecimalMin(value = "0.01", inclusive = true, message = "O valor unitário deve ser maior que zero")
    private Double valueUnit;

    @Min(value = 1, message = "A quantidade mínima de unidades é 1")
    private int units;

    private OrderEntity order;
}
