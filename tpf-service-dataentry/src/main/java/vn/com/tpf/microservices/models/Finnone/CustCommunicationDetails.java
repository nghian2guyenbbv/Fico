
package vn.com.tpf.microservices.models.Finnone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for custCommunicationDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="custCommunicationDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="customerParameter" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}customerParameter"/>
 *         &lt;element name="communicationDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}communicationDetails"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "custCommunicationDetails", propOrder = {
    "customerParameter",
    "communicationDetails"
})
public class CustCommunicationDetails {

    @XmlElement(required = true)
    protected CustomerParameter customerParameter;
    @XmlElement(required = true)
    protected CommunicationDetails communicationDetails;

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
     * Gets the value of the communicationDetails property.
     * 
     * @return
     *     possible object is
     *     {@link CommunicationDetails }
     *     
     */
    public CommunicationDetails getCommunicationDetails() {
        return communicationDetails;
    }

    /**
     * Sets the value of the communicationDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link CommunicationDetails }
     *     
     */
    public void setCommunicationDetails(CommunicationDetails value) {
        this.communicationDetails = value;
    }

}
