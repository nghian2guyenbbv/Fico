
package vn.com.tpf.microservices.models.Finnone;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for custIdentificationDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="custIdentificationDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="customerParameter" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}customerParameter"/>
 *         &lt;element name="updateIdentificationDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}updateIdentificationDetails" maxOccurs="10"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "custIdentificationDetails", propOrder = {
    "customerParameter",
    "updateIdentificationDetails"
})
public class CustIdentificationDetails {

    @XmlElement(required = true)
    protected CustomerParameter customerParameter;
    @XmlElement(required = true)
    protected List<UpdateIdentificationDetails> updateIdentificationDetails;

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
     * Gets the value of the updateIdentificationDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the updateIdentificationDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUpdateIdentificationDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UpdateIdentificationDetails }
     * 
     * 
     */
    public List<UpdateIdentificationDetails> getUpdateIdentificationDetails() {
        if (updateIdentificationDetails == null) {
            updateIdentificationDetails = new ArrayList<UpdateIdentificationDetails>();
        }
        return this.updateIdentificationDetails;
    }

}
