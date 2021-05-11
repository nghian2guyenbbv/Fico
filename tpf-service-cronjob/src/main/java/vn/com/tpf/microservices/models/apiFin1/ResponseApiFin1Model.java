package vn.com.tpf.microservices.models.apiFin1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseApiFin1Model {
    private String transactionId;
    private int responseCode;
    private String responseMessage;
    private Timestamp responseDate;
    private Object responseData;
}
