
package vn.com.tpf.microservices.models.Finnone;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for childDocs complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="childDocs">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="referenceType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="entityType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="documentName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="recievingDate" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType"/>
 *         &lt;element name="referenceId" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="documentReceiveState" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="verificationStatus" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="physicalState" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="reason" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="tentativeDate" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType" minOccurs="0"/>
 *         &lt;element name="mandatory" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType" minOccurs="0"/>
 *         &lt;element name="remarks" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="attachmentDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}attachmentDetails" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="isStoreId" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType" minOccurs="0"/>
 *         &lt;element name="ecmDocumentId" type="{http://schema.base.ws.pro.finnone.nucleus.com}LongType" minOccurs="0"/>
 *         &lt;element name="ecmDocumentVersion" type="{http://schema.base.ws.pro.finnone.nucleus.com}DoubleType" minOccurs="0"/>
 *         &lt;element name="attachmentOtcDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}attachmentDetails" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="deleteFlag" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "childDocs", propOrder = {
    "referenceType",
    "entityType",
    "documentName",
    "recievingDate",
    "referenceId",
    "documentReceiveState",
    "verificationStatus",
    "physicalState",
    "reason",
    "tentativeDate",
    "mandatory",
    "remarks",
    "attachmentDetails",
    "isStoreId",
    "ecmDocumentId",
    "ecmDocumentVersion",
    "attachmentOtcDetails",
    "deleteFlag"
})
public class ChildDocs {

    @XmlElement(required = true)
    protected String referenceType;
    @XmlElement(required = true)
    protected String entityType;
    @XmlElement(required = true)
    protected String documentName;
    @XmlElement(required = true)
    protected XMLGregorianCalendar recievingDate;
    protected List<String> referenceId;
    protected Integer documentReceiveState;
    protected String verificationStatus;
    protected String physicalState;
    protected String reason;
    protected XMLGregorianCalendar tentativeDate;
    protected Boolean mandatory;
    @XmlElement(required = true)
    protected String remarks;
    protected List<AttachmentDetails> attachmentDetails;
    protected Boolean isStoreId;
    protected Long ecmDocumentId;
    protected Double ecmDocumentVersion;
    protected List<AttachmentDetails> attachmentOtcDetails;
    protected Boolean deleteFlag;

    /**
     * Gets the value of the referenceType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReferenceType() {
        return referenceType;
    }

    /**
     * Sets the value of the referenceType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReferenceType(String value) {
        this.referenceType = value;
    }

    /**
     * Gets the value of the entityType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntityType() {
        return entityType;
    }

    /**
     * Sets the value of the entityType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntityType(String value) {
        this.entityType = value;
    }

    /**
     * Gets the value of the documentName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumentName() {
        return documentName;
    }

    /**
     * Sets the value of the documentName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumentName(String value) {
        this.documentName = value;
    }

    /**
     * Gets the value of the recievingDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getRecievingDate() {
        return recievingDate;
    }

    /**
     * Sets the value of the recievingDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setRecievingDate(XMLGregorianCalendar value) {
        this.recievingDate = value;
    }

    /**
     * Gets the value of the referenceId property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the referenceId property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReferenceId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getReferenceId() {
        if (referenceId == null) {
            referenceId = new ArrayList<String>();
        }
        return this.referenceId;
    }

    /**
     * Gets the value of the documentReceiveState property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getDocumentReceiveState() {
        return documentReceiveState;
    }

    /**
     * Sets the value of the documentReceiveState property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setDocumentReceiveState(Integer value) {
        this.documentReceiveState = value;
    }

    /**
     * Gets the value of the verificationStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVerificationStatus() {
        return verificationStatus;
    }

    /**
     * Sets the value of the verificationStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVerificationStatus(String value) {
        this.verificationStatus = value;
    }

    /**
     * Gets the value of the physicalState property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhysicalState() {
        return physicalState;
    }

    /**
     * Sets the value of the physicalState property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhysicalState(String value) {
        this.physicalState = value;
    }

    /**
     * Gets the value of the reason property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReason() {
        return reason;
    }

    /**
     * Sets the value of the reason property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReason(String value) {
        this.reason = value;
    }

    /**
     * Gets the value of the tentativeDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTentativeDate() {
        return tentativeDate;
    }

    /**
     * Sets the value of the tentativeDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTentativeDate(XMLGregorianCalendar value) {
        this.tentativeDate = value;
    }

    /**
     * Gets the value of the mandatory property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isMandatory() {
        return mandatory;
    }

    /**
     * Sets the value of the mandatory property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setMandatory(Boolean value) {
        this.mandatory = value;
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
     * Gets the value of the attachmentDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the attachmentDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAttachmentDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AttachmentDetails }
     * 
     * 
     */
    public List<AttachmentDetails> getAttachmentDetails() {
        if (attachmentDetails == null) {
            attachmentDetails = new ArrayList<AttachmentDetails>();
        }
        return this.attachmentDetails;
    }

    /**
     * Gets the value of the isStoreId property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsStoreId() {
        return isStoreId;
    }

    /**
     * Sets the value of the isStoreId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsStoreId(Boolean value) {
        this.isStoreId = value;
    }

    /**
     * Gets the value of the ecmDocumentId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getEcmDocumentId() {
        return ecmDocumentId;
    }

    /**
     * Sets the value of the ecmDocumentId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setEcmDocumentId(Long value) {
        this.ecmDocumentId = value;
    }

    /**
     * Gets the value of the ecmDocumentVersion property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getEcmDocumentVersion() {
        return ecmDocumentVersion;
    }

    /**
     * Sets the value of the ecmDocumentVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setEcmDocumentVersion(Double value) {
        this.ecmDocumentVersion = value;
    }

    /**
     * Gets the value of the attachmentOtcDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the attachmentOtcDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAttachmentOtcDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AttachmentDetails }
     * 
     * 
     */
    public List<AttachmentDetails> getAttachmentOtcDetails() {
        if (attachmentOtcDetails == null) {
            attachmentOtcDetails = new ArrayList<AttachmentDetails>();
        }
        return this.attachmentOtcDetails;
    }

    /**
     * Gets the value of the deleteFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDeleteFlag() {
        return deleteFlag;
    }

    /**
     * Sets the value of the deleteFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDeleteFlag(Boolean value) {
        this.deleteFlag = value;
    }

}
