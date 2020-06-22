
package vn.com.tpf.microservices.models.Finnone;

import vn.com.tpf.microservices.models.Finnone.cas.MoneyType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;



/**
 * <p>Java class for loanParameterDetail complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="loanParameterDetail">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="advanceEmiDeducted" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType"/>
 *         &lt;element name="disbursalTo" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="disbursalType" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="downPaymentPaidDirectly" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType"/>
 *         &lt;element name="dueDay" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="emi" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="installmentMode" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="installmentType" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="insuranceFunded" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType"/>
 *         &lt;element name="interestStartDate" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType" minOccurs="0"/>
 *         &lt;element name="moratorium" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="moratoriumInDays" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="moratoriumType" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="moratoriumInstallments" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="interestChargedInMoratorium" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="numberOfDisbursal" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="numberOfAdvanceInstallments" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="paymentFrequency" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="repayScheduleBasedOn" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="sanctionedAmount" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="tenure" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="interestRate" type="{http://schema.base.ws.pro.finnone.nucleus.com}DecimalType" minOccurs="0"/>
 *         &lt;element name="interestChargeMethod" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="interestRateType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="insuranceFunding" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}insuranceFunding" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="regionalData" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}regionalData" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "loanParameterDetail", propOrder = {
    "advanceEmiDeducted",
    "disbursalTo",
    "disbursalType",
    "downPaymentPaidDirectly",
    "dueDay",
    "emi",
    "installmentMode",
    "installmentType",
    "insuranceFunded",
    "interestStartDate",
    "moratorium",
    "moratoriumInDays",
    "moratoriumType",
    "moratoriumInstallments",
    "interestChargedInMoratorium",
    "numberOfDisbursal",
    "numberOfAdvanceInstallments",
    "paymentFrequency",
    "repayScheduleBasedOn",
    "sanctionedAmount",
    "tenure",
    "interestRate",
    "interestChargeMethod",
    "interestRateType",
    "insuranceFunding",
    "regionalData"
})
public class LoanParameterDetail {

    protected int advanceEmiDeducted;
    protected String disbursalTo;
    protected Integer disbursalType;
    protected int downPaymentPaidDirectly;
    protected Integer dueDay;
    protected MoneyType emi;
    protected Integer installmentMode;
    protected Integer installmentType;
    protected int insuranceFunded;
    protected XMLGregorianCalendar interestStartDate;
    protected Integer moratorium;
    protected Integer moratoriumInDays;
    protected Integer moratoriumType;
    protected Integer moratoriumInstallments;
    protected Integer interestChargedInMoratorium;
    protected Integer numberOfDisbursal;
    protected Integer numberOfAdvanceInstallments;
    protected Integer paymentFrequency;
    protected String repayScheduleBasedOn;
    protected MoneyType sanctionedAmount;
    protected Integer tenure;
    protected BigDecimal interestRate;
    protected String interestChargeMethod;
    protected String interestRateType;
    protected List<InsuranceFunding> insuranceFunding;
    protected RegionalData regionalData;

    /**
     * Gets the value of the advanceEmiDeducted property.
     * 
     */
    public int getAdvanceEmiDeducted() {
        return advanceEmiDeducted;
    }

    /**
     * Sets the value of the advanceEmiDeducted property.
     * 
     */
    public void setAdvanceEmiDeducted(int value) {
        this.advanceEmiDeducted = value;
    }

    /**
     * Gets the value of the disbursalTo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisbursalTo() {
        return disbursalTo;
    }

    /**
     * Sets the value of the disbursalTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisbursalTo(String value) {
        this.disbursalTo = value;
    }

    /**
     * Gets the value of the disbursalType property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getDisbursalType() {
        return disbursalType;
    }

    /**
     * Sets the value of the disbursalType property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setDisbursalType(Integer value) {
        this.disbursalType = value;
    }

    /**
     * Gets the value of the downPaymentPaidDirectly property.
     * 
     */
    public int getDownPaymentPaidDirectly() {
        return downPaymentPaidDirectly;
    }

    /**
     * Sets the value of the downPaymentPaidDirectly property.
     * 
     */
    public void setDownPaymentPaidDirectly(int value) {
        this.downPaymentPaidDirectly = value;
    }

    /**
     * Gets the value of the dueDay property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getDueDay() {
        return dueDay;
    }

    /**
     * Sets the value of the dueDay property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setDueDay(Integer value) {
        this.dueDay = value;
    }

    /**
     * Gets the value of the emi property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getEmi() {
        return emi;
    }

    /**
     * Sets the value of the emi property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setEmi(MoneyType value) {
        this.emi = value;
    }

    /**
     * Gets the value of the installmentMode property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getInstallmentMode() {
        return installmentMode;
    }

    /**
     * Sets the value of the installmentMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setInstallmentMode(Integer value) {
        this.installmentMode = value;
    }

    /**
     * Gets the value of the installmentType property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getInstallmentType() {
        return installmentType;
    }

    /**
     * Sets the value of the installmentType property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setInstallmentType(Integer value) {
        this.installmentType = value;
    }

    /**
     * Gets the value of the insuranceFunded property.
     * 
     */
    public int getInsuranceFunded() {
        return insuranceFunded;
    }

    /**
     * Sets the value of the insuranceFunded property.
     * 
     */
    public void setInsuranceFunded(int value) {
        this.insuranceFunded = value;
    }

    /**
     * Gets the value of the interestStartDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getInterestStartDate() {
        return interestStartDate;
    }

    /**
     * Sets the value of the interestStartDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setInterestStartDate(XMLGregorianCalendar value) {
        this.interestStartDate = value;
    }

    /**
     * Gets the value of the moratorium property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMoratorium() {
        return moratorium;
    }

    /**
     * Sets the value of the moratorium property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMoratorium(Integer value) {
        this.moratorium = value;
    }

    /**
     * Gets the value of the moratoriumInDays property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMoratoriumInDays() {
        return moratoriumInDays;
    }

    /**
     * Sets the value of the moratoriumInDays property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMoratoriumInDays(Integer value) {
        this.moratoriumInDays = value;
    }

    /**
     * Gets the value of the moratoriumType property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMoratoriumType() {
        return moratoriumType;
    }

    /**
     * Sets the value of the moratoriumType property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMoratoriumType(Integer value) {
        this.moratoriumType = value;
    }

    /**
     * Gets the value of the moratoriumInstallments property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMoratoriumInstallments() {
        return moratoriumInstallments;
    }

    /**
     * Sets the value of the moratoriumInstallments property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMoratoriumInstallments(Integer value) {
        this.moratoriumInstallments = value;
    }

    /**
     * Gets the value of the interestChargedInMoratorium property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getInterestChargedInMoratorium() {
        return interestChargedInMoratorium;
    }

    /**
     * Sets the value of the interestChargedInMoratorium property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setInterestChargedInMoratorium(Integer value) {
        this.interestChargedInMoratorium = value;
    }

    /**
     * Gets the value of the numberOfDisbursal property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumberOfDisbursal() {
        return numberOfDisbursal;
    }

    /**
     * Sets the value of the numberOfDisbursal property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumberOfDisbursal(Integer value) {
        this.numberOfDisbursal = value;
    }

    /**
     * Gets the value of the numberOfAdvanceInstallments property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumberOfAdvanceInstallments() {
        return numberOfAdvanceInstallments;
    }

    /**
     * Sets the value of the numberOfAdvanceInstallments property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumberOfAdvanceInstallments(Integer value) {
        this.numberOfAdvanceInstallments = value;
    }

    /**
     * Gets the value of the paymentFrequency property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPaymentFrequency() {
        return paymentFrequency;
    }

    /**
     * Sets the value of the paymentFrequency property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPaymentFrequency(Integer value) {
        this.paymentFrequency = value;
    }

    /**
     * Gets the value of the repayScheduleBasedOn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRepayScheduleBasedOn() {
        return repayScheduleBasedOn;
    }

    /**
     * Sets the value of the repayScheduleBasedOn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRepayScheduleBasedOn(String value) {
        this.repayScheduleBasedOn = value;
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
     * Gets the value of the tenure property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTenure() {
        return tenure;
    }

    /**
     * Sets the value of the tenure property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTenure(Integer value) {
        this.tenure = value;
    }

    /**
     * Gets the value of the interestRate property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getInterestRate() {
        return interestRate;
    }

    /**
     * Sets the value of the interestRate property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setInterestRate(BigDecimal value) {
        this.interestRate = value;
    }

    /**
     * Gets the value of the interestChargeMethod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInterestChargeMethod() {
        return interestChargeMethod;
    }

    /**
     * Sets the value of the interestChargeMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInterestChargeMethod(String value) {
        this.interestChargeMethod = value;
    }

    /**
     * Gets the value of the interestRateType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInterestRateType() {
        return interestRateType;
    }

    /**
     * Sets the value of the interestRateType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInterestRateType(String value) {
        this.interestRateType = value;
    }

    /**
     * Gets the value of the insuranceFunding property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the insuranceFunding property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInsuranceFunding().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InsuranceFunding }
     * 
     * 
     */
    public List<InsuranceFunding> getInsuranceFunding() {
        if (insuranceFunding == null) {
            insuranceFunding = new ArrayList<InsuranceFunding>();
        }
        return this.insuranceFunding;
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

}
