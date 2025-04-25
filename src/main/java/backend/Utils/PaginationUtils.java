package backend.Utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public class PaginationUtils {
    public static final int DEFAULT_LIMIT = 10;
    public static final int DEFAULT_PAGE_NUMBER = 1;

    public static Pageable createPageable(Integer pageNumber, Integer limit) {
        int pageSize = (limit != null) ? limit : DEFAULT_LIMIT;
        int pageIndex = (pageNumber != null ? pageNumber : DEFAULT_PAGE_NUMBER) - 1;
        return PageRequest.of(Math.max(pageIndex, 0), pageSize);
    }

    public static long calculateOffset(int pageNumber, int pageSize) {
        return (long) Math.max(pageNumber, 0) * pageSize;
    }

    public static <T, R> Mono<PageResponse<R>> fetchPagedResponse(
            R2dbcEntityTemplate r2dbcEntityTemplate,
            Class<T> entityClass,
            Function<T, R> mapper,
            int pageNumber,
            int pageSize,
            String activeColumn
    ) {
        Pageable pageable = createPageable(pageNumber, pageSize);
        long offset = calculateOffset(pageable.getPageNumber(), pageable.getPageSize());

        Mono<Long> count = r2dbcEntityTemplate.count(Query.query(Criteria.where(activeColumn).isTrue()), entityClass);

        Query query = Query.query(Criteria.where(activeColumn).isTrue())
                .limit(pageable.getPageSize())
                .offset(offset)
                .sort(pageable.getSort().isEmpty() ? Sort.by("id").ascending() : pageable.getSort());

        return count.flatMap(totalRecords ->
                r2dbcEntityTemplate.select(query, entityClass)
                        .map(mapper)
                        .collectList()
                        .map(results -> new PageResponse<>(results, pageable.getPageNumber() + 1, pageable.getPageSize(), totalRecords))
        );
    }
}
