
package vn.com.tpf.microservices.models.Finnone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for updateEducationDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="updateEducationDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="deleteFlag" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType" minOccurs="0"/>
 *         &lt;element name="educationDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}educationDetails"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateEducationDetails", propOrder = {
    "deleteFlag",
    "educationDetails"
})
public class UpdateEducationDetails {

    protected Boolean deleteFlag;
    @XmlElement(required = true)
    protected EducationDetails educationDetails;

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
     * Gets the value of the educationDetails property.
     * 
     * @return
     *     possible object is
     *     {@link EducationDetails }
     *     
     */
    public EducationDetails getEducationDetails() {
        return educationDetails;
    }

    /**
     * Sets the value of the educationDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link EducationDetails }
     *     
     */
    public void setEducationDetails(EducationDetails value) {
        this.educationDetails = value;
    }

}
