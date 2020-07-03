
package vn.com.tpf.microservices.models.Finnone;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for customerParameter complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="customerParameter">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="partyRelationship" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="partyRole" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntegerType" minOccurs="0"/>
 *         &lt;element name="customerType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="isExistingCustomer" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType" minOccurs="0"/>
 *         &lt;element name="customerNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="cifNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "customerParameter", propOrder = {
    "partyRelationship",
    "partyRole",
    "customerType",
    "isExistingCustomer",
    "customerNumber",
    "cifNumber"
})
public class CustomerParameter {

    protected String partyRelationship;
    protected BigInteger partyRole;
    protected String customerType;
    protected Boolean isExistingCustomer;
    protected String customerNumber;
    protected String cifNumber;

    /**
     * Gets the value of the partyRelationship property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPartyRelationship() {
        return partyRelationship;
    }

    /**
     * Sets the value of the partyRelationship property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPartyRelationship(String value) {
        this.partyRelationship = value;
    }

    /**
     * Gets the value of the partyRole property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getPartyRole() {
        return partyRole;
    }

    /**
     * Sets the value of the partyRole property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setPartyRole(BigInteger value) {
        this.partyRole = value;
    }

    /**
     * Gets the value of the customerType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerType() {
        return customerType;
    }

    /**
     * Sets the value of the customerType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerType(String value) {
        this.customerType = value;
    }

    /**
     * Gets the value of the isExistingCustomer property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsExistingCustomer() {
        return isExistingCustomer;
    }

    /**
     * Sets the value of the isExistingCustomer property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsExistingCustomer(Boolean value) {
        this.isExistingCustomer = value;
    }

    /**
     * Gets the value of the customerNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerNumber() {
        return customerNumber;
    }

    /**
     * Sets the value of the customerNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerNumber(String value) {
        this.customerNumber = value;
    }

    /**
     * Gets the value of the cifNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCifNumber() {
        return cifNumber;
    }

    /**
     * Sets the value of the cifNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCifNumber(String value) {
        this.cifNumber = value;
    }

}
