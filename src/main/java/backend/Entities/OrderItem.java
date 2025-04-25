package backend.Entities;

import backend.Utils.DateStringUtils;
import backend.Utils.OrderNoGenerator;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("orderItem")
public class OrderItem {

    public static final String ID_COLUMN = "orderId";
    public static final String ORDER_NO_COLUMN = "orderNo";
    public static final String TOTAL_PRICE_COLUMN = "totalPrice";
    public static final String PRODUCT_ID_COLUMN = "productId";
    public static final String PRODUCT_CODE_COLUMN = "productCode";
    public static final String PRODUCT_NAME_COLUMN = "productName";
    public static final String PRICE_COLUMN = "price";
    public static final String QUANTITY_COLUMN = "quantity";
    public static final String STATUS_COLUMN = "status";
    public static final String CREATED_DATE_COLUMN = "createdDate";

    @Id
    @Column(ID_COLUMN)
    private Long orderId;
    @Column(ORDER_NO_COLUMN)
    private String orderNo;
    @Column(CREATED_DATE_COLUMN)
    @JsonSerialize(using = DateStringUtils.class)
    private LocalDateTime createdDate;
    @Column(TOTAL_PRICE_COLUMN)
    private BigDecimal totalPrice;
    @Column(PRODUCT_ID_COLUMN)
    private Long productId;
    @Column(PRODUCT_CODE_COLUMN)
    private String productCode;
    @Column(PRODUCT_NAME_COLUMN)
    private String productName;
    @Column(PRICE_COLUMN)
    private BigDecimal price;
    @Column(QUANTITY_COLUMN)
    private int quantity;
    @Column(STATUS_COLUMN)
    private boolean status;

}
