
package vn.com.tpf.microservices.models.Finnone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import vn.com.tpf.microservices.models.Finnone.cas.MoneyType;


/**
 * <p>Java class for updOtherIncomeDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="updOtherIncomeDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="incomeExpense" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="currentYearIncome" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType"/>
 *         &lt;element name="previousYearIncome" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType"/>
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
@XmlType(name = "updOtherIncomeDetails", propOrder = {
    "incomeExpense",
    "currentYearIncome",
    "previousYearIncome",
    "deleteFlag"
})
public class UpdOtherIncomeDetails {

    @XmlElement(required = true)
    protected String incomeExpense;
    @XmlElement(required = true)
    protected MoneyType currentYearIncome;
    @XmlElement(required = true)
    protected MoneyType previousYearIncome;
    protected Boolean deleteFlag;

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
