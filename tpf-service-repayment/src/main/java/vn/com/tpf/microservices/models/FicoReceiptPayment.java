package vn.com.tpf.microservices.models;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "fico_receipt_pay", schema = "payoo")
public class FicoReceiptPayment implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    @Column(name="id")
    private long id;

    @Column(name="branch_id")
    private int branchId;

    @Column(name="user_code")
    private String userCode;

    @Column(name="tenant_id")
    private int tenantId;

    @Column(name="receipt_pay_out_mode")
    private String receiptPayOutMode;

    @Column(name="payment_sub_mode")
    private String paymentSubMode;

    @Column(name="receipt_against")
    private String receiptAgainst;

    @Column(name="loan_account_no")
    private String loanAccountNo;

    @Column(name="transaction_currency_code")
    private String transactionCurrencyCode;

    @Column(name="instrument_date")
    private String instrumentDate;

    @Column(name="transaction_value_date")
    private String transactionValueDate;

    @Column(name="receipt_or_payout_amount")
    private long receiptOrPayoutAmount;

    @Column(name="auto_allocation")
    private String autoAllocation;

    @Column(name="receipt_no")
    private String receiptNo;

    @Column(name="instrument_reference_number")
    private String instrumentReferenceNumber;

    @Column(name="receipt_purpose")
    private String receiptPurpose;

    @Column(name="source_account_number")
    private String sourceAccountNumber;

    @Column(name="deposit_date")
    private String depositDate;

    @Column(name="deposit_bank_account_number")
    private String depositBankAccountNumber;

    @Column(name="realization_date")
    private String realizationDate;

    @Column(name="receipt_transaction_status")
    private String receiptTransactionStatus;

    @Column(name="process_till_maker")
    private String processTillMaker;

    @Column(name="request_channel")
    private String requestChannel;
}
