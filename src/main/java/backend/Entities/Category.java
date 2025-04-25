package backend.Entities;

import backend.Dto.CategoryDto;
import backend.Utils.DateStringUtils;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("category")
public class Category {

    public static final String ID_COLUMN = "categoryId";
    public static final String CATEGORY_CODE_COLUMN = "categoryCode";
    public static final String CATEGORY_NAME_COLUMN = "categoryName";
    public static final String STATUS_COLUMN = "status";
    public static final String CREATED_DATE_COLUMN = "createdDate";
    public static final String UPDATED_DATE_COLUMN = "updatedDate";

    @Id
    @Column(ID_COLUMN)
    private Long categoryId;
    @Column(CATEGORY_CODE_COLUMN)
    private String categoryCode;
    @Column(CATEGORY_NAME_COLUMN)
    private String categoryName;
    @Column(STATUS_COLUMN)
    private boolean status;
    @Column(CREATED_DATE_COLUMN)
    @JsonSerialize(using = DateStringUtils.class)
    private LocalDateTime createdDate;
    @Column(UPDATED_DATE_COLUMN)
    @JsonSerialize(using = DateStringUtils.class)
    private LocalDateTime updatedDate;

    public static CategoryBuilder from(CategoryDto categoryDto) {
        return Category.builder()
                .categoryId(categoryDto.getCategoryId())
                .categoryCode(categoryDto.getCategoryCode())
                .categoryName(categoryDto.getCategoryName())
                .status(categoryDto.isStatus())
                .createdDate(categoryDto.getCreatedDate())
                .updatedDate(categoryDto.getUpdatedDate());
    }
}
