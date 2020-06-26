
package vn.com.tpf.microservices.models.Finnone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import vn.com.tpf.microservices.models.Finnone.cas.MoneyType;


/**
 * <p>Java class for accessoryDetails complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="accessoryDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="accessory" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="costToBeFinanced" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "accessoryDetails", propOrder = {
    "accessory",
    "costToBeFinanced"
})
public class AccessoryDetails {

    @XmlElement(required = true)
    protected String accessory;
    protected MoneyType costToBeFinanced;

    /**
     * Gets the value of the accessory property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getAccessory() {
        return accessory;
    }

    /**
     * Sets the value of the accessory property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setAccessory(String value) {
        this.accessory = value;
    }

    /**
     * Gets the value of the costToBeFinanced property.
     *
     * @return
     *     possible object is
     *     {@link MoneyType }
     *
     */
    public MoneyType getCostToBeFinanced() {
        return costToBeFinanced;
    }

    /**
     * Sets the value of the costToBeFinanced property.
     *
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *
     */
    public void setCostToBeFinanced(MoneyType value) {
        this.costToBeFinanced = value;
    }

}
