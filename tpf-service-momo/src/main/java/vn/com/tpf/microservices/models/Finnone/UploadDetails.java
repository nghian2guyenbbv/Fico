
package vn.com.tpf.microservices.models.Finnone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for uploadDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="uploadDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="fileType" type="{http://schema.base.ws.pro.finnone.nucleus.com}StringType"/>
 *         &lt;element name="uploadFile" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}definitionMimeDetection"/>
 *         &lt;element name="remarks" type="{http://schema.base.ws.pro.finnone.nucleus.com}StringType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "uploadDetails", propOrder = {
    "fileType",
    "uploadFile",
    "remarks"
})
public class UploadDetails {

    @XmlElement(required = true)
    protected String fileType;
    @XmlElement(required = true)
    protected DefinitionMimeDetection uploadFile;
    @XmlElement(required = true)
    protected String remarks;

    /**
     * Gets the value of the fileType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFileType() {
        return fileType;
    }

    /**
     * Sets the value of the fileType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFileType(String value) {
        this.fileType = value;
    }

    /**
     * Gets the value of the uploadFile property.
     * 
     * @return
     *     possible object is
     *     {@link DefinitionMimeDetection }
     *     
     */
    public DefinitionMimeDetection getUploadFile() {
        return uploadFile;
    }

    /**
     * Sets the value of the uploadFile property.
     * 
     * @param value
     *     allowed object is
     *     {@link DefinitionMimeDetection }
     *     
     */
    public void setUploadFile(DefinitionMimeDetection value) {
        this.uploadFile = value;
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
