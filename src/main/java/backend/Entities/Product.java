package backend.Entities;

import backend.Dto.ProductDto;
import backend.Utils.DateStringUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("product")
public class Product {

    public static final String ID_COLUMN = "productId";
    public static final String PRODUCT_CODE_COLUMN = "productCode";
    public static final String PRODUCT_NAME_COLUMN = "productName";
    public static final String COST_COLUMN = "cost";
    public static final String PRICE_COLUMN = "price";
    public static final String QUANTITY_COLUMN = "quantity";
    public static final String UNIT_COLUMN = "unit";
    public static final String IMAGE_PATH_COLUMN = "imagePath";
    public static final String STATUS_COLUMN = "status";
    public static final String CATEGORY_NAME_COLUMN = "categoryName";
    public static final String CATEGORY_CODE_COLUMN = "categoryCode";
    public static final String CREATED_DATE_COLUMN = "createdDate";
    public static final String UPDATED_DATE_COLUMN = "updatedDate";

    @Id
    @Column(ID_COLUMN)
    private Long productId;
    @Column(PRODUCT_CODE_COLUMN)
    private String productCode;
    @Column(PRODUCT_NAME_COLUMN)
    private String productName;
    @Column(COST_COLUMN)
    private BigDecimal cost;
    @Column(PRICE_COLUMN)
    private BigDecimal price;
    @Column(QUANTITY_COLUMN)
    private int quantity;
    @Column(UNIT_COLUMN)
    private String unit;
    @Column(IMAGE_PATH_COLUMN)
    private String imagePath;
    @Column(STATUS_COLUMN)
    private boolean status;
    @Column(CATEGORY_NAME_COLUMN)
    private String categoryName;
    @Column(CATEGORY_CODE_COLUMN)
    private String categoryCode;
    @Column(CREATED_DATE_COLUMN)
    @JsonSerialize(using = DateStringUtils.class)
    private LocalDateTime createdDate;
    @Column(UPDATED_DATE_COLUMN)
    @JsonSerialize(using = DateStringUtils.class)
    private LocalDateTime updatedDate;

    public static ProductBuilder from(ProductDto productDto) {
        return Product.builder()
                .productId(productDto.getProductId())
                .productCode(productDto.getProductCode())
                .productName(productDto.getProductName())
                .cost(productDto.getCost())
                .price(productDto.getPrice())
                .quantity(productDto.getQuantity())
                .unit(productDto.getUnit())
                .imagePath(productDto.getImagePath())
                .status(productDto.isStatus())
                .categoryName(productDto.getCategoryName())
                .categoryCode(productDto.getCategoryCode())
                .createdDate(productDto.getCreatedDate())
                .updatedDate(productDto.getUpdatedDate());
    }
}
