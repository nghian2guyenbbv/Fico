
package vn.com.tpf.microservices.models.Finnone;

import vn.com.tpf.microservices.models.Finnone.cas.MoneyType;
import vn.com.tpf.microservices.models.Finnone.cas.MoneyType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;



/**
 * <p>Java class for insuranceFunding complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="insuranceFunding">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="typeOfInsurance" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="insured" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="sumInsured" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType"/>
 *         &lt;element name="insuranceProvider" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="premiumAmount" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType"/>
 *         &lt;element name="startDate" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType"/>
 *         &lt;element name="endDate" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "insuranceFunding", propOrder = {
    "typeOfInsurance",
    "insured",
    "sumInsured",
    "insuranceProvider",
    "premiumAmount",
    "startDate",
    "endDate"
})
public class InsuranceFunding {

    @XmlElement(required = true)
    protected String typeOfInsurance;
    @XmlElement(required = true)
    protected String insured;
    @XmlElement(required = true)
    protected MoneyType sumInsured;
    @XmlElement(required = true)
    protected String insuranceProvider;
    @XmlElement(required = true)
    protected MoneyType premiumAmount;
    @XmlElement(required = true)
    protected XMLGregorianCalendar startDate;
    @XmlElement(required = true)
    protected XMLGregorianCalendar endDate;

    /**
     * Gets the value of the typeOfInsurance property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTypeOfInsurance() {
        return typeOfInsurance;
    }

    /**
     * Sets the value of the typeOfInsurance property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTypeOfInsurance(String value) {
        this.typeOfInsurance = value;
    }

    /**
     * Gets the value of the insured property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInsured() {
        return insured;
    }

    /**
     * Sets the value of the insured property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInsured(String value) {
        this.insured = value;
    }

    /**
     * Gets the value of the sumInsured property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getSumInsured() {
        return sumInsured;
    }

    /**
     * Sets the value of the sumInsured property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setSumInsured(MoneyType value) {
        this.sumInsured = value;
    }

    /**
     * Gets the value of the insuranceProvider property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInsuranceProvider() {
        return insuranceProvider;
    }

    /**
     * Sets the value of the insuranceProvider property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInsuranceProvider(String value) {
        this.insuranceProvider = value;
    }

    /**
     * Gets the value of the premiumAmount property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getPremiumAmount() {
        return premiumAmount;
    }

    /**
     * Sets the value of the premiumAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setPremiumAmount(MoneyType value) {
        this.premiumAmount = value;
    }

    /**
     * Gets the value of the startDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getStartDate() {
        return startDate;
    }

    /**
     * Sets the value of the startDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setStartDate(XMLGregorianCalendar value) {
        this.startDate = value;
    }

    /**
     * Gets the value of the endDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getEndDate() {
        return endDate;
    }

    /**
     * Sets the value of the endDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setEndDate(XMLGregorianCalendar value) {
        this.endDate = value;
    }

}
