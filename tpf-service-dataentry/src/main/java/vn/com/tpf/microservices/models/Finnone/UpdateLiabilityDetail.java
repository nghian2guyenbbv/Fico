
package vn.com.tpf.microservices.models.Finnone;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

import vn.com.tpf.microservices.models.Finnone.cas.MoneyType;


/**
 * <p>Java class for updateLiabilityDetail complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="updateLiabilityDetail">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="liabilityType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="loanType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="institutionName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="currentBalance" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType"/>
 *         &lt;element name="emiStartDate" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType"/>
 *         &lt;element name="installmentFrequency" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="installmentAmount" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType"/>
 *         &lt;element name="salaryFlag" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType" minOccurs="0"/>
 *         &lt;element name="jointLiabilty" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType" minOccurs="0"/>
 *         &lt;element name="accountNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="sanctionedAmount" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="noOfDefaults" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="considerForObligation" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType" minOccurs="0"/>
 *         &lt;element name="isBalanceTransfer" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType" minOccurs="0"/>
 *         &lt;element name="rateOfInterest" type="{http://schema.base.ws.pro.finnone.nucleus.com}DecimalType" minOccurs="0"/>
 *         &lt;element name="endUseOfLoan" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="closureAmount" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="securityGiven" type="{http://schema.base.ws.pro.finnone.nucleus.com}StringType" minOccurs="0"/>
 *         &lt;element name="description" type="{http://schema.base.ws.pro.finnone.nucleus.com}StringType" minOccurs="0"/>
 *         &lt;element name="bankType" type="{http://schema.base.ws.pro.finnone.nucleus.com}StringType" minOccurs="0"/>
 *         &lt;element name="referenceHostSystem" type="{http://schema.base.ws.pro.finnone.nucleus.com}StringType" minOccurs="0"/>
 *         &lt;element name="deleteFlag" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType" minOccurs="0"/>
 *         &lt;element name="regionalData" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}regionalData" minOccurs="0"/>
 *         &lt;element name="dynamicFormDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}dynamicFormDetails" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateLiabilityDetail", propOrder = {
    "liabilityType",
    "loanType",
    "institutionName",
    "currentBalance",
    "emiStartDate",
    "installmentFrequency",
    "installmentAmount",
    "salaryFlag",
    "jointLiabilty",
    "accountNumber",
    "sanctionedAmount",
    "noOfDefaults",
    "considerForObligation",
    "isBalanceTransfer",
    "rateOfInterest",
    "endUseOfLoan",
    "closureAmount",
    "securityGiven",
    "description",
    "bankType",
    "referenceHostSystem",
    "deleteFlag",
    "regionalData",
    "dynamicFormDetails"
})
public class UpdateLiabilityDetail {

    @XmlElement(required = true)
    protected String liabilityType;
    @XmlElement(required = true)
    protected String loanType;
    @XmlElement(required = true)
    protected String institutionName;
    @XmlElement(required = true)
    protected MoneyType currentBalance;
    @XmlElement(required = true)
    protected XMLGregorianCalendar emiStartDate;
    @XmlElement(required = true)
    protected String installmentFrequency;
    @XmlElement(required = true)
    protected MoneyType installmentAmount;
    protected Boolean salaryFlag;
    protected Boolean jointLiabilty;
    protected String accountNumber;
    protected MoneyType sanctionedAmount;
    protected Integer noOfDefaults;
    protected Boolean considerForObligation;
    protected Boolean isBalanceTransfer;
    protected BigDecimal rateOfInterest;
    protected String endUseOfLoan;
    protected MoneyType closureAmount;
    protected String securityGiven;
    protected String description;
    protected String bankType;
    protected String referenceHostSystem;
    protected Boolean deleteFlag;
    protected RegionalData regionalData;
    protected List<DynamicFormDetails> dynamicFormDetails;

    /**
     * Gets the value of the liabilityType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLiabilityType() {
        return liabilityType;
    }

    /**
     * Sets the value of the liabilityType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLiabilityType(String value) {
        this.liabilityType = value;
    }

    /**
     * Gets the value of the loanType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLoanType() {
        return loanType;
    }

    /**
     * Sets the value of the loanType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLoanType(String value) {
        this.loanType = value;
    }

    /**
     * Gets the value of the institutionName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstitutionName() {
        return institutionName;
    }

    /**
     * Sets the value of the institutionName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstitutionName(String value) {
        this.institutionName = value;
    }

    /**
     * Gets the value of the currentBalance property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getCurrentBalance() {
        return currentBalance;
    }

    /**
     * Sets the value of the currentBalance property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setCurrentBalance(MoneyType value) {
        this.currentBalance = value;
    }

    /**
     * Gets the value of the emiStartDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getEmiStartDate() {
        return emiStartDate;
    }

    /**
     * Sets the value of the emiStartDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setEmiStartDate(XMLGregorianCalendar value) {
        this.emiStartDate = value;
    }

    /**
     * Gets the value of the installmentFrequency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstallmentFrequency() {
        return installmentFrequency;
    }

    /**
     * Sets the value of the installmentFrequency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstallmentFrequency(String value) {
        this.installmentFrequency = value;
    }

    /**
     * Gets the value of the installmentAmount property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getInstallmentAmount() {
        return installmentAmount;
    }

    /**
     * Sets the value of the installmentAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setInstallmentAmount(MoneyType value) {
        this.installmentAmount = value;
    }

    /**
     * Gets the value of the salaryFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSalaryFlag() {
        return salaryFlag;
    }

    /**
     * Sets the value of the salaryFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSalaryFlag(Boolean value) {
        this.salaryFlag = value;
    }

    /**
     * Gets the value of the jointLiabilty property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isJointLiabilty() {
        return jointLiabilty;
    }

    /**
     * Sets the value of the jointLiabilty property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setJointLiabilty(Boolean value) {
        this.jointLiabilty = value;
    }

    /**
     * Gets the value of the accountNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * Sets the value of the accountNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountNumber(String value) {
        this.accountNumber = value;
    }

    /**
     * Gets the value of the sanctionedAmount property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getSanctionedAmount() {
        return sanctionedAmount;
    }

    /**
     * Sets the value of the sanctionedAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setSanctionedAmount(MoneyType value) {
        this.sanctionedAmount = value;
    }

    /**
     * Gets the value of the noOfDefaults property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNoOfDefaults() {
        return noOfDefaults;
    }

    /**
     * Sets the value of the noOfDefaults property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNoOfDefaults(Integer value) {
        this.noOfDefaults = value;
    }

    /**
     * Gets the value of the considerForObligation property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isConsiderForObligation() {
        return considerForObligation;
    }

    /**
     * Sets the value of the considerForObligation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setConsiderForObligation(Boolean value) {
        this.considerForObligation = value;
    }

    /**
     * Gets the value of the isBalanceTransfer property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsBalanceTransfer() {
        return isBalanceTransfer;
    }

    /**
     * Sets the value of the isBalanceTransfer property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsBalanceTransfer(Boolean value) {
        this.isBalanceTransfer = value;
    }

    /**
     * Gets the value of the rateOfInterest property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRateOfInterest() {
        return rateOfInterest;
    }

    /**
     * Sets the value of the rateOfInterest property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRateOfInterest(BigDecimal value) {
        this.rateOfInterest = value;
    }

    /**
     * Gets the value of the endUseOfLoan property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEndUseOfLoan() {
        return endUseOfLoan;
    }

    /**
     * Sets the value of the endUseOfLoan property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndUseOfLoan(String value) {
        this.endUseOfLoan = value;
    }

    /**
     * Gets the value of the closureAmount property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getClosureAmount() {
        return closureAmount;
    }

    /**
     * Sets the value of the closureAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setClosureAmount(MoneyType value) {
        this.closureAmount = value;
    }

    /**
     * Gets the value of the securityGiven property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSecurityGiven() {
        return securityGiven;
    }

    /**
     * Sets the value of the securityGiven property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecurityGiven(String value) {
        this.securityGiven = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the bankType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBankType() {
        return bankType;
    }

    /**
     * Sets the value of the bankType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBankType(String value) {
        this.bankType = value;
    }

    /**
     * Gets the value of the referenceHostSystem property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReferenceHostSystem() {
        return referenceHostSystem;
    }

    /**
     * Sets the value of the referenceHostSystem property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReferenceHostSystem(String value) {
        this.referenceHostSystem = value;
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

    /**
     * Gets the value of the regionalData property.
     * 
     * @return
     *     possible object is
     *     {@link RegionalData }
     *     
     */
    public RegionalData getRegionalData() {
        return regionalData;
    }

    /**
     * Sets the value of the regionalData property.
     * 
     * @param value
     *     allowed object is
     *     {@link RegionalData }
     *     
     */
    public void setRegionalData(RegionalData value) {
        this.regionalData = value;
    }

    /**
     * Gets the value of the dynamicFormDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dynamicFormDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDynamicFormDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DynamicFormDetails }
     * 
     * 
     */
    public List<DynamicFormDetails> getDynamicFormDetails() {
        if (dynamicFormDetails == null) {
            dynamicFormDetails = new ArrayList<DynamicFormDetails>();
        }
        return this.dynamicFormDetails;
    }

}
