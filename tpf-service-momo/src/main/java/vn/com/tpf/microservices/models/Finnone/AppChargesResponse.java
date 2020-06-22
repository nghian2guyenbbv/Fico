
package vn.com.tpf.microservices.models.Finnone;
import vn.com.tpf.microservices.models.Finnone.cas.MoneyType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;



/**
 * <p>Java class for appChargesResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="appChargesResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="trancheId" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="chargeId" type="{http://schema.base.ws.pro.finnone.nucleus.com}LongType"/>
 *         &lt;element name="chargeName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="applicantName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="chargeAmount" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType"/>
 *         &lt;element name="effectiveChargeAmount" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType"/>
 *         &lt;element name="taxAmount" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "appChargesResponse", propOrder = {
    "trancheId",
    "chargeId",
    "chargeName",
    "applicantName",
    "chargeAmount",
    "effectiveChargeAmount",
    "taxAmount"
})
public class AppChargesResponse {

    @XmlElement(required = true)
    protected String trancheId;
    protected long chargeId;
    @XmlElement(required = true)
    protected String chargeName;
    @XmlElement(required = true)
    protected String applicantName;
    @XmlElement(required = true)
    protected MoneyType chargeAmount;
    @XmlElement(required = true)
    protected MoneyType effectiveChargeAmount;
    @XmlElement(required = true)
    protected MoneyType taxAmount;

    /**
     * Gets the value of the trancheId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTrancheId() {
        return trancheId;
    }

    /**
     * Sets the value of the trancheId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTrancheId(String value) {
        this.trancheId = value;
    }

    /**
     * Gets the value of the chargeId property.
     * 
     */
    public long getChargeId() {
        return chargeId;
    }

    /**
     * Sets the value of the chargeId property.
     * 
     */
    public void setChargeId(long value) {
        this.chargeId = value;
    }

    /**
     * Gets the value of the chargeName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChargeName() {
        return chargeName;
    }

    /**
     * Sets the value of the chargeName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChargeName(String value) {
        this.chargeName = value;
    }

    /**
     * Gets the value of the applicantName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApplicantName() {
        return applicantName;
    }

    /**
     * Sets the value of the applicantName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApplicantName(String value) {
        this.applicantName = value;
    }

    /**
     * Gets the value of the chargeAmount property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getChargeAmount() {
        return chargeAmount;
    }

    /**
     * Sets the value of the chargeAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setChargeAmount(MoneyType value) {
        this.chargeAmount = value;
    }

    /**
     * Gets the value of the effectiveChargeAmount property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getEffectiveChargeAmount() {
        return effectiveChargeAmount;
    }

    /**
     * Sets the value of the effectiveChargeAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setEffectiveChargeAmount(MoneyType value) {
        this.effectiveChargeAmount = value;
    }

    /**
     * Gets the value of the taxAmount property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getTaxAmount() {
        return taxAmount;
    }

    /**
     * Sets the value of the taxAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setTaxAmount(MoneyType value) {
        this.taxAmount = value;
    }

}
