
package vn.com.tpf.microservices.models.Finnone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for custEducationDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="custEducationDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="customerParameter" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}customerParameter"/>
 *         &lt;element name="updateEducationDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}updateEducationDetails"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "custEducationDetails", propOrder = {
    "customerParameter",
    "updateEducationDetails"
})
public class CustEducationDetails {

    @XmlElement(required = true)
    protected CustomerParameter customerParameter;
    @XmlElement(required = true)
    protected UpdateEducationDetails updateEducationDetails;

    /**
     * Gets the value of the customerParameter property.
     * 
     * @return
     *     possible object is
     *     {@link CustomerParameter }
     *     
     */
    public CustomerParameter getCustomerParameter() {
        return customerParameter;
    }

    /**
     * Sets the value of the customerParameter property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustomerParameter }
     *     
     */
    public void setCustomerParameter(CustomerParameter value) {
        this.customerParameter = value;
    }

    /**
     * Gets the value of the updateEducationDetails property.
     * 
     * @return
     *     possible object is
     *     {@link UpdateEducationDetails }
     *     
     */
    public UpdateEducationDetails getUpdateEducationDetails() {
        return updateEducationDetails;
    }

    /**
     * Sets the value of the updateEducationDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link UpdateEducationDetails }
     *     
     */
    public void setUpdateEducationDetails(UpdateEducationDetails value) {
        this.updateEducationDetails = value;
    }

}
