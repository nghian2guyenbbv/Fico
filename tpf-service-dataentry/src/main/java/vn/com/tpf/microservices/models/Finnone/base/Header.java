
package vn.com.tpf.microservices.models.Finnone.base;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Header complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Header">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="version" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0016Type"/>
 *         &lt;element name="requestId" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0255Type"/>
 *         &lt;element name="serviceId" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0016Type"/>
 *         &lt;element name="operationId" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0016Type"/>
 *         &lt;element name="transmissionPrimitive" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0032Type" minOccurs="0"/>
 *         &lt;element ref="{http://schema.base.ws.pro.finnone.nucleus.com}languageCode"/>
 *         &lt;element ref="{http://schema.base.ws.pro.finnone.nucleus.com}tenantId"/>
 *         &lt;element ref="{http://schema.base.ws.pro.finnone.nucleus.com}tenantName" minOccurs="0"/>
 *         &lt;element name="transactionId" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0016Type" minOccurs="0"/>
 *         &lt;element name="userDetail" type="{http://schema.base.ws.pro.finnone.nucleus.com}UserInfoType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Header", propOrder = {
    "version",
    "requestId",
    "serviceId",
    "operationId",
    "transmissionPrimitive",
    "languageCode",
    "tenantId",
    "tenantName",
    "transactionId",
    "userDetail"
})
public class Header {

    @XmlElement(required = true)
    protected String version;
    @XmlElement(required = true)
    protected String requestId;
    @XmlElement(required = true)
    protected String serviceId;
    @XmlElement(required = true)
    protected String operationId;
    protected String transmissionPrimitive;
    @XmlElement(required = true)
    protected String languageCode;
    protected long tenantId;
    protected String tenantName;
    protected String transactionId;
    @XmlElement(required = true)
    protected UserInfoType userDetail;

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Gets the value of the requestId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Sets the value of the requestId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestId(String value) {
        this.requestId = value;
    }

    /**
     * Gets the value of the serviceId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceId() {
        return serviceId;
    }

    /**
     * Sets the value of the serviceId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceId(String value) {
        this.serviceId = value;
    }

    /**
     * Gets the value of the operationId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOperationId() {
        return operationId;
    }

    /**
     * Sets the value of the operationId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOperationId(String value) {
        this.operationId = value;
    }

    /**
     * Gets the value of the transmissionPrimitive property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransmissionPrimitive() {
        return transmissionPrimitive;
    }

    /**
     * Sets the value of the transmissionPrimitive property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransmissionPrimitive(String value) {
        this.transmissionPrimitive = value;
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
     * Gets the value of the transactionId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * Sets the value of the transactionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionId(String value) {
        this.transactionId = value;
    }

    /**
     * Gets the value of the userDetail property.
     * 
     * @return
     *     possible object is
     *     {@link UserInfoType }
     *     
     */
    public UserInfoType getUserDetail() {
        return userDetail;
    }

    /**
     * Sets the value of the userDetail property.
     * 
     * @param value
     *     allowed object is
     *     {@link UserInfoType }
     *     
     */
    public void setUserDetail(UserInfoType value) {
        this.userDetail = value;
    }

}
