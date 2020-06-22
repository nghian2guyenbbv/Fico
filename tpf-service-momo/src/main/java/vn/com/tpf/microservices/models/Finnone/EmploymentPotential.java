
package vn.com.tpf.microservices.models.Finnone;

import vn.com.tpf.microservices.models.Finnone.cas.MoneyType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;



/**
 * <p>Java class for employmentPotential complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="employmentPotential">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="employPotential" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="monthlyIncome" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="monthlyExpenditure" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="amountRepayment" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="partTimeJob" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="settleAbroad" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "employmentPotential", propOrder = {
    "employPotential",
    "monthlyIncome",
    "monthlyExpenditure",
    "amountRepayment",
    "partTimeJob",
    "settleAbroad"
})
public class EmploymentPotential {

    protected String employPotential;
    protected MoneyType monthlyIncome;
    protected MoneyType monthlyExpenditure;
    protected MoneyType amountRepayment;
    protected String partTimeJob;
    protected String settleAbroad;

    /**
     * Gets the value of the employPotential property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmployPotential() {
        return employPotential;
    }

    /**
     * Sets the value of the employPotential property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmployPotential(String value) {
        this.employPotential = value;
    }

    /**
     * Gets the value of the monthlyIncome property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getMonthlyIncome() {
        return monthlyIncome;
    }

    /**
     * Sets the value of the monthlyIncome property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setMonthlyIncome(MoneyType value) {
        this.monthlyIncome = value;
    }

    /**
     * Gets the value of the monthlyExpenditure property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getMonthlyExpenditure() {
        return monthlyExpenditure;
    }

    /**
     * Sets the value of the monthlyExpenditure property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setMonthlyExpenditure(MoneyType value) {
        this.monthlyExpenditure = value;
    }

    /**
     * Gets the value of the amountRepayment property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getAmountRepayment() {
        return amountRepayment;
    }

    /**
     * Sets the value of the amountRepayment property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setAmountRepayment(MoneyType value) {
        this.amountRepayment = value;
    }

    /**
     * Gets the value of the partTimeJob property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPartTimeJob() {
        return partTimeJob;
    }

    /**
     * Sets the value of the partTimeJob property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPartTimeJob(String value) {
        this.partTimeJob = value;
    }

    /**
     * Gets the value of the settleAbroad property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSettleAbroad() {
        return settleAbroad;
    }

    /**
     * Sets the value of the settleAbroad property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSettleAbroad(String value) {
        this.settleAbroad = value;
    }

}
