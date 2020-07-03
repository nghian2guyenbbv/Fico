
package vn.com.tpf.microservices.models.Finnone;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for vapGrid complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="vapGrid">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="item" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="itemCost" type="{http://schema.base.ws.pro.finnone.nucleus.com}AmountType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "vapGrid", propOrder = {
    "item",
    "itemCost"
})
public class VapGrid {

    @XmlElement(required = true)
    protected String item;
    @XmlElement(required = true)
    protected BigDecimal itemCost;

    /**
     * Gets the value of the item property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItem() {
        return item;
    }

    /**
     * Sets the value of the item property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItem(String value) {
        this.item = value;
    }

    /**
     * Gets the value of the itemCost property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getItemCost() {
        return itemCost;
    }

    /**
     * Sets the value of the itemCost property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setItemCost(BigDecimal value) {
        this.itemCost = value;
    }

}
