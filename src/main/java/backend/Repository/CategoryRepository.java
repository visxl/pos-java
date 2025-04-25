package backend.Repository;

import backend.Entities.Category;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CategoryRepository extends R2dbcRepository<Category, Long> {
    @Query(value = "SELECT * FROM category WHERE categoryName = :categoryName")
    Flux<Category> findByCategory(String category);
}
