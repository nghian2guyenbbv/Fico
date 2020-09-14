
package vn.com.tpf.microservices.models.Finnone;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import vn.com.tpf.microservices.models.Finnone.cas.MoneyType;


/**
 * <p>Java class for loanInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="loanInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="endUseOfLoan" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="topUpAmount" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="additionalAmount" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="reason" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="loanAmountRequested" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType"/>
 *         &lt;element name="loanPurpose" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="loanPurposeDesc" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="productCode" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="schemeCode" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="requestedRate" type="{http://schema.base.ws.pro.finnone.nucleus.com}DecimalType" minOccurs="0"/>
 *         &lt;element name="requestedTenure" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="fundedFor" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="chassisApplicationNum" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "loanInfo", propOrder = {
    "endUseOfLoan",
    "topUpAmount",
    "additionalAmount",
    "reason",
    "loanAmountRequested",
    "loanPurpose",
    "loanPurposeDesc",
    "productCode",
    "schemeCode",
    "requestedRate",
    "requestedTenure",
    "fundedFor",
    "chassisApplicationNum"
})
public class LoanInfo {

    protected String endUseOfLoan;
    protected MoneyType topUpAmount;
    protected MoneyType additionalAmount;
    protected String reason;
    @XmlElement(required = true)
    protected MoneyType loanAmountRequested;
    @XmlElement(required = true)
    protected String loanPurpose;
    protected String loanPurposeDesc;
    @XmlElement(required = true)
    protected String productCode;
    @XmlElement(required = true)
    protected String schemeCode;
    protected BigDecimal requestedRate;
    protected Integer requestedTenure;
    protected String fundedFor;
    protected String chassisApplicationNum;

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
     * Gets the value of the topUpAmount property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getTopUpAmount() {
        return topUpAmount;
    }

    /**
     * Sets the value of the topUpAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setTopUpAmount(MoneyType value) {
        this.topUpAmount = value;
    }

    /**
     * Gets the value of the additionalAmount property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getAdditionalAmount() {
        return additionalAmount;
    }

    /**
     * Sets the value of the additionalAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setAdditionalAmount(MoneyType value) {
        this.additionalAmount = value;
    }

    /**
     * Gets the value of the reason property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReason() {
        return reason;
    }

    /**
     * Sets the value of the reason property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReason(String value) {
        this.reason = value;
    }

    /**
     * Gets the value of the loanAmountRequested property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getLoanAmountRequested() {
        return loanAmountRequested;
    }

    /**
     * Sets the value of the loanAmountRequested property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setLoanAmountRequested(MoneyType value) {
        this.loanAmountRequested = value;
    }

    /**
     * Gets the value of the loanPurpose property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLoanPurpose() {
        return loanPurpose;
    }

    /**
     * Sets the value of the loanPurpose property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLoanPurpose(String value) {
        this.loanPurpose = value;
    }

    /**
     * Gets the value of the loanPurposeDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLoanPurposeDesc() {
        return loanPurposeDesc;
    }

    /**
     * Sets the value of the loanPurposeDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLoanPurposeDesc(String value) {
        this.loanPurposeDesc = value;
    }

    /**
     * Gets the value of the productCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductCode() {
        return productCode;
    }

    /**
     * Sets the value of the productCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductCode(String value) {
        this.productCode = value;
    }

    /**
     * Gets the value of the schemeCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSchemeCode() {
        return schemeCode;
    }

    /**
     * Sets the value of the schemeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSchemeCode(String value) {
        this.schemeCode = value;
    }

    /**
     * Gets the value of the requestedRate property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRequestedRate() {
        return requestedRate;
    }

    /**
     * Sets the value of the requestedRate property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRequestedRate(BigDecimal value) {
        this.requestedRate = value;
    }

    /**
     * Gets the value of the requestedTenure property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getRequestedTenure() {
        return requestedTenure;
    }

    /**
     * Sets the value of the requestedTenure property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRequestedTenure(Integer value) {
        this.requestedTenure = value;
    }

    /**
     * Gets the value of the fundedFor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFundedFor() {
        return fundedFor;
    }

    /**
     * Sets the value of the fundedFor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFundedFor(String value) {
        this.fundedFor = value;
    }

    /**
     * Gets the value of the chassisApplicationNum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChassisApplicationNum() {
        return chassisApplicationNum;
    }

    /**
     * Sets the value of the chassisApplicationNum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChassisApplicationNum(String value) {
        this.chassisApplicationNum = value;
    }

}
