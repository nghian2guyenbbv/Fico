
package vn.com.tpf.microservices.models.Finnone.base;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Currency complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Currency">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://schema.base.ws.pro.finnone.nucleus.com}currencyId"/>
 *         &lt;element ref="{http://schema.base.ws.pro.finnone.nucleus.com}currencyISOCode"/>
 *         &lt;element ref="{http://schema.base.ws.pro.finnone.nucleus.com}currencyDescription"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Currency", propOrder = {
    "currencyId",
    "currencyISOCode",
    "currencyDescription"
})
public class Currency {

    protected long currencyId;
    @XmlElement(required = true)
    protected String currencyISOCode;
    @XmlElement(required = true)
    protected String currencyDescription;

    /**
     * Gets the value of the currencyId property.
     * 
     */
    public long getCurrencyId() {
        return currencyId;
    }

    /**
     * Sets the value of the currencyId property.
     * 
     */
    public void setCurrencyId(long value) {
        this.currencyId = value;
    }

    /**
     * Gets the value of the currencyISOCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrencyISOCode() {
        return currencyISOCode;
    }

    /**
     * Sets the value of the currencyISOCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrencyISOCode(String value) {
        this.currencyISOCode = value;
    }

    /**
     * Gets the value of the currencyDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrencyDescription() {
        return currencyDescription;
    }

    /**
     * Sets the value of the currencyDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrencyDescription(String value) {
        this.currencyDescription = value;
    }

}
