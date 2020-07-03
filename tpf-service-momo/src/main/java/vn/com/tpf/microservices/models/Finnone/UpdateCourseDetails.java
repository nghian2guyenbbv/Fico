
package vn.com.tpf.microservices.models.Finnone;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for updateCourseDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="updateCourseDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="universityName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="collegeName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="collegeRating" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="courseName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="enrollmentNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="courseApprovedBy" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="ifOthers" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="educationStream" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="courseDuration" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="startDate" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType" minOccurs="0"/>
 *         &lt;element name="EndDate" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType" minOccurs="0"/>
 *         &lt;element name="website" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="emailAddress" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="contactPerson" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="contactPhone" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}phoneNumber" minOccurs="0"/>
 *         &lt;element name="contactPersonEmail" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="contactMobile" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}phoneNumber" minOccurs="0"/>
 *         &lt;element name="address" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}addressDetails" minOccurs="0"/>
 *         &lt;element name="bankAccount" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}updateBankAccount" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="componentCourseFee" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}componentCourseFee" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="employmentPotential" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}employmentPotential" minOccurs="0"/>
 *         &lt;element name="educationHistories" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}educationHistories" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="courseFundSources" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}courseFundSources" maxOccurs="10" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateCourseDetails", propOrder = {
    "universityName",
    "collegeName",
    "collegeRating",
    "courseName",
    "enrollmentNumber",
    "courseApprovedBy",
    "ifOthers",
    "educationStream",
    "courseDuration",
    "startDate",
    "endDate",
    "website",
    "emailAddress",
    "contactPerson",
    "contactPhone",
    "contactPersonEmail",
    "contactMobile",
    "address",
    "bankAccount",
    "componentCourseFee",
    "employmentPotential",
    "educationHistories",
    "courseFundSources"
})
public class UpdateCourseDetails {

    @XmlElement(required = true)
    protected String universityName;
    @XmlElement(required = true)
    protected String collegeName;
    protected String collegeRating;
    protected String courseName;
    protected String enrollmentNumber;
    protected String courseApprovedBy;
    protected String ifOthers;
    protected String educationStream;
    protected Integer courseDuration;
    protected XMLGregorianCalendar startDate;
    @XmlElement(name = "EndDate")
    protected XMLGregorianCalendar endDate;
    protected String website;
    protected String emailAddress;
    protected String contactPerson;
    protected PhoneNumber contactPhone;
    protected String contactPersonEmail;
    protected PhoneNumber contactMobile;
    protected AddressDetails address;
    protected List<UpdateBankAccount> bankAccount;
    protected List<ComponentCourseFee> componentCourseFee;
    protected EmploymentPotential employmentPotential;
    protected List<EducationHistories> educationHistories;
    protected List<CourseFundSources> courseFundSources;

    /**
     * Gets the value of the universityName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUniversityName() {
        return universityName;
    }

    /**
     * Sets the value of the universityName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUniversityName(String value) {
        this.universityName = value;
    }

    /**
     * Gets the value of the collegeName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCollegeName() {
        return collegeName;
    }

    /**
     * Sets the value of the collegeName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCollegeName(String value) {
        this.collegeName = value;
    }

    /**
     * Gets the value of the collegeRating property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCollegeRating() {
        return collegeRating;
    }

    /**
     * Sets the value of the collegeRating property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCollegeRating(String value) {
        this.collegeRating = value;
    }

    /**
     * Gets the value of the courseName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * Sets the value of the courseName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCourseName(String value) {
        this.courseName = value;
    }

    /**
     * Gets the value of the enrollmentNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnrollmentNumber() {
        return enrollmentNumber;
    }

    /**
     * Sets the value of the enrollmentNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnrollmentNumber(String value) {
        this.enrollmentNumber = value;
    }

    /**
     * Gets the value of the courseApprovedBy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCourseApprovedBy() {
        return courseApprovedBy;
    }

    /**
     * Sets the value of the courseApprovedBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCourseApprovedBy(String value) {
        this.courseApprovedBy = value;
    }

    /**
     * Gets the value of the ifOthers property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIfOthers() {
        return ifOthers;
    }

    /**
     * Sets the value of the ifOthers property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIfOthers(String value) {
        this.ifOthers = value;
    }

    /**
     * Gets the value of the educationStream property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEducationStream() {
        return educationStream;
    }

    /**
     * Sets the value of the educationStream property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEducationStream(String value) {
        this.educationStream = value;
    }

    /**
     * Gets the value of the courseDuration property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCourseDuration() {
        return courseDuration;
    }

    /**
     * Sets the value of the courseDuration property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCourseDuration(Integer value) {
        this.courseDuration = value;
    }

    /**
     * Gets the value of the startDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getStartDate() {
        return startDate;
    }

    /**
     * Sets the value of the startDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setStartDate(XMLGregorianCalendar value) {
        this.startDate = value;
    }

    /**
     * Gets the value of the endDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getEndDate() {
        return endDate;
    }

    /**
     * Sets the value of the endDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setEndDate(XMLGregorianCalendar value) {
        this.endDate = value;
    }

    /**
     * Gets the value of the website property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWebsite() {
        return website;
    }

    /**
     * Sets the value of the website property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWebsite(String value) {
        this.website = value;
    }

    /**
     * Gets the value of the emailAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Sets the value of the emailAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmailAddress(String value) {
        this.emailAddress = value;
    }

    /**
     * Gets the value of the contactPerson property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContactPerson() {
        return contactPerson;
    }

    /**
     * Sets the value of the contactPerson property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContactPerson(String value) {
        this.contactPerson = value;
    }

    /**
     * Gets the value of the contactPhone property.
     * 
     * @return
     *     possible object is
     *     {@link PhoneNumber }
     *     
     */
    public PhoneNumber getContactPhone() {
        return contactPhone;
    }

    /**
     * Sets the value of the contactPhone property.
     * 
     * @param value
     *     allowed object is
     *     {@link PhoneNumber }
     *     
     */
    public void setContactPhone(PhoneNumber value) {
        this.contactPhone = value;
    }

    /**
     * Gets the value of the contactPersonEmail property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContactPersonEmail() {
        return contactPersonEmail;
    }

    /**
     * Sets the value of the contactPersonEmail property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContactPersonEmail(String value) {
        this.contactPersonEmail = value;
    }

    /**
     * Gets the value of the contactMobile property.
     * 
     * @return
     *     possible object is
     *     {@link PhoneNumber }
     *     
     */
    public PhoneNumber getContactMobile() {
        return contactMobile;
    }

    /**
     * Sets the value of the contactMobile property.
     * 
     * @param value
     *     allowed object is
     *     {@link PhoneNumber }
     *     
     */
    public void setContactMobile(PhoneNumber value) {
        this.contactMobile = value;
    }

    /**
     * Gets the value of the address property.
     * 
     * @return
     *     possible object is
     *     {@link AddressDetails }
     *     
     */
    public AddressDetails getAddress() {
        return address;
    }

    /**
     * Sets the value of the address property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddressDetails }
     *     
     */
    public void setAddress(AddressDetails value) {
        this.address = value;
    }

    /**
     * Gets the value of the bankAccount property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bankAccount property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBankAccount().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UpdateBankAccount }
     * 
     * 
     */
    public List<UpdateBankAccount> getBankAccount() {
        if (bankAccount == null) {
            bankAccount = new ArrayList<UpdateBankAccount>();
        }
        return this.bankAccount;
    }

    /**
     * Gets the value of the componentCourseFee property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the componentCourseFee property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getComponentCourseFee().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ComponentCourseFee }
     * 
     * 
     */
    public List<ComponentCourseFee> getComponentCourseFee() {
        if (componentCourseFee == null) {
            componentCourseFee = new ArrayList<ComponentCourseFee>();
        }
        return this.componentCourseFee;
    }

    /**
     * Gets the value of the employmentPotential property.
     * 
     * @return
     *     possible object is
     *     {@link EmploymentPotential }
     *     
     */
    public EmploymentPotential getEmploymentPotential() {
        return employmentPotential;
    }

    /**
     * Sets the value of the employmentPotential property.
     * 
     * @param value
     *     allowed object is
     *     {@link EmploymentPotential }
     *     
     */
    public void setEmploymentPotential(EmploymentPotential value) {
        this.employmentPotential = value;
    }

    /**
     * Gets the value of the educationHistories property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the educationHistories property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEducationHistories().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EducationHistories }
     * 
     * 
     */
    public List<EducationHistories> getEducationHistories() {
        if (educationHistories == null) {
            educationHistories = new ArrayList<EducationHistories>();
        }
        return this.educationHistories;
    }

    /**
     * Gets the value of the courseFundSources property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the courseFundSources property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCourseFundSources().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CourseFundSources }
     * 
     * 
     */
    public List<CourseFundSources> getCourseFundSources() {
        if (courseFundSources == null) {
            courseFundSources = new ArrayList<CourseFundSources>();
        }
        return this.courseFundSources;
    }

}
