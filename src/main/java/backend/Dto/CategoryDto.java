package backend.Dto;

import backend.Entities.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto {

    private Long categoryId;
    private String categoryCode;
    private String categoryName;
    private boolean status;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public static Category updateField(Category existingCategory, CategoryDto categoryDto) {
        existingCategory.setCategoryCode(categoryDto.getCategoryCode());
        existingCategory.setCategoryName(categoryDto.getCategoryName());
        existingCategory.setStatus(categoryDto.isStatus());
        return existingCategory;
    }
}
