package vn.com.tpf.microservices.models;

import java.util.Date;

public class SearchModel {
    private String search_value;
    private String transaction_id;

    private long loan_id;
    private String loan_account_no;
    private String identification_number;
    private long amount;

    private Date create_date;

    private Integer partner_id;

    private Integer flag_closure;

    private String device;


    public SearchModel() {
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public long getLoan_id() {
        return loan_id;
    }

    public void setLoan_id(long loan_id) {
        this.loan_id = loan_id;
    }

    public String getLoan_account_no() {
        return loan_account_no;
    }

    public void setLoan_account_no(String loan_account_no) {
        this.loan_account_no = loan_account_no;
    }

    public String getIdentification_number() {
        return identification_number;
    }

    public void setIdentification_number(String identification_number) {
        this.identification_number = identification_number;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getSearch_value() {
        return search_value;
    }

    public void setSearch_value(String search_value) {
        this.search_value = search_value;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public Integer getPartner_id() {
        return partner_id;
    }

    public void setPartner_id(int partner_id) {
        this.partner_id = partner_id;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public Integer getFlag_closure() {
        return flag_closure;
    }

    public void setFlag_closure(Integer flag_closure) {
        this.flag_closure = flag_closure;
    }
}

