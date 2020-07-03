
package vn.com.tpf.microservices.models.Finnone;
import vn.com.tpf.microservices.models.Finnone.cas.MoneyType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;



/**
 * <p>Java class for collateralInsuranceDetail complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="collateralInsuranceDetail">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="insuranceCompany" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="policyNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="startDate" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType"/>
 *         &lt;element name="maturityDate" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType"/>
 *         &lt;element name="coverageType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="coverageAmount" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType"/>
 *         &lt;element name="premiumAmount" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType"/>
 *         &lt;element name="lossPayee" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "collateralInsuranceDetail", propOrder = {
    "insuranceCompany",
    "policyNumber",
    "startDate",
    "maturityDate",
    "coverageType",
    "coverageAmount",
    "premiumAmount",
    "lossPayee"
})
public class CollateralInsuranceDetail {

    @XmlElement(required = true)
    protected String insuranceCompany;
    @XmlElement(required = true)
    protected String policyNumber;
    @XmlElement(required = true)
    protected XMLGregorianCalendar startDate;
    @XmlElement(required = true)
    protected XMLGregorianCalendar maturityDate;
    @XmlElement(required = true)
    protected String coverageType;
    @XmlElement(required = true)
    protected MoneyType coverageAmount;
    @XmlElement(required = true)
    protected MoneyType premiumAmount;
    @XmlElement(required = true)
    protected String lossPayee;

    /**
     * Gets the value of the insuranceCompany property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInsuranceCompany() {
        return insuranceCompany;
    }

    /**
     * Sets the value of the insuranceCompany property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInsuranceCompany(String value) {
        this.insuranceCompany = value;
    }

    /**
     * Gets the value of the policyNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPolicyNumber() {
        return policyNumber;
    }

    /**
     * Sets the value of the policyNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPolicyNumber(String value) {
        this.policyNumber = value;
    }

    /**
     * Gets the value of the startDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getStartDate() {
        return startDate;
    }

    /**
     * Sets the value of the startDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setStartDate(XMLGregorianCalendar value) {
        this.startDate = value;
    }

    /**
     * Gets the value of the maturityDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getMaturityDate() {
        return maturityDate;
    }

    /**
     * Sets the value of the maturityDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setMaturityDate(XMLGregorianCalendar value) {
        this.maturityDate = value;
    }

    /**
     * Gets the value of the coverageType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCoverageType() {
        return coverageType;
    }

    /**
     * Sets the value of the coverageType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCoverageType(String value) {
        this.coverageType = value;
    }

    /**
     * Gets the value of the coverageAmount property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getCoverageAmount() {
        return coverageAmount;
    }

    /**
     * Sets the value of the coverageAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setCoverageAmount(MoneyType value) {
        this.coverageAmount = value;
    }

    /**
     * Gets the value of the premiumAmount property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getPremiumAmount() {
        return premiumAmount;
    }

    /**
     * Sets the value of the premiumAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setPremiumAmount(MoneyType value) {
        this.premiumAmount = value;
    }

    /**
     * Gets the value of the lossPayee property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLossPayee() {
        return lossPayee;
    }

    /**
     * Sets the value of the lossPayee property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLossPayee(String value) {
        this.lossPayee = value;
    }

}
