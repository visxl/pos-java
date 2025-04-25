package backend.Controller;

import backend.Dto.AddQuantityRequest;
import backend.Dto.ProductCategory;
import backend.Dto.ProductDto;
import backend.Dto.ProductPosDto;
import backend.Entities.Product;
import backend.Service.ProductService;
import backend.Utils.PageResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/product")
@AllArgsConstructor
@CrossOrigin("*")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/all")
    public Flux<ProductDto> findAll() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Product> findById(@PathVariable  Long id) {
        return productService.findById(id);
    }

    @PostMapping("/create")
    public Mono<ProductDto> create(@RequestBody ProductDto productDto) {
        return productService.create(productDto);
    }

    @PutMapping("/update")
    public Mono<ProductDto> update(@RequestBody ProductDto productDto) {
        return productService.update(productDto);
    }

    @DeleteMapping("/delete")
    public Mono<ProductDto> delete(@RequestBody ProductDto productDto) {
        return productService.delete(productDto);
    }

    @GetMapping
    public Mono<PageResponse<ProductCategory>> findPagination(@RequestParam(defaultValue = "10", required = false) Integer pageSize,
                                                              @RequestParam Integer pageNumber) {
        return productService.findPagination(pageSize, pageNumber);
    }


    @GetMapping("/search")
    public Mono<PageResponse<ProductCategory>> searchText(@RequestParam String searchText,
                                                          @RequestParam(defaultValue = "10", required = false) Integer pageSize,
                                                          @RequestParam Integer pageNumber) {
        return productService.searchText(searchText, pageSize, pageNumber);
    }

    @GetMapping("/category")
    public Mono<PageResponse<ProductCategory>> findByCategoryName(@RequestParam("name") String categoryName,
                                                          @RequestParam(defaultValue = "10", required = false) Integer pageSize,
                                                          @RequestParam Integer pageNumber) {
        return productService.findByCategoryName(categoryName, pageSize, pageNumber);
    }

    @GetMapping("/find")
    public Flux<ProductPosDto> findByProductCode(@RequestParam String text) {
        return productService.findByProductCode(text);
    }

    @PutMapping("/add/{id}")
    public Mono<ResponseEntity<ProductDto>> addQuantityToProduct(
            @PathVariable("id") Long productId,
            @RequestBody AddQuantityRequest request
    ) {
        return productService.addQuantity(productId, request.addQuantity())
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().build()));
    }

    @GetMapping("/check-code/{code}")
    public Mono<Boolean> checkCodeExists(@PathVariable String code) {
      return productService.existsByProductCode(code);
    }

}
