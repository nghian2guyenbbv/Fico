package vn.com.tpf.microservices.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@NamedStoredProcedureQuery(
        name = "getlistqueue",
        procedureName = "payoo.getlistqueue",
        resultClasses = FicoTransPayQueue.class,
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, type = int.class)
        }
)
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "fico_trans_pay_queue", schema = "payoo")
public class FicoTransPayQueue {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    @Column(name="id")
    private long id;

    @Column(name="transaction_id", unique = true, nullable = false)
    @JsonProperty("transaction_id")
    private String transactionId;

    @Column(name="loanid", nullable = false)
    @JsonProperty("loan_id")
    private long loanId;

    @Column(name="create_date")
    @JsonProperty("create_date")
    private Timestamp createDate;

    @Column(name="loan_account_no")
    @JsonProperty("loan_account_no")
    private String loanAccountNo;

    @Column(name="identification_number")
    @JsonProperty("identification_number")
    private String identificationNumber;

    @Column(name="amount")
    @Min(0)
    @NotNull
    private long amount;

    private int status;

    @Column(name="partner_id")
    private int partnerId;
}
