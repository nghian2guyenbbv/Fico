
package vn.com.tpf.microservices.models.Finnone;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for instrumentDetailDAS complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="instrumentDetailDAS">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="referenceNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="employerName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="employerId" type="{http://schema.base.ws.pro.finnone.nucleus.com}LongType" minOccurs="0"/>
 *         &lt;element name="employeeNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="departmentName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="location" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="costCentre" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="designation" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="repaymentContribution" type="{http://schema.base.ws.pro.finnone.nucleus.com}DecimalType" minOccurs="0"/>
 *         &lt;element name="capAmount" type="{http://schema.base.ws.pro.finnone.nucleus.com}AmountType" minOccurs="0"/>
 *         &lt;element name="repaymentTowards" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="dynamicFormDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}dynamicFormDetails" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "instrumentDetailDAS", propOrder = {
    "referenceNumber",
    "employerName",
    "employerId",
    "employeeNumber",
    "departmentName",
    "location",
    "costCentre",
    "designation",
    "repaymentContribution",
    "capAmount",
    "repaymentTowards",
    "dynamicFormDetails"
})
public class InstrumentDetailDAS {

    protected String referenceNumber;
    protected String employerName;
    protected Long employerId;
    protected String employeeNumber;
    protected String departmentName;
    protected String location;
    protected String costCentre;
    protected String designation;
    protected BigDecimal repaymentContribution;
    protected BigDecimal capAmount;
    protected String repaymentTowards;
    protected List<DynamicFormDetails> dynamicFormDetails;

    /**
     * Gets the value of the referenceNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReferenceNumber() {
        return referenceNumber;
    }

    /**
     * Sets the value of the referenceNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReferenceNumber(String value) {
        this.referenceNumber = value;
    }

    /**
     * Gets the value of the employerName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmployerName() {
        return employerName;
    }

    /**
     * Sets the value of the employerName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmployerName(String value) {
        this.employerName = value;
    }

    /**
     * Gets the value of the employerId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getEmployerId() {
        return employerId;
    }

    /**
     * Sets the value of the employerId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setEmployerId(Long value) {
        this.employerId = value;
    }

    /**
     * Gets the value of the employeeNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmployeeNumber() {
        return employeeNumber;
    }

    /**
     * Sets the value of the employeeNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmployeeNumber(String value) {
        this.employeeNumber = value;
    }

    /**
     * Gets the value of the departmentName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDepartmentName() {
        return departmentName;
    }

    /**
     * Sets the value of the departmentName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDepartmentName(String value) {
        this.departmentName = value;
    }

    /**
     * Gets the value of the location property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the value of the location property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocation(String value) {
        this.location = value;
    }

    /**
     * Gets the value of the costCentre property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCostCentre() {
        return costCentre;
    }

    /**
     * Sets the value of the costCentre property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCostCentre(String value) {
        this.costCentre = value;
    }

    /**
     * Gets the value of the designation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDesignation() {
        return designation;
    }

    /**
     * Sets the value of the designation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDesignation(String value) {
        this.designation = value;
    }

    /**
     * Gets the value of the repaymentContribution property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRepaymentContribution() {
        return repaymentContribution;
    }

    /**
     * Sets the value of the repaymentContribution property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRepaymentContribution(BigDecimal value) {
        this.repaymentContribution = value;
    }

    /**
     * Gets the value of the capAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCapAmount() {
        return capAmount;
    }

    /**
     * Sets the value of the capAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCapAmount(BigDecimal value) {
        this.capAmount = value;
    }

    /**
     * Gets the value of the repaymentTowards property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRepaymentTowards() {
        return repaymentTowards;
    }

    /**
     * Sets the value of the repaymentTowards property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRepaymentTowards(String value) {
        this.repaymentTowards = value;
    }

    /**
     * Gets the value of the dynamicFormDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dynamicFormDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDynamicFormDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DynamicFormDetails }
     * 
     * 
     */
    public List<DynamicFormDetails> getDynamicFormDetails() {
        if (dynamicFormDetails == null) {
            dynamicFormDetails = new ArrayList<DynamicFormDetails>();
        }
        return this.dynamicFormDetails;
    }

}
