
package vn.com.tpf.microservices.models.Finnone;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import vn.com.tpf.microservices.models.Finnone.cas.MoneyType;


/**
 * <p>Java class for updatePropertyDetail complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="updatePropertyDetail">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="applicationType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="propertyDescription" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="propertyType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="natureOfProperty" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="contractorDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}contractorDetails" minOccurs="0"/>
 *         &lt;element name="builderDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}builderDetails" minOccurs="0"/>
 *         &lt;element name="otherDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}otherDetails"/>
 *         &lt;element name="addressDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}addressDetails"/>
 *         &lt;element name="sellerDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}sellerDetails" minOccurs="0"/>
 *         &lt;element name="updateRegistrationDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}updateRegistrationDetails" minOccurs="0"/>
 *         &lt;element name="updateOwnershipDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}updateOwnershipDetails" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="cersaiDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}cersaiDetails" minOccurs="0"/>
 *         &lt;element name="propertyCost" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="approxPropertyCost" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="loanAmountReq" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updatePropertyDetail", propOrder = {
    "applicationType",
    "propertyDescription",
    "propertyType",
    "natureOfProperty",
    "contractorDetails",
    "builderDetails",
    "otherDetails",
    "addressDetails",
    "sellerDetails",
    "updateRegistrationDetails",
    "updateOwnershipDetails",
    "cersaiDetails",
    "propertyCost",
    "approxPropertyCost",
    "loanAmountReq"
})
public class UpdatePropertyDetail {

    protected String applicationType;
    protected String propertyDescription;
    @XmlElement(required = true)
    protected String propertyType;
    @XmlElement(required = true)
    protected String natureOfProperty;
    protected ContractorDetails contractorDetails;
    protected BuilderDetails builderDetails;
    @XmlElement(required = true)
    protected OtherDetails otherDetails;
    @XmlElement(required = true)
    protected AddressDetails addressDetails;
    protected SellerDetails sellerDetails;
    protected UpdateRegistrationDetails updateRegistrationDetails;
    protected List<UpdateOwnershipDetails> updateOwnershipDetails;
    protected CersaiDetails cersaiDetails;
    protected MoneyType propertyCost;
    protected MoneyType approxPropertyCost;
    protected MoneyType loanAmountReq;

    /**
     * Gets the value of the applicationType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApplicationType() {
        return applicationType;
    }

    /**
     * Sets the value of the applicationType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApplicationType(String value) {
        this.applicationType = value;
    }

    /**
     * Gets the value of the propertyDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPropertyDescription() {
        return propertyDescription;
    }

    /**
     * Sets the value of the propertyDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPropertyDescription(String value) {
        this.propertyDescription = value;
    }

    /**
     * Gets the value of the propertyType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPropertyType() {
        return propertyType;
    }

    /**
     * Sets the value of the propertyType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPropertyType(String value) {
        this.propertyType = value;
    }

    /**
     * Gets the value of the natureOfProperty property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNatureOfProperty() {
        return natureOfProperty;
    }

    /**
     * Sets the value of the natureOfProperty property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNatureOfProperty(String value) {
        this.natureOfProperty = value;
    }

    /**
     * Gets the value of the contractorDetails property.
     * 
     * @return
     *     possible object is
     *     {@link ContractorDetails }
     *     
     */
    public ContractorDetails getContractorDetails() {
        return contractorDetails;
    }

    /**
     * Sets the value of the contractorDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContractorDetails }
     *     
     */
    public void setContractorDetails(ContractorDetails value) {
        this.contractorDetails = value;
    }

    /**
     * Gets the value of the builderDetails property.
     * 
     * @return
     *     possible object is
     *     {@link BuilderDetails }
     *     
     */
    public BuilderDetails getBuilderDetails() {
        return builderDetails;
    }

    /**
     * Sets the value of the builderDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link BuilderDetails }
     *     
     */
    public void setBuilderDetails(BuilderDetails value) {
        this.builderDetails = value;
    }

    /**
     * Gets the value of the otherDetails property.
     * 
     * @return
     *     possible object is
     *     {@link OtherDetails }
     *     
     */
    public OtherDetails getOtherDetails() {
        return otherDetails;
    }

    /**
     * Sets the value of the otherDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link OtherDetails }
     *     
     */
    public void setOtherDetails(OtherDetails value) {
        this.otherDetails = value;
    }

    /**
     * Gets the value of the addressDetails property.
     * 
     * @return
     *     possible object is
     *     {@link AddressDetails }
     *     
     */
    public AddressDetails getAddressDetails() {
        return addressDetails;
    }

    /**
     * Sets the value of the addressDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddressDetails }
     *     
     */
    public void setAddressDetails(AddressDetails value) {
        this.addressDetails = value;
    }

    /**
     * Gets the value of the sellerDetails property.
     * 
     * @return
     *     possible object is
     *     {@link SellerDetails }
     *     
     */
    public SellerDetails getSellerDetails() {
        return sellerDetails;
    }

    /**
     * Sets the value of the sellerDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link SellerDetails }
     *     
     */
    public void setSellerDetails(SellerDetails value) {
        this.sellerDetails = value;
    }

    /**
     * Gets the value of the updateRegistrationDetails property.
     * 
     * @return
     *     possible object is
     *     {@link UpdateRegistrationDetails }
     *     
     */
    public UpdateRegistrationDetails getUpdateRegistrationDetails() {
        return updateRegistrationDetails;
    }

    /**
     * Sets the value of the updateRegistrationDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link UpdateRegistrationDetails }
     *     
     */
    public void setUpdateRegistrationDetails(UpdateRegistrationDetails value) {
        this.updateRegistrationDetails = value;
    }

    /**
     * Gets the value of the updateOwnershipDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the updateOwnershipDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUpdateOwnershipDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UpdateOwnershipDetails }
     * 
     * 
     */
    public List<UpdateOwnershipDetails> getUpdateOwnershipDetails() {
        if (updateOwnershipDetails == null) {
            updateOwnershipDetails = new ArrayList<UpdateOwnershipDetails>();
        }
        return this.updateOwnershipDetails;
    }

    /**
     * Gets the value of the cersaiDetails property.
     * 
     * @return
     *     possible object is
     *     {@link CersaiDetails }
     *     
     */
    public CersaiDetails getCersaiDetails() {
        return cersaiDetails;
    }

    /**
     * Sets the value of the cersaiDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link CersaiDetails }
     *     
     */
    public void setCersaiDetails(CersaiDetails value) {
        this.cersaiDetails = value;
    }

    /**
     * Gets the value of the propertyCost property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getPropertyCost() {
        return propertyCost;
    }

    /**
     * Sets the value of the propertyCost property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setPropertyCost(MoneyType value) {
        this.propertyCost = value;
    }

    /**
     * Gets the value of the approxPropertyCost property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getApproxPropertyCost() {
        return approxPropertyCost;
    }

    /**
     * Sets the value of the approxPropertyCost property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setApproxPropertyCost(MoneyType value) {
        this.approxPropertyCost = value;
    }

    /**
     * Gets the value of the loanAmountReq property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getLoanAmountReq() {
        return loanAmountReq;
    }

    /**
     * Sets the value of the loanAmountReq property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setLoanAmountReq(MoneyType value) {
        this.loanAmountReq = value;
    }

}
