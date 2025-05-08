package br.com.mounit.test.order_service.resource;

import br.com.mounit.test.order_service.domain.dtos.OrderDTO;
import br.com.mounit.test.order_service.service.OrderInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

//@Api(tags = "Finalization")
@RestController
@RequestMapping("/v1/order")
public class Resource {

    private final OrderInterface orderInterface;

    public Resource(OrderInterface orderInterface) {
        this.orderInterface = orderInterface;
    }

    @GetMapping("/status")
//    @ApiOperation(value = "List tasks by Queue identify")
//    @ApiResponses(value = {
//            @ApiResponse(code = 404, message = "Tasks not found"),
//            @ApiResponse(code = 500, message = "Error to search protocol")
//    })
    // @PreAuthorize("(" + READ_SCOPE + ")")
    public ResponseEntity<Page<OrderDTO>> getAllPageable(
            @RequestParam(name = "status", required = false) String filterFieldName,
            @RequestParam(name = "fieldValue", required = false) String filterFieldValue,

            Pageable pageable) {
//        return ResponseEntity.ok(
//                this.taskService.getAllPageableByStages(queueConfigurationId,
//                        dateCreationBegin,
//                        dateCreationEnd,
//                        dateLimitedBegin,
//                        dateLimitedEnd,
//                        dateCreationProtocolBegin,
//                        dateCreationProtocolEnd,
//                        protocolId,
//                        filterFieldName,
//                        filterFieldValue,
//                        pageable)
//        );
    }

    //    @ApiIgnore
    @GetMapping("/queue/{queueConfigurationId}/stage-totals")
//    @ApiOperation(value = "List totaly of tasks by stages identifiers (Just for use intern)")
//    @ApiResponses(value = {
//            @ApiResponse(code = 404, message = "Tasks not found"),
//            @ApiResponse(code = 500, message = "Error to search protocol")
//    })
    // @PreAuthorize("(" + CLIENT_SCOPE + ")")
    public ResponseEntity<List<TaskProjection>> getTaskStageTotals(@PathVariable Long queueConfigurationId) {
        return ResponseEntity.ok(this.taskService.getTaskStageTotals(queueConfigurationId));
    }

    //    @ApiIgnore
    @GetMapping("/{orderId}")
//    @ApiOperation(value = "List totaly of tasks by stages identifiers (Just for use intern)")
//    @ApiResponses(value = {
//            @ApiResponse(code = 404, message = "Tasks not found"),
//            @ApiResponse(code = 500, message = "Error to search protocol")
//    })
    // @PreAuthorize("(" + CLIENT_SCOPE + ")")
    public ResponseEntity<String> getTaskExistenceByStage(@PathVariable Long stageId) {
        return ResponseEntity.ok(this.taskService.getTaskByStage(stageId));
    }

    //    @ApiIgnore
    @GetMapping("/status")
//    @ApiOperation(value = "Find one task by identify")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "404", message = "Tasks not found"),
//            @ApiResponse(responseCode = "500", message = "Error to search protocol")
//    })
    // @PreAuthorize("(" + READ_SCOPE + ")")
    public ResponseEntity<?> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(orderInterface.getAllById(id));
    }


    //    @ApiIgnore
    @PostMapping("/receive")
//    @ApiOperation(value = "List totaly of tasks by queue identifiers (Just for use intern)")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "404", message = "Tasks not found"),
//            @ApiResponse(responseCode = "500", message = "Error to search protocol")
//    })
    // @PreAuthorize("(" + CLIENT_SCOPE + ")")
    public CompletableFuture<String> receive(@RequestBody OrderDTO orderDTO) {
        return orderInterface.processAsync(orderDTO);
    }
}
