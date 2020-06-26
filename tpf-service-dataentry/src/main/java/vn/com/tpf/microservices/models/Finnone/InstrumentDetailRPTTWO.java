
package vn.com.tpf.microservices.models.Finnone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import vn.com.tpf.microservices.models.Finnone.cas.MoneyType;


/**
 * <p>Java class for instrumentDetailRPTTWO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="instrumentDetailRPTTWO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="numericOne" type="{http://schema.base.ws.pro.finnone.nucleus.com}LongType" minOccurs="0"/>
 *         &lt;element name="numericTwo" type="{http://schema.base.ws.pro.finnone.nucleus.com}LongType"/>
 *         &lt;element name="alphaNumericOne" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="alphaNumericTwo" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="alphaNumericThree" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="alphaNumericFour" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="moneyFieldOne" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType"/>
 *         &lt;element name="moneyFieldTwo" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "instrumentDetailRPTTWO", propOrder = {
    "numericOne",
    "numericTwo",
    "alphaNumericOne",
    "alphaNumericTwo",
    "alphaNumericThree",
    "alphaNumericFour",
    "moneyFieldOne",
    "moneyFieldTwo"
})
public class InstrumentDetailRPTTWO {

    protected Long numericOne;
    protected long numericTwo;
    @XmlElement(required = true)
    protected String alphaNumericOne;
    @XmlElement(required = true)
    protected String alphaNumericTwo;
    @XmlElement(required = true)
    protected String alphaNumericThree;
    protected String alphaNumericFour;
    @XmlElement(required = true)
    protected MoneyType moneyFieldOne;
    protected MoneyType moneyFieldTwo;

    /**
     * Gets the value of the numericOne property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getNumericOne() {
        return numericOne;
    }

    /**
     * Sets the value of the numericOne property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setNumericOne(Long value) {
        this.numericOne = value;
    }

    /**
     * Gets the value of the numericTwo property.
     * 
     */
    public long getNumericTwo() {
        return numericTwo;
    }

    /**
     * Sets the value of the numericTwo property.
     * 
     */
    public void setNumericTwo(long value) {
        this.numericTwo = value;
    }

    /**
     * Gets the value of the alphaNumericOne property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlphaNumericOne() {
        return alphaNumericOne;
    }

    /**
     * Sets the value of the alphaNumericOne property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlphaNumericOne(String value) {
        this.alphaNumericOne = value;
    }

    /**
     * Gets the value of the alphaNumericTwo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlphaNumericTwo() {
        return alphaNumericTwo;
    }

    /**
     * Sets the value of the alphaNumericTwo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlphaNumericTwo(String value) {
        this.alphaNumericTwo = value;
    }

    /**
     * Gets the value of the alphaNumericThree property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlphaNumericThree() {
        return alphaNumericThree;
    }

    /**
     * Sets the value of the alphaNumericThree property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlphaNumericThree(String value) {
        this.alphaNumericThree = value;
    }

    /**
     * Gets the value of the alphaNumericFour property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlphaNumericFour() {
        return alphaNumericFour;
    }

    /**
     * Sets the value of the alphaNumericFour property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlphaNumericFour(String value) {
        this.alphaNumericFour = value;
    }

    /**
     * Gets the value of the moneyFieldOne property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getMoneyFieldOne() {
        return moneyFieldOne;
    }

    /**
     * Sets the value of the moneyFieldOne property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setMoneyFieldOne(MoneyType value) {
        this.moneyFieldOne = value;
    }

    /**
     * Gets the value of the moneyFieldTwo property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getMoneyFieldTwo() {
        return moneyFieldTwo;
    }

    /**
     * Sets the value of the moneyFieldTwo property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setMoneyFieldTwo(MoneyType value) {
        this.moneyFieldTwo = value;
    }

}
