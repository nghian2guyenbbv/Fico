package vn.com.tpf.microservices.models;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "fico_customer_current_amount_live_bank", schema = "payoo")
public class FicoCustomerLiveBank {
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

    @Column(name = "final_due_flag")
    @JsonProperty("final_due_flag")
    private String finalDueFlag;

    @Column(name = "early_closure_flag")
    @JsonProperty("early_closure_flag")
    private String early_closure_flag;

    @Column(name = "early_closure_date")
    @JsonProperty("early_closure_date")
    private Timestamp early_closure_date;

    @Column(name = "early_closure_amt",nullable = true)
    @JsonProperty("early_closure_amt")
    private long earlyClosureAmt;

    public long getEarlyClosureAmt() {
        return earlyClosureAmt;
    }

    public void setEarlyClosureAmt(long earlyClosureAmt) {
        this.earlyClosureAmt = earlyClosureAmt;
    }

    public String getEarly_closure_flag() {
        return early_closure_flag;
    }

    public void setEarly_closure_flag(String early_closure_flag) {
        this.early_closure_flag = early_closure_flag;
    }

    public Timestamp getEarly_closure_date() {
        return early_closure_date;
    }

    public void setEarly_closure_date(Timestamp early_closure_date) {
        this.early_closure_date = early_closure_date;
    }

    public String getFinalDueFlag() {
        return finalDueFlag;
    }

    public void setFinalDueFlag(String finalDueFlag) {
        this.finalDueFlag = finalDueFlag;
    }

    public FicoCustomerLiveBank() {
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
