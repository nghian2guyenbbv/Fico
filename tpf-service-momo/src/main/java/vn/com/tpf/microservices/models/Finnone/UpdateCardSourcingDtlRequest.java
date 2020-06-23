
package vn.com.tpf.microservices.models.Finnone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="appParameter" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}appParameter"/>
 *         &lt;element name="cardSourcingDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}cardSourcingDetails"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "appParameter",
    "cardSourcingDetails"
})
@XmlRootElement(name = "updateCardSourcingDtlRequest")
public class UpdateCardSourcingDtlRequest {

    @XmlElement(required = true)
    protected AppParameter appParameter;
    @XmlElement(required = true)
    protected CardSourcingDetails cardSourcingDetails;

    /**
     * Gets the value of the appParameter property.
     * 
     * @return
     *     possible object is
     *     {@link AppParameter }
     *     
     */
    public AppParameter getAppParameter() {
        return appParameter;
    }

    /**
     * Sets the value of the appParameter property.
     * 
     * @param value
     *     allowed object is
     *     {@link AppParameter }
     *     
     */
    public void setAppParameter(AppParameter value) {
        this.appParameter = value;
    }

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

}
