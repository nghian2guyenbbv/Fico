
package vn.com.tpf.microservices.models.Finnone.base;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * Common Application/System FaultType
 * 			
 * 
 * <p>Java class for FaultType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FaultType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://schema.base.ws.pro.finnone.nucleus.com}detail" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FaultType", propOrder = {
    "detail"
})
public class FaultType {

    protected ExceptionDetailType detail;

    /**
     * Gets the value of the detail property.
     * 
     * @return
     *     possible object is
     *     {@link ExceptionDetailType }
     *     
     */
    public ExceptionDetailType getDetail() {
        return detail;
    }

    /**
     * Sets the value of the detail property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExceptionDetailType }
     *     
     */
    public void setDetail(ExceptionDetailType value) {
        this.detail = value;
    }

}
