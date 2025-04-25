package backend.Controller;

import backend.Dto.CategoryDto;
import backend.Dto.FindCategory;
import backend.Service.CategoryService;
import backend.Utils.PageResponse;
import backend.Utils.ResponseMessage;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/category")
@AllArgsConstructor
@CrossOrigin("*")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/all")
    public Flux<CategoryDto> findAll() {
        return categoryService.findAll();
    }

    @GetMapping("/categories")
    public Flux<FindCategory> findAllCategory() {
        return categoryService.findAllCategory();
    }

    @GetMapping
    public Mono<PageResponse<CategoryDto>> findPagination(
            @RequestParam(defaultValue = "10", required = false) Integer pageSize,
            @RequestParam Integer pageNumber) {
        return categoryService.findPagination(pageSize, pageNumber);
    }

    @PostMapping("/create")
    public Mono<ResponseEntity<ResponseMessage>> create(@RequestBody CategoryDto categoryDto) {
        return categoryService.create(categoryDto);
    }

    @PutMapping("/update")
    public Mono<ResponseEntity<ResponseMessage>> update(@RequestBody CategoryDto categoryDto) {
        return categoryService.update(categoryDto);
    }

    @DeleteMapping("/delete")
    public Mono<ResponseEntity<ResponseMessage>> delete(@RequestBody CategoryDto categoryDto) {
        return categoryService.delete(categoryDto);
    }
}
