package br.com.mounit.test.order_service;

import br.com.mounit.test.order_service.domain.data.entities.OrderEntity;
import br.com.mounit.test.order_service.domain.data.repositories.OrderRepository;
import br.com.mounit.test.order_service.domain.dtos.OrderDTO;
import br.com.mounit.test.order_service.domain.dtos.ProductDTO;
import br.com.mounit.test.order_service.domain.exceptions.OrderNotFoundException;
import br.com.mounit.test.order_service.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        // Configuração necessária antes de cada teste (caso precise)
    }

    @Test
    void testSumProducts() {
        Set<ProductDTO> products = new HashSet<>();
        products.add(new ProductDTO("Product 1", 10.0, 1));
        products.add(new ProductDTO("Product 2", 20.0, 2));

        Double sum = orderService.sumProducts(products);

        assertEquals(50.0, sum, "A soma dos produtos deve ser 50.0");
    }

    @Test
    void testSaveOrder() {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(1L);
        orderEntity.setTotal(100.0);

        Mockito.when(orderRepository.save(Mockito.any(OrderEntity.class))).thenReturn(orderEntity);

        OrderEntity savedOrder = orderService.saveOrder(orderEntity);

        assertNotNull(savedOrder);
        assertEquals(1L, savedOrder.getId());
        assertEquals(100.0, savedOrder.getTotal());
    }

    @Test
    void testProcessAsync() throws Exception {
        Set<ProductDTO> productDTOSet = new HashSet<>();
        productDTOSet.add(new ProductDTO("Product 1", 10.0, 1));
        productDTOSet.add(new ProductDTO("Product 2", 20.0, 2));

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setProductDTOSet(productDTOSet);

        String response = orderService.process(orderDTO);

        assertEquals("Processamento concluído", response);
        Mockito.verify(orderRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void testGetAllById_OrderFound() throws Exception {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(1L);
        orderEntity.setTotal(100.0);

        Mockito.when(orderRepository.findWithProductsById(1L)).thenReturn(Optional.of(orderEntity));

        OrderDTO orderDTO = orderService.getAllById(1L);

        assertNotNull(orderDTO);
        assertEquals(1L, orderDTO.getId());
        assertEquals(100.0, orderDTO.getTotal());
    }

    @Test
    void testGetAllById_OrderNotFound() {
        Mockito.when(orderRepository.findWithProductsById(1L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> {
            orderService.getAllById(1L);
        });
    }
}
