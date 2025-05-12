package br.com.mounit.test.order_service.resource;

import br.com.mounit.test.order_service.domain.dtos.OrderDTO;
import br.com.mounit.test.order_service.domain.enuns.StatusEnum;
import br.com.mounit.test.order_service.domain.exceptions.OrderNotFoundException;
import br.com.mounit.test.order_service.service.OrderInterface;
import io.swagger.annotations.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Slf4j
@Validated
@RestController
@RequestMapping("/v1/order")
@Api(tags = "Order Management")
public class OrderController {

    public static final String LOG_MSG_IP_ORIGEM = "# IP origem: {}";
    private final OrderInterface orderInterface;

    @Autowired
    public OrderController(OrderInterface orderInterface) {
        this.orderInterface = orderInterface;
    }

    @ApiOperation(value = "Retrieve all orders (paginated)", response = Page.class)
    @GetMapping("/all")
    public ResponseEntity<Page<OrderDTO>> getAllPageable(
            @ApiParam(value = "Pagination information") Pageable pageable, HttpServletRequest request) {
        log.info(LOG_MSG_IP_ORIGEM, request.getRemoteAddr());
        return ResponseEntity.ok(orderInterface.getAll(pageable));
    }

    @ApiOperation(value = "Retrieve an order by ID", response = OrderDTO.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Order found"),
            @ApiResponse(code = 404, message = "Order not found")
    })
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(
            @ApiParam(value = "ID of the order", required = true) @PathVariable Long orderId, HttpServletRequest request) throws Exception {
        log.info(LOG_MSG_IP_ORIGEM, request.getRemoteAddr());
        log.info("Buscando pedido pelo ID: {}", orderId);
        return ResponseEntity.ok(orderInterface.getAllById(orderId));
    }


    @ApiOperation(value = "Retrieve orders by status (paginated)", response = Page.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Orders found"),
            @ApiResponse(code = 204, message = "No orders found with the given status"),
            @ApiResponse(code = 400, message = "Invalid status provided")
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<Set<OrderDTO>>> getOrdersByStatus(
            @ApiParam(value = "Status of the orders to retrieve (PENDING, PROCESSING, COMPLETED)", required = true)
            @PathVariable String status,
            @ApiParam(value = "Pagination information") Pageable pageable) {
        try {
            StatusEnum statusEnum = StatusEnum.valueOf(status.toUpperCase());
            Page<Set<OrderDTO>> ordersByStatus = orderInterface.getAllByStatus(statusEnum.name(), pageable);
            if (ordersByStatus.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(ordersByStatus);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status provided. Allowed values are: PENDING, PROCESSING, COMPLETED");
        }
    }

    @ApiOperation(value = "Receive and process an order", response = String.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Order received successfully"),
            @ApiResponse(code = 400, message = "Validation failed")
    })
    @PostMapping("/receive")
    public ResponseEntity<String> receive(
            @ApiParam(value = "Order data to be processed", required = true)
            @Valid @RequestBody OrderDTO orderDTO, HttpServletRequest request) throws OrderNotFoundException {
        log.info(LOG_MSG_IP_ORIGEM, request.getRemoteAddr());
        log.info("Recebendo pedido: {}", orderDTO);
        return ResponseEntity.ok(orderInterface.process(orderDTO));
    }
}
