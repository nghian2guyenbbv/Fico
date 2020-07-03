
package vn.com.tpf.microservices.models.Finnone;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="gender" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="salutation" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="firstName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="middleName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="lastName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="aliasName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="mothersMaidenName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="dateOfBirth" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType"/>
 *         &lt;element name="maritalStatus" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="constitutionCode" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="placeOfBirth" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="nationality" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="residentType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="customerCategoryCode" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="personWithDisability" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="customerSegment" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="regionalData" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}regionalData" minOccurs="0"/>
 *         &lt;element name="dynamicFormDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}dynamicFormDetails" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="imageDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}imageDetails" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "personInfo", propOrder = {
    "gender",
    "salutation",
    "firstName",
    "middleName",
    "lastName",
    "aliasName",
    "mothersMaidenName",
    "dateOfBirth",
    "maritalStatus",
    "constitutionCode",
    "placeOfBirth",
    "nationality",
    "residentType",
    "customerCategoryCode",
    "personWithDisability",
    "customerSegment",
    "regionalData",
    "dynamicFormDetails",
    "imageDetails"
})
public class PersonInfo {

    @XmlElement(required = true)
    protected String gender;
    protected String salutation;
    @XmlElement(required = true)
    protected String firstName;
    protected String middleName;
    @XmlElement(required = true)
    protected String lastName;
    protected String aliasName;
    protected String mothersMaidenName;
    @XmlElement(required = true)
    protected XMLGregorianCalendar dateOfBirth;
    @XmlElement(required = true)
    protected String maritalStatus;
    @XmlElement(required = true)
    protected String constitutionCode;
    protected String placeOfBirth;
    @XmlElement(required = true)
    protected String nationality;
    @XmlElement(required = true)
    protected String residentType;
    @XmlElement(required = true)
    protected String customerCategoryCode;
    protected String personWithDisability;
    protected String customerSegment;
    protected RegionalData regionalData;
    protected List<DynamicFormDetails> dynamicFormDetails;
    protected ImageDetails imageDetails;

    /**
     * Gets the value of the gender property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the value of the gender property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGender(String value) {
        this.gender = value;
    }

    /**
     * Gets the value of the salutation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSalutation() {
        return salutation;
    }

    /**
     * Sets the value of the salutation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSalutation(String value) {
        this.salutation = value;
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
     * Gets the value of the middleName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMiddleName() {
        return middleName;
    }

    /**
     * Sets the value of the middleName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMiddleName(String value) {
        this.middleName = value;
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
     * Gets the value of the aliasName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAliasName() {
        return aliasName;
    }

    /**
     * Sets the value of the aliasName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAliasName(String value) {
        this.aliasName = value;
    }

    /**
     * Gets the value of the mothersMaidenName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMothersMaidenName() {
        return mothersMaidenName;
    }

    /**
     * Sets the value of the mothersMaidenName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMothersMaidenName(String value) {
        this.mothersMaidenName = value;
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
     * Gets the value of the maritalStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaritalStatus() {
        return maritalStatus;
    }

    /**
     * Sets the value of the maritalStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaritalStatus(String value) {
        this.maritalStatus = value;
    }

    /**
     * Gets the value of the constitutionCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConstitutionCode() {
        return constitutionCode;
    }

    /**
     * Sets the value of the constitutionCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConstitutionCode(String value) {
        this.constitutionCode = value;
    }

    /**
     * Gets the value of the placeOfBirth property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    /**
     * Sets the value of the placeOfBirth property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlaceOfBirth(String value) {
        this.placeOfBirth = value;
    }

    /**
     * Gets the value of the nationality property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNationality() {
        return nationality;
    }

    /**
     * Sets the value of the nationality property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNationality(String value) {
        this.nationality = value;
    }

    /**
     * Gets the value of the residentType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResidentType() {
        return residentType;
    }

    /**
     * Sets the value of the residentType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResidentType(String value) {
        this.residentType = value;
    }

    /**
     * Gets the value of the customerCategoryCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerCategoryCode() {
        return customerCategoryCode;
    }

    /**
     * Sets the value of the customerCategoryCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerCategoryCode(String value) {
        this.customerCategoryCode = value;
    }

    /**
     * Gets the value of the personWithDisability property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPersonWithDisability() {
        return personWithDisability;
    }

    /**
     * Sets the value of the personWithDisability property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPersonWithDisability(String value) {
        this.personWithDisability = value;
    }

    /**
     * Gets the value of the customerSegment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerSegment() {
        return customerSegment;
    }

    /**
     * Sets the value of the customerSegment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerSegment(String value) {
        this.customerSegment = value;
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

    /**
     * Gets the value of the imageDetails property.
     * 
     * @return
     *     possible object is
     *     {@link ImageDetails }
     *     
     */
    public ImageDetails getImageDetails() {
        return imageDetails;
    }

    /**
     * Sets the value of the imageDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link ImageDetails }
     *     
     */
    public void setImageDetails(ImageDetails value) {
        this.imageDetails = value;
    }

}
