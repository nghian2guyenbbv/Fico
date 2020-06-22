
package vn.com.tpf.microservices.models.Finnone;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="appParameter" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}appParameter"/>
 *         &lt;element name="customerDet" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}customerDet" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="updateLoanDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}updateLoanDetails" minOccurs="0"/>
 *         &lt;element name="cardLoanDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}updateCardLoanDetails" minOccurs="0"/>
 *         &lt;element name="referenceDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}referenceDetails" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="recieptDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}updateRecieptDetails" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="instrumentPostApprovalDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}updateInstrumentPostApprovalDetails" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="executeChecklist" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType" minOccurs="0"/>
 *         &lt;element name="documents" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}updateDocuments" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="dynamicFormDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}updateDynamicFormDetails" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="uploadDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}uploadDetails" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="additionalFields" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}additionalFields" minOccurs="0"/>
 *         &lt;element name="geoTagging" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}geoTagging" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "appParameter",
    "customerDet",
    "updateLoanDetails",
    "cardLoanDetails",
    "referenceDetails",
    "recieptDetails",
    "instrumentPostApprovalDetails",
    "executeChecklist",
    "documents",
    "dynamicFormDetails",
    "uploadDetails",
    "additionalFields",
    "geoTagging"
})
@XmlRootElement(name = "updateApplicationRequest")
public class UpdateApplicationRequest {

    @XmlElement(required = true)
    protected AppParameter appParameter;
    protected List<CustomerDet> customerDet;
    protected UpdateLoanDetails updateLoanDetails;
    protected UpdateCardLoanDetails cardLoanDetails;
    protected List<ReferenceDetails> referenceDetails;
    protected List<UpdateRecieptDetails> recieptDetails;
    protected List<UpdateInstrumentPostApprovalDetails> instrumentPostApprovalDetails;
    protected Boolean executeChecklist;
    protected List<UpdateDocuments> documents;
    protected List<UpdateDynamicFormDetails> dynamicFormDetails;
    protected List<UploadDetails> uploadDetails;
    protected AdditionalFields additionalFields;
    protected GeoTagging geoTagging;

    /**
     * Gets the value of the appParameter property.
     * 
     * @return
     *     possible object is
     *     {@link AppParameter }
     *     
     */
    public AppParameter getAppParameter() {
        return appParameter;
    }

    /**
     * Sets the value of the appParameter property.
     * 
     * @param value
     *     allowed object is
     *     {@link AppParameter }
     *     
     */
    public void setAppParameter(AppParameter value) {
        this.appParameter = value;
    }

    /**
     * Gets the value of the customerDet property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the customerDet property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCustomerDet().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CustomerDet }
     * 
     * 
     */
    public List<CustomerDet> getCustomerDet() {
        if (customerDet == null) {
            customerDet = new ArrayList<CustomerDet>();
        }
        return this.customerDet;
    }

    /**
     * Gets the value of the updateLoanDetails property.
     * 
     * @return
     *     possible object is
     *     {@link UpdateLoanDetails }
     *     
     */
    public UpdateLoanDetails getUpdateLoanDetails() {
        return updateLoanDetails;
    }

    /**
     * Sets the value of the updateLoanDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link UpdateLoanDetails }
     *     
     */
    public void setUpdateLoanDetails(UpdateLoanDetails value) {
        this.updateLoanDetails = value;
    }

    /**
     * Gets the value of the cardLoanDetails property.
     * 
     * @return
     *     possible object is
     *     {@link UpdateCardLoanDetails }
     *     
     */
    public UpdateCardLoanDetails getCardLoanDetails() {
        return cardLoanDetails;
    }

    /**
     * Sets the value of the cardLoanDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link UpdateCardLoanDetails }
     *     
     */
    public void setCardLoanDetails(UpdateCardLoanDetails value) {
        this.cardLoanDetails = value;
    }

    /**
     * Gets the value of the referenceDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the referenceDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReferenceDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ReferenceDetails }
     * 
     * 
     */
    public List<ReferenceDetails> getReferenceDetails() {
        if (referenceDetails == null) {
            referenceDetails = new ArrayList<ReferenceDetails>();
        }
        return this.referenceDetails;
    }

    /**
     * Gets the value of the recieptDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the recieptDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRecieptDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UpdateRecieptDetails }
     * 
     * 
     */
    public List<UpdateRecieptDetails> getRecieptDetails() {
        if (recieptDetails == null) {
            recieptDetails = new ArrayList<UpdateRecieptDetails>();
        }
        return this.recieptDetails;
    }

    /**
     * Gets the value of the instrumentPostApprovalDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the instrumentPostApprovalDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInstrumentPostApprovalDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UpdateInstrumentPostApprovalDetails }
     * 
     * 
     */
    public List<UpdateInstrumentPostApprovalDetails> getInstrumentPostApprovalDetails() {
        if (instrumentPostApprovalDetails == null) {
            instrumentPostApprovalDetails = new ArrayList<UpdateInstrumentPostApprovalDetails>();
        }
        return this.instrumentPostApprovalDetails;
    }

    /**
     * Gets the value of the executeChecklist property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isExecuteChecklist() {
        return executeChecklist;
    }

    /**
     * Sets the value of the executeChecklist property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setExecuteChecklist(Boolean value) {
        this.executeChecklist = value;
    }

    /**
     * Gets the value of the documents property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the documents property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDocuments().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UpdateDocuments }
     * 
     * 
     */
    public List<UpdateDocuments> getDocuments() {
        if (documents == null) {
            documents = new ArrayList<UpdateDocuments>();
        }
        return this.documents;
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
     * {@link UpdateDynamicFormDetails }
     * 
     * 
     */
    public List<UpdateDynamicFormDetails> getDynamicFormDetails() {
        if (dynamicFormDetails == null) {
            dynamicFormDetails = new ArrayList<UpdateDynamicFormDetails>();
        }
        return this.dynamicFormDetails;
    }

    /**
     * Gets the value of the uploadDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the uploadDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUploadDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UploadDetails }
     * 
     * 
     */
    public List<UploadDetails> getUploadDetails() {
        if (uploadDetails == null) {
            uploadDetails = new ArrayList<UploadDetails>();
        }
        return this.uploadDetails;
    }

    /**
     * Gets the value of the additionalFields property.
     * 
     * @return
     *     possible object is
     *     {@link AdditionalFields }
     *     
     */
    public AdditionalFields getAdditionalFields() {
        return additionalFields;
    }

    /**
     * Sets the value of the additionalFields property.
     * 
     * @param value
     *     allowed object is
     *     {@link AdditionalFields }
     *     
     */
    public void setAdditionalFields(AdditionalFields value) {
        this.additionalFields = value;
    }

    /**
     * Gets the value of the geoTagging property.
     * 
     * @return
     *     possible object is
     *     {@link GeoTagging }
     *     
     */
    public GeoTagging getGeoTagging() {
        return geoTagging;
    }

    /**
     * Sets the value of the geoTagging property.
     * 
     * @param value
     *     allowed object is
     *     {@link GeoTagging }
     *     
     */
    public void setGeoTagging(GeoTagging value) {
        this.geoTagging = value;
    }

}
