
package vn.com.tpf.microservices.models.Finnone;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import vn.com.tpf.microservices.models.Finnone.cas.MoneyType;


/**
 * <p>Java class for updateApplicationCharges complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="updateApplicationCharges">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="chargeId" type="{http://schema.base.ws.pro.finnone.nucleus.com}LongType" minOccurs="0"/>
 *         &lt;element name="trancheId" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="isAmountBased" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType" minOccurs="0"/>
 *         &lt;element name="chargeRate" type="{http://schema.base.ws.pro.finnone.nucleus.com}DecimalType" minOccurs="0"/>
 *         &lt;element name="effectiveChargeAmount" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="chargeAdjustmentType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="chargeNotApplicableInCAS" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType" minOccurs="0"/>
 *         &lt;element name="deleteFlag" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateApplicationCharges", propOrder = {
    "chargeId",
    "trancheId",
    "isAmountBased",
    "chargeRate",
    "effectiveChargeAmount",
    "chargeAdjustmentType",
    "chargeNotApplicableInCAS",
    "deleteFlag"
})
public class UpdateApplicationCharges {

    protected Long chargeId;
    protected String trancheId;
    protected Boolean isAmountBased;
    protected BigDecimal chargeRate;
    protected MoneyType effectiveChargeAmount;
    protected String chargeAdjustmentType;
    protected Boolean chargeNotApplicableInCAS;
    protected Boolean deleteFlag;

    /**
     * Gets the value of the chargeId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getChargeId() {
        return chargeId;
    }

    /**
     * Sets the value of the chargeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setChargeId(Long value) {
        this.chargeId = value;
    }

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
     * Gets the value of the isAmountBased property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsAmountBased() {
        return isAmountBased;
    }

    /**
     * Sets the value of the isAmountBased property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsAmountBased(Boolean value) {
        this.isAmountBased = value;
    }

    /**
     * Gets the value of the chargeRate property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getChargeRate() {
        return chargeRate;
    }

    /**
     * Sets the value of the chargeRate property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setChargeRate(BigDecimal value) {
        this.chargeRate = value;
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
     * Gets the value of the chargeAdjustmentType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChargeAdjustmentType() {
        return chargeAdjustmentType;
    }

    /**
     * Sets the value of the chargeAdjustmentType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChargeAdjustmentType(String value) {
        this.chargeAdjustmentType = value;
    }

    /**
     * Gets the value of the chargeNotApplicableInCAS property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isChargeNotApplicableInCAS() {
        return chargeNotApplicableInCAS;
    }

    /**
     * Sets the value of the chargeNotApplicableInCAS property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setChargeNotApplicableInCAS(Boolean value) {
        this.chargeNotApplicableInCAS = value;
    }

    /**
     * Gets the value of the deleteFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDeleteFlag() {
        return deleteFlag;
    }

    /**
     * Sets the value of the deleteFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDeleteFlag(Boolean value) {
        this.deleteFlag = value;
    }

}
