
package vn.com.tpf.microservices.models.Finnone;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for referenceDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="referenceDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="relationship" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="addressDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}addressDetails"/>
 *         &lt;element name="phoneNumber" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}phoneNumber" maxOccurs="2"/>
 *         &lt;element name="emailAddress" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "referenceDetails", propOrder = {
    "name",
    "relationship",
    "addressDetails",
    "phoneNumber",
    "emailAddress"
})
public class ReferenceDetails {

    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    protected String relationship;
    @XmlElement(required = true)
    protected AddressDetails addressDetails;

    public void setPhoneNumber(List<PhoneNumber> phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @XmlElement(required = true)
    protected List<PhoneNumber> phoneNumber;
    protected String emailAddress;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
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

    /**
     * Gets the value of the phoneNumber property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the phoneNumber property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPhoneNumber().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PhoneNumber }
     * 
     * 
     */
    public List<PhoneNumber> getPhoneNumber() {
        if (phoneNumber == null) {
            phoneNumber = new ArrayList<PhoneNumber>();
        }
        return this.phoneNumber;
    }

    /**
     * Gets the value of the emailAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Sets the value of the emailAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmailAddress(String value) {
        this.emailAddress = value;
    }

}
