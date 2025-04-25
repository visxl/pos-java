package backend.Service;

import backend.Dto.CategoryDto;
import backend.Dto.FindCategory;
import backend.Dto.ProductCategory;
import backend.Utils.PageResponse;
import backend.Utils.ResponseMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public interface CategoryService {

    Flux<CategoryDto> findAll();
    Mono<PageResponse<CategoryDto>> findPagination(Integer pageSize, Integer pageNumber);
    Mono<ResponseEntity<ResponseMessage>> create(CategoryDto categoryDto);
    Mono<ResponseEntity<ResponseMessage>>  update(CategoryDto categoryDto);
    Mono<ResponseEntity<ResponseMessage>> delete(CategoryDto categoryDto);

    Flux<FindCategory> findAllCategory();
}
