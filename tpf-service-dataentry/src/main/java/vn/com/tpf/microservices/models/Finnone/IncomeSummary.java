
package vn.com.tpf.microservices.models.Finnone;

import vn.com.tpf.microservices.models.Finnone.cas.MoneyType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;



/**
 * <p>Java class for incomeSummary complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="incomeSummary">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="monthlyNetIncome" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="annualNetIncome" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="annualGrossIncome" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="otherIncome" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "incomeSummary", propOrder = {
    "monthlyNetIncome",
    "annualNetIncome",
    "annualGrossIncome",
    "otherIncome"
})
public class IncomeSummary {

    protected MoneyType monthlyNetIncome;
    protected MoneyType annualNetIncome;
    protected MoneyType annualGrossIncome;
    protected MoneyType otherIncome;

    /**
     * Gets the value of the monthlyNetIncome property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getMonthlyNetIncome() {
        return monthlyNetIncome;
    }

    /**
     * Sets the value of the monthlyNetIncome property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setMonthlyNetIncome(MoneyType value) {
        this.monthlyNetIncome = value;
    }

    /**
     * Gets the value of the annualNetIncome property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getAnnualNetIncome() {
        return annualNetIncome;
    }

    /**
     * Sets the value of the annualNetIncome property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setAnnualNetIncome(MoneyType value) {
        this.annualNetIncome = value;
    }

    /**
     * Gets the value of the annualGrossIncome property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getAnnualGrossIncome() {
        return annualGrossIncome;
    }

    /**
     * Sets the value of the annualGrossIncome property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setAnnualGrossIncome(MoneyType value) {
        this.annualGrossIncome = value;
    }

    /**
     * Gets the value of the otherIncome property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getOtherIncome() {
        return otherIncome;
    }

    /**
     * Sets the value of the otherIncome property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setOtherIncome(MoneyType value) {
        this.otherIncome = value;
    }

}
