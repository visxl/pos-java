package backend.Service;

import backend.Dto.OrderDetailItem;
import backend.Dto.OrderItemRequest;
import backend.Dto.OrderDetailDto;
import backend.Entities.OrderDetail;
import backend.Entities.OrderItem;
import backend.Utils.PageResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface OrderItemService {

    Mono<PageResponse<OrderDetailItem>> findPagination(Integer pageSize, Integer pageNumber);
    Mono<List<OrderItem>> createOrder(List<OrderItemRequest> orderRequests, Long orderId);
}
