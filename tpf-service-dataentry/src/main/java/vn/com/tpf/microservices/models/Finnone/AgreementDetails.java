
package vn.com.tpf.microservices.models.Finnone;
import vn.com.tpf.microservices.models.Finnone.cas.MoneyType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;




/**
 * <p>Java class for agreementDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="agreementDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="registrationNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType"/>
 *         &lt;element name="agreementType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="sro" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="saleDeedNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="registrationDate" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType"/>
 *         &lt;element name="saleDeedDate" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType" minOccurs="0"/>
 *         &lt;element name="agreementValue" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="amenitiesAgreementValue" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="agreementRemarks" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "agreementDetails", propOrder = {
    "registrationNumber",
    "agreementType",
    "sro",
    "saleDeedNumber",
    "registrationDate",
    "saleDeedDate",
    "agreementValue",
    "amenitiesAgreementValue",
    "agreementRemarks"
})
public class AgreementDetails {

    protected int registrationNumber;
    @XmlElement(required = true)
    protected String agreementType;
    @XmlElement(required = true)
    protected String sro;
    protected Integer saleDeedNumber;
    @XmlElement(required = true)
    protected XMLGregorianCalendar registrationDate;
    protected XMLGregorianCalendar saleDeedDate;
    protected MoneyType agreementValue;
    protected MoneyType amenitiesAgreementValue;
    protected String agreementRemarks;

    /**
     * Gets the value of the registrationNumber property.
     * 
     */
    public int getRegistrationNumber() {
        return registrationNumber;
    }

    /**
     * Sets the value of the registrationNumber property.
     * 
     */
    public void setRegistrationNumber(int value) {
        this.registrationNumber = value;
    }

    /**
     * Gets the value of the agreementType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgreementType() {
        return agreementType;
    }

    /**
     * Sets the value of the agreementType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgreementType(String value) {
        this.agreementType = value;
    }

    /**
     * Gets the value of the sro property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSro() {
        return sro;
    }

    /**
     * Sets the value of the sro property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSro(String value) {
        this.sro = value;
    }

    /**
     * Gets the value of the saleDeedNumber property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSaleDeedNumber() {
        return saleDeedNumber;
    }

    /**
     * Sets the value of the saleDeedNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSaleDeedNumber(Integer value) {
        this.saleDeedNumber = value;
    }

    /**
     * Gets the value of the registrationDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getRegistrationDate() {
        return registrationDate;
    }

    /**
     * Sets the value of the registrationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setRegistrationDate(XMLGregorianCalendar value) {
        this.registrationDate = value;
    }

    /**
     * Gets the value of the saleDeedDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getSaleDeedDate() {
        return saleDeedDate;
    }

    /**
     * Sets the value of the saleDeedDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setSaleDeedDate(XMLGregorianCalendar value) {
        this.saleDeedDate = value;
    }

    /**
     * Gets the value of the agreementValue property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getAgreementValue() {
        return agreementValue;
    }

    /**
     * Sets the value of the agreementValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setAgreementValue(MoneyType value) {
        this.agreementValue = value;
    }

    /**
     * Gets the value of the amenitiesAgreementValue property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getAmenitiesAgreementValue() {
        return amenitiesAgreementValue;
    }

    /**
     * Sets the value of the amenitiesAgreementValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setAmenitiesAgreementValue(MoneyType value) {
        this.amenitiesAgreementValue = value;
    }

    /**
     * Gets the value of the agreementRemarks property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgreementRemarks() {
        return agreementRemarks;
    }

    /**
     * Sets the value of the agreementRemarks property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgreementRemarks(String value) {
        this.agreementRemarks = value;
    }

}
