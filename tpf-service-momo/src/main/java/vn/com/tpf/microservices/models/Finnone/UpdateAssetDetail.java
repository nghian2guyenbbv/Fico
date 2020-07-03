
package vn.com.tpf.microservices.models.Finnone;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import vn.com.tpf.microservices.models.Finnone.cas.MoneyType;


/**
 * <p>Java class for updateAssetDetail complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="updateAssetDetail">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="financeMode" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="assetModelCategory" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="make" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="model" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="assetVarient" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="assetLevel" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="assetCost" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType"/>
 *         &lt;element name="downPaymentAmount" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType"/>
 *         &lt;element name="dealerDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}dealerDetails" minOccurs="0"/>
 *         &lt;element name="vehicleDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}vehicleDetails" minOccurs="0"/>
 *         &lt;element name="updateAccessoryDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}updateAccessoryDetails" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="updOtherComponentsFunded" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}updOtherComponentsFunded" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="invoiceDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}invoiceDetails" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="bodyDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}bodyDetails" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateAssetDetail", propOrder = {
    "financeMode",
    "assetModelCategory",
    "make",
    "model",
    "assetVarient",
    "assetLevel",
    "assetCost",
    "downPaymentAmount",
    "dealerDetails",
    "vehicleDetails",
    "updateAccessoryDetails",
    "updOtherComponentsFunded",
    "invoiceDetails",
    "bodyDetails"
})
public class UpdateAssetDetail {

    protected String financeMode;
    @XmlElement(required = true)
    protected String assetModelCategory;
    @XmlElement(required = true)
    protected String make;
    @XmlElement(required = true)
    protected String model;
    protected String assetVarient;
    protected String assetLevel;
    @XmlElement(required = true)
    protected MoneyType assetCost;
    @XmlElement(required = true)
    protected MoneyType downPaymentAmount;
    protected DealerDetails dealerDetails;
    protected VehicleDetails vehicleDetails;
    protected List<UpdateAccessoryDetails> updateAccessoryDetails;
    protected List<UpdOtherComponentsFunded> updOtherComponentsFunded;
    protected List<InvoiceDetails> invoiceDetails;
    protected BodyDetails bodyDetails;

    /**
     * Gets the value of the financeMode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFinanceMode() {
        return financeMode;
    }

    /**
     * Sets the value of the financeMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFinanceMode(String value) {
        this.financeMode = value;
    }

    /**
     * Gets the value of the assetModelCategory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssetModelCategory() {
        return assetModelCategory;
    }

    /**
     * Sets the value of the assetModelCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssetModelCategory(String value) {
        this.assetModelCategory = value;
    }

    /**
     * Gets the value of the make property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMake() {
        return make;
    }

    /**
     * Sets the value of the make property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMake(String value) {
        this.make = value;
    }

    /**
     * Gets the value of the model property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets the value of the model property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModel(String value) {
        this.model = value;
    }

    /**
     * Gets the value of the assetVarient property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssetVarient() {
        return assetVarient;
    }

    /**
     * Sets the value of the assetVarient property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssetVarient(String value) {
        this.assetVarient = value;
    }

    /**
     * Gets the value of the assetLevel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssetLevel() {
        return assetLevel;
    }

    /**
     * Sets the value of the assetLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssetLevel(String value) {
        this.assetLevel = value;
    }

    /**
     * Gets the value of the assetCost property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getAssetCost() {
        return assetCost;
    }

    /**
     * Sets the value of the assetCost property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setAssetCost(MoneyType value) {
        this.assetCost = value;
    }

    /**
     * Gets the value of the downPaymentAmount property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getDownPaymentAmount() {
        return downPaymentAmount;
    }

    /**
     * Sets the value of the downPaymentAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setDownPaymentAmount(MoneyType value) {
        this.downPaymentAmount = value;
    }

    /**
     * Gets the value of the dealerDetails property.
     * 
     * @return
     *     possible object is
     *     {@link DealerDetails }
     *     
     */
    public DealerDetails getDealerDetails() {
        return dealerDetails;
    }

    /**
     * Sets the value of the dealerDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link DealerDetails }
     *     
     */
    public void setDealerDetails(DealerDetails value) {
        this.dealerDetails = value;
    }

    /**
     * Gets the value of the vehicleDetails property.
     * 
     * @return
     *     possible object is
     *     {@link VehicleDetails }
     *     
     */
    public VehicleDetails getVehicleDetails() {
        return vehicleDetails;
    }

    /**
     * Sets the value of the vehicleDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link VehicleDetails }
     *     
     */
    public void setVehicleDetails(VehicleDetails value) {
        this.vehicleDetails = value;
    }

    /**
     * Gets the value of the updateAccessoryDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the updateAccessoryDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUpdateAccessoryDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UpdateAccessoryDetails }
     * 
     * 
     */
    public List<UpdateAccessoryDetails> getUpdateAccessoryDetails() {
        if (updateAccessoryDetails == null) {
            updateAccessoryDetails = new ArrayList<UpdateAccessoryDetails>();
        }
        return this.updateAccessoryDetails;
    }

    /**
     * Gets the value of the updOtherComponentsFunded property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the updOtherComponentsFunded property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUpdOtherComponentsFunded().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UpdOtherComponentsFunded }
     * 
     * 
     */
    public List<UpdOtherComponentsFunded> getUpdOtherComponentsFunded() {
        if (updOtherComponentsFunded == null) {
            updOtherComponentsFunded = new ArrayList<UpdOtherComponentsFunded>();
        }
        return this.updOtherComponentsFunded;
    }

    /**
     * Gets the value of the invoiceDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the invoiceDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInvoiceDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InvoiceDetails }
     * 
     * 
     */
    public List<InvoiceDetails> getInvoiceDetails() {
        if (invoiceDetails == null) {
            invoiceDetails = new ArrayList<InvoiceDetails>();
        }
        return this.invoiceDetails;
    }

    /**
     * Gets the value of the bodyDetails property.
     * 
     * @return
     *     possible object is
     *     {@link BodyDetails }
     *     
     */
    public BodyDetails getBodyDetails() {
        return bodyDetails;
    }

    /**
     * Sets the value of the bodyDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link BodyDetails }
     *     
     */
    public void setBodyDetails(BodyDetails value) {
        this.bodyDetails = value;
    }

}
