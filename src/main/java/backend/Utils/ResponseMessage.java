package backend.Utils;

import org.springframework.http.HttpStatus;

public class ResponseMessage {
    private String message;

    public ResponseMessage(HttpStatus status, String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}