
package vn.com.tpf.microservices.models.Finnone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

import vn.com.tpf.microservices.models.Finnone.cas.MoneyType;


/**
 * <p>Java class for updateRecieptDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="updateRecieptDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="paymentMode" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="recievedFrom" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType"/>
 *         &lt;element name="businessPartnerName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="recieptNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="recieptDate" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType"/>
 *         &lt;element name="recieptAmount" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType"/>
 *         &lt;element name="channel" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="instrumentNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="instrumentDate" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType" minOccurs="0"/>
 *         &lt;element name="isMicrBased" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType" minOccurs="0"/>
 *         &lt;element name="micrCode" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="bank" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="bankBranch" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="subPaymentModeType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="recieptId" type="{http://schema.base.ws.pro.finnone.nucleus.com}LongType" minOccurs="0"/>
 *         &lt;element name="deleteFlag" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType" minOccurs="0"/>
 *         &lt;element name="createReceiptFlag" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType" minOccurs="0"/>
 *         &lt;element name="instrumentId" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateRecieptDetails", propOrder = {
    "paymentMode",
    "recievedFrom",
    "businessPartnerName",
    "recieptNumber",
    "recieptDate",
    "recieptAmount",
    "channel",
    "instrumentNumber",
    "instrumentDate",
    "isMicrBased",
    "micrCode",
    "bank",
    "bankBranch",
    "subPaymentModeType",
    "recieptId",
    "deleteFlag",
    "createReceiptFlag",
    "instrumentId"
})
public class UpdateRecieptDetails {

    @XmlElement(required = true)
    protected String paymentMode;
    protected int recievedFrom;
    @XmlElement(required = true)
    protected String businessPartnerName;
    protected String recieptNumber;
    @XmlElement(required = true)
    protected XMLGregorianCalendar recieptDate;
    @XmlElement(required = true)
    protected MoneyType recieptAmount;
    protected String channel;
    protected Integer instrumentNumber;
    protected XMLGregorianCalendar instrumentDate;
    protected Boolean isMicrBased;
    protected String micrCode;
    protected String bank;
    protected String bankBranch;
    protected String subPaymentModeType;
    protected Long recieptId;
    protected Boolean deleteFlag;
    protected Boolean createReceiptFlag;
    protected String instrumentId;

    /**
     * Gets the value of the paymentMode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaymentMode() {
        return paymentMode;
    }

    /**
     * Sets the value of the paymentMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaymentMode(String value) {
        this.paymentMode = value;
    }

    /**
     * Gets the value of the recievedFrom property.
     * 
     */
    public int getRecievedFrom() {
        return recievedFrom;
    }

    /**
     * Sets the value of the recievedFrom property.
     * 
     */
    public void setRecievedFrom(int value) {
        this.recievedFrom = value;
    }

    /**
     * Gets the value of the businessPartnerName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBusinessPartnerName() {
        return businessPartnerName;
    }

    /**
     * Sets the value of the businessPartnerName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBusinessPartnerName(String value) {
        this.businessPartnerName = value;
    }

    /**
     * Gets the value of the recieptNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecieptNumber() {
        return recieptNumber;
    }

    /**
     * Sets the value of the recieptNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecieptNumber(String value) {
        this.recieptNumber = value;
    }

    /**
     * Gets the value of the recieptDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getRecieptDate() {
        return recieptDate;
    }

    /**
     * Sets the value of the recieptDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setRecieptDate(XMLGregorianCalendar value) {
        this.recieptDate = value;
    }

    /**
     * Gets the value of the recieptAmount property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getRecieptAmount() {
        return recieptAmount;
    }

    /**
     * Sets the value of the recieptAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setRecieptAmount(MoneyType value) {
        this.recieptAmount = value;
    }

    /**
     * Gets the value of the channel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChannel() {
        return channel;
    }

    /**
     * Sets the value of the channel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChannel(String value) {
        this.channel = value;
    }

    /**
     * Gets the value of the instrumentNumber property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getInstrumentNumber() {
        return instrumentNumber;
    }

    /**
     * Sets the value of the instrumentNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setInstrumentNumber(Integer value) {
        this.instrumentNumber = value;
    }

    /**
     * Gets the value of the instrumentDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getInstrumentDate() {
        return instrumentDate;
    }

    /**
     * Sets the value of the instrumentDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setInstrumentDate(XMLGregorianCalendar value) {
        this.instrumentDate = value;
    }

    /**
     * Gets the value of the isMicrBased property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsMicrBased() {
        return isMicrBased;
    }

    /**
     * Sets the value of the isMicrBased property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsMicrBased(Boolean value) {
        this.isMicrBased = value;
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
     * Gets the value of the bank property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBank() {
        return bank;
    }

    /**
     * Sets the value of the bank property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBank(String value) {
        this.bank = value;
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
     * Gets the value of the subPaymentModeType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubPaymentModeType() {
        return subPaymentModeType;
    }

    /**
     * Sets the value of the subPaymentModeType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubPaymentModeType(String value) {
        this.subPaymentModeType = value;
    }

    /**
     * Gets the value of the recieptId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getRecieptId() {
        return recieptId;
    }

    /**
     * Sets the value of the recieptId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setRecieptId(Long value) {
        this.recieptId = value;
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

    /**
     * Gets the value of the createReceiptFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCreateReceiptFlag() {
        return createReceiptFlag;
    }

    /**
     * Sets the value of the createReceiptFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCreateReceiptFlag(Boolean value) {
        this.createReceiptFlag = value;
    }

    /**
     * Gets the value of the instrumentId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstrumentId() {
        return instrumentId;
    }

    /**
     * Sets the value of the instrumentId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstrumentId(String value) {
        this.instrumentId = value;
    }

}
