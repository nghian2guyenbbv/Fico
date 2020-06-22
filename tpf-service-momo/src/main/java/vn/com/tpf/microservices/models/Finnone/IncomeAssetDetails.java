
package vn.com.tpf.microservices.models.Finnone;

import vn.com.tpf.microservices.models.Finnone.cas.MoneyType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;



/**
 * <p>Java class for incomeAssetDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="incomeAssetDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="incomeDetailAssetType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="incomeDetailAssetCategory" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="assetTypeDescription" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0255Type"/>
 *         &lt;element name="assetValue" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "incomeAssetDetails", propOrder = {
    "incomeDetailAssetType",
    "incomeDetailAssetCategory",
    "assetTypeDescription",
    "assetValue"
})
public class IncomeAssetDetails {

    @XmlElement(required = true)
    protected String incomeDetailAssetType;
    @XmlElement(required = true)
    protected String incomeDetailAssetCategory;
    @XmlElement(required = true)
    protected String assetTypeDescription;
    @XmlElement(required = true)
    protected MoneyType assetValue;

    /**
     * Gets the value of the incomeDetailAssetType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIncomeDetailAssetType() {
        return incomeDetailAssetType;
    }

    /**
     * Sets the value of the incomeDetailAssetType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIncomeDetailAssetType(String value) {
        this.incomeDetailAssetType = value;
    }

    /**
     * Gets the value of the incomeDetailAssetCategory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIncomeDetailAssetCategory() {
        return incomeDetailAssetCategory;
    }

    /**
     * Sets the value of the incomeDetailAssetCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIncomeDetailAssetCategory(String value) {
        this.incomeDetailAssetCategory = value;
    }

    /**
     * Gets the value of the assetTypeDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssetTypeDescription() {
        return assetTypeDescription;
    }

    /**
     * Sets the value of the assetTypeDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssetTypeDescription(String value) {
        this.assetTypeDescription = value;
    }

    /**
     * Gets the value of the assetValue property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getAssetValue() {
        return assetValue;
    }

    /**
     * Sets the value of the assetValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setAssetValue(MoneyType value) {
        this.assetValue = value;
    }

}
