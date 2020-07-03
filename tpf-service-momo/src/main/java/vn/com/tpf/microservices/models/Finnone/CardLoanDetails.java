
package vn.com.tpf.microservices.models.Finnone;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for cardLoanDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="cardLoanDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cardSourcingDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}cardSourcingDetails"/>
 *         &lt;element name="cardDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}cardDetails" maxOccurs="4" minOccurs="0"/>
 *         &lt;element name="pricingCode" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="surrogateCode" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="delieveryOption" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cardLoanDetails", propOrder = {
    "cardSourcingDetails",
    "cardDetails",
    "pricingCode",
    "surrogateCode",
    "delieveryOption"
})
public class CardLoanDetails {

    @XmlElement(required = true)
    protected CardSourcingDetails cardSourcingDetails;
    protected List<CardDetails> cardDetails;
    protected String pricingCode;
    protected String surrogateCode;
    protected String delieveryOption;

    /**
     * Gets the value of the cardSourcingDetails property.
     * 
     * @return
     *     possible object is
     *     {@link CardSourcingDetails }
     *     
     */
    public CardSourcingDetails getCardSourcingDetails() {
        return cardSourcingDetails;
    }

    /**
     * Sets the value of the cardSourcingDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link CardSourcingDetails }
     *     
     */
    public void setCardSourcingDetails(CardSourcingDetails value) {
        this.cardSourcingDetails = value;
    }

    /**
     * Gets the value of the cardDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cardDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCardDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CardDetails }
     * 
     * 
     */
    public List<CardDetails> getCardDetails() {
        if (cardDetails == null) {
            cardDetails = new ArrayList<CardDetails>();
        }
        return this.cardDetails;
    }

    /**
     * Gets the value of the pricingCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPricingCode() {
        return pricingCode;
    }

    /**
     * Sets the value of the pricingCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPricingCode(String value) {
        this.pricingCode = value;
    }

    /**
     * Gets the value of the surrogateCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSurrogateCode() {
        return surrogateCode;
    }

    /**
     * Sets the value of the surrogateCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSurrogateCode(String value) {
        this.surrogateCode = value;
    }

    /**
     * Gets the value of the delieveryOption property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDelieveryOption() {
        return delieveryOption;
    }

    /**
     * Sets the value of the delieveryOption property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDelieveryOption(String value) {
        this.delieveryOption = value;
    }

}
