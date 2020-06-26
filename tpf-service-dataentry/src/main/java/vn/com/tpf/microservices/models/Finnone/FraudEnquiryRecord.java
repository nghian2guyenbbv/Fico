
package vn.com.tpf.microservices.models.Finnone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for fraudEnquiryRecord complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="fraudEnquiryRecord">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="fraudCode" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="fraudEnquiryResponse" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}fraudEnquiryResponse"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fraudEnquiryRecord", propOrder = {
    "fraudCode",
    "fraudEnquiryResponse"
})
public class FraudEnquiryRecord {

    @XmlElement(required = true)
    protected String fraudCode;
    @XmlElement(required = true)
    protected FraudEnquiryResponse fraudEnquiryResponse;

    /**
     * Gets the value of the fraudCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFraudCode() {
        return fraudCode;
    }

    /**
     * Sets the value of the fraudCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFraudCode(String value) {
        this.fraudCode = value;
    }

    /**
     * Gets the value of the fraudEnquiryResponse property.
     * 
     * @return
     *     possible object is
     *     {@link FraudEnquiryResponse }
     *     
     */
    public FraudEnquiryResponse getFraudEnquiryResponse() {
        return fraudEnquiryResponse;
    }

    /**
     * Sets the value of the fraudEnquiryResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link FraudEnquiryResponse }
     *     
     */
    public void setFraudEnquiryResponse(FraudEnquiryResponse value) {
        this.fraudEnquiryResponse = value;
    }

}
