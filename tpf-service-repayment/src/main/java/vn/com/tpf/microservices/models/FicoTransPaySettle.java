package vn.com.tpf.microservices.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Date;

@Data
@Builder
@Table(name = "fico_trans_pay_settle", schema = "payoo")
@Entity
public class FicoTransPaySettle {
    @Id
    @Column(name="id")
    private long id;

    @Column(name="transaction_id", unique = true, nullable = false)
    private String transactionId;

    @Column(name="loanid", nullable = false)
    private long loanId;

    @Column(name="create_date")
    private Timestamp createDate;

    @Column(name="loan_account_no")
    private String loanAccountNo;

    @Column(name="identification_number")
    private String identificationNumber;

    @Column(name="amount")
    private long amount;

    @Column(name="is_completed")
    private int iscompleted;

    @Column(name="flag_settle")
    private long flagsettle;

    @Column(name="trans_date")
    private Date transDate;

}
