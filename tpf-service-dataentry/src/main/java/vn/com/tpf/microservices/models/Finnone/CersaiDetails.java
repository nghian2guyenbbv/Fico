
package vn.com.tpf.microservices.models.Finnone;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for cersaiDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="cersaiDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="serialNo" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType"/>
 *         &lt;element name="numberOfDocuments" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType"/>
 *         &lt;element name="documentType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="locality" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="docTitleNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="surveyNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="sroName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="documentTaluka" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="documentPincode" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="documentDistrict" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="documentState" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="registrationDate" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType" minOccurs="0"/>
 *         &lt;element name="remarks" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="documentTypeOthers" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cersaiDetails", propOrder = {
    "serialNo",
    "numberOfDocuments",
    "documentType",
    "locality",
    "docTitleNumber",
    "surveyNumber",
    "sroName",
    "documentTaluka",
    "documentPincode",
    "documentDistrict",
    "documentState",
    "registrationDate",
    "remarks",
    "documentTypeOthers"
})
public class CersaiDetails {

    protected int serialNo;
    protected int numberOfDocuments;
    @XmlElement(required = true)
    protected String documentType;
    protected String locality;
    @XmlElement(required = true)
    protected String docTitleNumber;
    protected String surveyNumber;
    protected String sroName;
    protected String documentTaluka;
    @XmlElement(required = true)
    protected String documentPincode;
    protected String documentDistrict;
    @XmlElement(required = true)
    protected String documentState;
    protected XMLGregorianCalendar registrationDate;
    protected String remarks;
    protected String documentTypeOthers;

    /**
     * Gets the value of the serialNo property.
     * 
     */
    public int getSerialNo() {
        return serialNo;
    }

    /**
     * Sets the value of the serialNo property.
     * 
     */
    public void setSerialNo(int value) {
        this.serialNo = value;
    }

    /**
     * Gets the value of the numberOfDocuments property.
     * 
     */
    public int getNumberOfDocuments() {
        return numberOfDocuments;
    }

    /**
     * Sets the value of the numberOfDocuments property.
     * 
     */
    public void setNumberOfDocuments(int value) {
        this.numberOfDocuments = value;
    }

    /**
     * Gets the value of the documentType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumentType() {
        return documentType;
    }

    /**
     * Sets the value of the documentType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumentType(String value) {
        this.documentType = value;
    }

    /**
     * Gets the value of the locality property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocality() {
        return locality;
    }

    /**
     * Sets the value of the locality property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocality(String value) {
        this.locality = value;
    }

    /**
     * Gets the value of the docTitleNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocTitleNumber() {
        return docTitleNumber;
    }

    /**
     * Sets the value of the docTitleNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocTitleNumber(String value) {
        this.docTitleNumber = value;
    }

    /**
     * Gets the value of the surveyNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSurveyNumber() {
        return surveyNumber;
    }

    /**
     * Sets the value of the surveyNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSurveyNumber(String value) {
        this.surveyNumber = value;
    }

    /**
     * Gets the value of the sroName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSroName() {
        return sroName;
    }

    /**
     * Sets the value of the sroName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSroName(String value) {
        this.sroName = value;
    }

    /**
     * Gets the value of the documentTaluka property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumentTaluka() {
        return documentTaluka;
    }

    /**
     * Sets the value of the documentTaluka property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumentTaluka(String value) {
        this.documentTaluka = value;
    }

    /**
     * Gets the value of the documentPincode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumentPincode() {
        return documentPincode;
    }

    /**
     * Sets the value of the documentPincode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumentPincode(String value) {
        this.documentPincode = value;
    }

    /**
     * Gets the value of the documentDistrict property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumentDistrict() {
        return documentDistrict;
    }

    /**
     * Sets the value of the documentDistrict property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumentDistrict(String value) {
        this.documentDistrict = value;
    }

    /**
     * Gets the value of the documentState property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumentState() {
        return documentState;
    }

    /**
     * Sets the value of the documentState property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumentState(String value) {
        this.documentState = value;
    }

    /**
     * Gets the value of the registrationDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getRegistrationDate() {
        return registrationDate;
    }

    /**
     * Sets the value of the registrationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setRegistrationDate(XMLGregorianCalendar value) {
        this.registrationDate = value;
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
     * Gets the value of the documentTypeOthers property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumentTypeOthers() {
        return documentTypeOthers;
    }

    /**
     * Sets the value of the documentTypeOthers property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumentTypeOthers(String value) {
        this.documentTypeOthers = value;
    }

}
