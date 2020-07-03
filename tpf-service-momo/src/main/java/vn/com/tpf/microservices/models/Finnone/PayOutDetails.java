
package vn.com.tpf.microservices.models.Finnone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import vn.com.tpf.microservices.models.Finnone.cas.MoneyType;


/**
 * <p>Java class for payOutDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="payOutDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="businessPartnerName" type="{http://schema.base.ws.pro.finnone.nucleus.com}StringType" minOccurs="0"/>
 *         &lt;element name="payout" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="comission" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="frequency" type="{http://schema.base.ws.pro.finnone.nucleus.com}StringType" minOccurs="0"/>
 *         &lt;element name="clawback" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "payOutDetails", propOrder = {
    "businessPartnerName",
    "payout",
    "comission",
    "frequency",
    "clawback"
})
public class PayOutDetails {

    protected String businessPartnerName;
    protected MoneyType payout;
    protected MoneyType comission;
    protected String frequency;
    protected Boolean clawback;

    /**
     * Gets the value of the businessPartnerName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBusinessPartnerName() {
        return businessPartnerName;
    }

    /**
     * Sets the value of the businessPartnerName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBusinessPartnerName(String value) {
        this.businessPartnerName = value;
    }

    /**
     * Gets the value of the payout property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getPayout() {
        return payout;
    }

    /**
     * Sets the value of the payout property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setPayout(MoneyType value) {
        this.payout = value;
    }

    /**
     * Gets the value of the comission property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getComission() {
        return comission;
    }

    /**
     * Sets the value of the comission property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setComission(MoneyType value) {
        this.comission = value;
    }

    /**
     * Gets the value of the frequency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFrequency() {
        return frequency;
    }

    /**
     * Sets the value of the frequency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFrequency(String value) {
        this.frequency = value;
    }

    /**
     * Gets the value of the clawback property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isClawback() {
        return clawback;
    }

    /**
     * Sets the value of the clawback property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setClawback(Boolean value) {
        this.clawback = value;
    }

}
