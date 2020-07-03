
package vn.com.tpf.microservices.models.Finnone.base;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Tenant complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Tenant">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://schema.base.ws.pro.finnone.nucleus.com}tenantId"/>
 *         &lt;element ref="{http://schema.base.ws.pro.finnone.nucleus.com}tenantName" minOccurs="0"/>
 *         &lt;element ref="{http://schema.base.ws.pro.finnone.nucleus.com}languageCode"/>
 *         &lt;element ref="{http://schema.base.ws.pro.finnone.nucleus.com}currencyISOCode"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Tenant", propOrder = {
    "tenantId",
    "tenantName",
    "languageCode",
    "currencyISOCode"
})
public class Tenant {

    protected long tenantId;
    protected String tenantName;
    @XmlElement(required = true)
    protected String languageCode;
    @XmlElement(required = true)
    protected String currencyISOCode;

    /**
     * Gets the value of the tenantId property.
     * 
     */
    public long getTenantId() {
        return tenantId;
    }

    /**
     * Sets the value of the tenantId property.
     * 
     */
    public void setTenantId(long value) {
        this.tenantId = value;
    }

    /**
     * Gets the value of the tenantName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTenantName() {
        return tenantName;
    }

    /**
     * Sets the value of the tenantName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTenantName(String value) {
        this.tenantName = value;
    }

    /**
     * Gets the value of the languageCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLanguageCode() {
        return languageCode;
    }

    /**
     * Sets the value of the languageCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLanguageCode(String value) {
        this.languageCode = value;
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

}
