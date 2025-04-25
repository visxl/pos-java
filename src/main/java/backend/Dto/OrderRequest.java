package backend.Dto;

import java.util.List;

public record OrderRequest(
        Long orderId,
        Long orderNo,
        List<OrderItemRequest> orderItems
) {
}
