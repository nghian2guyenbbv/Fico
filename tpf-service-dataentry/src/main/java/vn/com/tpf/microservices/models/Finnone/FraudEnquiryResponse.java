
package vn.com.tpf.microservices.models.Finnone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for fraudEnquiryResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="fraudEnquiryResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="responseXml" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fraudEnquiryResponse", propOrder = {
    "responseXml"
})
public class FraudEnquiryResponse {

    @XmlElement(required = true)
    protected String responseXml;

    /**
     * Gets the value of the responseXml property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponseXml() {
        return responseXml;
    }

    /**
     * Sets the value of the responseXml property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponseXml(String value) {
        this.responseXml = value;
    }

}
