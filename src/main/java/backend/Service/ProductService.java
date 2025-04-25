package backend.Service;

import backend.Dto.ProductCategory;
import backend.Dto.ProductDto;
import backend.Dto.ProductPosDto;
import backend.Entities.Product;
import backend.Utils.PageResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public interface ProductService {

//    basic function
    Flux<ProductDto> findAll();
    Mono<Product> findById(Long id);
    Mono<ProductDto> create(ProductDto productDto);
    Mono<ProductDto> update(ProductDto productDto);
    Mono<ProductDto> delete(ProductDto productDto);
    Mono<PageResponse<ProductCategory>> findPagination(Integer pageSize, Integer pageNumber);
    Mono<PageResponse<ProductCategory>> searchText(String searchText, Integer pageSize, Integer pageNumber);

//    pos function
    Mono<PageResponse<ProductCategory>> findByCategoryName(String categoryName, Integer pageSize, Integer pageNumber);
    Flux<ProductPosDto> findByProductCode(String productCode);
    Mono<ProductDto> addQuantity(Long productId, int quantityToAdd);
    Mono<Boolean> existsByProductCode(String productCode);
//    Mono<String> generateUniqueProductCode();
}
