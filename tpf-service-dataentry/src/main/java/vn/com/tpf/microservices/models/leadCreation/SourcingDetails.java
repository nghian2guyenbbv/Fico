
package vn.com.tpf.microservices.models.leadCreation;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for sourcingDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="sourcingDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="sourcingChannel" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="alternateChannelMode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="sourcingBranch" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="applicationNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="channelType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cardsRmName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="employeeName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="employeeNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="employeeMobileNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="leadBranch" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="referalName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="showrooms" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dealers" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="referralCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FieldsourcingDetails1" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FieldsourcingDetails2" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FieldsourcingDetails3" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FieldsourcingDetails4" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FieldsourcingDetails5" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FieldsourcingDetails6" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FieldsourcingDetails7" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FieldsourcingDetails8" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FieldsourcingDetails9" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FieldsourcingDetails10" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sourcingDetails", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", propOrder = {
    "sourcingChannel",
    "alternateChannelMode",
    "sourcingBranch",
    "applicationNumber",
    "channelType",
    "cardsRmName",
    "employeeName",
    "employeeNumber",
    "employeeMobileNumber",
    "leadBranch",
    "referalName",
    "showrooms",
    "dealers",
    "referralCode",
    "fieldsourcingDetails1",
    "fieldsourcingDetails2",
    "fieldsourcingDetails3",
    "fieldsourcingDetails4",
    "fieldsourcingDetails5",
    "fieldsourcingDetails6",
    "fieldsourcingDetails7",
    "fieldsourcingDetails8",
    "fieldsourcingDetails9",
    "fieldsourcingDetails10"
})
public class SourcingDetails {

    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String sourcingChannel;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String alternateChannelMode;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String sourcingBranch;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService")
    protected String applicationNumber;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService")
    protected String channelType;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService")
    protected String cardsRmName;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String employeeName;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String employeeNumber;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService")
    protected String employeeMobileNumber;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService")
    protected String leadBranch;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService")
    protected String referalName;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService")
    protected String showrooms;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService")
    protected String dealers;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService")
    protected String referralCode;
    @XmlElement(name = "FieldsourcingDetails1", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String fieldsourcingDetails1;
    @XmlElement(name = "FieldsourcingDetails2", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String fieldsourcingDetails2;
    @XmlElement(name = "FieldsourcingDetails3", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String fieldsourcingDetails3;
    @XmlElement(name = "FieldsourcingDetails4", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String fieldsourcingDetails4;
    @XmlElement(name = "FieldsourcingDetails5", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String fieldsourcingDetails5;
    @XmlElement(name = "FieldsourcingDetails6", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String fieldsourcingDetails6;
    @XmlElement(name = "FieldsourcingDetails7", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String fieldsourcingDetails7;
    @XmlElement(name = "FieldsourcingDetails8", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String fieldsourcingDetails8;
    @XmlElement(name = "FieldsourcingDetails9", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String fieldsourcingDetails9;
    @XmlElement(name = "FieldsourcingDetails10", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String fieldsourcingDetails10;

    /**
     * Gets the value of the sourcingChannel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourcingChannel() {
        return sourcingChannel;
    }

    /**
     * Sets the value of the sourcingChannel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourcingChannel(String value) {
        this.sourcingChannel = value;
    }

    /**
     * Gets the value of the alternateChannelMode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlternateChannelMode() {
        return alternateChannelMode;
    }

    /**
     * Sets the value of the alternateChannelMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlternateChannelMode(String value) {
        this.alternateChannelMode = value;
    }

    /**
     * Gets the value of the sourcingBranch property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourcingBranch() {
        return sourcingBranch;
    }

    /**
     * Sets the value of the sourcingBranch property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourcingBranch(String value) {
        this.sourcingBranch = value;
    }

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
     * Gets the value of the channelType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChannelType() {
        return channelType;
    }

    /**
     * Sets the value of the channelType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChannelType(String value) {
        this.channelType = value;
    }

    /**
     * Gets the value of the cardsRmName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCardsRmName() {
        return cardsRmName;
    }

    /**
     * Sets the value of the cardsRmName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCardsRmName(String value) {
        this.cardsRmName = value;
    }

    /**
     * Gets the value of the employeeName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmployeeName() {
        return employeeName;
    }

    /**
     * Sets the value of the employeeName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmployeeName(String value) {
        this.employeeName = value;
    }

    /**
     * Gets the value of the employeeNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmployeeNumber() {
        return employeeNumber;
    }

    /**
     * Sets the value of the employeeNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmployeeNumber(String value) {
        this.employeeNumber = value;
    }

    /**
     * Gets the value of the employeeMobileNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmployeeMobileNumber() {
        return employeeMobileNumber;
    }

    /**
     * Sets the value of the employeeMobileNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmployeeMobileNumber(String value) {
        this.employeeMobileNumber = value;
    }

    /**
     * Gets the value of the leadBranch property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLeadBranch() {
        return leadBranch;
    }

    /**
     * Sets the value of the leadBranch property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLeadBranch(String value) {
        this.leadBranch = value;
    }

    /**
     * Gets the value of the referalName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReferalName() {
        return referalName;
    }

    /**
     * Sets the value of the referalName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReferalName(String value) {
        this.referalName = value;
    }

    /**
     * Gets the value of the showrooms property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShowrooms() {
        return showrooms;
    }

    /**
     * Sets the value of the showrooms property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShowrooms(String value) {
        this.showrooms = value;
    }

    /**
     * Gets the value of the dealers property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDealers() {
        return dealers;
    }

    /**
     * Sets the value of the dealers property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDealers(String value) {
        this.dealers = value;
    }

    /**
     * Gets the value of the referralCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReferralCode() {
        return referralCode;
    }

    /**
     * Sets the value of the referralCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReferralCode(String value) {
        this.referralCode = value;
    }

    /**
     * Gets the value of the fieldsourcingDetails1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFieldsourcingDetails1() {
        return fieldsourcingDetails1;
    }

    /**
     * Sets the value of the fieldsourcingDetails1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFieldsourcingDetails1(String value) {
        this.fieldsourcingDetails1 = value;
    }

    /**
     * Gets the value of the fieldsourcingDetails2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFieldsourcingDetails2() {
        return fieldsourcingDetails2;
    }

    /**
     * Sets the value of the fieldsourcingDetails2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFieldsourcingDetails2(String value) {
        this.fieldsourcingDetails2 = value;
    }

    /**
     * Gets the value of the fieldsourcingDetails3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFieldsourcingDetails3() {
        return fieldsourcingDetails3;
    }

    /**
     * Sets the value of the fieldsourcingDetails3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFieldsourcingDetails3(String value) {
        this.fieldsourcingDetails3 = value;
    }

    /**
     * Gets the value of the fieldsourcingDetails4 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFieldsourcingDetails4() {
        return fieldsourcingDetails4;
    }

    /**
     * Sets the value of the fieldsourcingDetails4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFieldsourcingDetails4(String value) {
        this.fieldsourcingDetails4 = value;
    }

    /**
     * Gets the value of the fieldsourcingDetails5 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFieldsourcingDetails5() {
        return fieldsourcingDetails5;
    }

    /**
     * Sets the value of the fieldsourcingDetails5 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFieldsourcingDetails5(String value) {
        this.fieldsourcingDetails5 = value;
    }

    /**
     * Gets the value of the fieldsourcingDetails6 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFieldsourcingDetails6() {
        return fieldsourcingDetails6;
    }

    /**
     * Sets the value of the fieldsourcingDetails6 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFieldsourcingDetails6(String value) {
        this.fieldsourcingDetails6 = value;
    }

    /**
     * Gets the value of the fieldsourcingDetails7 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFieldsourcingDetails7() {
        return fieldsourcingDetails7;
    }

    /**
     * Sets the value of the fieldsourcingDetails7 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFieldsourcingDetails7(String value) {
        this.fieldsourcingDetails7 = value;
    }

    /**
     * Gets the value of the fieldsourcingDetails8 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFieldsourcingDetails8() {
        return fieldsourcingDetails8;
    }

    /**
     * Sets the value of the fieldsourcingDetails8 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFieldsourcingDetails8(String value) {
        this.fieldsourcingDetails8 = value;
    }

    /**
     * Gets the value of the fieldsourcingDetails9 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFieldsourcingDetails9() {
        return fieldsourcingDetails9;
    }

    /**
     * Sets the value of the fieldsourcingDetails9 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFieldsourcingDetails9(String value) {
        this.fieldsourcingDetails9 = value;
    }

    /**
     * Gets the value of the fieldsourcingDetails10 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFieldsourcingDetails10() {
        return fieldsourcingDetails10;
    }

    /**
     * Sets the value of the fieldsourcingDetails10 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFieldsourcingDetails10(String value) {
        this.fieldsourcingDetails10 = value;
    }

}
