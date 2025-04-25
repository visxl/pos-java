package backend.Dto;

import backend.Entities.Product;
import backend.Utils.DateStringUtils;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {

    private Long productId;
    private String productCode;
    private String productName;
    private BigDecimal cost;
    private BigDecimal price;
    private int quantity;
    private String unit;
    private String imagePath;
    private boolean status;
    private String categoryName;
    private String categoryCode;
    @JsonSerialize(using = DateStringUtils.class)
    private LocalDateTime createdDate;
    @JsonSerialize(using = DateStringUtils.class)
    private LocalDateTime updatedDate;

    public static Product updateField(Product existingProduct, ProductDto productDto) {
        existingProduct.setProductName(productDto.getProductName());
        existingProduct.setProductCode(productDto.getProductCode());
        existingProduct.setCost(productDto.getCost());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setQuantity(productDto.getQuantity());
        existingProduct.setUnit(productDto.getUnit());
        existingProduct.setImagePath(productDto.getImagePath());
        existingProduct.setStatus(productDto.isStatus());
        existingProduct.setCategoryName(productDto.getCategoryName());
        existingProduct.setCategoryCode(productDto.getCategoryCode());
        return existingProduct;
    }

}
