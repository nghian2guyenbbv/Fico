
package vn.com.tpf.microservices.models.Finnone;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

import vn.com.tpf.microservices.models.Finnone.cas.MoneyType;


/**
 * <p>Java class for occupationInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="occupationInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="occupationType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="employerCode" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="natureOfBusiness" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="industry" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="employmentStatus" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="fromDate" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType" minOccurs="0"/>
 *         &lt;element name="toDate" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType" minOccurs="0"/>
 *         &lt;element name="organizationName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="natureOfProfession" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="natureOfOccupation" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="departmentName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="designation" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="employeeNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="isMajorEmployment" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType"/>
 *         &lt;element name="remarks" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0255Type" minOccurs="0"/>
 *         &lt;element name="registrationNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="yearsInProfession" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="employmentType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="grossYearlyIncome" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="contractExpiryDate" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType" minOccurs="0"/>
 *         &lt;element name="incomeFromProfesssion" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="monthlyIncome" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="subIndustry" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="typeOfCompany" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="orgWebURL" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="ageOfRetirement" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="dateOfEstablishment" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType" minOccurs="0"/>
 *         &lt;element name="lengthOfBusiness" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="typeOfOwnership" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="employmentLocation" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="yearsInOccupation" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="yearsInJob" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="monthsInJob" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="regionalData" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}regionalData" minOccurs="0"/>
 *         &lt;element name="dynamicFormDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}dynamicFormDetails" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "occupationInfo", propOrder = {
    "occupationType",
    "employerCode",
    "natureOfBusiness",
    "industry",
    "employmentStatus",
    "fromDate",
    "toDate",
    "organizationName",
    "natureOfProfession",
    "natureOfOccupation",
    "departmentName",
    "designation",
    "employeeNumber",
    "isMajorEmployment",
    "remarks",
    "registrationNumber",
    "yearsInProfession",
    "employmentType",
    "grossYearlyIncome",
    "contractExpiryDate",
    "incomeFromProfesssion",
    "monthlyIncome",
    "subIndustry",
    "typeOfCompany",
    "orgWebURL",
    "ageOfRetirement",
    "dateOfEstablishment",
    "lengthOfBusiness",
    "typeOfOwnership",
    "employmentLocation",
    "yearsInOccupation",
    "yearsInJob",
    "monthsInJob",
    "regionalData",
    "dynamicFormDetails"
})
public class OccupationInfo {

    @XmlElement(required = true)
    protected String occupationType;
    protected String employerCode;
    protected String natureOfBusiness;
    protected String industry;
    protected String employmentStatus;
    protected XMLGregorianCalendar fromDate;
    protected XMLGregorianCalendar toDate;
    protected String organizationName;
    protected String natureOfProfession;
    protected String natureOfOccupation;
    protected String departmentName;
    protected String designation;
    protected String employeeNumber;
    protected int isMajorEmployment;
    protected String remarks;
    protected String registrationNumber;
    protected Integer yearsInProfession;
    protected String employmentType;
    protected MoneyType grossYearlyIncome;
    protected XMLGregorianCalendar contractExpiryDate;
    protected MoneyType incomeFromProfesssion;
    protected MoneyType monthlyIncome;
    protected String subIndustry;
    protected String typeOfCompany;
    protected String orgWebURL;
    protected Integer ageOfRetirement;
    protected XMLGregorianCalendar dateOfEstablishment;
    protected Integer lengthOfBusiness;
    protected String typeOfOwnership;
    protected String employmentLocation;
    protected Integer yearsInOccupation;
    protected Integer yearsInJob;
    protected Integer monthsInJob;
    protected RegionalData regionalData;
    protected List<DynamicFormDetails> dynamicFormDetails;

    /**
     * Gets the value of the occupationType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOccupationType() {
        return occupationType;
    }

    /**
     * Sets the value of the occupationType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOccupationType(String value) {
        this.occupationType = value;
    }

    /**
     * Gets the value of the employerCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmployerCode() {
        return employerCode;
    }

    /**
     * Sets the value of the employerCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmployerCode(String value) {
        this.employerCode = value;
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
     * Gets the value of the employmentStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmploymentStatus() {
        return employmentStatus;
    }

    /**
     * Sets the value of the employmentStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmploymentStatus(String value) {
        this.employmentStatus = value;
    }

    /**
     * Gets the value of the fromDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFromDate() {
        return fromDate;
    }

    /**
     * Sets the value of the fromDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFromDate(XMLGregorianCalendar value) {
        this.fromDate = value;
    }

    /**
     * Gets the value of the toDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getToDate() {
        return toDate;
    }

    /**
     * Sets the value of the toDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setToDate(XMLGregorianCalendar value) {
        this.toDate = value;
    }

    /**
     * Gets the value of the organizationName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrganizationName() {
        return organizationName;
    }

    /**
     * Sets the value of the organizationName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrganizationName(String value) {
        this.organizationName = value;
    }

    /**
     * Gets the value of the natureOfProfession property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNatureOfProfession() {
        return natureOfProfession;
    }

    /**
     * Sets the value of the natureOfProfession property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNatureOfProfession(String value) {
        this.natureOfProfession = value;
    }

    /**
     * Gets the value of the natureOfOccupation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNatureOfOccupation() {
        return natureOfOccupation;
    }

    /**
     * Sets the value of the natureOfOccupation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNatureOfOccupation(String value) {
        this.natureOfOccupation = value;
    }

    /**
     * Gets the value of the departmentName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDepartmentName() {
        return departmentName;
    }

    /**
     * Sets the value of the departmentName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDepartmentName(String value) {
        this.departmentName = value;
    }

    /**
     * Gets the value of the designation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDesignation() {
        return designation;
    }

    /**
     * Sets the value of the designation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDesignation(String value) {
        this.designation = value;
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
     * Gets the value of the isMajorEmployment property.
     * 
     */
    public int getIsMajorEmployment() {
        return isMajorEmployment;
    }

    /**
     * Sets the value of the isMajorEmployment property.
     * 
     */
    public void setIsMajorEmployment(int value) {
        this.isMajorEmployment = value;
    }

    /**
     * Gets the value of the remarks property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * Sets the value of the remarks property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRemarks(String value) {
        this.remarks = value;
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
     * Gets the value of the yearsInProfession property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getYearsInProfession() {
        return yearsInProfession;
    }

    /**
     * Sets the value of the yearsInProfession property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setYearsInProfession(Integer value) {
        this.yearsInProfession = value;
    }

    /**
     * Gets the value of the employmentType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmploymentType() {
        return employmentType;
    }

    /**
     * Sets the value of the employmentType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmploymentType(String value) {
        this.employmentType = value;
    }

    /**
     * Gets the value of the grossYearlyIncome property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getGrossYearlyIncome() {
        return grossYearlyIncome;
    }

    /**
     * Sets the value of the grossYearlyIncome property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setGrossYearlyIncome(MoneyType value) {
        this.grossYearlyIncome = value;
    }

    /**
     * Gets the value of the contractExpiryDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getContractExpiryDate() {
        return contractExpiryDate;
    }

    /**
     * Sets the value of the contractExpiryDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setContractExpiryDate(XMLGregorianCalendar value) {
        this.contractExpiryDate = value;
    }

    /**
     * Gets the value of the incomeFromProfesssion property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getIncomeFromProfesssion() {
        return incomeFromProfesssion;
    }

    /**
     * Sets the value of the incomeFromProfesssion property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setIncomeFromProfesssion(MoneyType value) {
        this.incomeFromProfesssion = value;
    }

    /**
     * Gets the value of the monthlyIncome property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getMonthlyIncome() {
        return monthlyIncome;
    }

    /**
     * Sets the value of the monthlyIncome property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setMonthlyIncome(MoneyType value) {
        this.monthlyIncome = value;
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
     * Gets the value of the typeOfCompany property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTypeOfCompany() {
        return typeOfCompany;
    }

    /**
     * Sets the value of the typeOfCompany property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTypeOfCompany(String value) {
        this.typeOfCompany = value;
    }

    /**
     * Gets the value of the orgWebURL property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrgWebURL() {
        return orgWebURL;
    }

    /**
     * Sets the value of the orgWebURL property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrgWebURL(String value) {
        this.orgWebURL = value;
    }

    /**
     * Gets the value of the ageOfRetirement property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAgeOfRetirement() {
        return ageOfRetirement;
    }

    /**
     * Sets the value of the ageOfRetirement property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAgeOfRetirement(Integer value) {
        this.ageOfRetirement = value;
    }

    /**
     * Gets the value of the dateOfEstablishment property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateOfEstablishment() {
        return dateOfEstablishment;
    }

    /**
     * Sets the value of the dateOfEstablishment property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateOfEstablishment(XMLGregorianCalendar value) {
        this.dateOfEstablishment = value;
    }

    /**
     * Gets the value of the lengthOfBusiness property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getLengthOfBusiness() {
        return lengthOfBusiness;
    }

    /**
     * Sets the value of the lengthOfBusiness property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setLengthOfBusiness(Integer value) {
        this.lengthOfBusiness = value;
    }

    /**
     * Gets the value of the typeOfOwnership property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTypeOfOwnership() {
        return typeOfOwnership;
    }

    /**
     * Sets the value of the typeOfOwnership property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTypeOfOwnership(String value) {
        this.typeOfOwnership = value;
    }

    /**
     * Gets the value of the employmentLocation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmploymentLocation() {
        return employmentLocation;
    }

    /**
     * Sets the value of the employmentLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmploymentLocation(String value) {
        this.employmentLocation = value;
    }

    /**
     * Gets the value of the yearsInOccupation property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getYearsInOccupation() {
        return yearsInOccupation;
    }

    /**
     * Sets the value of the yearsInOccupation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setYearsInOccupation(Integer value) {
        this.yearsInOccupation = value;
    }

    /**
     * Gets the value of the yearsInJob property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getYearsInJob() {
        return yearsInJob;
    }

    /**
     * Sets the value of the yearsInJob property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setYearsInJob(Integer value) {
        this.yearsInJob = value;
    }

    /**
     * Gets the value of the monthsInJob property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMonthsInJob() {
        return monthsInJob;
    }

    /**
     * Sets the value of the monthsInJob property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMonthsInJob(Integer value) {
        this.monthsInJob = value;
    }

    /**
     * Gets the value of the regionalData property.
     * 
     * @return
     *     possible object is
     *     {@link RegionalData }
     *     
     */
    public RegionalData getRegionalData() {
        return regionalData;
    }

    /**
     * Sets the value of the regionalData property.
     * 
     * @param value
     *     allowed object is
     *     {@link RegionalData }
     *     
     */
    public void setRegionalData(RegionalData value) {
        this.regionalData = value;
    }

    /**
     * Gets the value of the dynamicFormDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dynamicFormDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDynamicFormDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DynamicFormDetails }
     * 
     * 
     */
    public List<DynamicFormDetails> getDynamicFormDetails() {
        if (dynamicFormDetails == null) {
            dynamicFormDetails = new ArrayList<DynamicFormDetails>();
        }
        return this.dynamicFormDetails;
    }

}
