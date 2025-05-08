package br.com.mounit.test.order_service.service.impl;

import br.com.mounit.test.order_service.domain.data.entities.OrderEntity;
import br.com.mounit.test.order_service.domain.data.repositories.OrderRepository;
import br.com.mounit.test.order_service.domain.dtos.OrderDTO;
import br.com.mounit.test.order_service.domain.dtos.ProductDTO;
import br.com.mounit.test.order_service.domain.enuns.StatusEnum;
import br.com.mounit.test.order_service.domain.exceptions.OrderNotFoundException;
import br.com.mounit.test.order_service.service.OrderInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class OrderServiceImpl implements OrderInterface {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Double sumProducts(Set<ProductDTO> productDTOSet) {
        return productDTOSet.stream()
                .map(ProductDTO::getValueUnit)
                .reduce(0.0, Double::sum);
    }

    @Override
    public OrderEntity saveOrder(OrderEntity orderEntity) {
        return orderRepository.save(orderEntity);
    }

    @Async
    public CompletableFuture<String> processAsync(OrderDTO orderDTO) {
        Double sum = sumProducts(orderDTO.getProductDTOSet()); // simula um processamento demorado
        OrderEntity orderEntity = OrderEntity.builder()
                .total(sum)
                .status(StatusEnum.COMPLETED.getDescription())
                .build();
        saveOrder(orderEntity);
        return CompletableFuture.completedFuture("Processamento concluído");
    }

    @Override
    public Set<OrderDTO> getAllByStatus(String status) {
        return Set.of();
    }

    @Override
    public OrderDTO getAllById(Long id) throws Exception {
        return orderRepository.findById(id)
                .map(this::toOrderDTO)
                .orElseThrow(() -> new OrderNotFoundException("Order-ID:  ".concat(id.toString()).concat(" não encontrado na base de dados.")));
    }

    private OrderDTO toOrderDTO(OrderEntity orderEntity) {
        Set<ProductDTO> produtos = Optional.ofNullable(orderEntity.getProductDTOSet())
                .orElse(Collections.emptySet())
                .stream()
                .map(prod -> ProductDTO.builder()
                        .name(prod.getName())
                        .valueUnit(prod.getValueUnit())
                        .units(prod.getUnits())
                        .build())
                .collect(Collectors.toSet());

        return OrderDTO.builder()
                .id(orderEntity.getId())
                .status(orderEntity.getStatus())
                .productDTOSet(produtos)
                .total(orderEntity.getTotal())
                .build();
    }

}
