
package vn.com.tpf.microservices.models.Finnone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for financialUploadFile complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="financialUploadFile">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="remarks" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0255Type"/>
 *         &lt;element name="financialFile" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}definitionMimeDetection"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "financialUploadFile", propOrder = {
    "remarks",
    "financialFile"
})
public class FinancialUploadFile {

    @XmlElement(required = true)
    protected String remarks;
    @XmlElement(required = true)
    protected DefinitionMimeDetection financialFile;

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
     * Gets the value of the financialFile property.
     * 
     * @return
     *     possible object is
     *     {@link DefinitionMimeDetection }
     *     
     */
    public DefinitionMimeDetection getFinancialFile() {
        return financialFile;
    }

    /**
     * Sets the value of the financialFile property.
     * 
     * @param value
     *     allowed object is
     *     {@link DefinitionMimeDetection }
     *     
     */
    public void setFinancialFile(DefinitionMimeDetection value) {
        this.financialFile = value;
    }

}
