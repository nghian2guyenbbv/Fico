
package vn.com.tpf.microservices.models.Finnone.base;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Common Application/System FaultType
 * 			
 * 
 * <p>Java class for ExceptionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ExceptionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://schema.base.ws.pro.finnone.nucleus.com}faultReason"/>
 *         &lt;element ref="{http://schema.base.ws.pro.finnone.nucleus.com}faultMessage"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ExceptionType", propOrder = {
    "faultReason",
    "faultMessage"
})
public class ExceptionType {

    @XmlElement(required = true)
    protected String faultReason;
    @XmlElement(required = true)
    protected String faultMessage;

    /**
     * Gets the value of the faultReason property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFaultReason() {
        return faultReason;
    }

    /**
     * Sets the value of the faultReason property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFaultReason(String value) {
        this.faultReason = value;
    }

    /**
     * Gets the value of the faultMessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFaultMessage() {
        return faultMessage;
    }

    /**
     * Sets the value of the faultMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFaultMessage(String value) {
        this.faultMessage = value;
    }

}
