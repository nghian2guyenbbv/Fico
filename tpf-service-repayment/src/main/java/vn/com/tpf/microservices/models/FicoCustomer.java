package vn.com.tpf.microservices.models;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Entity
@Table(name = "fico_customer_current_amount", schema = "payoo")
public class FicoCustomer {
    @Id
    //@GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="loanid", nullable = false, unique = true)
    @JsonProperty("loan_id")
    private long loanId;

    @Column(name="duedate")
    @JsonProperty("due_date")
    private Timestamp dueDate;

    @Column(name="loan_account_no", unique = true, nullable = false)
    @JsonProperty("loan_account_no")
    private String loanAccountNo;

    @Column(name="identification_number")
    @JsonProperty("identification_number")
    private String identificationNumber;

    @Column(name="customer_name")
    @JsonProperty("customer_name")
    private String customerName;

    @Column(name="installment_amount")
    @JsonProperty("installment_amount")
    private long installmentAmount;

    @Column(name = "net_amount")
    @JsonProperty("net_amount")
    private long netAmount;

    @Column(name = "loan_active")
    @JsonProperty("loan_active")
    private boolean loanActive;

    public FicoCustomer() {
    }

    public boolean isLoanActive() {
        return loanActive;
    }

    public void setLoanActive(boolean loanActive) {
        this.loanActive = loanActive;
    }

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public String getDueDate() {
//        return DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(new java.sql.Date(dueDate.getTime()).toLocalDate());
//        return DateTimeFormatter.ISO_DATE.format(dueDate.toLocalDateTime());
        if (dueDate != null) {
            return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(dueDate.toLocalDateTime());
        }else{
            return null;
        }
//        return dueDate;
    }

    public void setDueDate(Timestamp dueDate) {
        this.dueDate = dueDate;
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public long getInstallmentAmount() {
        return installmentAmount;
    }

    public void setInstallmentAmount(long installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

    public long getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(long netAmount) {
        this.netAmount = netAmount;
    }

    @Override
    public String toString() {
        return "{\n"
                + " \"loanId\":" + loanId
                + ",\n  \"dueDate\":\"" + dueDate + "\""
                + ",\n  \"loanAccountNo\":\"" + loanAccountNo + "\""
                + ",\n  \"identificationNumber\":\"" + identificationNumber + "\""
                + ",\n  \"customerName\":\"" + customerName + "\""
                + ",\n  \"installmentAmount\":\"" + installmentAmount + "\""
                + ",\n  \"netAmount\":\"" + netAmount + "\""
                + "\n}";
    }

    /*@Override
    public String toString() {
        return "{\"FicoCustomer\":{"
                + "                        \"loanId\":\"" + loanId + "\""
                + ",                         \"dueDate\":" + dueDate
                + ",                         \"loanAccountNo\":\"" + loanAccountNo + "\""
                + ",                         \"identificationNumber\":\"" + identificationNumber + "\""
                + ",                         \"customerName\":\"" + customerName + "\""
                + ",                         \"installmentAmount\":\"" + installmentAmount + "\""
                + ",                         \"netAmount\":\"" + netAmount + "\""
                + "}}";
    }*/
}
