package br.com.mounit.test.order_service;

import br.com.mounit.test.order_service.domain.data.entities.OrderEntity;
import br.com.mounit.test.order_service.domain.data.entities.ProductEntity;
import br.com.mounit.test.order_service.domain.data.repositories.OrderRepository;
import br.com.mounit.test.order_service.domain.data.repositories.ProductRepository;
import br.com.mounit.test.order_service.domain.enuns.StatusEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class RepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testSaveAndFindProduct() {
        OrderEntity order = new OrderEntity();
        order.setStatus(StatusEnum.PENDING.getDescription());
        order.setTotal(0.0);
        OrderEntity savedOrder = orderRepository.save(order);

        ProductEntity product = new ProductEntity();
        product.setName("Repository Product");
        product.setUnits(1);
        product.setValueUnit(1.0);
        product.setOrder(savedOrder);

        ProductEntity saved = productRepository.save(product);

        Optional<ProductEntity> found = productRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Repository Product", found.get().getName());
    }

//    @Test
//    public void testSaveAndFindOrder() {
//        // Criando a ordem primeiro
//        OrderEntity order = new OrderEntity();
//        order.setStatus(StatusEnum.COMPLETED.getDescription());
//        order.setTotal(0.0);
//
//        // Criando o produto e associando à ordem
//        ProductEntity product = new ProductEntity();
//        product.setName("Order Product");
//        product.setUnits(1);
//        product.setValueUnit(1.0);
//        product.setOrder(order); // Associando o produto à ordem
//
//        // Salvando a ordem
//        OrderEntity savedOrder = orderRepository.save(order);
//
//        // Buscando e verificando a ordem
//        Optional<OrderEntity> foundOrder = orderRepository.findById(savedOrder.getId());
//        assertTrue(foundOrder.isPresent());
//        assertEquals(StatusEnum.COMPLETED.getDescription(), foundOrder.get().getStatus());
//        assertEquals(1, foundOrder.get().getProductDTOSet().size());
//    }

}
