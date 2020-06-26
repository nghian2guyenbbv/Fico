
package vn.com.tpf.microservices.models.Finnone;

import vn.com.tpf.microservices.models.Finnone.cas.MoneyType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;



/**
 * <p>Java class for contractorDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="contractorDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="architect" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="contrctor" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="costOfConstruction" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="costOfLand" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "contractorDetails", propOrder = {
    "architect",
    "contrctor",
    "costOfConstruction",
    "costOfLand"
})
public class ContractorDetails {

    protected String architect;
    protected String contrctor;
    protected MoneyType costOfConstruction;
    protected MoneyType costOfLand;

    /**
     * Gets the value of the architect property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArchitect() {
        return architect;
    }

    /**
     * Sets the value of the architect property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArchitect(String value) {
        this.architect = value;
    }

    /**
     * Gets the value of the contrctor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContrctor() {
        return contrctor;
    }

    /**
     * Sets the value of the contrctor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContrctor(String value) {
        this.contrctor = value;
    }

    /**
     * Gets the value of the costOfConstruction property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getCostOfConstruction() {
        return costOfConstruction;
    }

    /**
     * Sets the value of the costOfConstruction property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setCostOfConstruction(MoneyType value) {
        this.costOfConstruction = value;
    }

    /**
     * Gets the value of the costOfLand property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getCostOfLand() {
        return costOfLand;
    }

    /**
     * Sets the value of the costOfLand property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setCostOfLand(MoneyType value) {
        this.costOfLand = value;
    }

}
