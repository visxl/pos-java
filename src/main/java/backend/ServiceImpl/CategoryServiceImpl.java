package backend.ServiceImpl;

import backend.Dto.CategoryDto;
import backend.Dto.FindCategory;
import backend.Dto.ProductCategory;
import backend.Entities.Category;
import backend.Entities.Product;
import backend.Mapper.CategoryMapper;
import backend.Repository.CategoryRepository;
import backend.Service.CategoryService;
import backend.Utils.PageResponse;
import backend.Utils.ResponseMessage;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    @Override
    public Flux<CategoryDto> findAll() {
        return categoryRepository.findAll()
                .map(CategoryMapper::toDto);
    }

    @Override
    public Mono<PageResponse<CategoryDto>> findPagination(Integer pageSize, Integer pageNumber) {
        Criteria criteria = Criteria.where(Category.STATUS_COLUMN).isTrue();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(Category.CATEGORY_CODE_COLUMN).ascending());
        Mono<Long> totalRecordsMono = r2dbcEntityTemplate.count(Query.query(criteria), Category.class);

        Flux<CategoryDto> categoryDtoFlux = r2dbcEntityTemplate.select(Category.class)
                .matching(Query.query(criteria).with(pageable))
                .all()
                .map(CategoryMapper::toDto);
        return totalRecordsMono.flatMap(totalRecords ->
                categoryDtoFlux.collectList()
                        .map(content -> new PageResponse<>(content, pageNumber, pageSize, totalRecords))
                );
    }

    @Override
    public Mono<ResponseEntity<ResponseMessage>> create(CategoryDto categoryDto) {
        return categoryRepository.save(Category.from(categoryDto)
                .createdDate(LocalDateTime.now())
                .status(true)
                .build())
                .map(CategoryMapper::toDto)
                .map(savedCategory -> {
                    String message = "Category created success! Category Name : " + savedCategory.getCategoryName();
                    return ResponseEntity
                            .status(HttpStatus.CREATED)
                            .body(new ResponseMessage(HttpStatus.CREATED, message));
                });
    }

    @Override
    public Mono<ResponseEntity<ResponseMessage>> update(CategoryDto categoryDto) {
        return categoryRepository.findById(categoryDto.getCategoryId())
                .flatMap(exisitingCategory -> {
                    CategoryDto.updateField(exisitingCategory, categoryDto)
                            .setUpdatedDate(LocalDateTime.now());
                    return categoryRepository.save(exisitingCategory);
                })
                .map(CategoryMapper::toDto)
                .map(updatedCategory -> {
                    String message = "Category ID = " + updatedCategory.getCategoryId() + " has been modified!";
                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .body(new ResponseMessage(HttpStatus.OK, message));
                })
                .switchIfEmpty(Mono.just(
                        ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(new ResponseMessage(HttpStatus.NOT_FOUND, "Category Not Found!"))
                ));
    }

    @Override
    public Mono<ResponseEntity<ResponseMessage>> delete(CategoryDto categoryDto) {
        return categoryRepository.findById(categoryDto.getCategoryId())
                .flatMap(existingCategory -> {
                    CategoryDto.updateField(existingCategory, categoryDto);
                    existingCategory.setStatus(false);
                    existingCategory.setUpdatedDate(LocalDateTime.now());
                    return categoryRepository.save(existingCategory);
                })
                .map(CategoryMapper::toDto)
                .map(savedCategory -> {
                    String message = "Category ID = " + savedCategory.getCategoryId() + " has been deleted";
                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .body(new ResponseMessage(HttpStatus.OK, message));
                })
                .switchIfEmpty(Mono.just(
                        ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(new ResponseMessage(HttpStatus.NOT_FOUND, "Category Not Found"))
                ));
    }

    @Override
    public Flux<FindCategory> findAllCategory() {
        return r2dbcEntityTemplate.select(Category.class)
                .all()
                .map(category -> new FindCategory(
                        category.getCategoryCode(),
                        category.getCategoryName()
                ));
    }

}
