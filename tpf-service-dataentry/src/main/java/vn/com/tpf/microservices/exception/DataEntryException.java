package vn.com.tpf.microservices.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DataEntryException extends Exception {
    private int type;
    private String message;

    public DataEntryException(int type, String message) {
        this.type = type;
        this.message = message;
    }
}
