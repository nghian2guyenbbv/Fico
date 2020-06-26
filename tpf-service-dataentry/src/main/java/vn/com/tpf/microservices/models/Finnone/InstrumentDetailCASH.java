
package vn.com.tpf.microservices.models.Finnone;

import vn.com.tpf.microservices.models.Finnone.cas.MoneyType;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;



/**
 * <p>Java class for instrumentDetailCASH complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="instrumentDetailCASH">
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
 *         &lt;element name="dynamicFormDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}dynamicFormDetails" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "instrumentDetailCASH", propOrder = {
    "numericOne",
    "numericTwo",
    "alphaNumericOne",
    "alphaNumericTwo",
    "alphaNumericThree",
    "alphaNumericFour",
    "moneyFieldOne",
    "moneyFieldTwo",
    "dynamicFormDetails"
})
public class InstrumentDetailCASH {

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
    protected List<DynamicFormDetails> dynamicFormDetails;

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

    /**
     * Gets the value of the dynamicFormDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dynamicFormDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDynamicFormDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DynamicFormDetails }
     * 
     * 
     */
    public List<DynamicFormDetails> getDynamicFormDetails() {
        if (dynamicFormDetails == null) {
            dynamicFormDetails = new ArrayList<DynamicFormDetails>();
        }
        return this.dynamicFormDetails;
    }

}
