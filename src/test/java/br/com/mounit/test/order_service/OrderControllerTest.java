package br.com.mounit.test.order_service;

import br.com.mounit.test.order_service.domain.data.repositories.OrderRepository;
import br.com.mounit.test.order_service.domain.dtos.OrderDTO;
import br.com.mounit.test.order_service.domain.enuns.StatusEnum;
import br.com.mounit.test.order_service.domain.exceptions.DuplicateOrderException;
import br.com.mounit.test.order_service.domain.exceptions.OrderNotFoundException;
import br.com.mounit.test.order_service.resource.OrderController;
import br.com.mounit.test.order_service.service.OrderInterface;
import br.com.mounit.test.order_service.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderInterface orderInterface;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @InjectMocks
    private OrderController orderController;

    private OrderDTO orderDTO;
    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        orderDTO = new OrderDTO();
        orderDTO.setId(1L);
        orderDTO.setStatus(StatusEnum.PENDING.getDescription());
        orderDTO.setTotal(15.6);
        request = new MockHttpServletRequest();
        request.setRemoteAddr("127.0.0.1");
    }

    @Test
    void testGetAllPageable() {
        List<OrderDTO> orders = Arrays.asList(orderDTO);
        Page<OrderDTO> page = new PageImpl<>(orders);
        when(orderInterface.getAll(any(Pageable.class))).thenReturn(page);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("127.0.0.1");

        ResponseEntity<Page<OrderDTO>> response = orderController.getAllPageable(Pageable.unpaged(), request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getContent().size());
        verify(orderInterface, times(1)).getAll(any(Pageable.class));
    }

    @Test
    void testGetOrderById_Success() throws Exception {
        when(orderInterface.getAllById(anyLong())).thenReturn(orderDTO);

        ResponseEntity<OrderDTO> response = orderController.getOrderById(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orderDTO, response.getBody());
        verify(orderInterface, times(1)).getAllById(1L);
    }

    @Test
    void testGetOrderById_NotFound() throws Exception {
        when(orderInterface.getAllById(anyLong())).thenThrow(new Exception("Order not found"));

        Exception exception = assertThrows(Exception.class, () -> {
            orderController.getOrderById(1L, request);
        });

        assertEquals("Order not found", exception.getMessage());
        verify(orderInterface, times(1)).getAllById(1L);
    }

    @Test
    void testReceiveOrder_Success() throws OrderNotFoundException {
        when(orderInterface.process(any())).thenReturn("Processamento concluído");
        ResponseEntity<String> result = orderController.receive(orderDTO, request);

        assertDoesNotThrow(() -> {
            assertEquals("Processamento concluído", result.getBody());
        });
        verify(orderInterface, times(1)).process(any(OrderDTO.class));
    }

    @Test
    void getOrdersByStatus_validStatus_shouldReturnOkWithOrders() throws Exception {

        String status = StatusEnum.PENDING.name();
        Pageable pageable = PageRequest.of(0, 10);
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(1L);
        orderDTO.setStatus(status);
        Set<OrderDTO> orderDTOSet = new HashSet<>(Collections.singletonList(orderDTO));
        Page<Set<OrderDTO>> expectedPage = new PageImpl<>(Collections.singletonList(orderDTOSet), pageable, 1);

        when(orderInterface.getAllByStatus(status, pageable)).thenReturn(expectedPage);

        ResponseEntity<Page<Set<OrderDTO>>> responseEntity = orderController.getOrdersByStatus(status, pageable);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedPage, responseEntity.getBody());
    }

    @Test
    void processOrder_duplicateOrderExists_shouldThrowDuplicateOrderException() throws Exception {

        OrderDTO duplicateOrderDTO = new OrderDTO();
        duplicateOrderDTO.setClientId(123L);
        duplicateOrderDTO.setTotal(99.99);

        // Simula que já existe uma ordem duplicada no banco de dados
        when(orderRepository.existsByClientIdAndTotalAndCreatedAtBetween(
                any(Long.class), // Use any() matcher for clientId
                any(Double.class), // Use any() matcher for total
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(true);

        DuplicateOrderException exception = assertThrows(DuplicateOrderException.class, () -> {
            orderService.process(duplicateOrderDTO);
        });

        assertEquals("Order duplicado", exception.getMessage());
    }
}
