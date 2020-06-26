
package vn.com.tpf.microservices.models.Finnone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for directors complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="directors">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="directorType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="salutationType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="directorName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="designation" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="qualificationType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="directorSince" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType" minOccurs="0"/>
 *         &lt;element name="dateOfBirth" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType" minOccurs="0"/>
 *         &lt;element name="relationship" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="uniqueIDNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="isDirectorStakeHolder" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType" minOccurs="0"/>
 *         &lt;element name="holdingPercantage" type="{http://schema.base.ws.pro.finnone.nucleus.com}DoubleType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "directors", propOrder = {
    "directorType",
    "salutationType",
    "directorName",
    "designation",
    "qualificationType",
    "directorSince",
    "dateOfBirth",
    "relationship",
    "uniqueIDNumber",
    "isDirectorStakeHolder",
    "holdingPercantage"
})
public class Directors {

    @XmlElement(required = true)
    protected String directorType;
    @XmlElement(required = true)
    protected String salutationType;
    @XmlElement(required = true)
    protected String directorName;
    @XmlElement(required = true)
    protected String designation;
    @XmlElement(required = true)
    protected String qualificationType;
    protected XMLGregorianCalendar directorSince;
    protected XMLGregorianCalendar dateOfBirth;
    protected String relationship;
    @XmlElement(required = true)
    protected String uniqueIDNumber;
    protected Boolean isDirectorStakeHolder;
    protected Double holdingPercantage;

    /**
     * Gets the value of the directorType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDirectorType() {
        return directorType;
    }

    /**
     * Sets the value of the directorType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDirectorType(String value) {
        this.directorType = value;
    }

    /**
     * Gets the value of the salutationType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSalutationType() {
        return salutationType;
    }

    /**
     * Sets the value of the salutationType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSalutationType(String value) {
        this.salutationType = value;
    }

    /**
     * Gets the value of the directorName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDirectorName() {
        return directorName;
    }

    /**
     * Sets the value of the directorName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDirectorName(String value) {
        this.directorName = value;
    }

    /**
     * Gets the value of the designation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDesignation() {
        return designation;
    }

    /**
     * Sets the value of the designation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDesignation(String value) {
        this.designation = value;
    }

    /**
     * Gets the value of the qualificationType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQualificationType() {
        return qualificationType;
    }

    /**
     * Sets the value of the qualificationType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQualificationType(String value) {
        this.qualificationType = value;
    }

    /**
     * Gets the value of the directorSince property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDirectorSince() {
        return directorSince;
    }

    /**
     * Sets the value of the directorSince property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDirectorSince(XMLGregorianCalendar value) {
        this.directorSince = value;
    }

    /**
     * Gets the value of the dateOfBirth property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets the value of the dateOfBirth property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateOfBirth(XMLGregorianCalendar value) {
        this.dateOfBirth = value;
    }

    /**
     * Gets the value of the relationship property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRelationship() {
        return relationship;
    }

    /**
     * Sets the value of the relationship property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRelationship(String value) {
        this.relationship = value;
    }

    /**
     * Gets the value of the uniqueIDNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUniqueIDNumber() {
        return uniqueIDNumber;
    }

    /**
     * Sets the value of the uniqueIDNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUniqueIDNumber(String value) {
        this.uniqueIDNumber = value;
    }

    /**
     * Gets the value of the isDirectorStakeHolder property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsDirectorStakeHolder() {
        return isDirectorStakeHolder;
    }

    /**
     * Sets the value of the isDirectorStakeHolder property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsDirectorStakeHolder(Boolean value) {
        this.isDirectorStakeHolder = value;
    }

    /**
     * Gets the value of the holdingPercantage property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getHoldingPercantage() {
        return holdingPercantage;
    }

    /**
     * Sets the value of the holdingPercantage property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setHoldingPercantage(Double value) {
        this.holdingPercantage = value;
    }

}
