
package vn.com.tpf.microservices.models.Finnone;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for applicationDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="applicationDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="loanApplicationType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="officer" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="sourcingChannel" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="groupName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="pivotName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="dst" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="cre" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="alternateChannelMode" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="sourcingBranch" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="employeeName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="employeeNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="showroom" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="dealer" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="salesOfficer" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="employeeContactNum" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}phoneNumber" minOccurs="0"/>
 *         &lt;element name="referralcode" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="applicationFormNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="connectorCode" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="dseCode" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "applicationDetails", propOrder = {
    "loanApplicationType",
    "officer",
    "sourcingChannel",
    "groupName",
    "pivotName",
    "dst",
    "cre",
    "alternateChannelMode",
    "sourcingBranch",
    "employeeName",
    "employeeNumber",
    "showroom",
    "dealer",
    "salesOfficer",
    "employeeContactNum",
    "referralcode",
    "applicationFormNumber",
    "connectorCode",
    "dseCode"
})
public class ApplicationDetails {

    protected String loanApplicationType;
    @XmlElement(required = true)
    protected String officer;
    @XmlElement(required = true)
    protected String sourcingChannel;
    protected String groupName;
    protected String pivotName;
    protected String dst;
    protected String cre;
    protected String alternateChannelMode;
    protected String sourcingBranch;
    protected String employeeName;
    protected String employeeNumber;
    protected String showroom;
    protected String dealer;
    protected String salesOfficer;
    protected PhoneNumber employeeContactNum;
    protected String referralcode;
    protected String applicationFormNumber;
    protected String connectorCode;
    protected String dseCode;

    /**
     * Gets the value of the loanApplicationType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLoanApplicationType() {
        return loanApplicationType;
    }

    /**
     * Sets the value of the loanApplicationType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLoanApplicationType(String value) {
        this.loanApplicationType = value;
    }

    /**
     * Gets the value of the officer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOfficer() {
        return officer;
    }

    /**
     * Sets the value of the officer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOfficer(String value) {
        this.officer = value;
    }

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
     * Gets the value of the groupName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * Sets the value of the groupName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGroupName(String value) {
        this.groupName = value;
    }

    /**
     * Gets the value of the pivotName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPivotName() {
        return pivotName;
    }

    /**
     * Sets the value of the pivotName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPivotName(String value) {
        this.pivotName = value;
    }

    /**
     * Gets the value of the dst property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDst() {
        return dst;
    }

    /**
     * Sets the value of the dst property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDst(String value) {
        this.dst = value;
    }

    /**
     * Gets the value of the cre property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCre() {
        return cre;
    }

    /**
     * Sets the value of the cre property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCre(String value) {
        this.cre = value;
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
     * Gets the value of the showroom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShowroom() {
        return showroom;
    }

    /**
     * Sets the value of the showroom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShowroom(String value) {
        this.showroom = value;
    }

    /**
     * Gets the value of the dealer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDealer() {
        return dealer;
    }

    /**
     * Sets the value of the dealer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDealer(String value) {
        this.dealer = value;
    }

    /**
     * Gets the value of the salesOfficer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSalesOfficer() {
        return salesOfficer;
    }

    /**
     * Sets the value of the salesOfficer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSalesOfficer(String value) {
        this.salesOfficer = value;
    }

    /**
     * Gets the value of the employeeContactNum property.
     * 
     * @return
     *     possible object is
     *     {@link PhoneNumber }
     *     
     */
    public PhoneNumber getEmployeeContactNum() {
        return employeeContactNum;
    }

    /**
     * Sets the value of the employeeContactNum property.
     * 
     * @param value
     *     allowed object is
     *     {@link PhoneNumber }
     *     
     */
    public void setEmployeeContactNum(PhoneNumber value) {
        this.employeeContactNum = value;
    }

    /**
     * Gets the value of the referralcode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReferralcode() {
        return referralcode;
    }

    /**
     * Sets the value of the referralcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReferralcode(String value) {
        this.referralcode = value;
    }

    /**
     * Gets the value of the applicationFormNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApplicationFormNumber() {
        return applicationFormNumber;
    }

    /**
     * Sets the value of the applicationFormNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApplicationFormNumber(String value) {
        this.applicationFormNumber = value;
    }

    /**
     * Gets the value of the connectorCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConnectorCode() {
        return connectorCode;
    }

    /**
     * Sets the value of the connectorCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConnectorCode(String value) {
        this.connectorCode = value;
    }

    /**
     * Gets the value of the dseCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDseCode() {
        return dseCode;
    }

    /**
     * Sets the value of the dseCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDseCode(String value) {
        this.dseCode = value;
    }

}
