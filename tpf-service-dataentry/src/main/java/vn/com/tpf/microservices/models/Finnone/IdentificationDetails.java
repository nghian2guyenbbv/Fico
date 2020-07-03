
package vn.com.tpf.microservices.models.Finnone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for identificationDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="identificationDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="identificationType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="identificationNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="issueDate" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType" minOccurs="0"/>
 *         &lt;element name="expiryDate" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType" minOccurs="0"/>
 *         &lt;element name="issuingCountry" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
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
@XmlType(name = "identificationDetails", propOrder = {
    "identificationType",
    "identificationNumber",
    "issueDate",
    "expiryDate",
    "issuingCountry",
    "regionalData"
})
public class IdentificationDetails {

    @XmlElement(required = true)
    protected String identificationType;
    @XmlElement(required = true)
    protected String identificationNumber;
    protected XMLGregorianCalendar issueDate;
    protected XMLGregorianCalendar expiryDate;
    @XmlElement(required = true)
    protected String issuingCountry;
    protected RegionalData regionalData;

    /**
     * Gets the value of the identificationType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificationType() {
        return identificationType;
    }

    /**
     * Sets the value of the identificationType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificationType(String value) {
        this.identificationType = value;
    }

    /**
     * Gets the value of the identificationNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificationNumber() {
        return identificationNumber;
    }

    /**
     * Sets the value of the identificationNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificationNumber(String value) {
        this.identificationNumber = value;
    }

    /**
     * Gets the value of the issueDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getIssueDate() {
        return issueDate;
    }

    /**
     * Sets the value of the issueDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setIssueDate(XMLGregorianCalendar value) {
        this.issueDate = value;
    }

    /**
     * Gets the value of the expiryDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getExpiryDate() {
        return expiryDate;
    }

    /**
     * Sets the value of the expiryDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setExpiryDate(XMLGregorianCalendar value) {
        this.expiryDate = value;
    }

    /**
     * Gets the value of the issuingCountry property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssuingCountry() {
        return issuingCountry;
    }

    /**
     * Sets the value of the issuingCountry property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssuingCountry(String value) {
        this.issuingCountry = value;
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
