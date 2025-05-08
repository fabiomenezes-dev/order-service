package br.com.mounit.test.order_service.domain.data.repositories;

import br.com.mounit.test.order_service.domain.data.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}
