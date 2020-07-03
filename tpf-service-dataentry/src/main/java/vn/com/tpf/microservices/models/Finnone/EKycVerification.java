
package vn.com.tpf.microservices.models.Finnone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for eKycVerification complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="eKycVerification">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ekycStatus" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType"/>
 *         &lt;element name="verificationType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="eKycResponse" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}eKycResponse"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "eKycVerification", propOrder = {
    "ekycStatus",
    "verificationType",
    "eKycResponse"
})
public class EKycVerification {

    protected boolean ekycStatus;
    @XmlElement(required = true)
    protected String verificationType;
    @XmlElement(required = true)
    protected EKycResponse eKycResponse;

    /**
     * Gets the value of the ekycStatus property.
     * 
     */
    public boolean isEkycStatus() {
        return ekycStatus;
    }

    /**
     * Sets the value of the ekycStatus property.
     * 
     */
    public void setEkycStatus(boolean value) {
        this.ekycStatus = value;
    }

    /**
     * Gets the value of the verificationType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVerificationType() {
        return verificationType;
    }

    /**
     * Sets the value of the verificationType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVerificationType(String value) {
        this.verificationType = value;
    }

    /**
     * Gets the value of the eKycResponse property.
     * 
     * @return
     *     possible object is
     *     {@link EKycResponse }
     *     
     */
    public EKycResponse getEKycResponse() {
        return eKycResponse;
    }

    /**
     * Sets the value of the eKycResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link EKycResponse }
     *     
     */
    public void setEKycResponse(EKycResponse value) {
        this.eKycResponse = value;
    }

}
