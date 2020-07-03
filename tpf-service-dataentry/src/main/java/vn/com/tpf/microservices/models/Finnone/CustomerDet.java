
package vn.com.tpf.microservices.models.Finnone;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for customerDet complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="customerDet">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="customerParameter" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}customerParameter" minOccurs="0"/>
 *         &lt;element name="personInfo" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}personInfo" minOccurs="0"/>
 *         &lt;element name="updateIdentificationDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}updateIdentificationDetails" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="updateAddressDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}updateAddressDetails" maxOccurs="4" minOccurs="0"/>
 *         &lt;element name="communicationDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}communicationDetails" minOccurs="0"/>
 *         &lt;element name="updateEducationDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}updateEducationDetails" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="updateFamily" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}updateFamily" minOccurs="0"/>
 *         &lt;element name="updateOccupationInfo" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}updateOccupationInfo" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="yearsInTotalOccupation" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="monthsInTotalOccupation" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="financialDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}updateFinancialDetails" minOccurs="0"/>
 *         &lt;element name="bankDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}updateBankDetails" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="bankDetDynaFormDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}dynamicFormDetails" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="creditCardDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}updateCreditCardDetails" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="updNonIndvCust" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}updNonIndvCust" minOccurs="0"/>
 *         &lt;element name="externalCustInfo" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}regionalCustInfo" minOccurs="0"/>
 *         &lt;element name="eKycVerification" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}eKycVerification" minOccurs="0"/>
 *         &lt;element name="fraudEnquiryRecord" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}fraudEnquiryRecord" minOccurs="0"/>
 *         &lt;element name="creditBureauEnquiryRecord" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}creditBureauEnquiryRecord" minOccurs="0"/>
 *         &lt;element name="deleteFlag" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "customerDet", propOrder = {
    "customerParameter",
    "personInfo",
    "updateIdentificationDetails",
    "updateAddressDetails",
    "communicationDetails",
    "updateEducationDetails",
    "updateFamily",
    "updateOccupationInfo",
    "yearsInTotalOccupation",
    "monthsInTotalOccupation",
    "financialDetails",
    "bankDetails",
    "bankDetDynaFormDetails",
    "creditCardDetails",
    "updNonIndvCust",
    "externalCustInfo",
    "eKycVerification",
    "fraudEnquiryRecord",
    "creditBureauEnquiryRecord",
    "deleteFlag"
})
public class CustomerDet {

    protected CustomerParameter customerParameter;
    protected PersonInfo personInfo;
    protected List<UpdateIdentificationDetails> updateIdentificationDetails;
    protected List<UpdateAddressDetails> updateAddressDetails;
    protected CommunicationDetails communicationDetails;
    protected List<UpdateEducationDetails> updateEducationDetails;
    protected UpdateFamily updateFamily;
    protected List<UpdateOccupationInfo> updateOccupationInfo;
    protected Integer yearsInTotalOccupation;
    protected Integer monthsInTotalOccupation;
    protected UpdateFinancialDetails financialDetails;
    protected List<UpdateBankDetails> bankDetails;
    protected List<DynamicFormDetails> bankDetDynaFormDetails;
    protected List<UpdateCreditCardDetails> creditCardDetails;
    protected UpdNonIndvCust updNonIndvCust;
    protected RegionalCustInfo externalCustInfo;
    protected EKycVerification eKycVerification;
    protected FraudEnquiryRecord fraudEnquiryRecord;
    protected CreditBureauEnquiryRecord creditBureauEnquiryRecord;
    protected Boolean deleteFlag;

    /**
     * Gets the value of the customerParameter property.
     * 
     * @return
     *     possible object is
     *     {@link CustomerParameter }
     *     
     */
    public CustomerParameter getCustomerParameter() {
        return customerParameter;
    }

    /**
     * Sets the value of the customerParameter property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustomerParameter }
     *     
     */
    public void setCustomerParameter(CustomerParameter value) {
        this.customerParameter = value;
    }

    /**
     * Gets the value of the personInfo property.
     * 
     * @return
     *     possible object is
     *     {@link PersonInfo }
     *     
     */
    public PersonInfo getPersonInfo() {
        return personInfo;
    }

    /**
     * Sets the value of the personInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link PersonInfo }
     *     
     */
    public void setPersonInfo(PersonInfo value) {
        this.personInfo = value;
    }

    /**
     * Gets the value of the updateIdentificationDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the updateIdentificationDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUpdateIdentificationDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UpdateIdentificationDetails }
     * 
     * 
     */
    public List<UpdateIdentificationDetails> getUpdateIdentificationDetails() {
        if (updateIdentificationDetails == null) {
            updateIdentificationDetails = new ArrayList<UpdateIdentificationDetails>();
        }
        return this.updateIdentificationDetails;
    }

    /**
     * Gets the value of the updateAddressDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the updateAddressDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUpdateAddressDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UpdateAddressDetails }
     * 
     * 
     */
    public List<UpdateAddressDetails> getUpdateAddressDetails() {
        if (updateAddressDetails == null) {
            updateAddressDetails = new ArrayList<UpdateAddressDetails>();
        }
        return this.updateAddressDetails;
    }

    /**
     * Gets the value of the communicationDetails property.
     * 
     * @return
     *     possible object is
     *     {@link CommunicationDetails }
     *     
     */
    public CommunicationDetails getCommunicationDetails() {
        return communicationDetails;
    }

    /**
     * Sets the value of the communicationDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link CommunicationDetails }
     *     
     */
    public void setCommunicationDetails(CommunicationDetails value) {
        this.communicationDetails = value;
    }

    /**
     * Gets the value of the updateEducationDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the updateEducationDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUpdateEducationDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UpdateEducationDetails }
     * 
     * 
     */
    public List<UpdateEducationDetails> getUpdateEducationDetails() {
        if (updateEducationDetails == null) {
            updateEducationDetails = new ArrayList<UpdateEducationDetails>();
        }
        return this.updateEducationDetails;
    }

    /**
     * Gets the value of the updateFamily property.
     * 
     * @return
     *     possible object is
     *     {@link UpdateFamily }
     *     
     */
    public UpdateFamily getUpdateFamily() {
        return updateFamily;
    }

    /**
     * Sets the value of the updateFamily property.
     * 
     * @param value
     *     allowed object is
     *     {@link UpdateFamily }
     *     
     */
    public void setUpdateFamily(UpdateFamily value) {
        this.updateFamily = value;
    }

    /**
     * Gets the value of the updateOccupationInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the updateOccupationInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUpdateOccupationInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UpdateOccupationInfo }
     * 
     * 
     */
    public List<UpdateOccupationInfo> getUpdateOccupationInfo() {
        if (updateOccupationInfo == null) {
            updateOccupationInfo = new ArrayList<UpdateOccupationInfo>();
        }
        return this.updateOccupationInfo;
    }

    /**
     * Gets the value of the yearsInTotalOccupation property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getYearsInTotalOccupation() {
        return yearsInTotalOccupation;
    }

    /**
     * Sets the value of the yearsInTotalOccupation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setYearsInTotalOccupation(Integer value) {
        this.yearsInTotalOccupation = value;
    }

    /**
     * Gets the value of the monthsInTotalOccupation property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMonthsInTotalOccupation() {
        return monthsInTotalOccupation;
    }

    /**
     * Sets the value of the monthsInTotalOccupation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMonthsInTotalOccupation(Integer value) {
        this.monthsInTotalOccupation = value;
    }

    /**
     * Gets the value of the financialDetails property.
     * 
     * @return
     *     possible object is
     *     {@link UpdateFinancialDetails }
     *     
     */
    public UpdateFinancialDetails getFinancialDetails() {
        return financialDetails;
    }

    /**
     * Sets the value of the financialDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link UpdateFinancialDetails }
     *     
     */
    public void setFinancialDetails(UpdateFinancialDetails value) {
        this.financialDetails = value;
    }

    /**
     * Gets the value of the bankDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bankDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBankDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UpdateBankDetails }
     * 
     * 
     */
    public List<UpdateBankDetails> getBankDetails() {
        if (bankDetails == null) {
            bankDetails = new ArrayList<UpdateBankDetails>();
        }
        return this.bankDetails;
    }

    /**
     * Gets the value of the bankDetDynaFormDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bankDetDynaFormDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBankDetDynaFormDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DynamicFormDetails }
     * 
     * 
     */
    public List<DynamicFormDetails> getBankDetDynaFormDetails() {
        if (bankDetDynaFormDetails == null) {
            bankDetDynaFormDetails = new ArrayList<DynamicFormDetails>();
        }
        return this.bankDetDynaFormDetails;
    }

    /**
     * Gets the value of the creditCardDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the creditCardDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCreditCardDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UpdateCreditCardDetails }
     * 
     * 
     */
    public List<UpdateCreditCardDetails> getCreditCardDetails() {
        if (creditCardDetails == null) {
            creditCardDetails = new ArrayList<UpdateCreditCardDetails>();
        }
        return this.creditCardDetails;
    }

    /**
     * Gets the value of the updNonIndvCust property.
     * 
     * @return
     *     possible object is
     *     {@link UpdNonIndvCust }
     *     
     */
    public UpdNonIndvCust getUpdNonIndvCust() {
        return updNonIndvCust;
    }

    /**
     * Sets the value of the updNonIndvCust property.
     * 
     * @param value
     *     allowed object is
     *     {@link UpdNonIndvCust }
     *     
     */
    public void setUpdNonIndvCust(UpdNonIndvCust value) {
        this.updNonIndvCust = value;
    }

    /**
     * Gets the value of the externalCustInfo property.
     * 
     * @return
     *     possible object is
     *     {@link RegionalCustInfo }
     *     
     */
    public RegionalCustInfo getExternalCustInfo() {
        return externalCustInfo;
    }

    /**
     * Sets the value of the externalCustInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link RegionalCustInfo }
     *     
     */
    public void setExternalCustInfo(RegionalCustInfo value) {
        this.externalCustInfo = value;
    }

    /**
     * Gets the value of the eKycVerification property.
     * 
     * @return
     *     possible object is
     *     {@link EKycVerification }
     *     
     */
    public EKycVerification getEKycVerification() {
        return eKycVerification;
    }

    /**
     * Sets the value of the eKycVerification property.
     * 
     * @param value
     *     allowed object is
     *     {@link EKycVerification }
     *     
     */
    public void setEKycVerification(EKycVerification value) {
        this.eKycVerification = value;
    }

    /**
     * Gets the value of the fraudEnquiryRecord property.
     * 
     * @return
     *     possible object is
     *     {@link FraudEnquiryRecord }
     *     
     */
    public FraudEnquiryRecord getFraudEnquiryRecord() {
        return fraudEnquiryRecord;
    }

    /**
     * Sets the value of the fraudEnquiryRecord property.
     * 
     * @param value
     *     allowed object is
     *     {@link FraudEnquiryRecord }
     *     
     */
    public void setFraudEnquiryRecord(FraudEnquiryRecord value) {
        this.fraudEnquiryRecord = value;
    }

    /**
     * Gets the value of the creditBureauEnquiryRecord property.
     * 
     * @return
     *     possible object is
     *     {@link CreditBureauEnquiryRecord }
     *     
     */
    public CreditBureauEnquiryRecord getCreditBureauEnquiryRecord() {
        return creditBureauEnquiryRecord;
    }

    /**
     * Sets the value of the creditBureauEnquiryRecord property.
     * 
     * @param value
     *     allowed object is
     *     {@link CreditBureauEnquiryRecord }
     *     
     */
    public void setCreditBureauEnquiryRecord(CreditBureauEnquiryRecord value) {
        this.creditBureauEnquiryRecord = value;
    }

    /**
     * Gets the value of the deleteFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDeleteFlag() {
        return deleteFlag;
    }

    /**
     * Sets the value of the deleteFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDeleteFlag(Boolean value) {
        this.deleteFlag = value;
    }

}
