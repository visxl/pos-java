package backend.Utils;

import lombok.Data;

import java.util.List;

@Data
public class PageResponse<T> {
    private List<T> content;
    private int pageNumber;
    private int  pageSize;
    private long totalRecords;
    private int totalPages;

    public PageResponse(List<T> content, int pageNumber, int pageSize, long totalRecords){
        this.content = content;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalRecords = totalRecords;
        this.totalPages = (int) Math.ceil((double) totalRecords / pageSize);
    }
}
