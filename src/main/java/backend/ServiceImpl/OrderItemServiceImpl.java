package backend.ServiceImpl;

import backend.Dto.OrderDetailItem;
import backend.Dto.OrderItemRequest;
import backend.Entities.OrderDetail;
import backend.Dto.OrderDetailDto;
import backend.Entities.OrderItem;
import backend.Entities.Product;
import backend.Repository.OrderDetailRepository;
import backend.Repository.OrderItemRepository;
import backend.Repository.ProductRepository;
import backend.Service.OrderItemService;
import backend.Utils.OrderDetailUtil;
import backend.Utils.OrderNoGenerator;
import backend.Utils.PageResponse;
import backend.Utils.ResponseMessage;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    @Override
    public Mono<PageResponse<OrderDetailItem>> findPagination(Integer pageSize, Integer pageNumber) {
        Criteria criteria = Criteria.where(OrderDetail.ORDER_NO_COLUMN).isNotNull();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(OrderDetail.CREATED_DATE_COLUMN).descending());
        Mono<Long> totalRecordsMono = r2dbcEntityTemplate.count(Query.query(criteria), OrderDetail.class);

        Flux<OrderDetailItem> orderItemFlux = r2dbcEntityTemplate.select(OrderDetail.class)
                .matching(Query.query(criteria).with(pageable))
                .all()
                .flatMap( orderDetail -> r2dbcEntityTemplate.select(OrderItem.class)
                        .matching(Query.query(Criteria.where(OrderItem.ORDER_NO_COLUMN).is(orderDetail.getOrderNo())))
                        .all()
                        .collectList()
                        .map(orderItems -> OrderDetailUtil.toRecord(orderDetail,orderItems))
                );
        return totalRecordsMono.flatMap(totalRecords -> orderItemFlux.collectList()
                .map(content -> new PageResponse<>(content, pageNumber, pageSize, totalRecords))
        );
    }

    @Override
    public Mono<List<OrderItem>> createOrder(List<OrderItemRequest> orderRequests, Long orderId) {
        String orderNo = OrderNoGenerator.generateOrderNo();
        return Flux.fromIterable(orderRequests)
                .flatMap(orderRequest ->
                        r2dbcEntityTemplate.select(Product.class)
                                .matching(Query.query(Criteria.where(Product.PRODUCT_CODE_COLUMN).is(orderRequest.productCode())))
                                .one()
                                .switchIfEmpty(Mono.error(new ResponseStatusException(
                                                HttpStatus.NOT_FOUND, "Product Code not found")))
                                .flatMap(product -> {
                                    if (product.getQuantity() < orderRequest.quantity()) {
                                        return Mono.error(new ResponseStatusException(
                                                HttpStatus.NOT_FOUND, "Insufficient stock for product"
                                        ));
                                    }

                                    BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(orderRequest.quantity()));
                                    OrderItem orderItem = OrderItem.builder()
                                            .orderId(orderId)
                                            .orderNo(orderNo)
                                            .productId(product.getProductId())
                                            .productCode(product.getProductCode())
                                            .productName(product.getProductName())
                                            .quantity(orderRequest.quantity())
                                            .price(product.getPrice())
                                            .totalPrice(totalPrice)
                                            .createdDate(LocalDateTime.now())
                                            .status(product.isStatus())
                                            .build();

                                    return orderItemRepository.save(orderItem)
                                            .flatMap(savedOrderItem -> {
                                                int newQuantity = product.getQuantity() - orderRequest.quantity();
                                                return r2dbcEntityTemplate.update(Product.class)
                                                        .matching(Query.query(Criteria.where(Product.PRODUCT_CODE_COLUMN).is(orderRequest.productCode())))
                                                        .apply(Update.update(Product.QUANTITY_COLUMN, newQuantity))
                                                        .thenReturn(savedOrderItem);
                                            });
                                })
                )
                .collectList()
                .flatMap(orderItems -> {
                    List<OrderDetail> orderDetails = orderItems.stream()
                            .map(orderItem -> OrderDetail.builder()
                                    .orderId(orderItem.getOrderId())
                                    .orderNo(orderItem.getOrderNo())
                                    .createdDate(LocalDateTime.now())
                                    .build())
                            .collect(Collectors.toList());

                    return Flux.fromIterable(orderDetails)
                            .flatMap(orderDetailRepository::save)
                            .then(Mono.just(orderItems));
                });
//                .onErrorResume(e -> {
//                    System.err.println("Error creating order: " + e.getMessage());
//                    return Mono.error(new RuntimeException("Failed to create order: " + e.getMessage(), e));
//                });
    }




}
