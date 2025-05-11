package br.com.mounit.test.order_service.resource;

import br.com.mounit.test.order_service.domain.dtos.OrderDTO;
import br.com.mounit.test.order_service.service.OrderInterface;
import io.swagger.annotations.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/v1/order")
@Api(tags = "Order Management")
@Validated
public class OrderController {

    private final OrderInterface orderInterface;

    @Autowired
    public OrderController(OrderInterface orderInterface) {
        this.orderInterface = orderInterface;
    }

    @ApiOperation(value = "Retrieve all orders (paginated)", response = Page.class)
    @GetMapping("/all")
    public ResponseEntity<Page<OrderDTO>> getAllPageable(
            @ApiParam(value = "Pagination information") Pageable pageable) {
        return ResponseEntity.ok(orderInterface.getAll(pageable));
    }

    @ApiOperation(value = "Retrieve an order by ID", response = OrderDTO.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Order found"),
            @ApiResponse(code = 404, message = "Order not found")
    })
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(
            @ApiParam(value = "ID of the order", required = true) @PathVariable Long orderId) throws Exception {
        return ResponseEntity.ok(orderInterface.getAllById(orderId));
    }

    @ApiOperation(value = "Receive and process an order", response = String.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Order received successfully"),
            @ApiResponse(code = 400, message = "Validation failed")
    })
    @PostMapping("/receive")
    public CompletableFuture<String> receive(
            @ApiParam(value = "Order data to be processed", required = true)
            @Valid @RequestBody OrderDTO orderDTO) {
        return orderInterface.processAsync(orderDTO);
    }
}
