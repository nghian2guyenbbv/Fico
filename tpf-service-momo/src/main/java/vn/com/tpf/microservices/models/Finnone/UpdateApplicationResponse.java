
package vn.com.tpf.microservices.models.Finnone;

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
 *         &lt;element name="successMessage" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="failureMessage" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="customerNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="collateralNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="cifNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="vapDetailResponse" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}vapDetailResponse" maxOccurs="unbounded" minOccurs="0"/>
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
    "successMessage",
    "failureMessage",
    "customerNumber",
    "collateralNumber",
    "cifNumber",
    "vapDetailResponse",
    "appChargesResponse"
})
@XmlRootElement(name = "updateApplicationResponse")
public class UpdateApplicationResponse {

    @XmlElement(required = true)
    protected String applicationNumber;
    protected String successMessage;
    protected String failureMessage;
    protected String customerNumber;
    protected String collateralNumber;
    protected String cifNumber;
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
     * Gets the value of the successMessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSuccessMessage() {
        return successMessage;
    }

    /**
     * Sets the value of the successMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSuccessMessage(String value) {
        this.successMessage = value;
    }

    /**
     * Gets the value of the failureMessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFailureMessage() {
        return failureMessage;
    }

    /**
     * Sets the value of the failureMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFailureMessage(String value) {
        this.failureMessage = value;
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
