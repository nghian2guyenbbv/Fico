
package vn.com.tpf.microservices.models.Finnone.base;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * Common Application/System Fault Detail
 * 			
 * 
 * <p>Java class for ExceptionDetailType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ExceptionDetailType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element ref="{http://schema.base.ws.pro.finnone.nucleus.com}applicationException" minOccurs="0"/>
 *           &lt;element ref="{http://schema.base.ws.pro.finnone.nucleus.com}systemException" minOccurs="0"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ExceptionDetailType", propOrder = {
    "applicationException",
    "systemException"
})
public class ExceptionDetailType {

    protected ExceptionType applicationException;
    protected ExceptionType systemException;

    /**
     * Gets the value of the applicationException property.
     * 
     * @return
     *     possible object is
     *     {@link ExceptionType }
     *     
     */
    public ExceptionType getApplicationException() {
        return applicationException;
    }

    /**
     * Sets the value of the applicationException property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExceptionType }
     *     
     */
    public void setApplicationException(ExceptionType value) {
        this.applicationException = value;
    }

    /**
     * Gets the value of the systemException property.
     * 
     * @return
     *     possible object is
     *     {@link ExceptionType }
     *     
     */
    public ExceptionType getSystemException() {
        return systemException;
    }

    /**
     * Sets the value of the systemException property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExceptionType }
     *     
     */
    public void setSystemException(ExceptionType value) {
        this.systemException = value;
    }

}
