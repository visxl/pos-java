package backend.Entities;

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
@Table("invoice")
public class Invoice {

    public static final String ID_COLUMN = "invoiceId";
    public static final String INVOICE_NO_COLUMN = "invoiceNo";
    public static final String ORDER_NO_COLUMN = "orderNo";
    public static final String INVOICE_DATE_COLUMN = "invoiceDate";

    @Id
    @Column(ID_COLUMN)
    private Long invoiceId;
    @Column(INVOICE_NO_COLUMN)
    private String invoiceNo;
    @Column(ORDER_NO_COLUMN)
    private String orderNo;
    @Column(INVOICE_DATE_COLUMN)
    private LocalDateTime invoiceDate;


}
