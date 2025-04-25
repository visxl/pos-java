package backend.Dto;

import backend.Entities.Category;
import backend.Entities.Product;
import backend.Utils.DateStringUtils;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

public record ProductCategory(
        Long productId,
        String productCode,
        String productName,
        BigDecimal cost,
        BigDecimal price,
        int quantity,
        String unit,
        boolean status,
        String categoryCode,
        String categoryName,
        List<Category> categories
) {
    public ProductCategory withItems(List<Category> categories) {
        return new ProductCategory(
                this.productId,
                this.productCode,
                this.productName,
                this.cost,
                this.price,
                this.quantity,
                this.unit,
                this.status,
                this.categoryCode,
                this.categoryName,
                categories
        );
    }

}
