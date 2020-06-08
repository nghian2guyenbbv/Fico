package vn.com.tpf.microservices.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FicoRepaymentModel {

    @JsonProperty("ReceiptProcessingMO")
    private ReceiptProcessingMO receiptProcessingMO;
}
