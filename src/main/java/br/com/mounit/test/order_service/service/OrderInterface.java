package br.com.mounit.test.order_service.service;

import br.com.mounit.test.order_service.domain.data.entities.OrderEntity;
import br.com.mounit.test.order_service.domain.dtos.OrderDTO;
import br.com.mounit.test.order_service.domain.dtos.ProductDTO;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface OrderInterface {

    Double sumProducts(Set<ProductDTO> productDTOSet);


    OrderEntity saveOrder(OrderEntity orderEntity);

    CompletableFuture<String> processAsync(OrderDTO orderDTO);


    Set<OrderDTO> getAllByStatus(String status);

    OrderDTO getAllById(Long id) throws Exception;

}
