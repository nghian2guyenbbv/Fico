
package vn.com.tpf.microservices.models.Finnone;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import vn.com.tpf.microservices.models.Finnone.cas.MoneyType;


/**
 * <p>Java class for instrumentDetailPDC complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="instrumentDetailPDC">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="pdcCollectedType" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="micrBased" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType"/>
 *         &lt;element name="micrCode" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="instrumentNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}LongType"/>
 *         &lt;element name="instrumentAmount" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType"/>
 *         &lt;element name="numberOfPDC" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType"/>
 *         &lt;element name="drwAcctName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="accountType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="drwAcctNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}LongType"/>
 *         &lt;element name="drwAcctShortCode" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="pdcType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="bankCode" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="city" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="bankingLocation" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="bankBranch" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
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
@XmlType(name = "instrumentDetailPDC", propOrder = {
    "pdcCollectedType",
    "micrBased",
    "micrCode",
    "instrumentNumber",
    "instrumentAmount",
    "numberOfPDC",
    "drwAcctName",
    "accountType",
    "drwAcctNumber",
    "drwAcctShortCode",
    "pdcType",
    "bankCode",
    "city",
    "bankingLocation",
    "bankBranch",
    "dynamicFormDetails"
})
public class InstrumentDetailPDC {

    protected Integer pdcCollectedType;
    protected boolean micrBased;
    @XmlElement(required = true)
    protected String micrCode;
    protected long instrumentNumber;
    @XmlElement(required = true)
    protected MoneyType instrumentAmount;
    protected int numberOfPDC;
    @XmlElement(required = true)
    protected String drwAcctName;
    @XmlElement(required = true)
    protected String accountType;
    protected long drwAcctNumber;
    @XmlElement(required = true)
    protected String drwAcctShortCode;
    protected String pdcType;
    @XmlElement(required = true)
    protected String bankCode;
    protected String city;
    protected String bankingLocation;
    @XmlElement(required = true)
    protected String bankBranch;
    protected List<DynamicFormDetails> dynamicFormDetails;

    /**
     * Gets the value of the pdcCollectedType property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPdcCollectedType() {
        return pdcCollectedType;
    }

    /**
     * Sets the value of the pdcCollectedType property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPdcCollectedType(Integer value) {
        this.pdcCollectedType = value;
    }

    /**
     * Gets the value of the micrBased property.
     * 
     */
    public boolean isMicrBased() {
        return micrBased;
    }

    /**
     * Sets the value of the micrBased property.
     * 
     */
    public void setMicrBased(boolean value) {
        this.micrBased = value;
    }

    /**
     * Gets the value of the micrCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMicrCode() {
        return micrCode;
    }

    /**
     * Sets the value of the micrCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMicrCode(String value) {
        this.micrCode = value;
    }

    /**
     * Gets the value of the instrumentNumber property.
     * 
     */
    public long getInstrumentNumber() {
        return instrumentNumber;
    }

    /**
     * Sets the value of the instrumentNumber property.
     * 
     */
    public void setInstrumentNumber(long value) {
        this.instrumentNumber = value;
    }

    /**
     * Gets the value of the instrumentAmount property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getInstrumentAmount() {
        return instrumentAmount;
    }

    /**
     * Sets the value of the instrumentAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setInstrumentAmount(MoneyType value) {
        this.instrumentAmount = value;
    }

    /**
     * Gets the value of the numberOfPDC property.
     * 
     */
    public int getNumberOfPDC() {
        return numberOfPDC;
    }

    /**
     * Sets the value of the numberOfPDC property.
     * 
     */
    public void setNumberOfPDC(int value) {
        this.numberOfPDC = value;
    }

    /**
     * Gets the value of the drwAcctName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDrwAcctName() {
        return drwAcctName;
    }

    /**
     * Sets the value of the drwAcctName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDrwAcctName(String value) {
        this.drwAcctName = value;
    }

    /**
     * Gets the value of the accountType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountType() {
        return accountType;
    }

    /**
     * Sets the value of the accountType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountType(String value) {
        this.accountType = value;
    }

    /**
     * Gets the value of the drwAcctNumber property.
     * 
     */
    public long getDrwAcctNumber() {
        return drwAcctNumber;
    }

    /**
     * Sets the value of the drwAcctNumber property.
     * 
     */
    public void setDrwAcctNumber(long value) {
        this.drwAcctNumber = value;
    }

    /**
     * Gets the value of the drwAcctShortCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDrwAcctShortCode() {
        return drwAcctShortCode;
    }

    /**
     * Sets the value of the drwAcctShortCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDrwAcctShortCode(String value) {
        this.drwAcctShortCode = value;
    }

    /**
     * Gets the value of the pdcType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPdcType() {
        return pdcType;
    }

    /**
     * Sets the value of the pdcType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPdcType(String value) {
        this.pdcType = value;
    }

    /**
     * Gets the value of the bankCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBankCode() {
        return bankCode;
    }

    /**
     * Sets the value of the bankCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBankCode(String value) {
        this.bankCode = value;
    }

    /**
     * Gets the value of the city property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the value of the city property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCity(String value) {
        this.city = value;
    }

    /**
     * Gets the value of the bankingLocation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBankingLocation() {
        return bankingLocation;
    }

    /**
     * Sets the value of the bankingLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBankingLocation(String value) {
        this.bankingLocation = value;
    }

    /**
     * Gets the value of the bankBranch property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBankBranch() {
        return bankBranch;
    }

    /**
     * Sets the value of the bankBranch property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBankBranch(String value) {
        this.bankBranch = value;
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
