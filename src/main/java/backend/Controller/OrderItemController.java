package backend.Controller;

import backend.Dto.OrderDetailItem;
import backend.Dto.OrderRequest;
import backend.Entities.OrderItem;
import backend.Service.OrderItemService;
import backend.Utils.PageResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/orderItem")
@CrossOrigin("*")
public class OrderItemController {

    private final OrderItemService orderItemService;

    @GetMapping
    public Mono<PageResponse<OrderDetailItem>> findPagination(@RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                              @RequestParam Integer pageNumber) {
        return orderItemService.findPagination(pageSize, pageNumber);
    }

    @PostMapping("/create")
    public Mono<ResponseEntity<?>> createOrderItems(@RequestBody OrderRequest request) {
        return orderItemService.createOrder(request.orderItems(), request.orderId())
                .map(ResponseEntity::ok);
    }

}
