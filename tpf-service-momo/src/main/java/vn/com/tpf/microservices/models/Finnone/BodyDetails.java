
package vn.com.tpf.microservices.models.Finnone;
import vn.com.tpf.microservices.models.Finnone.cas.MoneyType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;



/**
 * <p>Java class for bodyDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="bodyDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="bodyType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="bodyManfName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="bodyCost" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType"/>
 *         &lt;element name="bodyCostAsPerGrid" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType"/>
 *         &lt;element name="bodyCostProposed" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "bodyDetails", propOrder = {
    "bodyType",
    "bodyManfName",
    "bodyCost",
    "bodyCostAsPerGrid",
    "bodyCostProposed"
})
public class BodyDetails {

    @XmlElement(required = true)
    protected String bodyType;
    @XmlElement(required = true)
    protected String bodyManfName;
    @XmlElement(required = true)
    protected MoneyType bodyCost;
    @XmlElement(required = true)
    protected MoneyType bodyCostAsPerGrid;
    protected MoneyType bodyCostProposed;

    /**
     * Gets the value of the bodyType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBodyType() {
        return bodyType;
    }

    /**
     * Sets the value of the bodyType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBodyType(String value) {
        this.bodyType = value;
    }

    /**
     * Gets the value of the bodyManfName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBodyManfName() {
        return bodyManfName;
    }

    /**
     * Sets the value of the bodyManfName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBodyManfName(String value) {
        this.bodyManfName = value;
    }

    /**
     * Gets the value of the bodyCost property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getBodyCost() {
        return bodyCost;
    }

    /**
     * Sets the value of the bodyCost property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setBodyCost(MoneyType value) {
        this.bodyCost = value;
    }

    /**
     * Gets the value of the bodyCostAsPerGrid property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getBodyCostAsPerGrid() {
        return bodyCostAsPerGrid;
    }

    /**
     * Sets the value of the bodyCostAsPerGrid property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setBodyCostAsPerGrid(MoneyType value) {
        this.bodyCostAsPerGrid = value;
    }

    /**
     * Gets the value of the bodyCostProposed property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getBodyCostProposed() {
        return bodyCostProposed;
    }

    /**
     * Sets the value of the bodyCostProposed property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setBodyCostProposed(MoneyType value) {
        this.bodyCostProposed = value;
    }

}
