package br.com.mounit.test.order_service.domain.data.entities;

import br.com.mounit.test.order_service.domain.dtos.ProductDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.context.annotation.Lazy;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Lazy
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order", schema = "order_service")
//@Where(clause = " deleted is null ")
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = false)
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "total", nullable = false)
    private Double total;

    @Column(name = "status", nullable = false)
    private String status;


    private Set<ProductDTO> productDTOSet;

    @Column(name = "updated")
    @UpdateTimestamp
    private LocalDateTime updated;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;
}
