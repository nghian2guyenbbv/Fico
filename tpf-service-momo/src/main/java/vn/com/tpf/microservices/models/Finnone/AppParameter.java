
package vn.com.tpf.microservices.models.Finnone;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for appParameter complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="appParameter">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="productProcessor" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="applicationNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="productTypeCode" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="branchCode" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="userName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="ecmCaseId" type="{http://schema.base.ws.pro.finnone.nucleus.com}LongType" minOccurs="0"/>
 *         &lt;element name="ecmCaseNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="moveToNextStageFlag" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "appParameter", propOrder = {
    "productProcessor",
    "applicationNumber",
    "productTypeCode",
    "branchCode",
    "userName",
    "ecmCaseId",
    "ecmCaseNumber",
    "moveToNextStageFlag"
})
public class AppParameter {

    @XmlElement(required = true)
    protected String productProcessor;
    @XmlElement(required = true)
    protected String applicationNumber;
    protected String productTypeCode;
    protected String branchCode;
    protected String userName;
    protected Long ecmCaseId;
    protected String ecmCaseNumber;
    protected boolean moveToNextStageFlag;

    /**
     * Gets the value of the productProcessor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductProcessor() {
        return productProcessor;
    }

    /**
     * Sets the value of the productProcessor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductProcessor(String value) {
        this.productProcessor = value;
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
     * Gets the value of the productTypeCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductTypeCode() {
        return productTypeCode;
    }

    /**
     * Sets the value of the productTypeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductTypeCode(String value) {
        this.productTypeCode = value;
    }

    /**
     * Gets the value of the branchCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBranchCode() {
        return branchCode;
    }

    /**
     * Sets the value of the branchCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBranchCode(String value) {
        this.branchCode = value;
    }

    /**
     * Gets the value of the userName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the value of the userName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserName(String value) {
        this.userName = value;
    }

    /**
     * Gets the value of the ecmCaseId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getEcmCaseId() {
        return ecmCaseId;
    }

    /**
     * Sets the value of the ecmCaseId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setEcmCaseId(Long value) {
        this.ecmCaseId = value;
    }

    /**
     * Gets the value of the ecmCaseNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEcmCaseNumber() {
        return ecmCaseNumber;
    }

    /**
     * Sets the value of the ecmCaseNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEcmCaseNumber(String value) {
        this.ecmCaseNumber = value;
    }

    /**
     * Gets the value of the moveToNextStageFlag property.
     * 
     */
    public boolean isMoveToNextStageFlag() {
        return moveToNextStageFlag;
    }

    /**
     * Sets the value of the moveToNextStageFlag property.
     * 
     */
    public void setMoveToNextStageFlag(boolean value) {
        this.moveToNextStageFlag = value;
    }

}
