package br.com.mounit.test.order_service.service;

import br.com.mounit.test.order_service.domain.data.entities.OrderEntity;
import br.com.mounit.test.order_service.domain.dtos.OrderDTO;
import br.com.mounit.test.order_service.domain.dtos.ProductDTO;
import br.com.mounit.test.order_service.domain.exceptions.OrderNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface OrderInterface {

    Double sumProducts(Set<ProductDTO> productDTOSet);

    OrderEntity saveOrder(OrderEntity orderEntity);

    Page<OrderDTO> getAll(Pageable pageable);

    String process(OrderDTO orderDTO) throws OrderNotFoundException;

    Page<Set<OrderDTO>> getAllByStatus(String status, Pageable pageable);

    OrderDTO getAllById(Long id) throws Exception;

}
