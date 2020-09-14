
package vn.com.tpf.microservices.models.Finnone;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for custFamilyDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="custFamilyDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="customerParameter" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}customerParameter"/>
 *         &lt;element name="noOfDependentChildren" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType"/>
 *         &lt;element name="updateFamilyDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}updateFamilyDetails" maxOccurs="10"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "custFamilyDetails", propOrder = {
    "customerParameter",
    "noOfDependentChildren",
    "updateFamilyDetails"
})
public class CustFamilyDetails {

    @XmlElement(required = true)
    protected CustomerParameter customerParameter;
    protected int noOfDependentChildren;
    @XmlElement(required = true)
    protected List<UpdateFamilyDetails> updateFamilyDetails;

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
     * Gets the value of the noOfDependentChildren property.
     * 
     */
    public int getNoOfDependentChildren() {
        return noOfDependentChildren;
    }

    /**
     * Sets the value of the noOfDependentChildren property.
     * 
     */
    public void setNoOfDependentChildren(int value) {
        this.noOfDependentChildren = value;
    }

    /**
     * Gets the value of the updateFamilyDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the updateFamilyDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUpdateFamilyDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UpdateFamilyDetails }
     * 
     * 
     */
    public List<UpdateFamilyDetails> getUpdateFamilyDetails() {
        if (updateFamilyDetails == null) {
            updateFamilyDetails = new ArrayList<UpdateFamilyDetails>();
        }
        return this.updateFamilyDetails;
    }

}
