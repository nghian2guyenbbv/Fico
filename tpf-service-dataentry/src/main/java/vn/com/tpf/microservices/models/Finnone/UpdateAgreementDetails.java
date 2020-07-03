
package vn.com.tpf.microservices.models.Finnone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for updateAgreementDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="updateAgreementDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="deleteFlag" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType" minOccurs="0"/>
 *         &lt;element name="agreementDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}agreementDetails" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateAgreementDetails", propOrder = {
    "deleteFlag",
    "agreementDetails"
})
public class UpdateAgreementDetails {

    protected Boolean deleteFlag;
    protected AgreementDetails agreementDetails;

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
     * Gets the value of the agreementDetails property.
     * 
     * @return
     *     possible object is
     *     {@link AgreementDetails }
     *     
     */
    public AgreementDetails getAgreementDetails() {
        return agreementDetails;
    }

    /**
     * Sets the value of the agreementDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link AgreementDetails }
     *     
     */
    public void setAgreementDetails(AgreementDetails value) {
        this.agreementDetails = value;
    }

}
