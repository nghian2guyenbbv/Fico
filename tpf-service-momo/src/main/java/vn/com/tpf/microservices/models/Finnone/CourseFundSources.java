
package vn.com.tpf.microservices.models.Finnone;

import vn.com.tpf.microservices.models.Finnone.cas.MoneyType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;



/**
 * <p>Java class for courseFundSources complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="courseFundSources">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="sourceOfFund" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="sourceAmount" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "courseFundSources", propOrder = {
    "sourceOfFund",
    "sourceAmount"
})
public class CourseFundSources {

    @XmlElement(required = true)
    protected String sourceOfFund;
    @XmlElement(required = true)
    protected MoneyType sourceAmount;

    /**
     * Gets the value of the sourceOfFund property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourceOfFund() {
        return sourceOfFund;
    }

    /**
     * Sets the value of the sourceOfFund property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourceOfFund(String value) {
        this.sourceOfFund = value;
    }

    /**
     * Gets the value of the sourceAmount property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getSourceAmount() {
        return sourceAmount;
    }

    /**
     * Sets the value of the sourceAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setSourceAmount(MoneyType value) {
        this.sourceAmount = value;
    }

}
