
package vn.com.tpf.microservices.models.Finnone;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for nomineeDetail complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="nomineeDetail">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nomineeName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="nomineeRelationship" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="dateOfBirth" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType"/>
 *         &lt;element name="entitlementPercent" type="{http://schema.base.ws.pro.finnone.nucleus.com}AmountType" minOccurs="0"/>
 *         &lt;element name="addressDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}addressDetails" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "nomineeDetail", propOrder = {
    "nomineeName",
    "nomineeRelationship",
    "dateOfBirth",
    "entitlementPercent",
    "addressDetails"
})
public class NomineeDetail {

    @XmlElement(required = true)
    protected String nomineeName;
    @XmlElement(required = true)
    protected String nomineeRelationship;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dateOfBirth;
    protected BigDecimal entitlementPercent;
    protected AddressDetails addressDetails;

    /**
     * Gets the value of the nomineeName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomineeName() {
        return nomineeName;
    }

    /**
     * Sets the value of the nomineeName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomineeName(String value) {
        this.nomineeName = value;
    }

    /**
     * Gets the value of the nomineeRelationship property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomineeRelationship() {
        return nomineeRelationship;
    }

    /**
     * Sets the value of the nomineeRelationship property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomineeRelationship(String value) {
        this.nomineeRelationship = value;
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
     * Gets the value of the entitlementPercent property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getEntitlementPercent() {
        return entitlementPercent;
    }

    /**
     * Sets the value of the entitlementPercent property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setEntitlementPercent(BigDecimal value) {
        this.entitlementPercent = value;
    }

    /**
     * Gets the value of the addressDetails property.
     * 
     * @return
     *     possible object is
     *     {@link AddressDetails }
     *     
     */
    public AddressDetails getAddressDetails() {
        return addressDetails;
    }

    /**
     * Sets the value of the addressDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddressDetails }
     *     
     */
    public void setAddressDetails(AddressDetails value) {
        this.addressDetails = value;
    }

}
