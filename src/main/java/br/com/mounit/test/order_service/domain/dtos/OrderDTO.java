package br.com.mounit.test.order_service.domain.dtos;

import br.com.mounit.test.order_service.domain.data.entities.OrderEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull(message = "O campo clientId não pode estar vazio")
    private Long clientId;

    @NotEmpty(message = "A lista de produtos não pode estar vazia")
    @JsonProperty(value = "products")
    private Set<ProductDTO> productDTOSet;

    @JsonProperty(value = "total")
    private Double total;

    @JsonProperty(value = "status")
    private String status;

    public static OrderDTO toOrderDTO(OrderEntity orderEntity) {
        Set<ProductDTO> produtos = Optional.ofNullable(orderEntity.getProductDTOSet())
                .orElse(Collections.emptySet())
                .stream()
                .map(prod -> ProductDTO.builder()
                        .name(prod.getName())
                        .valueUnit(prod.getValueUnit())
                        .units(prod.getUnits())
                        .build())
                .collect(Collectors.toSet());

        return OrderDTO.builder()
                .id(orderEntity.getId())
                .status(orderEntity.getStatus())
                .clientId(orderEntity.getClientId())
                .productDTOSet(produtos)
                .total(orderEntity.getTotal())
                .build();
    }

}
