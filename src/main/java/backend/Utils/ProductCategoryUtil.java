package backend.Utils;

import backend.Dto.ProductCategory;
import backend.Entities.Category;
import backend.Entities.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCategoryUtil {

    public static ProductCategory toRecord(List<Category> category,Product products) {
        return new ProductCategory(
                products.getProductId(),
                products.getProductCode(),
                products.getProductName(),
                products.getCost(),
                products.getPrice(),
                products.getQuantity(),
                products.getUnit(),
                products.isStatus(),
                products.getCategoryCode(),
                products.getCategoryName(),
                category
        );
    }
}
