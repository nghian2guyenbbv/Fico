
package vn.com.tpf.microservices.models;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="leadReferenceNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="message" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="leadApplicantId" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/>
 *         &lt;element name="customerId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="applicationNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="applicationId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "leadReferenceNumber",
    "message",
    "leadApplicantId",
    "customerId",
    "applicationNumber",
    "applicationId"
})
@XmlRootElement(name = "leadCreationResponse", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService")
public class LeadCreationResponse {

    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String leadReferenceNumber;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService")
    protected String message;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected List<String> leadApplicantId;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String customerId;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String applicationNumber;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService")
    protected long applicationId;

    /**
     * Gets the value of the leadReferenceNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLeadReferenceNumber() {
        return leadReferenceNumber;
    }

    /**
     * Sets the value of the leadReferenceNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLeadReferenceNumber(String value) {
        this.leadReferenceNumber = value;
    }

    /**
     * Gets the value of the message property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the value of the message property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessage(String value) {
        this.message = value;
    }

    /**
     * Gets the value of the leadApplicantId property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the leadApplicantId property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLeadApplicantId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getLeadApplicantId() {
        if (leadApplicantId == null) {
            leadApplicantId = new ArrayList<String>();
        }
        return this.leadApplicantId;
    }

    /**
     * Gets the value of the customerId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * Sets the value of the customerId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerId(String value) {
        this.customerId = value;
    }

    /**
     * Gets the value of the applicationNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApplicationNumber() {
        return applicationNumber;
    }

    /**
     * Sets the value of the applicationNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApplicationNumber(String value) {
        this.applicationNumber = value;
    }

    /**
     * Gets the value of the applicationId property.
     * 
     */
    public long getApplicationId() {
        return applicationId;
    }

    /**
     * Sets the value of the applicationId property.
     * 
     */
    public void setApplicationId(long value) {
        this.applicationId = value;
    }

}
