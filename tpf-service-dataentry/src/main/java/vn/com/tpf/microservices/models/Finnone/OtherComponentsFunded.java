
package vn.com.tpf.microservices.models.Finnone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import vn.com.tpf.microservices.models.Finnone.cas.MoneyType;


/**
 * <p>Java class for otherComponentsFunded complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="otherComponentsFunded">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="fundType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="capitalFunded" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="includeInAssetCost" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "otherComponentsFunded", propOrder = {
    "fundType",
    "capitalFunded",
    "includeInAssetCost"
})
public class OtherComponentsFunded {

    @XmlElement(required = true)
    protected String fundType;
    protected MoneyType capitalFunded;
    protected Boolean includeInAssetCost;

    /**
     * Gets the value of the fundType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFundType() {
        return fundType;
    }

    /**
     * Sets the value of the fundType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFundType(String value) {
        this.fundType = value;
    }

    /**
     * Gets the value of the capitalFunded property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getCapitalFunded() {
        return capitalFunded;
    }

    /**
     * Sets the value of the capitalFunded property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setCapitalFunded(MoneyType value) {
        this.capitalFunded = value;
    }

    /**
     * Gets the value of the includeInAssetCost property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIncludeInAssetCost() {
        return includeInAssetCost;
    }

    /**
     * Sets the value of the includeInAssetCost property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIncludeInAssetCost(Boolean value) {
        this.includeInAssetCost = value;
    }

}
