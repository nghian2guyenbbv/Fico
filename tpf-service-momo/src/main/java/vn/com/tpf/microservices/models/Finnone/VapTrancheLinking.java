
package vn.com.tpf.microservices.models.Finnone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for vapTrancheLinking complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="vapTrancheLinking">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="vapNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="splitId" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "vapTrancheLinking", propOrder = {
    "vapNumber",
    "splitId"
})
public class VapTrancheLinking {

    @XmlElement(required = true)
    protected String vapNumber;
    @XmlElement(required = true)
    protected String splitId;

    /**
     * Gets the value of the vapNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVapNumber() {
        return vapNumber;
    }

    /**
     * Sets the value of the vapNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVapNumber(String value) {
        this.vapNumber = value;
    }

    /**
     * Gets the value of the splitId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSplitId() {
        return splitId;
    }

    /**
     * Sets the value of the splitId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSplitId(String value) {
        this.splitId = value;
    }

}
