
package vn.com.tpf.microservices.models.Finnone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for regionalCustInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="regionalCustInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="englishFirstName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="englishSecondName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="englishThirdName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="englishLastName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="firstName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="secondName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="thirdName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="lastName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="subtribeName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="grandFatherName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="fatherName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="dateOfBirthG" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType" minOccurs="0"/>
 *         &lt;element name="dateOfBirthH" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType" minOccurs="0"/>
 *         &lt;element name="gender" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="expiryDateH" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType" minOccurs="0"/>
 *         &lt;element name="idExpiryDate" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType" minOccurs="0"/>
 *         &lt;element name="idIssueDate" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType" minOccurs="0"/>
 *         &lt;element name="issueDateH" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType" minOccurs="0"/>
 *         &lt;element name="issuePlaceCode" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="legalStatus" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="logId" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="nationalityCode" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="occupationCode" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="placeOfBirthCode" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="sponsorName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "regionalCustInfo", propOrder = {
    "englishFirstName",
    "englishSecondName",
    "englishThirdName",
    "englishLastName",
    "firstName",
    "secondName",
    "thirdName",
    "lastName",
    "subtribeName",
    "grandFatherName",
    "fatherName",
    "dateOfBirthG",
    "dateOfBirthH",
    "gender",
    "expiryDateH",
    "idExpiryDate",
    "idIssueDate",
    "issueDateH",
    "issuePlaceCode",
    "legalStatus",
    "logId",
    "nationalityCode",
    "occupationCode",
    "placeOfBirthCode",
    "sponsorName"
})
public class RegionalCustInfo {

    protected String englishFirstName;
    protected String englishSecondName;
    protected String englishThirdName;
    protected String englishLastName;
    protected String firstName;
    protected String secondName;
    protected String thirdName;
    protected String lastName;
    protected String subtribeName;
    protected String grandFatherName;
    protected String fatherName;
    protected XMLGregorianCalendar dateOfBirthG;
    protected XMLGregorianCalendar dateOfBirthH;
    protected String gender;
    protected XMLGregorianCalendar expiryDateH;
    protected XMLGregorianCalendar idExpiryDate;
    protected XMLGregorianCalendar idIssueDate;
    protected XMLGregorianCalendar issueDateH;
    protected String issuePlaceCode;
    protected String legalStatus;
    protected String logId;
    protected String nationalityCode;
    protected String occupationCode;
    protected String placeOfBirthCode;
    protected String sponsorName;

    /**
     * Gets the value of the englishFirstName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnglishFirstName() {
        return englishFirstName;
    }

    /**
     * Sets the value of the englishFirstName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnglishFirstName(String value) {
        this.englishFirstName = value;
    }

    /**
     * Gets the value of the englishSecondName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnglishSecondName() {
        return englishSecondName;
    }

    /**
     * Sets the value of the englishSecondName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnglishSecondName(String value) {
        this.englishSecondName = value;
    }

    /**
     * Gets the value of the englishThirdName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnglishThirdName() {
        return englishThirdName;
    }

    /**
     * Sets the value of the englishThirdName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnglishThirdName(String value) {
        this.englishThirdName = value;
    }

    /**
     * Gets the value of the englishLastName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnglishLastName() {
        return englishLastName;
    }

    /**
     * Sets the value of the englishLastName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnglishLastName(String value) {
        this.englishLastName = value;
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
     * Gets the value of the secondName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSecondName() {
        return secondName;
    }

    /**
     * Sets the value of the secondName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecondName(String value) {
        this.secondName = value;
    }

    /**
     * Gets the value of the thirdName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getThirdName() {
        return thirdName;
    }

    /**
     * Sets the value of the thirdName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setThirdName(String value) {
        this.thirdName = value;
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
     * Gets the value of the subtribeName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubtribeName() {
        return subtribeName;
    }

    /**
     * Sets the value of the subtribeName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubtribeName(String value) {
        this.subtribeName = value;
    }

    /**
     * Gets the value of the grandFatherName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGrandFatherName() {
        return grandFatherName;
    }

    /**
     * Sets the value of the grandFatherName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGrandFatherName(String value) {
        this.grandFatherName = value;
    }

    /**
     * Gets the value of the fatherName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFatherName() {
        return fatherName;
    }

    /**
     * Sets the value of the fatherName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFatherName(String value) {
        this.fatherName = value;
    }

    /**
     * Gets the value of the dateOfBirthG property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateOfBirthG() {
        return dateOfBirthG;
    }

    /**
     * Sets the value of the dateOfBirthG property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateOfBirthG(XMLGregorianCalendar value) {
        this.dateOfBirthG = value;
    }

    /**
     * Gets the value of the dateOfBirthH property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateOfBirthH() {
        return dateOfBirthH;
    }

    /**
     * Sets the value of the dateOfBirthH property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateOfBirthH(XMLGregorianCalendar value) {
        this.dateOfBirthH = value;
    }

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
     * Gets the value of the expiryDateH property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getExpiryDateH() {
        return expiryDateH;
    }

    /**
     * Sets the value of the expiryDateH property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setExpiryDateH(XMLGregorianCalendar value) {
        this.expiryDateH = value;
    }

    /**
     * Gets the value of the idExpiryDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getIdExpiryDate() {
        return idExpiryDate;
    }

    /**
     * Sets the value of the idExpiryDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setIdExpiryDate(XMLGregorianCalendar value) {
        this.idExpiryDate = value;
    }

    /**
     * Gets the value of the idIssueDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getIdIssueDate() {
        return idIssueDate;
    }

    /**
     * Sets the value of the idIssueDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setIdIssueDate(XMLGregorianCalendar value) {
        this.idIssueDate = value;
    }

    /**
     * Gets the value of the issueDateH property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getIssueDateH() {
        return issueDateH;
    }

    /**
     * Sets the value of the issueDateH property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setIssueDateH(XMLGregorianCalendar value) {
        this.issueDateH = value;
    }

    /**
     * Gets the value of the issuePlaceCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssuePlaceCode() {
        return issuePlaceCode;
    }

    /**
     * Sets the value of the issuePlaceCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssuePlaceCode(String value) {
        this.issuePlaceCode = value;
    }

    /**
     * Gets the value of the legalStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLegalStatus() {
        return legalStatus;
    }

    /**
     * Sets the value of the legalStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLegalStatus(String value) {
        this.legalStatus = value;
    }

    /**
     * Gets the value of the logId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLogId() {
        return logId;
    }

    /**
     * Sets the value of the logId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLogId(String value) {
        this.logId = value;
    }

    /**
     * Gets the value of the nationalityCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNationalityCode() {
        return nationalityCode;
    }

    /**
     * Sets the value of the nationalityCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNationalityCode(String value) {
        this.nationalityCode = value;
    }

    /**
     * Gets the value of the occupationCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOccupationCode() {
        return occupationCode;
    }

    /**
     * Sets the value of the occupationCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOccupationCode(String value) {
        this.occupationCode = value;
    }

    /**
     * Gets the value of the placeOfBirthCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlaceOfBirthCode() {
        return placeOfBirthCode;
    }

    /**
     * Sets the value of the placeOfBirthCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlaceOfBirthCode(String value) {
        this.placeOfBirthCode = value;
    }

    /**
     * Gets the value of the sponsorName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSponsorName() {
        return sponsorName;
    }

    /**
     * Sets the value of the sponsorName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSponsorName(String value) {
        this.sponsorName = value;
    }

}
