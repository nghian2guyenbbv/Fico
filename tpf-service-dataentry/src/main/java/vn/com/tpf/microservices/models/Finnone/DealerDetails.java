
package vn.com.tpf.microservices.models.Finnone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for dealerDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="dealerDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="boughtFrom" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="dealer" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="dealerLocation" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="purchasedFrom" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dealerDetails", propOrder = {
    "boughtFrom",
    "dealer",
    "dealerLocation",
    "purchasedFrom"
})
public class DealerDetails {

    @XmlElement(required = true)
    protected String boughtFrom;
    protected String dealer;
    protected String dealerLocation;
    protected String purchasedFrom;

    /**
     * Gets the value of the boughtFrom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBoughtFrom() {
        return boughtFrom;
    }

    /**
     * Sets the value of the boughtFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBoughtFrom(String value) {
        this.boughtFrom = value;
    }

    /**
     * Gets the value of the dealer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDealer() {
        return dealer;
    }

    /**
     * Sets the value of the dealer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDealer(String value) {
        this.dealer = value;
    }

    /**
     * Gets the value of the dealerLocation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDealerLocation() {
        return dealerLocation;
    }

    /**
     * Sets the value of the dealerLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDealerLocation(String value) {
        this.dealerLocation = value;
    }

    /**
     * Gets the value of the purchasedFrom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPurchasedFrom() {
        return purchasedFrom;
    }

    /**
     * Sets the value of the purchasedFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPurchasedFrom(String value) {
        this.purchasedFrom = value;
    }

}
