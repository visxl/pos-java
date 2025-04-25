package backend.Mapper;

import backend.Dto.ProductDto;
import backend.Dto.ProductPosDto;
import backend.Entities.Product;
import org.springframework.stereotype.Service;

public class ProductPosMapper {

    public static ProductPosDto toDto(Product product) {
        if (product == null) {
            return null;
        }

        ProductPosDto dto = new ProductPosDto();
        dto.setProductCode(product.getProductCode());
        dto.setProductName(product.getProductName());
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getQuantity());

        return dto;
    }

    // Convert from DTO to Entity
    public static Product toEntity(ProductPosDto dto) {
        if (dto == null) {
            return null;
        }

        Product entity = new Product();
        entity.setProductCode(entity.getProductCode());
        entity.setProductName(entity.getProductName());
        entity.setPrice(entity.getPrice());

        return entity;
    }
}
