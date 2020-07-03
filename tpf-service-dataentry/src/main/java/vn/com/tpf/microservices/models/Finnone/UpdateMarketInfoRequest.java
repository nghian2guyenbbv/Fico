
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
 *         &lt;element name="customerParameter" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}customerParameter"/>
 *         &lt;element name="marketInformation" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}marketInformation"/>
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
    "customerParameter",
    "marketInformation"
})
@XmlRootElement(name = "updateMarketInfoRequest")
public class UpdateMarketInfoRequest {

    @XmlElement(required = true)
    protected AppParameter appParameter;
    @XmlElement(required = true)
    protected CustomerParameter customerParameter;
    @XmlElement(required = true)
    protected MarketInformation marketInformation;

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
     * Gets the value of the customerParameter property.
     * 
     * @return
     *     possible object is
     *     {@link CustomerParameter }
     *     
     */
    public CustomerParameter getCustomerParameter() {
        return customerParameter;
    }

    /**
     * Sets the value of the customerParameter property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustomerParameter }
     *     
     */
    public void setCustomerParameter(CustomerParameter value) {
        this.customerParameter = value;
    }

    /**
     * Gets the value of the marketInformation property.
     * 
     * @return
     *     possible object is
     *     {@link MarketInformation }
     *     
     */
    public MarketInformation getMarketInformation() {
        return marketInformation;
    }

    /**
     * Sets the value of the marketInformation property.
     * 
     * @param value
     *     allowed object is
     *     {@link MarketInformation }
     *     
     */
    public void setMarketInformation(MarketInformation value) {
        this.marketInformation = value;
    }

}
