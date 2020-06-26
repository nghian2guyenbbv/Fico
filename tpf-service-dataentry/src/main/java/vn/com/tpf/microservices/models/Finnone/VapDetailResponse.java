
package vn.com.tpf.microservices.models.Finnone;

import vn.com.tpf.microservices.models.Finnone.cas.MoneyType;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;



/**
 * <p>Java class for vapDetailResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="vapDetailResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="vapNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}StringType" minOccurs="0"/>
 *         &lt;element name="grossVapAmount" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="vapAmountExclOfTax" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="totalVapTax" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="vapPayCharge" type="{http://schema.base.ws.pro.finnone.nucleus.com}StringType" minOccurs="0"/>
 *         &lt;element name="payOutDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}payOutDetails" maxOccurs="10" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "vapDetailResponse", propOrder = {
    "vapNumber",
    "grossVapAmount",
    "vapAmountExclOfTax",
    "totalVapTax",
    "vapPayCharge",
    "payOutDetails"
})
public class VapDetailResponse {

    protected String vapNumber;
    protected MoneyType grossVapAmount;
    protected MoneyType vapAmountExclOfTax;
    protected MoneyType totalVapTax;
    protected String vapPayCharge;
    protected List<PayOutDetails> payOutDetails;

    /**
     * Gets the value of the vapNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVapNumber() {
        return vapNumber;
    }

    /**
     * Sets the value of the vapNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVapNumber(String value) {
        this.vapNumber = value;
    }

    /**
     * Gets the value of the grossVapAmount property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getGrossVapAmount() {
        return grossVapAmount;
    }

    /**
     * Sets the value of the grossVapAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setGrossVapAmount(MoneyType value) {
        this.grossVapAmount = value;
    }

    /**
     * Gets the value of the vapAmountExclOfTax property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getVapAmountExclOfTax() {
        return vapAmountExclOfTax;
    }

    /**
     * Sets the value of the vapAmountExclOfTax property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setVapAmountExclOfTax(MoneyType value) {
        this.vapAmountExclOfTax = value;
    }

    /**
     * Gets the value of the totalVapTax property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getTotalVapTax() {
        return totalVapTax;
    }

    /**
     * Sets the value of the totalVapTax property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setTotalVapTax(MoneyType value) {
        this.totalVapTax = value;
    }

    /**
     * Gets the value of the vapPayCharge property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVapPayCharge() {
        return vapPayCharge;
    }

    /**
     * Sets the value of the vapPayCharge property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVapPayCharge(String value) {
        this.vapPayCharge = value;
    }

    /**
     * Gets the value of the payOutDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the payOutDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPayOutDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PayOutDetails }
     * 
     * 
     */
    public List<PayOutDetails> getPayOutDetails() {
        if (payOutDetails == null) {
            payOutDetails = new ArrayList<PayOutDetails>();
        }
        return this.payOutDetails;
    }

}
