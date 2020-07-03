
package vn.com.tpf.microservices.models.Finnone;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="attachedDocName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="attachedDocument" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}definitionMimeDetection"/>
 *         &lt;element name="geoTagging" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}geoTagging" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "attachmentDetails", propOrder = {
    "attachedDocName",
    "attachedDocument",
    "geoTagging"
})
public class AttachmentDetails {

    protected String attachedDocName;
    @XmlElement(required = true)
    protected byte[] attachedDocument;
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
     *     {@link DefinitionMimeDetection }
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
     *     {@link DefinitionMimeDetection }
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
