
package vn.com.tpf.microservices.models.Finnone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for updateInstrumentPostApprovalDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="updateInstrumentPostApprovalDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="deleteFlag" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType" minOccurs="0"/>
 *         &lt;element name="splitId" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="instrumentType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="receivedFromUri" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="bpOrCustomerUri" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="noOfInstallments" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType"/>
 *         &lt;element name="effectiveDate" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType"/>
 *         &lt;element name="expiryDate" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType"/>
 *         &lt;element name="instrumentDetailECS" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}instrumentDetailECS" minOccurs="0"/>
 *         &lt;element name="instrumentDetailPDC" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}instrumentDetailPDC" minOccurs="0"/>
 *         &lt;element name="instrumentDetailAutoDebit" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}instrumentDetailAutoDebit" minOccurs="0"/>
 *         &lt;element name="instrumentDetailNACH" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}instrumentDetailNACH" minOccurs="0"/>
 *         &lt;element name="instrumentDetailCASH" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}instrumentDetailCASH" minOccurs="0"/>
 *         &lt;element name="instrumentDetailDAS" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}instrumentDetailDAS" minOccurs="0"/>
 *         &lt;element name="instrumentDetailCoupon" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}instrumentDetailCoupon" minOccurs="0"/>
 *         &lt;element name="instrumentDetailEscrow" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}instrumentDetailEscrow" minOccurs="0"/>
 *         &lt;element name="instrumentDetailOTC" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}instrumentDetailOTC" minOccurs="0"/>
 *         &lt;element name="instrumentDetailRPTONE" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}instrumentDetailRPTONE" minOccurs="0"/>
 *         &lt;element name="instrumentDetailRPTTWO" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}instrumentDetailRPTTWO" minOccurs="0"/>
 *         &lt;element name="instrumentDetailRPTTHREE" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}instrumentDetailRPTTHREE" minOccurs="0"/>
 *         &lt;element name="instrumentDetailRPTFOUR" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}instrumentDetailRPTFOUR" minOccurs="0"/>
 *         &lt;element name="instrumentDetailRPTFIVE" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}instrumentDetailRPTFIVE" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateInstrumentPostApprovalDetails", propOrder = {
    "deleteFlag",
    "splitId",
    "instrumentType",
    "receivedFromUri",
    "bpOrCustomerUri",
    "noOfInstallments",
    "effectiveDate",
    "expiryDate",
    "instrumentDetailECS",
    "instrumentDetailPDC",
    "instrumentDetailAutoDebit",
    "instrumentDetailNACH",
    "instrumentDetailCASH",
    "instrumentDetailDAS",
    "instrumentDetailCoupon",
    "instrumentDetailEscrow",
    "instrumentDetailOTC",
    "instrumentDetailRPTONE",
    "instrumentDetailRPTTWO",
    "instrumentDetailRPTTHREE",
    "instrumentDetailRPTFOUR",
    "instrumentDetailRPTFIVE"
})
public class UpdateInstrumentPostApprovalDetails {

    protected Boolean deleteFlag;
    protected String splitId;
    @XmlElement(required = true)
    protected String instrumentType;
    @XmlElement(required = true)
    protected String receivedFromUri;
    @XmlElement(required = true)
    protected String bpOrCustomerUri;
    protected int noOfInstallments;
    @XmlElement(required = true)
    protected XMLGregorianCalendar effectiveDate;
    @XmlElement(required = true)
    protected XMLGregorianCalendar expiryDate;
    protected InstrumentDetailECS instrumentDetailECS;
    protected InstrumentDetailPDC instrumentDetailPDC;
    protected InstrumentDetailAutoDebit instrumentDetailAutoDebit;
    protected InstrumentDetailNACH instrumentDetailNACH;
    protected InstrumentDetailCASH instrumentDetailCASH;
    protected InstrumentDetailDAS instrumentDetailDAS;
    protected InstrumentDetailCoupon instrumentDetailCoupon;
    protected InstrumentDetailEscrow instrumentDetailEscrow;
    protected InstrumentDetailOTC instrumentDetailOTC;
    protected InstrumentDetailRPTONE instrumentDetailRPTONE;
    protected InstrumentDetailRPTTWO instrumentDetailRPTTWO;
    protected InstrumentDetailRPTTHREE instrumentDetailRPTTHREE;
    protected InstrumentDetailRPTFOUR instrumentDetailRPTFOUR;
    protected InstrumentDetailRPTFIVE instrumentDetailRPTFIVE;

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
     * Gets the value of the splitId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSplitId() {
        return splitId;
    }

    /**
     * Sets the value of the splitId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSplitId(String value) {
        this.splitId = value;
    }

    /**
     * Gets the value of the instrumentType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstrumentType() {
        return instrumentType;
    }

    /**
     * Sets the value of the instrumentType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstrumentType(String value) {
        this.instrumentType = value;
    }

    /**
     * Gets the value of the receivedFromUri property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReceivedFromUri() {
        return receivedFromUri;
    }

    /**
     * Sets the value of the receivedFromUri property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReceivedFromUri(String value) {
        this.receivedFromUri = value;
    }

    /**
     * Gets the value of the bpOrCustomerUri property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBpOrCustomerUri() {
        return bpOrCustomerUri;
    }

    /**
     * Sets the value of the bpOrCustomerUri property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBpOrCustomerUri(String value) {
        this.bpOrCustomerUri = value;
    }

    /**
     * Gets the value of the noOfInstallments property.
     * 
     */
    public int getNoOfInstallments() {
        return noOfInstallments;
    }

    /**
     * Sets the value of the noOfInstallments property.
     * 
     */
    public void setNoOfInstallments(int value) {
        this.noOfInstallments = value;
    }

    /**
     * Gets the value of the effectiveDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getEffectiveDate() {
        return effectiveDate;
    }

    /**
     * Sets the value of the effectiveDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setEffectiveDate(XMLGregorianCalendar value) {
        this.effectiveDate = value;
    }

    /**
     * Gets the value of the expiryDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getExpiryDate() {
        return expiryDate;
    }

    /**
     * Sets the value of the expiryDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setExpiryDate(XMLGregorianCalendar value) {
        this.expiryDate = value;
    }

    /**
     * Gets the value of the instrumentDetailECS property.
     * 
     * @return
     *     possible object is
     *     {@link InstrumentDetailECS }
     *     
     */
    public InstrumentDetailECS getInstrumentDetailECS() {
        return instrumentDetailECS;
    }

    /**
     * Sets the value of the instrumentDetailECS property.
     * 
     * @param value
     *     allowed object is
     *     {@link InstrumentDetailECS }
     *     
     */
    public void setInstrumentDetailECS(InstrumentDetailECS value) {
        this.instrumentDetailECS = value;
    }

    /**
     * Gets the value of the instrumentDetailPDC property.
     * 
     * @return
     *     possible object is
     *     {@link InstrumentDetailPDC }
     *     
     */
    public InstrumentDetailPDC getInstrumentDetailPDC() {
        return instrumentDetailPDC;
    }

    /**
     * Sets the value of the instrumentDetailPDC property.
     * 
     * @param value
     *     allowed object is
     *     {@link InstrumentDetailPDC }
     *     
     */
    public void setInstrumentDetailPDC(InstrumentDetailPDC value) {
        this.instrumentDetailPDC = value;
    }

    /**
     * Gets the value of the instrumentDetailAutoDebit property.
     * 
     * @return
     *     possible object is
     *     {@link InstrumentDetailAutoDebit }
     *     
     */
    public InstrumentDetailAutoDebit getInstrumentDetailAutoDebit() {
        return instrumentDetailAutoDebit;
    }

    /**
     * Sets the value of the instrumentDetailAutoDebit property.
     * 
     * @param value
     *     allowed object is
     *     {@link InstrumentDetailAutoDebit }
     *     
     */
    public void setInstrumentDetailAutoDebit(InstrumentDetailAutoDebit value) {
        this.instrumentDetailAutoDebit = value;
    }

    /**
     * Gets the value of the instrumentDetailNACH property.
     * 
     * @return
     *     possible object is
     *     {@link InstrumentDetailNACH }
     *     
     */
    public InstrumentDetailNACH getInstrumentDetailNACH() {
        return instrumentDetailNACH;
    }

    /**
     * Sets the value of the instrumentDetailNACH property.
     * 
     * @param value
     *     allowed object is
     *     {@link InstrumentDetailNACH }
     *     
     */
    public void setInstrumentDetailNACH(InstrumentDetailNACH value) {
        this.instrumentDetailNACH = value;
    }

    /**
     * Gets the value of the instrumentDetailCASH property.
     * 
     * @return
     *     possible object is
     *     {@link InstrumentDetailCASH }
     *     
     */
    public InstrumentDetailCASH getInstrumentDetailCASH() {
        return instrumentDetailCASH;
    }

    /**
     * Sets the value of the instrumentDetailCASH property.
     * 
     * @param value
     *     allowed object is
     *     {@link InstrumentDetailCASH }
     *     
     */
    public void setInstrumentDetailCASH(InstrumentDetailCASH value) {
        this.instrumentDetailCASH = value;
    }

    /**
     * Gets the value of the instrumentDetailDAS property.
     * 
     * @return
     *     possible object is
     *     {@link InstrumentDetailDAS }
     *     
     */
    public InstrumentDetailDAS getInstrumentDetailDAS() {
        return instrumentDetailDAS;
    }

    /**
     * Sets the value of the instrumentDetailDAS property.
     * 
     * @param value
     *     allowed object is
     *     {@link InstrumentDetailDAS }
     *     
     */
    public void setInstrumentDetailDAS(InstrumentDetailDAS value) {
        this.instrumentDetailDAS = value;
    }

    /**
     * Gets the value of the instrumentDetailCoupon property.
     * 
     * @return
     *     possible object is
     *     {@link InstrumentDetailCoupon }
     *     
     */
    public InstrumentDetailCoupon getInstrumentDetailCoupon() {
        return instrumentDetailCoupon;
    }

    /**
     * Sets the value of the instrumentDetailCoupon property.
     * 
     * @param value
     *     allowed object is
     *     {@link InstrumentDetailCoupon }
     *     
     */
    public void setInstrumentDetailCoupon(InstrumentDetailCoupon value) {
        this.instrumentDetailCoupon = value;
    }

    /**
     * Gets the value of the instrumentDetailEscrow property.
     * 
     * @return
     *     possible object is
     *     {@link InstrumentDetailEscrow }
     *     
     */
    public InstrumentDetailEscrow getInstrumentDetailEscrow() {
        return instrumentDetailEscrow;
    }

    /**
     * Sets the value of the instrumentDetailEscrow property.
     * 
     * @param value
     *     allowed object is
     *     {@link InstrumentDetailEscrow }
     *     
     */
    public void setInstrumentDetailEscrow(InstrumentDetailEscrow value) {
        this.instrumentDetailEscrow = value;
    }

    /**
     * Gets the value of the instrumentDetailOTC property.
     * 
     * @return
     *     possible object is
     *     {@link InstrumentDetailOTC }
     *     
     */
    public InstrumentDetailOTC getInstrumentDetailOTC() {
        return instrumentDetailOTC;
    }

    /**
     * Sets the value of the instrumentDetailOTC property.
     * 
     * @param value
     *     allowed object is
     *     {@link InstrumentDetailOTC }
     *     
     */
    public void setInstrumentDetailOTC(InstrumentDetailOTC value) {
        this.instrumentDetailOTC = value;
    }

    /**
     * Gets the value of the instrumentDetailRPTONE property.
     * 
     * @return
     *     possible object is
     *     {@link InstrumentDetailRPTONE }
     *     
     */
    public InstrumentDetailRPTONE getInstrumentDetailRPTONE() {
        return instrumentDetailRPTONE;
    }

    /**
     * Sets the value of the instrumentDetailRPTONE property.
     * 
     * @param value
     *     allowed object is
     *     {@link InstrumentDetailRPTONE }
     *     
     */
    public void setInstrumentDetailRPTONE(InstrumentDetailRPTONE value) {
        this.instrumentDetailRPTONE = value;
    }

    /**
     * Gets the value of the instrumentDetailRPTTWO property.
     * 
     * @return
     *     possible object is
     *     {@link InstrumentDetailRPTTWO }
     *     
     */
    public InstrumentDetailRPTTWO getInstrumentDetailRPTTWO() {
        return instrumentDetailRPTTWO;
    }

    /**
     * Sets the value of the instrumentDetailRPTTWO property.
     * 
     * @param value
     *     allowed object is
     *     {@link InstrumentDetailRPTTWO }
     *     
     */
    public void setInstrumentDetailRPTTWO(InstrumentDetailRPTTWO value) {
        this.instrumentDetailRPTTWO = value;
    }

    /**
     * Gets the value of the instrumentDetailRPTTHREE property.
     * 
     * @return
     *     possible object is
     *     {@link InstrumentDetailRPTTHREE }
     *     
     */
    public InstrumentDetailRPTTHREE getInstrumentDetailRPTTHREE() {
        return instrumentDetailRPTTHREE;
    }

    /**
     * Sets the value of the instrumentDetailRPTTHREE property.
     * 
     * @param value
     *     allowed object is
     *     {@link InstrumentDetailRPTTHREE }
     *     
     */
    public void setInstrumentDetailRPTTHREE(InstrumentDetailRPTTHREE value) {
        this.instrumentDetailRPTTHREE = value;
    }

    /**
     * Gets the value of the instrumentDetailRPTFOUR property.
     * 
     * @return
     *     possible object is
     *     {@link InstrumentDetailRPTFOUR }
     *     
     */
    public InstrumentDetailRPTFOUR getInstrumentDetailRPTFOUR() {
        return instrumentDetailRPTFOUR;
    }

    /**
     * Sets the value of the instrumentDetailRPTFOUR property.
     * 
     * @param value
     *     allowed object is
     *     {@link InstrumentDetailRPTFOUR }
     *     
     */
    public void setInstrumentDetailRPTFOUR(InstrumentDetailRPTFOUR value) {
        this.instrumentDetailRPTFOUR = value;
    }

    /**
     * Gets the value of the instrumentDetailRPTFIVE property.
     * 
     * @return
     *     possible object is
     *     {@link InstrumentDetailRPTFIVE }
     *     
     */
    public InstrumentDetailRPTFIVE getInstrumentDetailRPTFIVE() {
        return instrumentDetailRPTFIVE;
    }

    /**
     * Sets the value of the instrumentDetailRPTFIVE property.
     * 
     * @param value
     *     allowed object is
     *     {@link InstrumentDetailRPTFIVE }
     *     
     */
    public void setInstrumentDetailRPTFIVE(InstrumentDetailRPTFIVE value) {
        this.instrumentDetailRPTFIVE = value;
    }

}
