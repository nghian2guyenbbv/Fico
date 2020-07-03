
package vn.com.tpf.microservices.models.Finnone;

import vn.com.tpf.microservices.models.Finnone.cas.MoneyType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;



/**
 * <p>Java class for institutionInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="institutionInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="instName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="instDesc" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="incorporationDate" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType"/>
 *         &lt;element name="registrationType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="registrationNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="registrationExpiryDate" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType" minOccurs="0"/>
 *         &lt;element name="constitution" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="customerGroup" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="industryClasfctn" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="industry" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="subIndustry" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="natureOfBusiness" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="totalYearsOfOccupation" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="websiteUrl" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="netWorth" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="turnOver" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="monthlySale" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="monthlyPurchase" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="employeeStrength" type="{http://schema.base.ws.pro.finnone.nucleus.com}LongType" minOccurs="0"/>
 *         &lt;element name="isClassifiedCustomer" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "institutionInfo", propOrder = {
    "instName",
    "instDesc",
    "incorporationDate",
    "registrationType",
    "registrationNumber",
    "registrationExpiryDate",
    "constitution",
    "customerGroup",
    "industryClasfctn",
    "industry",
    "subIndustry",
    "natureOfBusiness",
    "totalYearsOfOccupation",
    "websiteUrl",
    "netWorth",
    "turnOver",
    "monthlySale",
    "monthlyPurchase",
    "employeeStrength",
    "isClassifiedCustomer"
})
public class InstitutionInfo {

    @XmlElement(required = true)
    protected String instName;
    protected String instDesc;
    @XmlElement(required = true)
    protected XMLGregorianCalendar incorporationDate;
    protected String registrationType;
    protected String registrationNumber;
    protected XMLGregorianCalendar registrationExpiryDate;
    @XmlElement(required = true)
    protected String constitution;
    protected String customerGroup;
    @XmlElement(required = true)
    protected String industryClasfctn;
    @XmlElement(required = true)
    protected String industry;
    protected String subIndustry;
    protected String natureOfBusiness;
    protected String totalYearsOfOccupation;
    protected String websiteUrl;
    protected MoneyType netWorth;
    protected MoneyType turnOver;
    protected MoneyType monthlySale;
    protected MoneyType monthlyPurchase;
    protected Long employeeStrength;
    protected Boolean isClassifiedCustomer;

    /**
     * Gets the value of the instName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstName() {
        return instName;
    }

    /**
     * Sets the value of the instName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstName(String value) {
        this.instName = value;
    }

    /**
     * Gets the value of the instDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstDesc() {
        return instDesc;
    }

    /**
     * Sets the value of the instDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstDesc(String value) {
        this.instDesc = value;
    }

    /**
     * Gets the value of the incorporationDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getIncorporationDate() {
        return incorporationDate;
    }

    /**
     * Sets the value of the incorporationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setIncorporationDate(XMLGregorianCalendar value) {
        this.incorporationDate = value;
    }

    /**
     * Gets the value of the registrationType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegistrationType() {
        return registrationType;
    }

    /**
     * Sets the value of the registrationType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegistrationType(String value) {
        this.registrationType = value;
    }

    /**
     * Gets the value of the registrationNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegistrationNumber() {
        return registrationNumber;
    }

    /**
     * Sets the value of the registrationNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegistrationNumber(String value) {
        this.registrationNumber = value;
    }

    /**
     * Gets the value of the registrationExpiryDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getRegistrationExpiryDate() {
        return registrationExpiryDate;
    }

    /**
     * Sets the value of the registrationExpiryDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setRegistrationExpiryDate(XMLGregorianCalendar value) {
        this.registrationExpiryDate = value;
    }

    /**
     * Gets the value of the constitution property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConstitution() {
        return constitution;
    }

    /**
     * Sets the value of the constitution property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConstitution(String value) {
        this.constitution = value;
    }

    /**
     * Gets the value of the customerGroup property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerGroup() {
        return customerGroup;
    }

    /**
     * Sets the value of the customerGroup property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerGroup(String value) {
        this.customerGroup = value;
    }

    /**
     * Gets the value of the industryClasfctn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndustryClasfctn() {
        return industryClasfctn;
    }

    /**
     * Sets the value of the industryClasfctn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndustryClasfctn(String value) {
        this.industryClasfctn = value;
    }

    /**
     * Gets the value of the industry property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndustry() {
        return industry;
    }

    /**
     * Sets the value of the industry property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndustry(String value) {
        this.industry = value;
    }

    /**
     * Gets the value of the subIndustry property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubIndustry() {
        return subIndustry;
    }

    /**
     * Sets the value of the subIndustry property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubIndustry(String value) {
        this.subIndustry = value;
    }

    /**
     * Gets the value of the natureOfBusiness property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNatureOfBusiness() {
        return natureOfBusiness;
    }

    /**
     * Sets the value of the natureOfBusiness property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNatureOfBusiness(String value) {
        this.natureOfBusiness = value;
    }

    /**
     * Gets the value of the totalYearsOfOccupation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalYearsOfOccupation() {
        return totalYearsOfOccupation;
    }

    /**
     * Sets the value of the totalYearsOfOccupation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalYearsOfOccupation(String value) {
        this.totalYearsOfOccupation = value;
    }

    /**
     * Gets the value of the websiteUrl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWebsiteUrl() {
        return websiteUrl;
    }

    /**
     * Sets the value of the websiteUrl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWebsiteUrl(String value) {
        this.websiteUrl = value;
    }

    /**
     * Gets the value of the netWorth property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getNetWorth() {
        return netWorth;
    }

    /**
     * Sets the value of the netWorth property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setNetWorth(MoneyType value) {
        this.netWorth = value;
    }

    /**
     * Gets the value of the turnOver property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getTurnOver() {
        return turnOver;
    }

    /**
     * Sets the value of the turnOver property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setTurnOver(MoneyType value) {
        this.turnOver = value;
    }

    /**
     * Gets the value of the monthlySale property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getMonthlySale() {
        return monthlySale;
    }

    /**
     * Sets the value of the monthlySale property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setMonthlySale(MoneyType value) {
        this.monthlySale = value;
    }

    /**
     * Gets the value of the monthlyPurchase property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getMonthlyPurchase() {
        return monthlyPurchase;
    }

    /**
     * Sets the value of the monthlyPurchase property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setMonthlyPurchase(MoneyType value) {
        this.monthlyPurchase = value;
    }

    /**
     * Gets the value of the employeeStrength property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getEmployeeStrength() {
        return employeeStrength;
    }

    /**
     * Sets the value of the employeeStrength property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setEmployeeStrength(Long value) {
        this.employeeStrength = value;
    }

    /**
     * Gets the value of the isClassifiedCustomer property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsClassifiedCustomer() {
        return isClassifiedCustomer;
    }

    /**
     * Sets the value of the isClassifiedCustomer property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsClassifiedCustomer(Boolean value) {
        this.isClassifiedCustomer = value;
    }

}
