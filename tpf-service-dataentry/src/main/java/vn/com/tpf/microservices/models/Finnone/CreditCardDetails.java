
package vn.com.tpf.microservices.models.Finnone;

import vn.com.tpf.microservices.models.Finnone.cas.MoneyType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;



/**
 * <p>Java class for creditCardDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="creditCardDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cardIssuer" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="cardNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="cardLimit" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="cardExpiryMonth" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="cardExpiryYear" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="regionalData" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}regionalData" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "creditCardDetails", propOrder = {
    "cardIssuer",
    "cardNumber",
    "cardLimit",
    "cardExpiryMonth",
    "cardExpiryYear",
    "regionalData"
})
public class CreditCardDetails {

    protected String cardIssuer;
    protected String cardNumber;
    protected MoneyType cardLimit;
    protected Integer cardExpiryMonth;
    protected Integer cardExpiryYear;
    protected RegionalData regionalData;

    /**
     * Gets the value of the cardIssuer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCardIssuer() {
        return cardIssuer;
    }

    /**
     * Sets the value of the cardIssuer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCardIssuer(String value) {
        this.cardIssuer = value;
    }

    /**
     * Gets the value of the cardNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * Sets the value of the cardNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCardNumber(String value) {
        this.cardNumber = value;
    }

    /**
     * Gets the value of the cardLimit property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getCardLimit() {
        return cardLimit;
    }

    /**
     * Sets the value of the cardLimit property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setCardLimit(MoneyType value) {
        this.cardLimit = value;
    }

    /**
     * Gets the value of the cardExpiryMonth property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCardExpiryMonth() {
        return cardExpiryMonth;
    }

    /**
     * Sets the value of the cardExpiryMonth property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCardExpiryMonth(Integer value) {
        this.cardExpiryMonth = value;
    }

    /**
     * Gets the value of the cardExpiryYear property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCardExpiryYear() {
        return cardExpiryYear;
    }

    /**
     * Sets the value of the cardExpiryYear property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCardExpiryYear(Integer value) {
        this.cardExpiryYear = value;
    }

    /**
     * Gets the value of the regionalData property.
     * 
     * @return
     *     possible object is
     *     {@link RegionalData }
     *     
     */
    public RegionalData getRegionalData() {
        return regionalData;
    }

    /**
     * Sets the value of the regionalData property.
     * 
     * @param value
     *     allowed object is
     *     {@link RegionalData }
     *     
     */
    public void setRegionalData(RegionalData value) {
        this.regionalData = value;
    }

}
