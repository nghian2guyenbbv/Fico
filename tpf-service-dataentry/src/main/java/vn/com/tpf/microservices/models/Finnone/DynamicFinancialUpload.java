
package vn.com.tpf.microservices.models.Finnone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for dynamicFinancialUpload complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="dynamicFinancialUpload">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="documentType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="documentName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="auditedYears" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="projectedYears" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="unauditedYears" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="financialUploadFile" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}financialUploadFile" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dynamicFinancialUpload", propOrder = {
    "documentType",
    "documentName",
    "auditedYears",
    "projectedYears",
    "unauditedYears",
    "financialUploadFile"
})
public class DynamicFinancialUpload {

    protected String documentType;
    protected String documentName;
    protected Integer auditedYears;
    protected Integer projectedYears;
    protected Integer unauditedYears;
    protected FinancialUploadFile financialUploadFile;

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
     * Gets the value of the auditedYears property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAuditedYears() {
        return auditedYears;
    }

    /**
     * Sets the value of the auditedYears property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAuditedYears(Integer value) {
        this.auditedYears = value;
    }

    /**
     * Gets the value of the projectedYears property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getProjectedYears() {
        return projectedYears;
    }

    /**
     * Sets the value of the projectedYears property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setProjectedYears(Integer value) {
        this.projectedYears = value;
    }

    /**
     * Gets the value of the unauditedYears property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getUnauditedYears() {
        return unauditedYears;
    }

    /**
     * Sets the value of the unauditedYears property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setUnauditedYears(Integer value) {
        this.unauditedYears = value;
    }

    /**
     * Gets the value of the financialUploadFile property.
     * 
     * @return
     *     possible object is
     *     {@link FinancialUploadFile }
     *     
     */
    public FinancialUploadFile getFinancialUploadFile() {
        return financialUploadFile;
    }

    /**
     * Sets the value of the financialUploadFile property.
     * 
     * @param value
     *     allowed object is
     *     {@link FinancialUploadFile }
     *     
     */
    public void setFinancialUploadFile(FinancialUploadFile value) {
        this.financialUploadFile = value;
    }

}
