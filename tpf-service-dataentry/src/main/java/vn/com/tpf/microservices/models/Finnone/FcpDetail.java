
package vn.com.tpf.microservices.models.Finnone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for fcpDetail complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="fcpDetail">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="installmentRoundingMethod" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="interestRoundingMethod" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="installmentRoundingPrecision" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="interestRoundingPrecision" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fcpDetail", propOrder = {
    "installmentRoundingMethod",
    "interestRoundingMethod",
    "installmentRoundingPrecision",
    "interestRoundingPrecision"
})
public class FcpDetail {

    protected Integer installmentRoundingMethod;
    protected Integer interestRoundingMethod;
    protected Integer installmentRoundingPrecision;
    protected Integer interestRoundingPrecision;

    /**
     * Gets the value of the installmentRoundingMethod property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getInstallmentRoundingMethod() {
        return installmentRoundingMethod;
    }

    /**
     * Sets the value of the installmentRoundingMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setInstallmentRoundingMethod(Integer value) {
        this.installmentRoundingMethod = value;
    }

    /**
     * Gets the value of the interestRoundingMethod property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getInterestRoundingMethod() {
        return interestRoundingMethod;
    }

    /**
     * Sets the value of the interestRoundingMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setInterestRoundingMethod(Integer value) {
        this.interestRoundingMethod = value;
    }

    /**
     * Gets the value of the installmentRoundingPrecision property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getInstallmentRoundingPrecision() {
        return installmentRoundingPrecision;
    }

    /**
     * Sets the value of the installmentRoundingPrecision property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setInstallmentRoundingPrecision(Integer value) {
        this.installmentRoundingPrecision = value;
    }

    /**
     * Gets the value of the interestRoundingPrecision property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getInterestRoundingPrecision() {
        return interestRoundingPrecision;
    }

    /**
     * Sets the value of the interestRoundingPrecision property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setInterestRoundingPrecision(Integer value) {
        this.interestRoundingPrecision = value;
    }

}
