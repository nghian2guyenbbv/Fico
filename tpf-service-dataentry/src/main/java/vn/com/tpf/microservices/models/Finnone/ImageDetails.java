
package vn.com.tpf.microservices.models.Finnone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for imageDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="imageDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="imageName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="imageFile" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}definitionMimeDetection"/>
 *         &lt;element name="reference" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "imageDetails", propOrder = {
    "imageName",
    "imageFile",
    "reference"
})
public class ImageDetails {

    @XmlElement(required = true)
    protected String imageName;
    @XmlElement(required = true)
    protected DefinitionMimeDetection imageFile;
    protected String reference;

    /**
     * Gets the value of the imageName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImageName() {
        return imageName;
    }

    /**
     * Sets the value of the imageName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImageName(String value) {
        this.imageName = value;
    }

    /**
     * Gets the value of the imageFile property.
     * 
     * @return
     *     possible object is
     *     {@link DefinitionMimeDetection }
     *     
     */
    public DefinitionMimeDetection getImageFile() {
        return imageFile;
    }

    /**
     * Sets the value of the imageFile property.
     * 
     * @param value
     *     allowed object is
     *     {@link DefinitionMimeDetection }
     *     
     */
    public void setImageFile(DefinitionMimeDetection value) {
        this.imageFile = value;
    }

    /**
     * Gets the value of the reference property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReference() {
        return reference;
    }

    /**
     * Sets the value of the reference property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReference(String value) {
        this.reference = value;
    }

}
