
package vn.com.tpf.microservices.models.Finnone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for ownershipDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ownershipDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ownerType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="ownershipStatus" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="ownerName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="linkedApplicant" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="percentageShare" type="{http://schema.base.ws.pro.finnone.nucleus.com}DoubleType" minOccurs="0"/>
 *         &lt;element name="ownershipFrom" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType" minOccurs="0"/>
 *         &lt;element name="ownershipTo" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ownershipDetails", propOrder = {
    "ownerType",
    "ownershipStatus",
    "ownerName",
    "linkedApplicant",
    "percentageShare",
    "ownershipFrom",
    "ownershipTo"
})
public class OwnershipDetails {

    @XmlElement(required = true)
    protected String ownerType;
    @XmlElement(required = true)
    protected String ownershipStatus;
    @XmlElement(required = true)
    protected String ownerName;
    @XmlElement(required = true)
    protected String linkedApplicant;
    protected Double percentageShare;
    protected XMLGregorianCalendar ownershipFrom;
    protected XMLGregorianCalendar ownershipTo;

    /**
     * Gets the value of the ownerType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOwnerType() {
        return ownerType;
    }

    /**
     * Sets the value of the ownerType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOwnerType(String value) {
        this.ownerType = value;
    }

    /**
     * Gets the value of the ownershipStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOwnershipStatus() {
        return ownershipStatus;
    }

    /**
     * Sets the value of the ownershipStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOwnershipStatus(String value) {
        this.ownershipStatus = value;
    }

    /**
     * Gets the value of the ownerName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOwnerName() {
        return ownerName;
    }

    /**
     * Sets the value of the ownerName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOwnerName(String value) {
        this.ownerName = value;
    }

    /**
     * Gets the value of the linkedApplicant property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLinkedApplicant() {
        return linkedApplicant;
    }

    /**
     * Sets the value of the linkedApplicant property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLinkedApplicant(String value) {
        this.linkedApplicant = value;
    }

    /**
     * Gets the value of the percentageShare property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getPercentageShare() {
        return percentageShare;
    }

    /**
     * Sets the value of the percentageShare property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setPercentageShare(Double value) {
        this.percentageShare = value;
    }

    /**
     * Gets the value of the ownershipFrom property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getOwnershipFrom() {
        return ownershipFrom;
    }

    /**
     * Sets the value of the ownershipFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setOwnershipFrom(XMLGregorianCalendar value) {
        this.ownershipFrom = value;
    }

    /**
     * Gets the value of the ownershipTo property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getOwnershipTo() {
        return ownershipTo;
    }

    /**
     * Sets the value of the ownershipTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setOwnershipTo(XMLGregorianCalendar value) {
        this.ownershipTo = value;
    }

}
