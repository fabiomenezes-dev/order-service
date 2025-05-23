package br.com.mounit.test.order_service.resource;

import br.com.mounit.test.order_service.domain.dtos.OrderDTO;
import br.com.mounit.test.order_service.domain.enuns.StatusEnum;
import br.com.mounit.test.order_service.domain.exceptions.DuplicateOrderException;
import br.com.mounit.test.order_service.domain.exceptions.OrderNotFoundException;
import br.com.mounit.test.order_service.service.OrderInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Order Management", description = "Endpoints for managing orders")
public class OrderController {

    public static final String LOG_MSG_IP_ORIGEM = "# IP origem: {}";
    private final OrderInterface orderInterface;

    @Autowired
    public OrderController(OrderInterface orderInterface) {
        this.orderInterface = orderInterface;
    }

    @Operation(summary = "Retrieve all orders (paginated)", description = "Returns a paginated list of all orders")
    @GetMapping("/all")
    public ResponseEntity<Page<OrderDTO>> getAllPageable(
            @Parameter(description = "Pagination information") Pageable pageable,
            HttpServletRequest request) {
        log.info(LOG_MSG_IP_ORIGEM, request.getRemoteAddr());
        return ResponseEntity.ok(orderInterface.getAll(pageable));
    }

    @Operation(summary = "Retrieve an order by ID", description = "Find an order by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order found"),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content)
    })
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(
            @Parameter(description = "ID of the order", required = true) @PathVariable Long orderId,
            HttpServletRequest request) throws Exception {
        log.info(LOG_MSG_IP_ORIGEM, request.getRemoteAddr());
        log.info("Buscando pedido pelo ID: {}", orderId);
        return ResponseEntity.ok(orderInterface.getAllById(orderId));
    }

    @Operation(summary = "Retrieve orders by status (paginated)", description = "Returns orders filtered by status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orders found"),
            @ApiResponse(responseCode = "204", description = "No orders found with the given status", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid status provided", content = @Content)
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<Set<OrderDTO>>> getOrdersByStatus(
            @Parameter(description = "Status of the orders to retrieve (PENDING, PROCESSING, COMPLETED)", required = true)
            @PathVariable String status,
            @Parameter(description = "Pagination information") Pageable pageable) {
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

    @Operation(summary = "Receive and process an order", description = "Accepts and processes a new order")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order received successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content)
    })
    @PostMapping("/receive")
    public ResponseEntity<String> receive(
            @Parameter(description = "Order data to be processed", required = true,
                    content = @Content(schema = @Schema(implementation = OrderDTO.class)))
            @Valid @RequestBody OrderDTO orderDTO,
            HttpServletRequest request) throws OrderNotFoundException, DuplicateOrderException {
        log.info(LOG_MSG_IP_ORIGEM, request.getRemoteAddr());
        log.info("Recebendo pedido: {}", orderDTO);
        return ResponseEntity.ok(orderInterface.process(orderDTO));
    }
}
