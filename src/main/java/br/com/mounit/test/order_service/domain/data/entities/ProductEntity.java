package br.com.mounit.test.order_service.domain.data.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Lazy;

@Data
@Lazy
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "valueUnit", nullable = false)
    private Double valueUnit;

    @Column(name = "units", nullable = false)
    private int units;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference
    private OrderEntity order;
}
