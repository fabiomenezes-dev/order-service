package br.com.mounit.test.order_service.domain.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.context.annotation.Lazy;

@Data
@Lazy
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product", schema = "order_service")
//@Where(clause = " deleted is null ")
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = false)
public class ProductEntity {

    private long id;
    private String name;
    private Double valueUnit;
    private int units;
    private OrderEntity orderEntity;
}
