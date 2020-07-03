
package vn.com.tpf.microservices.models.Finnone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for updateBankDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="updateBankDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="bankCode" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="branchCode" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="accountType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="accountNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="accountOpeningYear" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType"/>
 *         &lt;element name="bankDetailType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="bankDetailFile" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}bankDetailFile" minOccurs="0"/>
 *         &lt;element name="regionalData" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}regionalData" minOccurs="0"/>
 *         &lt;element name="deleteFlag" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType" minOccurs="0"/>
 *         &lt;element name="isSalaryAccountType" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType" minOccurs="0"/>
 *         &lt;element name="isRepaymentAccount" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType" minOccurs="0"/>
 *         &lt;element name="ifscCode" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateBankDetails", propOrder = {
    "bankCode",
    "branchCode",
    "accountType",
    "accountNumber",
    "accountOpeningYear",
    "bankDetailType",
    "bankDetailFile",
    "regionalData",
    "deleteFlag",
    "isSalaryAccountType",
    "isRepaymentAccount",
    "ifscCode"
})
public class UpdateBankDetails {

    @XmlElement(required = true)
    protected String bankCode;
    protected String branchCode;
    @XmlElement(required = true)
    protected String accountType;
    @XmlElement(required = true)
    protected String accountNumber;
    protected int accountOpeningYear;
    @XmlElement(required = true)
    protected String bankDetailType;
    protected BankDetailFile bankDetailFile;
    protected RegionalData regionalData;
    protected Boolean deleteFlag;
    protected Boolean isSalaryAccountType;
    protected Boolean isRepaymentAccount;
    @XmlElement(required = true)
    protected String ifscCode;

    /**
     * Gets the value of the bankCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBankCode() {
        return bankCode;
    }

    /**
     * Sets the value of the bankCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBankCode(String value) {
        this.bankCode = value;
    }

    /**
     * Gets the value of the branchCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBranchCode() {
        return branchCode;
    }

    /**
     * Sets the value of the branchCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBranchCode(String value) {
        this.branchCode = value;
    }

    /**
     * Gets the value of the accountType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountType() {
        return accountType;
    }

    /**
     * Sets the value of the accountType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountType(String value) {
        this.accountType = value;
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
     * Gets the value of the accountOpeningYear property.
     * 
     */
    public int getAccountOpeningYear() {
        return accountOpeningYear;
    }

    /**
     * Sets the value of the accountOpeningYear property.
     * 
     */
    public void setAccountOpeningYear(int value) {
        this.accountOpeningYear = value;
    }

    /**
     * Gets the value of the bankDetailType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBankDetailType() {
        return bankDetailType;
    }

    /**
     * Sets the value of the bankDetailType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBankDetailType(String value) {
        this.bankDetailType = value;
    }

    /**
     * Gets the value of the bankDetailFile property.
     * 
     * @return
     *     possible object is
     *     {@link BankDetailFile }
     *     
     */
    public BankDetailFile getBankDetailFile() {
        return bankDetailFile;
    }

    /**
     * Sets the value of the bankDetailFile property.
     * 
     * @param value
     *     allowed object is
     *     {@link BankDetailFile }
     *     
     */
    public void setBankDetailFile(BankDetailFile value) {
        this.bankDetailFile = value;
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
     * Gets the value of the isSalaryAccountType property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsSalaryAccountType() {
        return isSalaryAccountType;
    }

    /**
     * Sets the value of the isSalaryAccountType property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsSalaryAccountType(Boolean value) {
        this.isSalaryAccountType = value;
    }

    /**
     * Gets the value of the isRepaymentAccount property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsRepaymentAccount() {
        return isRepaymentAccount;
    }

    /**
     * Sets the value of the isRepaymentAccount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsRepaymentAccount(Boolean value) {
        this.isRepaymentAccount = value;
    }

    /**
     * Gets the value of the ifscCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIfscCode() {
        return ifscCode;
    }

    /**
     * Sets the value of the ifscCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIfscCode(String value) {
        this.ifscCode = value;
    }

}
