
package vn.com.tpf.microservices.models.leadCreation;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for communicationDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="communicationDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="modeOfCommunication" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="phoneNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="notificationType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="leadStatus" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="addComment" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="emailAddress" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="contactedBy" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="date" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "communicationDetails", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", propOrder = {
    "modeOfCommunication",
    "phoneNumber",
    "notificationType",
    "leadStatus",
    "addComment",
    "emailAddress",
    "contactedBy",
    "date"
})
public class CommunicationDetails {

    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String modeOfCommunication;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String phoneNumber;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String notificationType;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String leadStatus;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String addComment;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String emailAddress;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String contactedBy;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar date;

    /**
     * Gets the value of the modeOfCommunication property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModeOfCommunication() {
        return modeOfCommunication;
    }

    /**
     * Sets the value of the modeOfCommunication property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModeOfCommunication(String value) {
        this.modeOfCommunication = value;
    }

    /**
     * Gets the value of the phoneNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the value of the phoneNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhoneNumber(String value) {
        this.phoneNumber = value;
    }

    /**
     * Gets the value of the notificationType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNotificationType() {
        return notificationType;
    }

    /**
     * Sets the value of the notificationType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNotificationType(String value) {
        this.notificationType = value;
    }

    /**
     * Gets the value of the leadStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLeadStatus() {
        return leadStatus;
    }

    /**
     * Sets the value of the leadStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLeadStatus(String value) {
        this.leadStatus = value;
    }

    /**
     * Gets the value of the addComment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddComment() {
        return addComment;
    }

    /**
     * Sets the value of the addComment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddComment(String value) {
        this.addComment = value;
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
     * Gets the value of the contactedBy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContactedBy() {
        return contactedBy;
    }

    /**
     * Sets the value of the contactedBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContactedBy(String value) {
        this.contactedBy = value;
    }

    /**
     * Gets the value of the date property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDate() {
        return date;
    }

    /**
     * Sets the value of the date property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDate(XMLGregorianCalendar value) {
        this.date = value;
    }

}
