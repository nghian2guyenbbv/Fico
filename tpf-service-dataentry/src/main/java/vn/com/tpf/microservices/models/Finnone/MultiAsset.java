
package vn.com.tpf.microservices.models.Finnone;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import vn.com.tpf.microservices.models.Finnone.cas.MoneyType;


/**
 * <p>Java class for multiAsset complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="multiAsset">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="financeMode" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="assetType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="rate" type="{http://schema.base.ws.pro.finnone.nucleus.com}DecimalType" minOccurs="0"/>
 *         &lt;element name="tenure" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="assetModelCategory" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="make" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="model" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="assetVarient" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="assetLevel" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="assetCost" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType"/>
 *         &lt;element name="downPaymentAmount" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType"/>
 *         &lt;element name="dealerDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}dealerDetails" minOccurs="0"/>
 *         &lt;element name="quantity" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="collateralType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="collateralSubType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="ltv" type="{http://schema.base.ws.pro.finnone.nucleus.com}DecimalType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "multiAsset", propOrder = {
    "financeMode",
    "assetType",
    "rate",
    "tenure",
    "assetModelCategory",
    "make",
    "model",
    "assetVarient",
    "assetLevel",
    "assetCost",
    "downPaymentAmount",
    "dealerDetails",
    "quantity",
    "collateralType",
    "collateralSubType",
    "ltv"
})
public class MultiAsset {

    protected String financeMode;
    protected String assetType;
    protected BigDecimal rate;
    protected BigInteger tenure;
    @XmlElement(required = true)
    protected String assetModelCategory;
    @XmlElement(required = true)
    protected String make;
    @XmlElement(required = true)
    protected String model;
    protected String assetVarient;
    protected String assetLevel;
    @XmlElement(required = true)
    protected MoneyType assetCost;
    @XmlElement(required = true)
    protected MoneyType downPaymentAmount;
    protected DealerDetails dealerDetails;
    protected BigInteger quantity;
    @XmlElement(required = true)
    protected String collateralType;
    @XmlElement(required = true)
    protected String collateralSubType;
    @XmlElement(required = true)
    protected BigDecimal ltv;

    /**
     * Gets the value of the financeMode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFinanceMode() {
        return financeMode;
    }

    /**
     * Sets the value of the financeMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFinanceMode(String value) {
        this.financeMode = value;
    }

    /**
     * Gets the value of the assetType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssetType() {
        return assetType;
    }

    /**
     * Sets the value of the assetType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssetType(String value) {
        this.assetType = value;
    }

    /**
     * Gets the value of the rate property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRate() {
        return rate;
    }

    /**
     * Sets the value of the rate property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRate(BigDecimal value) {
        this.rate = value;
    }

    /**
     * Gets the value of the tenure property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTenure() {
        return tenure;
    }

    /**
     * Sets the value of the tenure property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTenure(BigInteger value) {
        this.tenure = value;
    }

    /**
     * Gets the value of the assetModelCategory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssetModelCategory() {
        return assetModelCategory;
    }

    /**
     * Sets the value of the assetModelCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssetModelCategory(String value) {
        this.assetModelCategory = value;
    }

    /**
     * Gets the value of the make property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMake() {
        return make;
    }

    /**
     * Sets the value of the make property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMake(String value) {
        this.make = value;
    }

    /**
     * Gets the value of the model property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets the value of the model property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModel(String value) {
        this.model = value;
    }

    /**
     * Gets the value of the assetVarient property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssetVarient() {
        return assetVarient;
    }

    /**
     * Sets the value of the assetVarient property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssetVarient(String value) {
        this.assetVarient = value;
    }

    /**
     * Gets the value of the assetLevel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssetLevel() {
        return assetLevel;
    }

    /**
     * Sets the value of the assetLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssetLevel(String value) {
        this.assetLevel = value;
    }

    /**
     * Gets the value of the assetCost property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getAssetCost() {
        return assetCost;
    }

    /**
     * Sets the value of the assetCost property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setAssetCost(MoneyType value) {
        this.assetCost = value;
    }

    /**
     * Gets the value of the downPaymentAmount property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getDownPaymentAmount() {
        return downPaymentAmount;
    }

    /**
     * Sets the value of the downPaymentAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setDownPaymentAmount(MoneyType value) {
        this.downPaymentAmount = value;
    }

    /**
     * Gets the value of the dealerDetails property.
     * 
     * @return
     *     possible object is
     *     {@link DealerDetails }
     *     
     */
    public DealerDetails getDealerDetails() {
        return dealerDetails;
    }

    /**
     * Sets the value of the dealerDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link DealerDetails }
     *     
     */
    public void setDealerDetails(DealerDetails value) {
        this.dealerDetails = value;
    }

    /**
     * Gets the value of the quantity property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getQuantity() {
        return quantity;
    }

    /**
     * Sets the value of the quantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setQuantity(BigInteger value) {
        this.quantity = value;
    }

    /**
     * Gets the value of the collateralType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCollateralType() {
        return collateralType;
    }

    /**
     * Sets the value of the collateralType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCollateralType(String value) {
        this.collateralType = value;
    }

    /**
     * Gets the value of the collateralSubType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCollateralSubType() {
        return collateralSubType;
    }

    /**
     * Sets the value of the collateralSubType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCollateralSubType(String value) {
        this.collateralSubType = value;
    }

    /**
     * Gets the value of the ltv property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getLtv() {
        return ltv;
    }

    /**
     * Sets the value of the ltv property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setLtv(BigDecimal value) {
        this.ltv = value;
    }

}
