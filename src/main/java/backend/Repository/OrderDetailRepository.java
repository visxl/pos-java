package backend.Repository;

import backend.Entities.OrderDetail;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends R2dbcRepository<OrderDetail, Long> {
}
