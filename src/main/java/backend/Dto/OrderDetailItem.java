package backend.Dto;

import backend.Entities.OrderItem;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDetailItem(
        Long id,
        String orderNo,
        LocalDateTime createdDate,
        List<OrderItem> orderItems
) {
}
