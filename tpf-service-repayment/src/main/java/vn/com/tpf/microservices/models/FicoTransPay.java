package vn.com.tpf.microservices.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "fico_trans_pay", schema = "payoo")
public class FicoTransPay implements Serializable {
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

    public FicoTransPay() {
    }

    public FicoTransPay(Timestamp createDate, String loanAccountNo, String identificationNumber, long amount) {
        this.createDate = createDate;
        this.loanAccountNo = loanAccountNo;
        this.identificationNumber = identificationNumber;
        this.amount = amount;
    }

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public String getLoanAccountNo() {
        return loanAccountNo;
    }

    public void setLoanAccountNo(String loanAccountNo) {
        this.loanAccountNo = loanAccountNo;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public FicoTransPay(String transactionId, long loanId, Timestamp createDate, String loanAccountNo, String identificationNumber, long amount) {
        this.transactionId = transactionId;
        this.loanId = loanId;
        this.createDate = createDate;
        this.loanAccountNo = loanAccountNo;
        this.identificationNumber = identificationNumber;
        this.amount = amount;
    }
}
