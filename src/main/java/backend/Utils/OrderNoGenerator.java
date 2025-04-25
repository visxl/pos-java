package backend.Utils;

import org.springframework.stereotype.Service;

@Service
public class OrderNoGenerator {
    private static int counter = 1;

    public static synchronized String generateOrderNo() {
        return "SALE" + String.format("%04d", counter++);
    }
}
