
package vn.com.tpf.microservices.models.Finnone;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element name="applicationNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="customerNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="cifNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="responseAdditionalData" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="collateralNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="interestRate" type="{http://schema.base.ws.pro.finnone.nucleus.com}DecimalType"/>
 *         &lt;element name="vapDetailResponse" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}vapDetailResponse" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="appChargesResponse" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}appChargesResponse" maxOccurs="unbounded" minOccurs="0"/>
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
    "applicationNumber",
    "customerNumber",
    "cifNumber",
    "responseAdditionalData",
    "collateralNumber",
    "interestRate",
    "vapDetailResponse",
    "appChargesResponse"
})
@XmlRootElement(name = "createApplicationResponse")
public class CreateApplicationResponse {

    @XmlElement(required = true)
    protected String applicationNumber;
    protected String customerNumber;
    protected String cifNumber;
    protected String responseAdditionalData;
    protected String collateralNumber;
    @XmlElement(required = true)
    protected BigDecimal interestRate;
    protected List<VapDetailResponse> vapDetailResponse;
    protected List<AppChargesResponse> appChargesResponse;

    /**
     * Gets the value of the applicationNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApplicationNumber() {
        return applicationNumber;
    }

    /**
     * Sets the value of the applicationNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApplicationNumber(String value) {
        this.applicationNumber = value;
    }

    /**
     * Gets the value of the customerNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerNumber() {
        return customerNumber;
    }

    /**
     * Sets the value of the customerNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerNumber(String value) {
        this.customerNumber = value;
    }

    /**
     * Gets the value of the cifNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCifNumber() {
        return cifNumber;
    }

    /**
     * Sets the value of the cifNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCifNumber(String value) {
        this.cifNumber = value;
    }

    /**
     * Gets the value of the responseAdditionalData property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponseAdditionalData() {
        return responseAdditionalData;
    }

    /**
     * Sets the value of the responseAdditionalData property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponseAdditionalData(String value) {
        this.responseAdditionalData = value;
    }

    /**
     * Gets the value of the collateralNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCollateralNumber() {
        return collateralNumber;
    }

    /**
     * Sets the value of the collateralNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCollateralNumber(String value) {
        this.collateralNumber = value;
    }

    /**
     * Gets the value of the interestRate property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getInterestRate() {
        return interestRate;
    }

    /**
     * Sets the value of the interestRate property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setInterestRate(BigDecimal value) {
        this.interestRate = value;
    }

    /**
     * Gets the value of the vapDetailResponse property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the vapDetailResponse property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVapDetailResponse().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link VapDetailResponse }
     * 
     * 
     */
    public List<VapDetailResponse> getVapDetailResponse() {
        if (vapDetailResponse == null) {
            vapDetailResponse = new ArrayList<VapDetailResponse>();
        }
        return this.vapDetailResponse;
    }

    /**
     * Gets the value of the appChargesResponse property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the appChargesResponse property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAppChargesResponse().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AppChargesResponse }
     * 
     * 
     */
    public List<AppChargesResponse> getAppChargesResponse() {
        if (appChargesResponse == null) {
            appChargesResponse = new ArrayList<AppChargesResponse>();
        }
        return this.appChargesResponse;
    }

}
