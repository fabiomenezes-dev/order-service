package br.com.mounit.test.order_service.domain.data.repositories;

import br.com.mounit.test.order_service.domain.data.entities.OrderEntity;
import br.com.mounit.test.order_service.domain.dtos.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    @EntityGraph(attributePaths = "productDTOSet")
    Page<OrderEntity> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "productDTOSet")
    Page<Set<OrderDTO>> findByStatus(String status, Pageable pageable);

    @EntityGraph(attributePaths = "productDTOSet")
    Optional<OrderEntity> findWithProductsById(Long id);

    boolean existsByClientIdAndTotalAndCreatedAtBetween(Long clientId, Double total, LocalDateTime startTime, LocalDateTime endTime);


}
