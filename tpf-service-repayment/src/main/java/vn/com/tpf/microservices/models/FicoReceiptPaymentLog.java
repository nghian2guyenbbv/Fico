package vn.com.tpf.microservices.models;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Builder
@Entity
@Table(name = "fico_receipt_payment_log", schema = "payoo")
public class FicoReceiptPaymentLog {
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

    @Column(name="card_approval_reference_number")
    private String cardApprovalReferenceNumber;

    @Column(name="receipt_against")
    private String receiptAgainst;

    @Column(name="loan_account_no")
    private String loanAccountNo;

    @Column(name="transaction_currency_code")
    private String transactionCurrencyCode;

    @Column(name="instrument_date")
    private Timestamp instrumentDate;

    @Column(name="transaction_value_date")
    private Timestamp transactionValueDate;

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
    private Timestamp depositDate;

    @Column(name="deposit_bank_account_number")
    private String depositBankAccountNumber;

    @Column(name="realization_date")
    private Timestamp realizationDate;

    @Column(name="receipt_transaction_status")
    private String receiptTransactionStatus;

    @Column(name="process_till_maker")
    private String processTillMaker;

    @Column(name="request_channel")
    private String requestChannel;

    @Column(name="receipt_payout_channel")
    private String receiptPayoutChannel;

    @Column(name="bounce_cancel_reason")
    private String bounceCancelReason;

    @Column(name="bounce_cancel_date")
    private Timestamp bounceCancelDate;

    @Column(name="bank_identification_code")
    private String bankIdentificationCode;

    @Column(name="bank_identification_type")
    private String bankIdentificationType;


    //--------------------------- RESPONSE --------------------------
    @Column(name="transaction_id")
    private String transactionId;

    @Column(name="response_code")
    private String responseCode;

    @Column(name="response_message")
    private String responseMessage;

    @Column(name="response_date")
    private Timestamp responseDate;

    @Column(name="payin_slip_referenc_number")
    private String payinSlipReferencNumber;

    @Column(name="receipt_id")
    private String receiptId;

    @Column(name="i18n_code")
    private String i18nCode;

    @Column(name="message_arguments")
    private String messageArguments;

    @Column(name="res_type")
    private String type;

    @Column(name="value")
    private String value;

    @Column(name="log_response")
    private String logResponse;
}
