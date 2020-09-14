
package vn.com.tpf.microservices.models.Finnone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import vn.com.tpf.microservices.models.Finnone.cas.MoneyType;


/**
 * <p>Java class for otherIncomeDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="otherIncomeDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="incomeExpense" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="currentYearIncome" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType"/>
 *         &lt;element name="previousYearIncome" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "otherIncomeDetails", propOrder = {
    "incomeExpense",
    "currentYearIncome",
    "previousYearIncome"
})
public class OtherIncomeDetails {

    @XmlElement(required = true)
    protected String incomeExpense;
    @XmlElement(required = true)
    protected MoneyType currentYearIncome;
    @XmlElement(required = true)
    protected MoneyType previousYearIncome;

    /**
     * Gets the value of the incomeExpense property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIncomeExpense() {
        return incomeExpense;
    }

    /**
     * Sets the value of the incomeExpense property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIncomeExpense(String value) {
        this.incomeExpense = value;
    }

    /**
     * Gets the value of the currentYearIncome property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getCurrentYearIncome() {
        return currentYearIncome;
    }

    /**
     * Sets the value of the currentYearIncome property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setCurrentYearIncome(MoneyType value) {
        this.currentYearIncome = value;
    }

    /**
     * Gets the value of the previousYearIncome property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getPreviousYearIncome() {
        return previousYearIncome;
    }

    /**
     * Sets the value of the previousYearIncome property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setPreviousYearIncome(MoneyType value) {
        this.previousYearIncome = value;
    }

}
