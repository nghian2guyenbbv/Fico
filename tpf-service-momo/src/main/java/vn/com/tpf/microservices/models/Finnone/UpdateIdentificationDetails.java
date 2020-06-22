
package vn.com.tpf.microservices.models.Finnone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for updateIdentificationDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="updateIdentificationDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="deleteFlag" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType" minOccurs="0"/>
 *         &lt;element name="identificationDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}identificationDetails"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateIdentificationDetails", propOrder = {
    "deleteFlag",
    "identificationDetails"
})
public class UpdateIdentificationDetails {

    protected Boolean deleteFlag;
    @XmlElement(required = true)
    protected IdentificationDetails identificationDetails;

    /**
     * Gets the value of the deleteFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDeleteFlag() {
        return deleteFlag;
    }

    /**
     * Sets the value of the deleteFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDeleteFlag(Boolean value) {
        this.deleteFlag = value;
    }

    /**
     * Gets the value of the identificationDetails property.
     * 
     * @return
     *     possible object is
     *     {@link IdentificationDetails }
     *     
     */
    public IdentificationDetails getIdentificationDetails() {
        return identificationDetails;
    }

    /**
     * Sets the value of the identificationDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link IdentificationDetails }
     *     
     */
    public void setIdentificationDetails(IdentificationDetails value) {
        this.identificationDetails = value;
    }

}
