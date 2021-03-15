package vn.com.tpf.microservices.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
public class ReceiptProcessingMO  {

    @JsonProperty("requestHeader")
    private ReqHeader requestHeader;

    @JsonProperty("receiptPayOutMode")
    private String receiptPayOutMode;

    @JsonProperty("paymentSubMode")
    private String paymentSubMode;

    @JsonProperty("receiptAgainst")
    private String receiptAgainst;

    @JsonProperty("loanAccountNo")
    private String loanAccountNo;

    @JsonProperty("transactionCurrencyCode")
    private String transactionCurrencyCode;

    @JsonProperty("instrumentDate")
    private String instrumentDate;

    @JsonProperty("transactionValueDate")
    private String transactionValueDate;

    @JsonProperty("receiptOrPayoutAmount")
    private long receiptOrPayoutAmount;

    @JsonProperty("autoAllocation")
    private String autoAllocation;

    @JsonProperty("receiptNo")
    private String receiptNo;

    @JsonProperty("instrumentReferenceNumber")
    private String instrumentReferenceNumber;

    @JsonProperty("receiptPurpose")
    private String receiptPurpose;

    @JsonProperty("sourceAccountNumber")
    private String sourceAccountNumber;

    @JsonProperty("depositDate")
    private String depositDate;

    @JsonProperty("depositBankAccountNumber")
    private String depositBankAccountNumber;

    @JsonProperty("realizationDate")
    private String realizationDate;

    @JsonProperty("receiptTransactionStatus")
    private String receiptTransactionStatus;

    @JsonProperty("processTillMaker")
    private boolean processTillMaker;

    @JsonProperty("requestChannel")
    private String requestChannel;

    @JsonProperty("receiptId")
    private int receiptId;

    @JsonProperty("applicationId")
    private String applicationId;

    @JsonProperty("applicationFormNumber")
    private String applicationFormNumber;

    @JsonProperty("drawerName")
    private String drawerName;

    @JsonProperty("bpType")
    private String bpType;

    @JsonProperty("businessPartnerId")
    private String businessPartnerId;

    @JsonProperty("receiptPayoutChannel")
    private String receiptPayoutChannel;

    @JsonProperty("sourceTransactionReferenceNumber")
    private String sourceTransactionReferenceNumber;

    @JsonProperty("receiptRemarks")
    private String receiptRemarks;

    @JsonProperty("representFlag")
    private String representFlag;

    @JsonProperty("depositorName")
    private String depositorName;

    @JsonProperty("identificationType")
    private String identificationType;

    @JsonProperty("identificationNumber")
    private String identificationNumber;

    @JsonProperty("bankIdentificationType")
    private String bankIdentificationType;

    @JsonProperty("bankIdentificationCode")
    private String bankIdentificationCode;

    @JsonProperty("cityCode")
    private String cityCode;

    @JsonProperty("bankCode")
    private String bankCode;

    @JsonProperty("bankBranchCode")
    private String bankBranchCode;

    @JsonProperty("vapId")
    private String vapId;

    @JsonProperty("chargeId")
    private String chargeId;

    @JsonProperty("chargeCodeId")
    private String chargeCodeId;

    @JsonProperty("allocatedAmount")
    private String allocatedAmount;

    @JsonProperty("payinSlipReferencNumber")
    private String payinSlipReferencNumber;

    @JsonProperty("bounceCancelReason")
    private String bounceCancelReason;

    @JsonProperty("bounceCancelDate")
    private String bounceCancelDate;

    @JsonProperty("requestAt")
    private String requestAt;
}
