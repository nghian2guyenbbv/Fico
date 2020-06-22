
package vn.com.tpf.microservices.models.leadCreation;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for personInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="personInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="leadApplicantId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="applicantRole" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="applicantType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="firstName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="lastName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="designation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nameOfTheEntity" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mobilePhone_Number" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="mobilePhone_IsdCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="landLinePhoneNumber_Std" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="landLinePhoneNumber_number" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="landLinePhoneNumber_Extn" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dateOfBirth" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="emailAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="city" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="borrowerType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="branch" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="deleteFlag" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="FieldPerInfo1" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FieldPerInfo2" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FieldPerInfo3" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FieldPerInfo4" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FieldPerInfo5" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FieldPerInfo6" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FieldPerInfo7" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FieldPerInfo8" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FieldPerInfo9" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FieldPerInfo10" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="workAndIncome" type="{http://www.nucleus.com/schemas/integration/leadCreationService}workAndIncomeType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "personInfo", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", propOrder = {
    "leadApplicantId",
    "applicantRole",
    "applicantType",
    "firstName",
    "lastName",
    "designation",
    "nameOfTheEntity",
    "mobilePhoneNumber",
    "mobilePhoneIsdCode",
    "landLinePhoneNumberStd",
    "landLinePhoneNumberNumber",
    "landLinePhoneNumberExtn",
    "dateOfBirth",
    "emailAddress",
    "city",
    "borrowerType",
    "branch",
    "deleteFlag",
    "fieldPerInfo1",
    "fieldPerInfo2",
    "fieldPerInfo3",
    "fieldPerInfo4",
    "fieldPerInfo5",
    "fieldPerInfo6",
    "fieldPerInfo7",
    "fieldPerInfo8",
    "fieldPerInfo9",
    "fieldPerInfo10",
    "workAndIncome"
})
public class PersonInfo {

    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String leadApplicantId;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String applicantRole;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String applicantType;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String firstName;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String lastName;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService")
    protected String designation;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService")
    protected String nameOfTheEntity;
    @XmlElement(name = "mobilePhone_Number", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String mobilePhoneNumber;
    @XmlElement(name = "mobilePhone_IsdCode", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String mobilePhoneIsdCode;
    @XmlElement(name = "landLinePhoneNumber_Std", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService")
    protected String landLinePhoneNumberStd;
    @XmlElement(name = "landLinePhoneNumber_number", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService")
    protected String landLinePhoneNumberNumber;
    @XmlElement(name = "landLinePhoneNumber_Extn", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService")
    protected String landLinePhoneNumberExtn;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateOfBirth;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService")
    protected String emailAddress;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String city;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String borrowerType;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService")
    protected String branch;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService")
    protected boolean deleteFlag;
    @XmlElement(name = "FieldPerInfo1", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String fieldPerInfo1;
    @XmlElement(name = "FieldPerInfo2", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String fieldPerInfo2;
    @XmlElement(name = "FieldPerInfo3", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String fieldPerInfo3;
    @XmlElement(name = "FieldPerInfo4", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String fieldPerInfo4;
    @XmlElement(name = "FieldPerInfo5", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String fieldPerInfo5;
    @XmlElement(name = "FieldPerInfo6", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String fieldPerInfo6;
    @XmlElement(name = "FieldPerInfo7", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String fieldPerInfo7;
    @XmlElement(name = "FieldPerInfo8", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String fieldPerInfo8;
    @XmlElement(name = "FieldPerInfo9", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String fieldPerInfo9;
    @XmlElement(name = "FieldPerInfo10", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String fieldPerInfo10;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService")
    protected WorkAndIncomeType workAndIncome;

    /**
     * Gets the value of the leadApplicantId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLeadApplicantId() {
        return leadApplicantId;
    }

    /**
     * Sets the value of the leadApplicantId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLeadApplicantId(String value) {
        this.leadApplicantId = value;
    }

    /**
     * Gets the value of the applicantRole property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApplicantRole() {
        return applicantRole;
    }

    /**
     * Sets the value of the applicantRole property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApplicantRole(String value) {
        this.applicantRole = value;
    }

    /**
     * Gets the value of the applicantType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApplicantType() {
        return applicantType;
    }

    /**
     * Sets the value of the applicantType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApplicantType(String value) {
        this.applicantType = value;
    }

    /**
     * Gets the value of the firstName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the value of the firstName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirstName(String value) {
        this.firstName = value;
    }

    /**
     * Gets the value of the lastName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the value of the lastName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastName(String value) {
        this.lastName = value;
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
     * Gets the value of the nameOfTheEntity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNameOfTheEntity() {
        return nameOfTheEntity;
    }

    /**
     * Sets the value of the nameOfTheEntity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNameOfTheEntity(String value) {
        this.nameOfTheEntity = value;
    }

    /**
     * Gets the value of the mobilePhoneNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    /**
     * Sets the value of the mobilePhoneNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMobilePhoneNumber(String value) {
        this.mobilePhoneNumber = value;
    }

    /**
     * Gets the value of the mobilePhoneIsdCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMobilePhoneIsdCode() {
        return mobilePhoneIsdCode;
    }

    /**
     * Sets the value of the mobilePhoneIsdCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMobilePhoneIsdCode(String value) {
        this.mobilePhoneIsdCode = value;
    }

    /**
     * Gets the value of the landLinePhoneNumberStd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLandLinePhoneNumberStd() {
        return landLinePhoneNumberStd;
    }

    /**
     * Sets the value of the landLinePhoneNumberStd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLandLinePhoneNumberStd(String value) {
        this.landLinePhoneNumberStd = value;
    }

    /**
     * Gets the value of the landLinePhoneNumberNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLandLinePhoneNumberNumber() {
        return landLinePhoneNumberNumber;
    }

    /**
     * Sets the value of the landLinePhoneNumberNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLandLinePhoneNumberNumber(String value) {
        this.landLinePhoneNumberNumber = value;
    }

    /**
     * Gets the value of the landLinePhoneNumberExtn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLandLinePhoneNumberExtn() {
        return landLinePhoneNumberExtn;
    }

    /**
     * Sets the value of the landLinePhoneNumberExtn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLandLinePhoneNumberExtn(String value) {
        this.landLinePhoneNumberExtn = value;
    }

    /**
     * Gets the value of the dateOfBirth property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets the value of the dateOfBirth property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateOfBirth(XMLGregorianCalendar value) {
        this.dateOfBirth = value;
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
     * Gets the value of the city property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the value of the city property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCity(String value) {
        this.city = value;
    }

    /**
     * Gets the value of the borrowerType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBorrowerType() {
        return borrowerType;
    }

    /**
     * Sets the value of the borrowerType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBorrowerType(String value) {
        this.borrowerType = value;
    }

    /**
     * Gets the value of the branch property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBranch() {
        return branch;
    }

    /**
     * Sets the value of the branch property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBranch(String value) {
        this.branch = value;
    }

    /**
     * Gets the value of the deleteFlag property.
     * 
     */
    public boolean isDeleteFlag() {
        return deleteFlag;
    }

    /**
     * Sets the value of the deleteFlag property.
     * 
     */
    public void setDeleteFlag(boolean value) {
        this.deleteFlag = value;
    }

    /**
     * Gets the value of the fieldPerInfo1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFieldPerInfo1() {
        return fieldPerInfo1;
    }

    /**
     * Sets the value of the fieldPerInfo1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFieldPerInfo1(String value) {
        this.fieldPerInfo1 = value;
    }

    /**
     * Gets the value of the fieldPerInfo2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFieldPerInfo2() {
        return fieldPerInfo2;
    }

    /**
     * Sets the value of the fieldPerInfo2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFieldPerInfo2(String value) {
        this.fieldPerInfo2 = value;
    }

    /**
     * Gets the value of the fieldPerInfo3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFieldPerInfo3() {
        return fieldPerInfo3;
    }

    /**
     * Sets the value of the fieldPerInfo3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFieldPerInfo3(String value) {
        this.fieldPerInfo3 = value;
    }

    /**
     * Gets the value of the fieldPerInfo4 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFieldPerInfo4() {
        return fieldPerInfo4;
    }

    /**
     * Sets the value of the fieldPerInfo4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFieldPerInfo4(String value) {
        this.fieldPerInfo4 = value;
    }

    /**
     * Gets the value of the fieldPerInfo5 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFieldPerInfo5() {
        return fieldPerInfo5;
    }

    /**
     * Sets the value of the fieldPerInfo5 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFieldPerInfo5(String value) {
        this.fieldPerInfo5 = value;
    }

    /**
     * Gets the value of the fieldPerInfo6 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFieldPerInfo6() {
        return fieldPerInfo6;
    }

    /**
     * Sets the value of the fieldPerInfo6 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFieldPerInfo6(String value) {
        this.fieldPerInfo6 = value;
    }

    /**
     * Gets the value of the fieldPerInfo7 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFieldPerInfo7() {
        return fieldPerInfo7;
    }

    /**
     * Sets the value of the fieldPerInfo7 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFieldPerInfo7(String value) {
        this.fieldPerInfo7 = value;
    }

    /**
     * Gets the value of the fieldPerInfo8 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFieldPerInfo8() {
        return fieldPerInfo8;
    }

    /**
     * Sets the value of the fieldPerInfo8 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFieldPerInfo8(String value) {
        this.fieldPerInfo8 = value;
    }

    /**
     * Gets the value of the fieldPerInfo9 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFieldPerInfo9() {
        return fieldPerInfo9;
    }

    /**
     * Sets the value of the fieldPerInfo9 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFieldPerInfo9(String value) {
        this.fieldPerInfo9 = value;
    }

    /**
     * Gets the value of the fieldPerInfo10 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFieldPerInfo10() {
        return fieldPerInfo10;
    }

    /**
     * Sets the value of the fieldPerInfo10 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFieldPerInfo10(String value) {
        this.fieldPerInfo10 = value;
    }

    /**
     * Gets the value of the workAndIncome property.
     * 
     * @return
     *     possible object is
     *     {@link WorkAndIncomeType }
     *     
     */
    public WorkAndIncomeType getWorkAndIncome() {
        return workAndIncome;
    }

    /**
     * Sets the value of the workAndIncome property.
     * 
     * @param value
     *     allowed object is
     *     {@link WorkAndIncomeType }
     *     
     */
    public void setWorkAndIncome(WorkAndIncomeType value) {
        this.workAndIncome = value;
    }

}
