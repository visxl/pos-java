package backend.Repository;

import backend.Entities.Invoice;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends R2dbcRepository<Invoice, Long> {
}
