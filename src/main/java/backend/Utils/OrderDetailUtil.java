package backend.Utils;

import backend.Dto.OrderDetailItem;
import backend.Entities.OrderDetail;
import backend.Entities.OrderItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailUtil {

    public static OrderDetailItem toRecord(OrderDetail orderDetail, List<OrderItem> orderItems) {
        return new OrderDetailItem(
                orderDetail.getId(),
                orderDetail.getOrderNo(),
                orderDetail.getCreatedDate(),
                orderItems
        );
    }
}
