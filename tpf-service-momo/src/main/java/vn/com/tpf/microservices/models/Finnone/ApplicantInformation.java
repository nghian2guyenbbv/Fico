
package vn.com.tpf.microservices.models.Finnone;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for applicantInformation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="applicantInformation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="partyRelationship" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="partyRole" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType"/>
 *         &lt;element name="customerType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="isExistingCustomer" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType"/>
 *         &lt;element name="customerNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="cifNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="personInfo" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}personInfo" minOccurs="0"/>
 *         &lt;element name="regionalCustInfo" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}regionalCustInfo" minOccurs="0"/>
 *         &lt;element name="identificationDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}identificationDetails" maxOccurs="10"/>
 *         &lt;element name="addressDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}addressDetails" maxOccurs="4"/>
 *         &lt;element name="communicationDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}communicationDetails"/>
 *         &lt;element name="family" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}family" minOccurs="0"/>
 *         &lt;element name="educationDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}educationDetails" maxOccurs="4" minOccurs="0"/>
 *         &lt;element name="occupationInfo" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}occupationInfo" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="yearsInTotalOccupation" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="monthsInTotalOccupation" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="financialDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}financialDetails"/>
 *         &lt;element name="bankDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}bankDetails" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="bankDetDynaFormDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}dynamicFormDetails" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="creditCardDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}creditCardDetails" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="nonIndvCust" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}nonIndvCust" minOccurs="0"/>
 *         &lt;element name="eKycVerification" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}eKycVerification" minOccurs="0"/>
 *         &lt;element name="fraudEnquiryRecord" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}fraudEnquiryRecord" minOccurs="0"/>
 *         &lt;element name="creditBureauEnquiryRecord" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}creditBureauEnquiryRecord" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "applicantInformation", propOrder = {
    "partyRelationship",
    "partyRole",
    "customerType",
    "isExistingCustomer",
    "customerNumber",
    "cifNumber",
    "personInfo",
    "regionalCustInfo",
    "identificationDetails",
    "addressDetails",
    "communicationDetails",
    "family",
    "educationDetails",
    "occupationInfo",
    "yearsInTotalOccupation",
    "monthsInTotalOccupation",
    "financialDetails",
    "bankDetails",
    "bankDetDynaFormDetails",
    "creditCardDetails",
    "nonIndvCust",
    "eKycVerification",
    "fraudEnquiryRecord",
    "creditBureauEnquiryRecord"
})
public class ApplicantInformation {

    @XmlElement(required = true)
    protected String partyRelationship;
    protected int partyRole;
    @XmlElement(required = true)
    protected String customerType;
    protected int isExistingCustomer;
    protected String customerNumber;
    protected String cifNumber;
    protected PersonInfo personInfo;
    protected RegionalCustInfo regionalCustInfo;

    public void setIdentificationDetails(List<IdentificationDetails> identificationDetails) {
        this.identificationDetails = identificationDetails;
    }

    public void setAddressDetails(List<AddressDetails> addressDetails) {
        this.addressDetails = addressDetails;
    }

    public void setOccupationInfo(List<OccupationInfo> occupationInfo) {
        this.occupationInfo = occupationInfo;
    }

    @XmlElement(required = true)
    protected List<IdentificationDetails> identificationDetails;
    @XmlElement(required = true)
    protected List<AddressDetails> addressDetails;
    @XmlElement(required = true)
    protected CommunicationDetails communicationDetails;
    protected Family family;
    protected List<EducationDetails> educationDetails;
    protected List<OccupationInfo> occupationInfo;
    protected Integer yearsInTotalOccupation;
    protected Integer monthsInTotalOccupation;
    @XmlElement(required = true)
    protected FinancialDetails financialDetails;
    protected List<BankDetails> bankDetails;
    protected List<DynamicFormDetails> bankDetDynaFormDetails;
    protected List<CreditCardDetails> creditCardDetails;
    protected NonIndvCust nonIndvCust;
    protected EKycVerification eKycVerification;
    protected FraudEnquiryRecord fraudEnquiryRecord;
    protected CreditBureauEnquiryRecord creditBureauEnquiryRecord;

    /**
     * Gets the value of the partyRelationship property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPartyRelationship() {
        return partyRelationship;
    }

    /**
     * Sets the value of the partyRelationship property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPartyRelationship(String value) {
        this.partyRelationship = value;
    }

    /**
     * Gets the value of the partyRole property.
     * 
     */
    public int getPartyRole() {
        return partyRole;
    }

    /**
     * Sets the value of the partyRole property.
     * 
     */
    public void setPartyRole(int value) {
        this.partyRole = value;
    }

    /**
     * Gets the value of the customerType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerType() {
        return customerType;
    }

    /**
     * Sets the value of the customerType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerType(String value) {
        this.customerType = value;
    }

    /**
     * Gets the value of the isExistingCustomer property.
     * 
     */
    public int getIsExistingCustomer() {
        return isExistingCustomer;
    }

    /**
     * Sets the value of the isExistingCustomer property.
     * 
     */
    public void setIsExistingCustomer(int value) {
        this.isExistingCustomer = value;
    }

    /**
     * Gets the value of the customerNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerNumber() {
        return customerNumber;
    }

    /**
     * Sets the value of the customerNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerNumber(String value) {
        this.customerNumber = value;
    }

    /**
     * Gets the value of the cifNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCifNumber() {
        return cifNumber;
    }

    /**
     * Sets the value of the cifNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCifNumber(String value) {
        this.cifNumber = value;
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
     * Gets the value of the regionalCustInfo property.
     * 
     * @return
     *     possible object is
     *     {@link RegionalCustInfo }
     *     
     */
    public RegionalCustInfo getRegionalCustInfo() {
        return regionalCustInfo;
    }

    /**
     * Sets the value of the regionalCustInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link RegionalCustInfo }
     *     
     */
    public void setRegionalCustInfo(RegionalCustInfo value) {
        this.regionalCustInfo = value;
    }

    /**
     * Gets the value of the identificationDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the identificationDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIdentificationDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IdentificationDetails }
     * 
     * 
     */
    public List<IdentificationDetails> getIdentificationDetails() {
        if (identificationDetails == null) {
            identificationDetails = new ArrayList<IdentificationDetails>();
        }
        return this.identificationDetails;
    }

    /**
     * Gets the value of the addressDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the addressDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAddressDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AddressDetails }
     * 
     * 
     */
    public List<AddressDetails> getAddressDetails() {
        if (addressDetails == null) {
            addressDetails = new ArrayList<AddressDetails>();
        }
        return this.addressDetails;
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
     * Gets the value of the family property.
     * 
     * @return
     *     possible object is
     *     {@link Family }
     *     
     */
    public Family getFamily() {
        return family;
    }

    /**
     * Sets the value of the family property.
     * 
     * @param value
     *     allowed object is
     *     {@link Family }
     *     
     */
    public void setFamily(Family value) {
        this.family = value;
    }

    /**
     * Gets the value of the educationDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the educationDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEducationDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EducationDetails }
     * 
     * 
     */
    public List<EducationDetails> getEducationDetails() {
        if (educationDetails == null) {
            educationDetails = new ArrayList<EducationDetails>();
        }
        return this.educationDetails;
    }

    /**
     * Gets the value of the occupationInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the occupationInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOccupationInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OccupationInfo }
     * 
     * 
     */
    public List<OccupationInfo> getOccupationInfo() {
        if (occupationInfo == null) {
            occupationInfo = new ArrayList<OccupationInfo>();
        }
        return this.occupationInfo;
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
     *     {@link FinancialDetails }
     *     
     */
    public FinancialDetails getFinancialDetails() {
        return financialDetails;
    }

    /**
     * Sets the value of the financialDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link FinancialDetails }
     *     
     */
    public void setFinancialDetails(FinancialDetails value) {
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
     * {@link BankDetails }
     * 
     * 
     */
    public List<BankDetails> getBankDetails() {
        if (bankDetails == null) {
            bankDetails = new ArrayList<BankDetails>();
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
     * {@link CreditCardDetails }
     * 
     * 
     */
    public List<CreditCardDetails> getCreditCardDetails() {
        if (creditCardDetails == null) {
            creditCardDetails = new ArrayList<CreditCardDetails>();
        }
        return this.creditCardDetails;
    }

    /**
     * Gets the value of the nonIndvCust property.
     * 
     * @return
     *     possible object is
     *     {@link NonIndvCust }
     *     
     */
    public NonIndvCust getNonIndvCust() {
        return nonIndvCust;
    }

    /**
     * Sets the value of the nonIndvCust property.
     * 
     * @param value
     *     allowed object is
     *     {@link NonIndvCust }
     *     
     */
    public void setNonIndvCust(NonIndvCust value) {
        this.nonIndvCust = value;
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

}
