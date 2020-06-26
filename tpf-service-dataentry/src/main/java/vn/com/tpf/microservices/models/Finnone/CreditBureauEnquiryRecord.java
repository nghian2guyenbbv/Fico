
package vn.com.tpf.microservices.models.Finnone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for creditBureauEnquiryRecord complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="creditBureauEnquiryRecord">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="reInitiated" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType"/>
 *         &lt;element name="bureauCode" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="requestReferenceNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}StringType"/>
 *         &lt;element name="creditBureauResponse" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}creditBureauResponse"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "creditBureauEnquiryRecord", propOrder = {
    "reInitiated",
    "bureauCode",
    "requestReferenceNumber",
    "creditBureauResponse"
})
public class CreditBureauEnquiryRecord {

    protected boolean reInitiated;
    @XmlElement(required = true)
    protected String bureauCode;
    @XmlElement(required = true)
    protected String requestReferenceNumber;
    @XmlElement(required = true)
    protected CreditBureauResponse creditBureauResponse;

    /**
     * Gets the value of the reInitiated property.
     * 
     */
    public boolean isReInitiated() {
        return reInitiated;
    }

    /**
     * Sets the value of the reInitiated property.
     * 
     */
    public void setReInitiated(boolean value) {
        this.reInitiated = value;
    }

    /**
     * Gets the value of the bureauCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBureauCode() {
        return bureauCode;
    }

    /**
     * Sets the value of the bureauCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBureauCode(String value) {
        this.bureauCode = value;
    }

    /**
     * Gets the value of the requestReferenceNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestReferenceNumber() {
        return requestReferenceNumber;
    }

    /**
     * Sets the value of the requestReferenceNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestReferenceNumber(String value) {
        this.requestReferenceNumber = value;
    }

    /**
     * Gets the value of the creditBureauResponse property.
     * 
     * @return
     *     possible object is
     *     {@link CreditBureauResponse }
     *     
     */
    public CreditBureauResponse getCreditBureauResponse() {
        return creditBureauResponse;
    }

    /**
     * Sets the value of the creditBureauResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link CreditBureauResponse }
     *     
     */
    public void setCreditBureauResponse(CreditBureauResponse value) {
        this.creditBureauResponse = value;
    }

}
