
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
 *         &lt;element name="productProcessor" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="branchCode" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="userName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="productTypeCode" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="ecmCaseId" type="{http://schema.base.ws.pro.finnone.nucleus.com}LongType" minOccurs="0"/>
 *         &lt;element name="ecmCaseNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="applicantInformation" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}applicantInformation" maxOccurs="4"/>
 *         &lt;element name="loanDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}loanDetails" minOccurs="0"/>
 *         &lt;element name="cardLoanDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}cardLoanDetails" minOccurs="0"/>
 *         &lt;element name="moveToNextStageFlag" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType"/>
 *         &lt;element name="referenceDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}referenceDetails" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="recieptDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}recieptDetails" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="documents" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}documents" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="dynamicFormDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}dynamicFormDetails" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="additionalFields" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}additionalFields" minOccurs="0"/>
 *         &lt;element name="comments" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}comments" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="uniqueIdentifier" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="uploadDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}uploadDetails" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="geoTagging" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}geoTagging" minOccurs="0"/>
 *         &lt;element name="executeChecklist" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType"/>
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
    "productProcessor",
    "branchCode",
    "userName",
    "productTypeCode",
    "ecmCaseId",
    "ecmCaseNumber",
    "applicantInformation",
    "loanDetails",
    "cardLoanDetails",
    "moveToNextStageFlag",
    "referenceDetails",
    "recieptDetails",
    "documents",
    "dynamicFormDetails",
    "additionalFields",
    "comments",
    "uniqueIdentifier",
    "uploadDetails",
    "geoTagging",
    "executeChecklist"
})
@XmlRootElement(name = "createApplicationRequest")
public class CreateApplicationRequest {

    @XmlElement(required = true)
    protected String productProcessor;

    public void setApplicantInformation(List<ApplicantInformation> applicantInformation) {
        this.applicantInformation = applicantInformation;
    }

    public void setReferenceDetails(List<ReferenceDetails> referenceDetails) {
        this.referenceDetails = referenceDetails;
    }

    public void setRecieptDetails(List<RecieptDetails> recieptDetails) {
        this.recieptDetails = recieptDetails;
    }

    public void setDocuments(List<Documents> documents) {
        this.documents = documents;
    }

    public void setDynamicFormDetails(List<DynamicFormDetails> dynamicFormDetails) {
        this.dynamicFormDetails = dynamicFormDetails;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }

    public void setUploadDetails(List<UploadDetails> uploadDetails) {
        this.uploadDetails = uploadDetails;
    }

    @XmlElement(required = true)
    protected String branchCode;
    @XmlElement(required = true)
    protected String userName;
    @XmlElement(required = true)
    protected String productTypeCode;
    protected Long ecmCaseId;
    protected String ecmCaseNumber;
    @XmlElement(required = true)
    protected List<ApplicantInformation> applicantInformation;
    protected LoanDetails loanDetails;
    protected CardLoanDetails cardLoanDetails;
    protected boolean moveToNextStageFlag;
    protected List<ReferenceDetails> referenceDetails;
    protected List<RecieptDetails> recieptDetails;
    protected List<Documents> documents;
    protected List<DynamicFormDetails> dynamicFormDetails;
    protected AdditionalFields additionalFields;
    protected List<Comments> comments;
    protected String uniqueIdentifier;
    protected List<UploadDetails> uploadDetails;
    protected GeoTagging geoTagging;
    protected boolean executeChecklist;

    /**
     * Gets the value of the productProcessor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductProcessor() {
        return productProcessor;
    }

    /**
     * Sets the value of the productProcessor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductProcessor(String value) {
        this.productProcessor = value;
    }

    /**
     * Gets the value of the branchCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBranchCode() {
        return branchCode;
    }

    /**
     * Sets the value of the branchCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBranchCode(String value) {
        this.branchCode = value;
    }

    /**
     * Gets the value of the userName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the value of the userName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserName(String value) {
        this.userName = value;
    }

    /**
     * Gets the value of the productTypeCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductTypeCode() {
        return productTypeCode;
    }

    /**
     * Sets the value of the productTypeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductTypeCode(String value) {
        this.productTypeCode = value;
    }

    /**
     * Gets the value of the ecmCaseId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getEcmCaseId() {
        return ecmCaseId;
    }

    /**
     * Sets the value of the ecmCaseId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setEcmCaseId(Long value) {
        this.ecmCaseId = value;
    }

    /**
     * Gets the value of the ecmCaseNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEcmCaseNumber() {
        return ecmCaseNumber;
    }

    /**
     * Sets the value of the ecmCaseNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEcmCaseNumber(String value) {
        this.ecmCaseNumber = value;
    }

    /**
     * Gets the value of the applicantInformation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the applicantInformation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getApplicantInformation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ApplicantInformation }
     * 
     * 
     */
    public List<ApplicantInformation> getApplicantInformation() {
        if (applicantInformation == null) {
            applicantInformation = new ArrayList<ApplicantInformation>();
        }
        return this.applicantInformation;
    }

    /**
     * Gets the value of the loanDetails property.
     * 
     * @return
     *     possible object is
     *     {@link LoanDetails }
     *     
     */
    public LoanDetails getLoanDetails() {
        return loanDetails;
    }

    /**
     * Sets the value of the loanDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link LoanDetails }
     *     
     */
    public void setLoanDetails(LoanDetails value) {
        this.loanDetails = value;
    }

    /**
     * Gets the value of the cardLoanDetails property.
     * 
     * @return
     *     possible object is
     *     {@link CardLoanDetails }
     *     
     */
    public CardLoanDetails getCardLoanDetails() {
        return cardLoanDetails;
    }

    /**
     * Sets the value of the cardLoanDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link CardLoanDetails }
     *     
     */
    public void setCardLoanDetails(CardLoanDetails value) {
        this.cardLoanDetails = value;
    }

    /**
     * Gets the value of the moveToNextStageFlag property.
     * 
     */
    public boolean isMoveToNextStageFlag() {
        return moveToNextStageFlag;
    }

    /**
     * Sets the value of the moveToNextStageFlag property.
     * 
     */
    public void setMoveToNextStageFlag(boolean value) {
        this.moveToNextStageFlag = value;
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
     * {@link RecieptDetails }
     * 
     * 
     */
    public List<RecieptDetails> getRecieptDetails() {
        if (recieptDetails == null) {
            recieptDetails = new ArrayList<RecieptDetails>();
        }
        return this.recieptDetails;
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
     * {@link Documents }
     * 
     * 
     */
    public List<Documents> getDocuments() {
        if (documents == null) {
            documents = new ArrayList<Documents>();
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
     * Gets the value of the comments property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the comments property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getComments().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Comments }
     * 
     * 
     */
    public List<Comments> getComments() {
        if (comments == null) {
            comments = new ArrayList<Comments>();
        }
        return this.comments;
    }

    /**
     * Gets the value of the uniqueIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    /**
     * Sets the value of the uniqueIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUniqueIdentifier(String value) {
        this.uniqueIdentifier = value;
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

    /**
     * Gets the value of the executeChecklist property.
     * 
     */
    public boolean isExecuteChecklist() {
        return executeChecklist;
    }

    /**
     * Sets the value of the executeChecklist property.
     * 
     */
    public void setExecuteChecklist(boolean value) {
        this.executeChecklist = value;
    }

}
