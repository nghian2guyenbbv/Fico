
package vn.com.tpf.microservices.models.Finnone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for updateFamilyDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="updateFamilyDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="deleteFlag" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType" minOccurs="0"/>
 *         &lt;element name="familyDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}familyDetails"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateFamilyDetails", propOrder = {
    "deleteFlag",
    "familyDetails"
})
public class UpdateFamilyDetails {

    protected Boolean deleteFlag;
    @XmlElement(required = true)
    protected FamilyDetails familyDetails;

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
     * Gets the value of the familyDetails property.
     * 
     * @return
     *     possible object is
     *     {@link FamilyDetails }
     *     
     */
    public FamilyDetails getFamilyDetails() {
        return familyDetails;
    }

    /**
     * Sets the value of the familyDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link FamilyDetails }
     *     
     */
    public void setFamilyDetails(FamilyDetails value) {
        this.familyDetails = value;
    }

}
