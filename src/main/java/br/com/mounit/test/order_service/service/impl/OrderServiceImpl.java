package br.com.mounit.test.order_service.service.impl;

import br.com.mounit.test.order_service.domain.data.entities.OrderEntity;
import br.com.mounit.test.order_service.domain.data.repositories.OrderRepository;
import br.com.mounit.test.order_service.domain.dtos.OrderDTO;
import br.com.mounit.test.order_service.domain.dtos.ProductDTO;
import br.com.mounit.test.order_service.domain.enuns.StatusEnum;
import br.com.mounit.test.order_service.domain.exceptions.DuplicateOrderException;
import br.com.mounit.test.order_service.domain.exceptions.OrderNotFoundException;
import br.com.mounit.test.order_service.service.OrderInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import static br.com.mounit.test.order_service.domain.dtos.ProductDTO.toProductEntity;

@Slf4j
@Service
public class OrderServiceImpl implements OrderInterface {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Double sumProducts(Set<ProductDTO> products) {
        return products.stream()
                .mapToDouble(product -> product.getValueUnit() * product.getUnits())
                .sum();
    }

    @Override
    public OrderEntity saveOrder(OrderEntity orderEntity) {
        return orderRepository.save(orderEntity);
    }


    public String process(OrderDTO orderDTO) throws DuplicateOrderException {
        if (this.isDuplicateOrder(orderDTO)) {
            throw new DuplicateOrderException("Order duplicado");
        }

        log.info("Processando pedido: {}", orderDTO);
        Double sum = sumProducts(orderDTO.getProductDTOSet());
        OrderEntity orderEntity = OrderEntity.builder()
                .total(sum)
                .status(StatusEnum.COMPLETED.getDescription())
                .clientId(orderDTO.getClientId())
                .build();

        orderEntity.setProductDTOSet(orderDTO.getProductDTOSet().stream()
                .map(productDTO -> toProductEntity(productDTO, orderEntity))
                .collect(Collectors.toSet()));

        orderRepository.save(orderEntity);

        return "Processamento concluído";
    }

    @Override
    public Page<Set<OrderDTO>> getAllByStatus(String status, Pageable pageable) {
        log.info("Buscando todas as ordens pelo status: {}", status);
        return orderRepository.findByStatus(StatusEnum.valueOf(status).getDescription(), pageable);

    }


    @Override
    @Transactional(readOnly = true)
    public Page<OrderDTO> getAll(Pageable pageable) {
        log.info("Buscando todas as ordens paginadas: {}", pageable);
        return orderRepository.findAll(pageable)
                .map(OrderDTO::toOrderDTO);
    }

    @Override
    @Cacheable("orders")
    public OrderDTO getAllById(Long id) throws Exception {
        return orderRepository.findWithProductsById(id)
                .map(OrderDTO::toOrderDTO)
                .orElseThrow(() -> new OrderNotFoundException("Order-ID:  ".concat(id.toString()).concat(" não encontrado na base de dados.")));
    }

    private boolean isDuplicateOrder(OrderDTO orderDTO) {

        LocalDateTime startTime = LocalDateTime.now().minusMinutes(5);
        LocalDateTime endTime = LocalDateTime.now().plusMinutes(5);

        return orderRepository.existsByClientIdAndTotalAndCreatedAtBetween(
                orderDTO.getClientId(),
                orderDTO.getTotal(),
                startTime,
                endTime
        );
    }

}
