package vn.com.tpf.microservices.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
public class CancelTransModel{

    private String transactionId;

    @NotEmpty(message = "cancelTransactionId not empty")
    private String cancelTransactionId;

    @NotNull
    private String cancelDescription;

    @JsonProperty("loanAccountNo")
    private String loanAccount;

    @JsonProperty("paymentDate")
    private Timestamp create_date;

    @NotNull
    private Integer partnerId;
}
