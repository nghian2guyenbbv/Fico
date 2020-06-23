
package vn.com.tpf.microservices.models.Finnone;
import vn.com.tpf.microservices.models.Finnone.cas.MoneyType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;



/**
 * <p>Java class for bTDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="bTDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="bankType" type="{http://schema.base.ws.pro.finnone.nucleus.com}StringType" minOccurs="0"/>
 *         &lt;element name="referenceHostSystem" type="{http://schema.base.ws.pro.finnone.nucleus.com}StringType" minOccurs="0"/>
 *         &lt;element name="institutionName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="accountNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="loanType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="mob" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="sanctionedAmount" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType"/>
 *         &lt;element name="currentBalance" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType"/>
 *         &lt;element name="closureAmount" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="loanClosureDate" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType" minOccurs="0"/>
 *         &lt;element name="linkedApplicant" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "bTDetails", propOrder = {
    "bankType",
    "referenceHostSystem",
    "institutionName",
    "accountNumber",
    "loanType",
    "mob",
    "sanctionedAmount",
    "currentBalance",
    "closureAmount",
    "loanClosureDate",
    "linkedApplicant"
})
public class BTDetails {

    protected String bankType;
    protected String referenceHostSystem;
    @XmlElement(required = true)
    protected String institutionName;
    protected String accountNumber;
    @XmlElement(required = true)
    protected String loanType;
    protected Integer mob;
    @XmlElement(required = true)
    protected MoneyType sanctionedAmount;
    @XmlElement(required = true)
    protected MoneyType currentBalance;
    protected MoneyType closureAmount;
    protected XMLGregorianCalendar loanClosureDate;
    protected String linkedApplicant;

    /**
     * Gets the value of the bankType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBankType() {
        return bankType;
    }

    /**
     * Sets the value of the bankType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBankType(String value) {
        this.bankType = value;
    }

    /**
     * Gets the value of the referenceHostSystem property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReferenceHostSystem() {
        return referenceHostSystem;
    }

    /**
     * Sets the value of the referenceHostSystem property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReferenceHostSystem(String value) {
        this.referenceHostSystem = value;
    }

    /**
     * Gets the value of the institutionName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstitutionName() {
        return institutionName;
    }

    /**
     * Sets the value of the institutionName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstitutionName(String value) {
        this.institutionName = value;
    }

    /**
     * Gets the value of the accountNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * Sets the value of the accountNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountNumber(String value) {
        this.accountNumber = value;
    }

    /**
     * Gets the value of the loanType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLoanType() {
        return loanType;
    }

    /**
     * Sets the value of the loanType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLoanType(String value) {
        this.loanType = value;
    }

    /**
     * Gets the value of the mob property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMob() {
        return mob;
    }

    /**
     * Sets the value of the mob property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMob(Integer value) {
        this.mob = value;
    }

    /**
     * Gets the value of the sanctionedAmount property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getSanctionedAmount() {
        return sanctionedAmount;
    }

    /**
     * Sets the value of the sanctionedAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setSanctionedAmount(MoneyType value) {
        this.sanctionedAmount = value;
    }

    /**
     * Gets the value of the currentBalance property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getCurrentBalance() {
        return currentBalance;
    }

    /**
     * Sets the value of the currentBalance property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setCurrentBalance(MoneyType value) {
        this.currentBalance = value;
    }

    /**
     * Gets the value of the closureAmount property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getClosureAmount() {
        return closureAmount;
    }

    /**
     * Sets the value of the closureAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setClosureAmount(MoneyType value) {
        this.closureAmount = value;
    }

    /**
     * Gets the value of the loanClosureDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLoanClosureDate() {
        return loanClosureDate;
    }

    /**
     * Sets the value of the loanClosureDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setLoanClosureDate(XMLGregorianCalendar value) {
        this.loanClosureDate = value;
    }

    /**
     * Gets the value of the linkedApplicant property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLinkedApplicant() {
        return linkedApplicant;
    }

    /**
     * Sets the value of the linkedApplicant property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLinkedApplicant(String value) {
        this.linkedApplicant = value;
    }

}
