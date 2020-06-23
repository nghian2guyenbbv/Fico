
package vn.com.tpf.microservices.models.Finnone;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

import vn.com.tpf.microservices.models.Finnone.cas.MoneyType;


/**
 * <p>Java class for updateVapDetail complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="updateVapDetail">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="vapNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="vapProduct" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="amtCompPolicy" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="payOutCompPolicy" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="vapTreatment" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="vapAmount" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="insuranceCompany" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="policyNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="startDate" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType" minOccurs="0"/>
 *         &lt;element name="maturityDate" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType" minOccurs="0"/>
 *         &lt;element name="coverageType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="coverageAmount" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="premiumAmount" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="insuredAgainst" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="lossPayee" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="ageOfVehicle" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="dateOfDelivery" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType" minOccurs="0"/>
 *         &lt;element name="distanceCovered" type="{http://schema.base.ws.pro.finnone.nucleus.com}LongType" minOccurs="0"/>
 *         &lt;element name="wheelRepairFrequency" type="{http://schema.base.ws.pro.finnone.nucleus.com}LongType" minOccurs="0"/>
 *         &lt;element name="vapQuantity" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="homeFurnishVap" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}vapGrid" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="accessoryDetailVap" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}vapGrid" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="implementsAttachment" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}vapGrid" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="nomineeDetail" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}nomineeDetail" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="chargeCode" type="{http://schema.base.ws.pro.finnone.nucleus.com}LongType" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="fcpDetail" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}fcpDetail" minOccurs="0"/>
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
@XmlType(name = "updateVapDetail", propOrder = {
    "vapNumber",
    "vapProduct",
    "amtCompPolicy",
    "payOutCompPolicy",
    "vapTreatment",
    "vapAmount",
    "insuranceCompany",
    "policyNumber",
    "startDate",
    "maturityDate",
    "coverageType",
    "coverageAmount",
    "premiumAmount",
    "insuredAgainst",
    "lossPayee",
    "ageOfVehicle",
    "dateOfDelivery",
    "distanceCovered",
    "wheelRepairFrequency",
    "vapQuantity",
    "homeFurnishVap",
    "accessoryDetailVap",
    "implementsAttachment",
    "nomineeDetail",
    "chargeCode",
    "fcpDetail",
    "deleteFlag"
})
public class UpdateVapDetail {

    @XmlElement(required = true)
    protected String vapNumber;
    @XmlElement(required = true)
    protected String vapProduct;
    @XmlElement(required = true)
    protected String amtCompPolicy;
    @XmlElement(required = true)
    protected String payOutCompPolicy;
    @XmlElement(required = true)
    protected String vapTreatment;
    protected MoneyType vapAmount;
    protected String insuranceCompany;
    protected String policyNumber;
    protected XMLGregorianCalendar startDate;
    protected XMLGregorianCalendar maturityDate;
    protected String coverageType;
    protected MoneyType coverageAmount;
    protected MoneyType premiumAmount;
    protected String insuredAgainst;
    protected String lossPayee;
    protected Integer ageOfVehicle;
    protected XMLGregorianCalendar dateOfDelivery;
    protected Long distanceCovered;
    protected Long wheelRepairFrequency;
    protected Integer vapQuantity;
    protected List<VapGrid> homeFurnishVap;
    protected List<VapGrid> accessoryDetailVap;
    protected List<VapGrid> implementsAttachment;
    protected List<NomineeDetail> nomineeDetail;
    @XmlElement(type = Long.class)
    protected List<Long> chargeCode;
    protected FcpDetail fcpDetail;
    protected Boolean deleteFlag;

    /**
     * Gets the value of the vapNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVapNumber() {
        return vapNumber;
    }

    /**
     * Sets the value of the vapNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVapNumber(String value) {
        this.vapNumber = value;
    }

    /**
     * Gets the value of the vapProduct property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVapProduct() {
        return vapProduct;
    }

    /**
     * Sets the value of the vapProduct property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVapProduct(String value) {
        this.vapProduct = value;
    }

    /**
     * Gets the value of the amtCompPolicy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAmtCompPolicy() {
        return amtCompPolicy;
    }

    /**
     * Sets the value of the amtCompPolicy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAmtCompPolicy(String value) {
        this.amtCompPolicy = value;
    }

    /**
     * Gets the value of the payOutCompPolicy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPayOutCompPolicy() {
        return payOutCompPolicy;
    }

    /**
     * Sets the value of the payOutCompPolicy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPayOutCompPolicy(String value) {
        this.payOutCompPolicy = value;
    }

    /**
     * Gets the value of the vapTreatment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVapTreatment() {
        return vapTreatment;
    }

    /**
     * Sets the value of the vapTreatment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVapTreatment(String value) {
        this.vapTreatment = value;
    }

    /**
     * Gets the value of the vapAmount property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getVapAmount() {
        return vapAmount;
    }

    /**
     * Sets the value of the vapAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setVapAmount(MoneyType value) {
        this.vapAmount = value;
    }

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
     * Gets the value of the insuredAgainst property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInsuredAgainst() {
        return insuredAgainst;
    }

    /**
     * Sets the value of the insuredAgainst property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInsuredAgainst(String value) {
        this.insuredAgainst = value;
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

    /**
     * Gets the value of the ageOfVehicle property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAgeOfVehicle() {
        return ageOfVehicle;
    }

    /**
     * Sets the value of the ageOfVehicle property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAgeOfVehicle(Integer value) {
        this.ageOfVehicle = value;
    }

    /**
     * Gets the value of the dateOfDelivery property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateOfDelivery() {
        return dateOfDelivery;
    }

    /**
     * Sets the value of the dateOfDelivery property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateOfDelivery(XMLGregorianCalendar value) {
        this.dateOfDelivery = value;
    }

    /**
     * Gets the value of the distanceCovered property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getDistanceCovered() {
        return distanceCovered;
    }

    /**
     * Sets the value of the distanceCovered property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setDistanceCovered(Long value) {
        this.distanceCovered = value;
    }

    /**
     * Gets the value of the wheelRepairFrequency property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getWheelRepairFrequency() {
        return wheelRepairFrequency;
    }

    /**
     * Sets the value of the wheelRepairFrequency property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setWheelRepairFrequency(Long value) {
        this.wheelRepairFrequency = value;
    }

    /**
     * Gets the value of the vapQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getVapQuantity() {
        return vapQuantity;
    }

    /**
     * Sets the value of the vapQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setVapQuantity(Integer value) {
        this.vapQuantity = value;
    }

    /**
     * Gets the value of the homeFurnishVap property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the homeFurnishVap property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHomeFurnishVap().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link VapGrid }
     * 
     * 
     */
    public List<VapGrid> getHomeFurnishVap() {
        if (homeFurnishVap == null) {
            homeFurnishVap = new ArrayList<VapGrid>();
        }
        return this.homeFurnishVap;
    }

    /**
     * Gets the value of the accessoryDetailVap property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the accessoryDetailVap property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAccessoryDetailVap().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link VapGrid }
     * 
     * 
     */
    public List<VapGrid> getAccessoryDetailVap() {
        if (accessoryDetailVap == null) {
            accessoryDetailVap = new ArrayList<VapGrid>();
        }
        return this.accessoryDetailVap;
    }

    /**
     * Gets the value of the implementsAttachment property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the implementsAttachment property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getImplementsAttachment().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link VapGrid }
     * 
     * 
     */
    public List<VapGrid> getImplementsAttachment() {
        if (implementsAttachment == null) {
            implementsAttachment = new ArrayList<VapGrid>();
        }
        return this.implementsAttachment;
    }

    /**
     * Gets the value of the nomineeDetail property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the nomineeDetail property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNomineeDetail().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NomineeDetail }
     * 
     * 
     */
    public List<NomineeDetail> getNomineeDetail() {
        if (nomineeDetail == null) {
            nomineeDetail = new ArrayList<NomineeDetail>();
        }
        return this.nomineeDetail;
    }

    /**
     * Gets the value of the chargeCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the chargeCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getChargeCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Long }
     * 
     * 
     */
    public List<Long> getChargeCode() {
        if (chargeCode == null) {
            chargeCode = new ArrayList<Long>();
        }
        return this.chargeCode;
    }

    /**
     * Gets the value of the fcpDetail property.
     * 
     * @return
     *     possible object is
     *     {@link FcpDetail }
     *     
     */
    public FcpDetail getFcpDetail() {
        return fcpDetail;
    }

    /**
     * Sets the value of the fcpDetail property.
     * 
     * @param value
     *     allowed object is
     *     {@link FcpDetail }
     *     
     */
    public void setFcpDetail(FcpDetail value) {
        this.fcpDetail = value;
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
