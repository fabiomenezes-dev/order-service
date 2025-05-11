package br.com.mounit.test.order_service;

import br.com.mounit.test.order_service.domain.dtos.OrderDTO;
import br.com.mounit.test.order_service.domain.enuns.StatusEnum;
import br.com.mounit.test.order_service.resource.OrderController;
import br.com.mounit.test.order_service.service.OrderInterface;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderInterface orderInterface;

    @InjectMocks
    private OrderController orderController;

    private OrderDTO orderDTO;

    @BeforeEach
    void setUp() {
        orderDTO = new OrderDTO();
        orderDTO.setId(1L);
        orderDTO.setStatus(StatusEnum.PENDING.getDescription());
        orderDTO.setTotal(15.6);
    }

    @Test
    void testGetAllPageable() {
        List<OrderDTO> orders = Arrays.asList(orderDTO);
        Page<OrderDTO> page = new PageImpl<>(orders);
        when(orderInterface.getAll(any(Pageable.class))).thenReturn(page);

        ResponseEntity<Page<OrderDTO>> response = orderController.getAllPageable(Pageable.unpaged());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getContent().size());
        verify(orderInterface, times(1)).getAll(any(Pageable.class));
    }

    @Test
    void testGetOrderById_Success() throws Exception {
        when(orderInterface.getAllById(anyLong())).thenReturn(orderDTO);

        ResponseEntity<OrderDTO> response = orderController.getOrderById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orderDTO, response.getBody());
        verify(orderInterface, times(1)).getAllById(1L);
    }

    @Test
    void testGetOrderById_NotFound() throws Exception {
        when(orderInterface.getAllById(anyLong())).thenThrow(new Exception("Order not found"));

        Exception exception = assertThrows(Exception.class, () -> {
            orderController.getOrderById(1L);
        });

        assertEquals("Order not found", exception.getMessage());
        verify(orderInterface, times(1)).getAllById(1L);
    }

    @Test
    void testReceiveOrder_Success() {

        CompletableFuture<String> future = CompletableFuture.completedFuture("Order received successfully");
        when(orderInterface.processAsync(any(OrderDTO.class))).thenReturn(future);


        CompletableFuture<String> result = orderController.receive(orderDTO);

        assertDoesNotThrow(() -> {
            assertEquals("Order received successfully", result.get());
        });
        verify(orderInterface, times(1)).processAsync(any(OrderDTO.class));
    }

    @Test
    void testReceiveOrder_ValidationFailed() {
        // Criando um pedido com dados inválidos
        OrderDTO invalidOrderDTO = new OrderDTO();
        invalidOrderDTO.setProductDTOSet(null); // Por exemplo, uma ID nula (supondo que a ID seja obrigatória)

        // Chamando o método com dados inválidos
        assertThrows(ConstraintViolationException.class, () -> {
            orderController.receive(invalidOrderDTO);
        });

        // Certificando que a validação falhou antes de chegar ao serviço
        verify(orderInterface, times(0)).processAsync(any(OrderDTO.class));
    }
}
