
package vn.com.tpf.microservices.models.Finnone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for dynamicFormDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="dynamicFormDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dynamicFormName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="dynamicFormData" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dynamicFormDetails", propOrder = {
    "dynamicFormName",
    "dynamicFormData"
})
public class DynamicFormDetails {

    @XmlElement(required = true)
    protected String dynamicFormName;
    @XmlElement(required = true)
    protected String dynamicFormData;

    /**
     * Gets the value of the dynamicFormName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDynamicFormName() {
        return dynamicFormName;
    }

    /**
     * Sets the value of the dynamicFormName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDynamicFormName(String value) {
        this.dynamicFormName = value;
    }

    /**
     * Gets the value of the dynamicFormData property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDynamicFormData() {
        return dynamicFormData;
    }

    /**
     * Sets the value of the dynamicFormData property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDynamicFormData(String value) {
        this.dynamicFormData = value;
    }

}
