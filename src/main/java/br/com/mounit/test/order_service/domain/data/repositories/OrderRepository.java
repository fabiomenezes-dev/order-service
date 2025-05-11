package br.com.mounit.test.order_service.domain.data.repositories;

import br.com.mounit.test.order_service.domain.data.entities.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    @EntityGraph(attributePaths = "productDTOSet")
    Page<OrderEntity> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "productDTOSet")
    Optional<OrderEntity> findWithProductsById(Long id);

}
