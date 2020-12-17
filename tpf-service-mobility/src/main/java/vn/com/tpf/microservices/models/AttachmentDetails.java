
package vn.com.tpf.microservices.models;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.*;


/**
 * <p>Java class for attachmentDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="attachmentDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="attachedDocName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="attachedDocument" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *         &lt;element name="geoTagging" type="{http://www.nucleus.com/schemas/integration/leadCreationService}geoTagging" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "attachmentDetails", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", propOrder = {
    "attachedDocName",
    "attachedDocument",
    "geoTagging"
})
public class AttachmentDetails {

    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService")
    protected String attachedDocName;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    @XmlMimeType("application/octet-stream")
    protected byte[] attachedDocument;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService")
    protected GeoTagging geoTagging;

    /**
     * Gets the value of the attachedDocName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAttachedDocName() {
        return attachedDocName;
    }

    /**
     * Sets the value of the attachedDocName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttachedDocName(String value) {
        this.attachedDocName = value;
    }

    /**
     * Gets the value of the attachedDocument property.
     * 
     * @return
     *     possible object is
     *     {@link DataHandler }
     *     
     */
    public byte[] getAttachedDocument() {
        return attachedDocument;
    }

    /**
     * Sets the value of the attachedDocument property.
     * 
     * @param value
     *     allowed object is
     *     {@link DataHandler }
     *     
     */
    public void setAttachedDocument(byte[] value) {
        this.attachedDocument = value;
    }

    /**
     * Gets the value of the geoTagging property.
     * 
     * @return
     *     possible object is
     *     {@link GeoTagging }
     *     
     */
    public GeoTagging getGeoTagging() {
        return geoTagging;
    }

    /**
     * Sets the value of the geoTagging property.
     * 
     * @param value
     *     allowed object is
     *     {@link GeoTagging }
     *     
     */
    public void setGeoTagging(GeoTagging value) {
        this.geoTagging = value;
    }

}
