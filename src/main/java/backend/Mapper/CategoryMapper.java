package backend.Mapper;

import backend.Dto.CategoryDto;
import backend.Entities.Category;

public class CategoryMapper {
    public static CategoryDto toDto(Category category) {
        if (category == null) {
            return null;
        }

        CategoryDto dto = new CategoryDto();
        dto.setCategoryId(category.getCategoryId());
        dto.setCategoryCode(category.getCategoryCode());
        dto.setCategoryName(category.getCategoryName());
        dto.setStatus(category.isStatus());
        dto.setCreatedDate(category.getCreatedDate());
        dto.setUpdatedDate(category.getUpdatedDate());

        return dto;
    }

    // Convert from DTO to Entity
    public static Category toEntity(CategoryDto dto) {
        if (dto == null) {
            return null;
        }

        Category entity = new Category();
        entity.setCategoryId(entity.getCategoryId());
        entity.setCategoryCode(entity.getCategoryCode());
        entity.setCategoryName(entity.getCategoryName());
        entity.setStatus(entity.isStatus());
        entity.setCreatedDate(entity.getCreatedDate());
        entity.setUpdatedDate(entity.getUpdatedDate());

        return entity;
    }
}
