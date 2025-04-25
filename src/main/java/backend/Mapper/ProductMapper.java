package backend.Mapper;

import backend.Dto.ProductDto;
import backend.Entities.Product;

public class ProductMapper {
    // Convert from Entity to DTO
    public static ProductDto toDto(Product product) {
        if (product == null) {
            return null;
        }

        ProductDto dto = new ProductDto();
        dto.setProductId(product.getProductId());
        dto.setProductCode(product.getProductCode());
        dto.setProductName(product.getProductName());
        dto.setCost(product.getCost());
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getQuantity());
        dto.setUnit(product.getUnit());
        dto.setStatus(product.isStatus());
        dto.setCategoryName(product.getCategoryName());
        dto.setCategoryCode(product.getCategoryCode());
        dto.setCreatedDate(product.getCreatedDate());
        dto.setUpdatedDate(product.getUpdatedDate());

        return dto;
    }

    // Convert from DTO to Entity
    public static Product toEntity(ProductDto dto) {
        if (dto == null) {
            return null;
        }

        Product entity = new Product();
        entity.setProductId(entity.getProductId());
        entity.setProductCode(entity.getProductCode());
        entity.setProductName(entity.getProductName());
        entity.setCost(entity.getCost());
        entity.setPrice(entity.getPrice());
        entity.setQuantity(entity.getQuantity());
        entity.setUnit(entity.getUnit());
        entity.setStatus(entity.isStatus());
        entity.setCategoryName(entity.getCategoryName());
        entity.setCategoryCode(entity.getCategoryCode());
        entity.setCreatedDate(entity.getCreatedDate());
        entity.setUpdatedDate(entity.getUpdatedDate());

        return entity;
    }
}
