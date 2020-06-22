
package vn.com.tpf.microservices.models.Finnone;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for bankDetailFile complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="bankDetailFile">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="evalPeriod" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType"/>
 *         &lt;element name="isConsiderForConsolidatedABB" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType"/>
 *         &lt;element name="remarks" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0255Type"/>
 *         &lt;element name="bankFile" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}definitionMimeDetection"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "bankDetailFile", propOrder = {
    "evalPeriod",
    "isConsiderForConsolidatedABB",
    "remarks",
    "bankFile"
})
public class BankDetailFile {

    protected int evalPeriod;
    protected int isConsiderForConsolidatedABB;
    @XmlElement(required = true)
    protected String remarks;
    @XmlElement(required = true)
    protected DefinitionMimeDetection bankFile;

    /**
     * Gets the value of the evalPeriod property.
     * 
     */
    public int getEvalPeriod() {
        return evalPeriod;
    }

    /**
     * Sets the value of the evalPeriod property.
     * 
     */
    public void setEvalPeriod(int value) {
        this.evalPeriod = value;
    }

    /**
     * Gets the value of the isConsiderForConsolidatedABB property.
     * 
     */
    public int getIsConsiderForConsolidatedABB() {
        return isConsiderForConsolidatedABB;
    }

    /**
     * Sets the value of the isConsiderForConsolidatedABB property.
     * 
     */
    public void setIsConsiderForConsolidatedABB(int value) {
        this.isConsiderForConsolidatedABB = value;
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
     * Gets the value of the bankFile property.
     * 
     * @return
     *     possible object is
     *     {@link DefinitionMimeDetection }
     *     
     */
    public DefinitionMimeDetection getBankFile() {
        return bankFile;
    }

    /**
     * Sets the value of the bankFile property.
     * 
     * @param value
     *     allowed object is
     *     {@link DefinitionMimeDetection }
     *     
     */
    public void setBankFile(DefinitionMimeDetection value) {
        this.bankFile = value;
    }

}
