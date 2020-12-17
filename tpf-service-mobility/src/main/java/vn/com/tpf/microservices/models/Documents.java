
package vn.com.tpf.microservices.models;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for documents complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="documents">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="referenceType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="entityType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="documentName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="recievingDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="referenceId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="attachmentDetails" type="{http://www.nucleus.com/schemas/integration/leadCreationService}attachmentDetails" minOccurs="0"/>
 *         &lt;element name="remarks" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "documents", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", propOrder = {
    "referenceType",
    "entityType",
    "documentName",
    "recievingDate",
    "referenceId",
    "attachmentDetails",
    "remarks"
})
public class Documents {

    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String referenceType;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String entityType;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String documentName;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar recievingDate;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService")
    protected String referenceId;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService")
    protected AttachmentDetails attachmentDetails;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String remarks;

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
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReferenceId() {
        return referenceId;
    }

    /**
     * Sets the value of the referenceId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReferenceId(String value) {
        this.referenceId = value;
    }

    /**
     * Gets the value of the attachmentDetails property.
     * 
     * @return
     *     possible object is
     *     {@link AttachmentDetails }
     *     
     */
    public AttachmentDetails getAttachmentDetails() {
        return attachmentDetails;
    }

    /**
     * Sets the value of the attachmentDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link AttachmentDetails }
     *     
     */
    public void setAttachmentDetails(AttachmentDetails value) {
        this.attachmentDetails = value;
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

}
