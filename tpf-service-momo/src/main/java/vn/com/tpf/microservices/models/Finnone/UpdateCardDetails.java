
package vn.com.tpf.microservices.models.Finnone;

import vn.com.tpf.microservices.models.Finnone.cas.MoneyType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;



/**
 * <p>Java class for updateCardDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="updateCardDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="primaryCard" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType"/>
 *         &lt;element name="embossedName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="secondLineEmbossing" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="cardLimit" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="networkGateway" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="applicantName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="addOnCard" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType" minOccurs="0"/>
 *         &lt;element name="assignedCardNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="companionCard" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType" minOccurs="0"/>
 *         &lt;element name="party" type="{http://schema.base.ws.pro.finnone.nucleus.com}LongType" minOccurs="0"/>
 *         &lt;element name="cardStatus" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="previousCardId" type="{http://schema.base.ws.pro.finnone.nucleus.com}LongType" minOccurs="0"/>
 *         &lt;element name="ccType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="creditCardPartner" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="chipCardRequired" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType" minOccurs="0"/>
 *         &lt;element name="embossingSignatureRequired" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType" minOccurs="0"/>
 *         &lt;element name="printPhotographOnCard" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType" minOccurs="0"/>
 *         &lt;element name="language" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="plasticType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="jpNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}LongType" minOccurs="0"/>
 *         &lt;element name="expiryMonth" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="expiryYear" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="jetPrivilegeMembershipTier" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="balanceTransferApplied" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType" minOccurs="0"/>
 *         &lt;element name="balanceTransferAppliedBank" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="transferfromCardNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="balanceTransferEmbossedName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="balanceTransferCardExpiryYear" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="balanceTransferCardExpiryMonth" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="balanceTranserAmount" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="autoDebitApplied" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType" minOccurs="0"/>
 *         &lt;element name="autoDebitAppliedBank" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="autoDebitAmount" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="autoDebitAppliedBankBranch" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="autoDebitAccNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="autoDebitPercentage" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="moneyTransferApplied" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType" minOccurs="0"/>
 *         &lt;element name="bank" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="bankBranch" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="transerFromAccNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="transferAmount" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="billingDueDay" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="customerNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="deleteFlag" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateCardDetails", propOrder = {
    "primaryCard",
    "embossedName",
    "secondLineEmbossing",
    "cardLimit",
    "networkGateway",
    "applicantName",
    "addOnCard",
    "assignedCardNumber",
    "companionCard",
    "party",
    "cardStatus",
    "previousCardId",
    "ccType",
    "creditCardPartner",
    "chipCardRequired",
    "embossingSignatureRequired",
    "printPhotographOnCard",
    "language",
    "plasticType",
    "jpNumber",
    "expiryMonth",
    "expiryYear",
    "jetPrivilegeMembershipTier",
    "balanceTransferApplied",
    "balanceTransferAppliedBank",
    "transferfromCardNumber",
    "balanceTransferEmbossedName",
    "balanceTransferCardExpiryYear",
    "balanceTransferCardExpiryMonth",
    "balanceTranserAmount",
    "autoDebitApplied",
    "autoDebitAppliedBank",
    "autoDebitAmount",
    "autoDebitAppliedBankBranch",
    "autoDebitAccNumber",
    "autoDebitPercentage",
    "moneyTransferApplied",
    "bank",
    "bankBranch",
    "transerFromAccNumber",
    "transferAmount",
    "billingDueDay",
    "customerNumber",
    "deleteFlag"
})
public class UpdateCardDetails {

    protected boolean primaryCard;
    @XmlElement(required = true)
    protected String embossedName;
    protected String secondLineEmbossing;
    protected MoneyType cardLimit;
    protected String networkGateway;
    @XmlElement(required = true)
    protected String applicantName;
    protected Boolean addOnCard;
    protected String assignedCardNumber;
    protected Boolean companionCard;
    protected Long party;
    protected String cardStatus;
    protected Long previousCardId;
    protected String ccType;
    protected String creditCardPartner;
    protected Boolean chipCardRequired;
    protected Boolean embossingSignatureRequired;
    protected Boolean printPhotographOnCard;
    protected String language;
    protected String plasticType;
    protected Long jpNumber;
    protected String expiryMonth;
    protected String expiryYear;
    protected String jetPrivilegeMembershipTier;
    protected Boolean balanceTransferApplied;
    protected String balanceTransferAppliedBank;
    protected String transferfromCardNumber;
    protected String balanceTransferEmbossedName;
    protected String balanceTransferCardExpiryYear;
    protected String balanceTransferCardExpiryMonth;
    protected MoneyType balanceTranserAmount;
    protected Boolean autoDebitApplied;
    protected String autoDebitAppliedBank;
    protected String autoDebitAmount;
    protected String autoDebitAppliedBankBranch;
    protected String autoDebitAccNumber;
    protected Integer autoDebitPercentage;
    protected Boolean moneyTransferApplied;
    protected String bank;
    protected String bankBranch;
    protected String transerFromAccNumber;
    protected MoneyType transferAmount;
    protected String billingDueDay;
    protected String customerNumber;
    protected Boolean deleteFlag;

    /**
     * Gets the value of the primaryCard property.
     * 
     */
    public boolean isPrimaryCard() {
        return primaryCard;
    }

    /**
     * Sets the value of the primaryCard property.
     * 
     */
    public void setPrimaryCard(boolean value) {
        this.primaryCard = value;
    }

    /**
     * Gets the value of the embossedName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmbossedName() {
        return embossedName;
    }

    /**
     * Sets the value of the embossedName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmbossedName(String value) {
        this.embossedName = value;
    }

    /**
     * Gets the value of the secondLineEmbossing property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSecondLineEmbossing() {
        return secondLineEmbossing;
    }

    /**
     * Sets the value of the secondLineEmbossing property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecondLineEmbossing(String value) {
        this.secondLineEmbossing = value;
    }

    /**
     * Gets the value of the cardLimit property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getCardLimit() {
        return cardLimit;
    }

    /**
     * Sets the value of the cardLimit property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setCardLimit(MoneyType value) {
        this.cardLimit = value;
    }

    /**
     * Gets the value of the networkGateway property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNetworkGateway() {
        return networkGateway;
    }

    /**
     * Sets the value of the networkGateway property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNetworkGateway(String value) {
        this.networkGateway = value;
    }

    /**
     * Gets the value of the applicantName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApplicantName() {
        return applicantName;
    }

    /**
     * Sets the value of the applicantName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApplicantName(String value) {
        this.applicantName = value;
    }

    /**
     * Gets the value of the addOnCard property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAddOnCard() {
        return addOnCard;
    }

    /**
     * Sets the value of the addOnCard property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAddOnCard(Boolean value) {
        this.addOnCard = value;
    }

    /**
     * Gets the value of the assignedCardNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssignedCardNumber() {
        return assignedCardNumber;
    }

    /**
     * Sets the value of the assignedCardNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssignedCardNumber(String value) {
        this.assignedCardNumber = value;
    }

    /**
     * Gets the value of the companionCard property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCompanionCard() {
        return companionCard;
    }

    /**
     * Sets the value of the companionCard property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCompanionCard(Boolean value) {
        this.companionCard = value;
    }

    /**
     * Gets the value of the party property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getParty() {
        return party;
    }

    /**
     * Sets the value of the party property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setParty(Long value) {
        this.party = value;
    }

    /**
     * Gets the value of the cardStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCardStatus() {
        return cardStatus;
    }

    /**
     * Sets the value of the cardStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCardStatus(String value) {
        this.cardStatus = value;
    }

    /**
     * Gets the value of the previousCardId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getPreviousCardId() {
        return previousCardId;
    }

    /**
     * Sets the value of the previousCardId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setPreviousCardId(Long value) {
        this.previousCardId = value;
    }

    /**
     * Gets the value of the ccType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCcType() {
        return ccType;
    }

    /**
     * Sets the value of the ccType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCcType(String value) {
        this.ccType = value;
    }

    /**
     * Gets the value of the creditCardPartner property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreditCardPartner() {
        return creditCardPartner;
    }

    /**
     * Sets the value of the creditCardPartner property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreditCardPartner(String value) {
        this.creditCardPartner = value;
    }

    /**
     * Gets the value of the chipCardRequired property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isChipCardRequired() {
        return chipCardRequired;
    }

    /**
     * Sets the value of the chipCardRequired property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setChipCardRequired(Boolean value) {
        this.chipCardRequired = value;
    }

    /**
     * Gets the value of the embossingSignatureRequired property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEmbossingSignatureRequired() {
        return embossingSignatureRequired;
    }

    /**
     * Sets the value of the embossingSignatureRequired property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEmbossingSignatureRequired(Boolean value) {
        this.embossingSignatureRequired = value;
    }

    /**
     * Gets the value of the printPhotographOnCard property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPrintPhotographOnCard() {
        return printPhotographOnCard;
    }

    /**
     * Sets the value of the printPhotographOnCard property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPrintPhotographOnCard(Boolean value) {
        this.printPhotographOnCard = value;
    }

    /**
     * Gets the value of the language property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Sets the value of the language property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLanguage(String value) {
        this.language = value;
    }

    /**
     * Gets the value of the plasticType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlasticType() {
        return plasticType;
    }

    /**
     * Sets the value of the plasticType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlasticType(String value) {
        this.plasticType = value;
    }

    /**
     * Gets the value of the jpNumber property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getJpNumber() {
        return jpNumber;
    }

    /**
     * Sets the value of the jpNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setJpNumber(Long value) {
        this.jpNumber = value;
    }

    /**
     * Gets the value of the expiryMonth property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpiryMonth() {
        return expiryMonth;
    }

    /**
     * Sets the value of the expiryMonth property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpiryMonth(String value) {
        this.expiryMonth = value;
    }

    /**
     * Gets the value of the expiryYear property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpiryYear() {
        return expiryYear;
    }

    /**
     * Sets the value of the expiryYear property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpiryYear(String value) {
        this.expiryYear = value;
    }

    /**
     * Gets the value of the jetPrivilegeMembershipTier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJetPrivilegeMembershipTier() {
        return jetPrivilegeMembershipTier;
    }

    /**
     * Sets the value of the jetPrivilegeMembershipTier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJetPrivilegeMembershipTier(String value) {
        this.jetPrivilegeMembershipTier = value;
    }

    /**
     * Gets the value of the balanceTransferApplied property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isBalanceTransferApplied() {
        return balanceTransferApplied;
    }

    /**
     * Sets the value of the balanceTransferApplied property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setBalanceTransferApplied(Boolean value) {
        this.balanceTransferApplied = value;
    }

    /**
     * Gets the value of the balanceTransferAppliedBank property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBalanceTransferAppliedBank() {
        return balanceTransferAppliedBank;
    }

    /**
     * Sets the value of the balanceTransferAppliedBank property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBalanceTransferAppliedBank(String value) {
        this.balanceTransferAppliedBank = value;
    }

    /**
     * Gets the value of the transferfromCardNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransferfromCardNumber() {
        return transferfromCardNumber;
    }

    /**
     * Sets the value of the transferfromCardNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransferfromCardNumber(String value) {
        this.transferfromCardNumber = value;
    }

    /**
     * Gets the value of the balanceTransferEmbossedName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBalanceTransferEmbossedName() {
        return balanceTransferEmbossedName;
    }

    /**
     * Sets the value of the balanceTransferEmbossedName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBalanceTransferEmbossedName(String value) {
        this.balanceTransferEmbossedName = value;
    }

    /**
     * Gets the value of the balanceTransferCardExpiryYear property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBalanceTransferCardExpiryYear() {
        return balanceTransferCardExpiryYear;
    }

    /**
     * Sets the value of the balanceTransferCardExpiryYear property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBalanceTransferCardExpiryYear(String value) {
        this.balanceTransferCardExpiryYear = value;
    }

    /**
     * Gets the value of the balanceTransferCardExpiryMonth property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBalanceTransferCardExpiryMonth() {
        return balanceTransferCardExpiryMonth;
    }

    /**
     * Sets the value of the balanceTransferCardExpiryMonth property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBalanceTransferCardExpiryMonth(String value) {
        this.balanceTransferCardExpiryMonth = value;
    }

    /**
     * Gets the value of the balanceTranserAmount property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getBalanceTranserAmount() {
        return balanceTranserAmount;
    }

    /**
     * Sets the value of the balanceTranserAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setBalanceTranserAmount(MoneyType value) {
        this.balanceTranserAmount = value;
    }

    /**
     * Gets the value of the autoDebitApplied property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAutoDebitApplied() {
        return autoDebitApplied;
    }

    /**
     * Sets the value of the autoDebitApplied property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAutoDebitApplied(Boolean value) {
        this.autoDebitApplied = value;
    }

    /**
     * Gets the value of the autoDebitAppliedBank property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAutoDebitAppliedBank() {
        return autoDebitAppliedBank;
    }

    /**
     * Sets the value of the autoDebitAppliedBank property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAutoDebitAppliedBank(String value) {
        this.autoDebitAppliedBank = value;
    }

    /**
     * Gets the value of the autoDebitAmount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAutoDebitAmount() {
        return autoDebitAmount;
    }

    /**
     * Sets the value of the autoDebitAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAutoDebitAmount(String value) {
        this.autoDebitAmount = value;
    }

    /**
     * Gets the value of the autoDebitAppliedBankBranch property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAutoDebitAppliedBankBranch() {
        return autoDebitAppliedBankBranch;
    }

    /**
     * Sets the value of the autoDebitAppliedBankBranch property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAutoDebitAppliedBankBranch(String value) {
        this.autoDebitAppliedBankBranch = value;
    }

    /**
     * Gets the value of the autoDebitAccNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAutoDebitAccNumber() {
        return autoDebitAccNumber;
    }

    /**
     * Sets the value of the autoDebitAccNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAutoDebitAccNumber(String value) {
        this.autoDebitAccNumber = value;
    }

    /**
     * Gets the value of the autoDebitPercentage property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAutoDebitPercentage() {
        return autoDebitPercentage;
    }

    /**
     * Sets the value of the autoDebitPercentage property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAutoDebitPercentage(Integer value) {
        this.autoDebitPercentage = value;
    }

    /**
     * Gets the value of the moneyTransferApplied property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isMoneyTransferApplied() {
        return moneyTransferApplied;
    }

    /**
     * Sets the value of the moneyTransferApplied property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setMoneyTransferApplied(Boolean value) {
        this.moneyTransferApplied = value;
    }

    /**
     * Gets the value of the bank property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBank() {
        return bank;
    }

    /**
     * Sets the value of the bank property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBank(String value) {
        this.bank = value;
    }

    /**
     * Gets the value of the bankBranch property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBankBranch() {
        return bankBranch;
    }

    /**
     * Sets the value of the bankBranch property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBankBranch(String value) {
        this.bankBranch = value;
    }

    /**
     * Gets the value of the transerFromAccNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTranserFromAccNumber() {
        return transerFromAccNumber;
    }

    /**
     * Sets the value of the transerFromAccNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTranserFromAccNumber(String value) {
        this.transerFromAccNumber = value;
    }

    /**
     * Gets the value of the transferAmount property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getTransferAmount() {
        return transferAmount;
    }

    /**
     * Sets the value of the transferAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setTransferAmount(MoneyType value) {
        this.transferAmount = value;
    }

    /**
     * Gets the value of the billingDueDay property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillingDueDay() {
        return billingDueDay;
    }

    /**
     * Sets the value of the billingDueDay property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillingDueDay(String value) {
        this.billingDueDay = value;
    }

    /**
     * Gets the value of the customerNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerNumber() {
        return customerNumber;
    }

    /**
     * Sets the value of the customerNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerNumber(String value) {
        this.customerNumber = value;
    }

    /**
     * Gets the value of the deleteFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDeleteFlag() {
        return deleteFlag;
    }

    /**
     * Sets the value of the deleteFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDeleteFlag(Boolean value) {
        this.deleteFlag = value;
    }

}
