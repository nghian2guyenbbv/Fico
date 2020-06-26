
package vn.com.tpf.microservices.models.Finnone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import vn.com.tpf.microservices.models.Finnone.cas.MoneyType;


/**
 * <p>Java class for otherDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="otherDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="propertyClassification" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="propertyOwnership" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="fairMarketValue" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="totalArea" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="constructionArea" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="propertyPurpose" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="age" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="residualAge" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "otherDetails", propOrder = {
    "propertyClassification",
    "propertyOwnership",
    "fairMarketValue",
    "totalArea",
    "constructionArea",
    "propertyPurpose",
    "age",
    "residualAge"
})
public class OtherDetails {

    protected String propertyClassification;
    protected String propertyOwnership;
    protected MoneyType fairMarketValue;
    protected String totalArea;
    protected String constructionArea;
    protected String propertyPurpose;
    protected Integer age;
    protected String residualAge;

    /**
     * Gets the value of the propertyClassification property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPropertyClassification() {
        return propertyClassification;
    }

    /**
     * Sets the value of the propertyClassification property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPropertyClassification(String value) {
        this.propertyClassification = value;
    }

    /**
     * Gets the value of the propertyOwnership property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPropertyOwnership() {
        return propertyOwnership;
    }

    /**
     * Sets the value of the propertyOwnership property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPropertyOwnership(String value) {
        this.propertyOwnership = value;
    }

    /**
     * Gets the value of the fairMarketValue property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getFairMarketValue() {
        return fairMarketValue;
    }

    /**
     * Sets the value of the fairMarketValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setFairMarketValue(MoneyType value) {
        this.fairMarketValue = value;
    }

    /**
     * Gets the value of the totalArea property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalArea() {
        return totalArea;
    }

    /**
     * Sets the value of the totalArea property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalArea(String value) {
        this.totalArea = value;
    }

    /**
     * Gets the value of the constructionArea property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConstructionArea() {
        return constructionArea;
    }

    /**
     * Sets the value of the constructionArea property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConstructionArea(String value) {
        this.constructionArea = value;
    }

    /**
     * Gets the value of the propertyPurpose property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPropertyPurpose() {
        return propertyPurpose;
    }

    /**
     * Sets the value of the propertyPurpose property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPropertyPurpose(String value) {
        this.propertyPurpose = value;
    }

    /**
     * Gets the value of the age property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAge() {
        return age;
    }

    /**
     * Sets the value of the age property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAge(Integer value) {
        this.age = value;
    }

    /**
     * Gets the value of the residualAge property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResidualAge() {
        return residualAge;
    }

    /**
     * Sets the value of the residualAge property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResidualAge(String value) {
        this.residualAge = value;
    }

}
