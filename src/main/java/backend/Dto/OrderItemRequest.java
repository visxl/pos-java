package backend.Dto;

public record OrderItemRequest(
        String productCode,
        int quantity
) {
}
