package br.com.mounit.test.order_service.domain.data.repositories;

import br.com.mounit.test.order_service.domain.data.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
