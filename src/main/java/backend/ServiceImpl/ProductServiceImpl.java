package backend.ServiceImpl;

import backend.Dto.ProductCategory;
import backend.Dto.ProductDto;
import backend.Dto.ProductPosDto;
import backend.Entities.Category;
import backend.Entities.Product;
import backend.Mapper.ProductMapper;
import backend.Mapper.ProductPosMapper;
import backend.Repository.CategoryRepository;
import backend.Repository.ProductRepository;
import backend.Service.ProductService;
import backend.Utils.PageResponse;
import backend.Utils.ProductCategoryUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    @Override
    public Flux<ProductDto> findAll() {
        return productRepository.findAll()
                .map(ProductMapper::toDto);
    }

    @Override
    public Mono<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Mono<ProductDto> create(ProductDto productDto) {
        return productRepository.save(Product.from(productDto)
                .createdDate(LocalDateTime.now())
                .status(true)
                .build())
                .map(ProductMapper::toDto);
    }

    @Override
    public Mono<ProductDto> update(ProductDto productDto) {
        return productRepository.findById(productDto.getProductId())
                .flatMap(exisitingProduct -> {
                    ProductDto.updateField(exisitingProduct, productDto)
                            .setUpdatedDate(LocalDateTime.now());
                    return productRepository.save(exisitingProduct);
                })
                .map(ProductMapper::toDto);
    }

    @Override
    public Mono<ProductDto> delete(ProductDto productDto) {
        return productRepository.findById(productDto.getProductId())
                .flatMap(existingProduct -> {
                    ProductDto.updateField(existingProduct, productDto);
                    existingProduct.setStatus(false);
                    existingProduct.setUpdatedDate(LocalDateTime.now());
                    return productRepository.save(existingProduct);
                })
                .map(ProductMapper::toDto);
    }

    @Override
    public Mono<PageResponse<ProductCategory>> findPagination(Integer pageSize, Integer pageNumber) {
        Criteria criteria = Criteria.where(Product.PRODUCT_CODE_COLUMN).isNotNull();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(Product.PRODUCT_CODE_COLUMN).ascending());
        Mono<Long> totalRecordsMono = r2dbcEntityTemplate.count(Query.query(criteria), Product.class);

        Flux<ProductCategory> productCategoryFlux = r2dbcEntityTemplate.select(Product.class)
                .matching(Query.query(criteria).with(pageable))
                .all()
                .flatMap(product -> r2dbcEntityTemplate.select(Category.class)
                        .matching(Query.query(Criteria
                                .where(Category.CATEGORY_CODE_COLUMN).is(product.getCategoryCode())))
                        .all()
                        .collectList()
                        .map(categories -> {
                            if (categories.isEmpty()) {
                                Mono.error(new RuntimeException());
                            }
                            return ProductCategoryUtil.toRecord(categories, product);
                        }));
        return totalRecordsMono.flatMap(totalRecords ->
                    productCategoryFlux.collectList()
                            .map(content -> new PageResponse<>(content, pageNumber, pageSize, totalRecords))
                );
    }

    @Override
    public Mono<PageResponse<ProductCategory>> searchText(String searchText, Integer pageSize, Integer pageNumber) {
        if (searchText == null || searchText.isBlank()) {
            return Mono.just(new PageResponse<>(List.of(), 1, pageSize != null ? pageSize : 10, 0));
        }

        Criteria criteria = Criteria
                .where(Product.PRODUCT_CODE_COLUMN).like('%' + searchText + '%')
                .or(Product.PRODUCT_NAME_COLUMN).like('%' + searchText + '%')
                .or(Product.CATEGORY_NAME_COLUMN).like('%' + searchText + '%');

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(Product.PRODUCT_CODE_COLUMN).ascending());

        Mono<Long> totalRecordsMono = r2dbcEntityTemplate.count(Query.query(criteria), Product.class);

        Flux<ProductCategory> productCategoryFlux = r2dbcEntityTemplate.select(Product.class)
                .matching(Query.query(criteria).with(pageable))
                .all()
                .flatMap(product -> r2dbcEntityTemplate.select(Category.class)
                        .matching(Query.query(Criteria
                                .where(Category.CATEGORY_CODE_COLUMN).is(product.getCategoryCode())))
                        .all()
                        .collectList()
                        .map(category -> ProductCategoryUtil.toRecord(category, product))
                );

        return totalRecordsMono.flatMap(totalRecords ->
                productCategoryFlux.collectList()
                        .map(content -> new PageResponse<>(content, pageNumber, pageSize, totalRecords))
        );
    }

    @Override
    public Mono<PageResponse<ProductCategory>> findByCategoryName(String categoryName, Integer pageSize, Integer pageNumber) {
        int size = (pageSize != null) ? pageSize : 10;
        int page = (pageNumber != null) ? pageNumber : 1;

        return r2dbcEntityTemplate.select(Category.class)
                .matching(Query.query(Criteria.where(Category.CATEGORY_NAME_COLUMN).like("%" + categoryName + "%")))
                .all()
                .map(Category::getCategoryCode)
                .collectList()
                .flatMap(categoryCodes -> {
                    if (categoryCodes.isEmpty()) {
                        return Mono.just(new PageResponse<>(List.of(), page, size, 0));
                    }

                    Criteria productCriteria = Criteria.where(Product.STATUS_COLUMN).isTrue()
                            .and(Product.CATEGORY_CODE_COLUMN).in(categoryCodes);

                    Query productQuery = Query.query(productCriteria)
                            .with(PageRequest.of(page - 1, size, Sort.by(Product.PRODUCT_CODE_COLUMN).ascending()));

                    Mono<Long> total = r2dbcEntityTemplate.count(Query.query(productCriteria), Product.class);

                    Flux<ProductCategory> result = r2dbcEntityTemplate.select(Product.class)
                            .matching(productQuery)
                            .all()
                            .flatMap(product -> r2dbcEntityTemplate.select(Category.class)
                                    .matching(Query.query(Criteria
                                            .where(Category.CATEGORY_CODE_COLUMN).is(product.getCategoryCode())))
                                    .all()
                                    .collectList()
                                    .map(categories -> ProductCategoryUtil.toRecord(categories, product)));

                    return total.flatMap(count -> result.collectList()
                            .map(content -> new PageResponse<>(content, page, size, count)));
                });
    }


    @Override
    public Flux<ProductPosDto> findByProductCode(String text) {
        return r2dbcEntityTemplate.select(Product.class)
                .matching(Query.query(Criteria
                        .where(Product.PRODUCT_CODE_COLUMN).like('%' + text + '%')
                        .or(Product.PRODUCT_NAME_COLUMN).like('%' + text + '%')))
                .all()
                .map(ProductPosMapper::toDto);
    }

    @Override
    public Mono<ProductDto> addQuantity(Long productId, int quantityToAdd) {
        return r2dbcEntityTemplate.select(Product.class)
                .matching(Query.query(Criteria.where(Product.ID_COLUMN).is(productId)))
                .one()
                .switchIfEmpty(Mono.error(new RuntimeException("Product not found with ID: " + productId)))
                .flatMap(product -> {
                    int newQuantity = product.getQuantity() + quantityToAdd;
                    product.setQuantity(newQuantity);
                    product.setUpdatedDate(LocalDateTime.now());
                    return r2dbcEntityTemplate.update(Product.class)
                            .matching(Query.query(Criteria.where(Product.ID_COLUMN).is(productId)))
                            .apply(Update.update(Product.QUANTITY_COLUMN, newQuantity)
                                    .set(Product.UPDATED_DATE_COLUMN, LocalDateTime.now()))
                            .thenReturn(product);
                })
                .map(ProductMapper::toDto);
    }

    public Mono<Boolean> existsByProductCode(String productCode) {
        return r2dbcEntityTemplate.select(Product.class)
                .matching(Query.query(Criteria.where(Product.PRODUCT_CODE_COLUMN).is(productCode)))
                .exists();
    }
//
//    public Mono<String> generateUniqueProductCode() {
//        String generated = String.valueOf((int)(Math.random() * 900000000) + 100000000);
//        return existsByProductCode(generated)
//                .flatMap(exists -> exists ? generateUniqueProductCode() : Mono.just(generated));
//    }
}
