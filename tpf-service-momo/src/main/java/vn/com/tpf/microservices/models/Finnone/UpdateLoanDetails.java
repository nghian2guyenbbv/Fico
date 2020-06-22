
package vn.com.tpf.microservices.models.Finnone;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for updateLoanDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="updateLoanDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="sourcingDetail" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}updateSourcingDetail" minOccurs="0"/>
 *         &lt;element name="btDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}updateBtDetails" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="updateCollateralDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}updateCollateralDetails" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="loanParameterDetail" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}loanParameterDetail" minOccurs="0"/>
 *         &lt;element name="assetDetailList" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}multipleAsset" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="courseDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}updateCourseDetails" minOccurs="0"/>
 *         &lt;element name="recommendationVerdict" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}recommendationVerdict" minOccurs="0"/>
 *         &lt;element name="vapDetail" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}updateVapDetail" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="vapTrancheLinking" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}vapTrancheLinking" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="chargePolicyExecFlag" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType" minOccurs="0"/>
 *         &lt;element name="applicationCharges" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}updateApplicationCharges" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="personalDiscussionForm" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}personalDiscussionForm" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateLoanDetails", propOrder = {
    "sourcingDetail",
    "btDetails",
    "updateCollateralDetails",
    "loanParameterDetail",
    "assetDetailList",
    "courseDetails",
    "recommendationVerdict",
    "vapDetail",
    "vapTrancheLinking",
    "chargePolicyExecFlag",
    "applicationCharges",
    "personalDiscussionForm"
})
public class UpdateLoanDetails {

    protected UpdateSourcingDetail sourcingDetail;
    protected List<UpdateBtDetails> btDetails;
    protected List<UpdateCollateralDetails> updateCollateralDetails;
    protected LoanParameterDetail loanParameterDetail;
    protected List<MultipleAsset> assetDetailList;
    protected UpdateCourseDetails courseDetails;
    protected RecommendationVerdict recommendationVerdict;
    protected List<UpdateVapDetail> vapDetail;
    protected List<VapTrancheLinking> vapTrancheLinking;
    protected Boolean chargePolicyExecFlag;
    protected List<UpdateApplicationCharges> applicationCharges;
    protected List<PersonalDiscussionForm> personalDiscussionForm;

    /**
     * Gets the value of the sourcingDetail property.
     * 
     * @return
     *     possible object is
     *     {@link UpdateSourcingDetail }
     *     
     */
    public UpdateSourcingDetail getSourcingDetail() {
        return sourcingDetail;
    }

    /**
     * Sets the value of the sourcingDetail property.
     * 
     * @param value
     *     allowed object is
     *     {@link UpdateSourcingDetail }
     *     
     */
    public void setSourcingDetail(UpdateSourcingDetail value) {
        this.sourcingDetail = value;
    }

    /**
     * Gets the value of the btDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the btDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBtDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UpdateBtDetails }
     * 
     * 
     */
    public List<UpdateBtDetails> getBtDetails() {
        if (btDetails == null) {
            btDetails = new ArrayList<UpdateBtDetails>();
        }
        return this.btDetails;
    }

    /**
     * Gets the value of the updateCollateralDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the updateCollateralDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUpdateCollateralDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UpdateCollateralDetails }
     * 
     * 
     */
    public List<UpdateCollateralDetails> getUpdateCollateralDetails() {
        if (updateCollateralDetails == null) {
            updateCollateralDetails = new ArrayList<UpdateCollateralDetails>();
        }
        return this.updateCollateralDetails;
    }

    /**
     * Gets the value of the loanParameterDetail property.
     * 
     * @return
     *     possible object is
     *     {@link LoanParameterDetail }
     *     
     */
    public LoanParameterDetail getLoanParameterDetail() {
        return loanParameterDetail;
    }

    /**
     * Sets the value of the loanParameterDetail property.
     * 
     * @param value
     *     allowed object is
     *     {@link LoanParameterDetail }
     *     
     */
    public void setLoanParameterDetail(LoanParameterDetail value) {
        this.loanParameterDetail = value;
    }

    /**
     * Gets the value of the assetDetailList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the assetDetailList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAssetDetailList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MultipleAsset }
     * 
     * 
     */
    public List<MultipleAsset> getAssetDetailList() {
        if (assetDetailList == null) {
            assetDetailList = new ArrayList<MultipleAsset>();
        }
        return this.assetDetailList;
    }

    /**
     * Gets the value of the courseDetails property.
     * 
     * @return
     *     possible object is
     *     {@link UpdateCourseDetails }
     *     
     */
    public UpdateCourseDetails getCourseDetails() {
        return courseDetails;
    }

    /**
     * Sets the value of the courseDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link UpdateCourseDetails }
     *     
     */
    public void setCourseDetails(UpdateCourseDetails value) {
        this.courseDetails = value;
    }

    /**
     * Gets the value of the recommendationVerdict property.
     * 
     * @return
     *     possible object is
     *     {@link RecommendationVerdict }
     *     
     */
    public RecommendationVerdict getRecommendationVerdict() {
        return recommendationVerdict;
    }

    /**
     * Sets the value of the recommendationVerdict property.
     * 
     * @param value
     *     allowed object is
     *     {@link RecommendationVerdict }
     *     
     */
    public void setRecommendationVerdict(RecommendationVerdict value) {
        this.recommendationVerdict = value;
    }

    /**
     * Gets the value of the vapDetail property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the vapDetail property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVapDetail().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UpdateVapDetail }
     * 
     * 
     */
    public List<UpdateVapDetail> getVapDetail() {
        if (vapDetail == null) {
            vapDetail = new ArrayList<UpdateVapDetail>();
        }
        return this.vapDetail;
    }

    /**
     * Gets the value of the vapTrancheLinking property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the vapTrancheLinking property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVapTrancheLinking().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link VapTrancheLinking }
     * 
     * 
     */
    public List<VapTrancheLinking> getVapTrancheLinking() {
        if (vapTrancheLinking == null) {
            vapTrancheLinking = new ArrayList<VapTrancheLinking>();
        }
        return this.vapTrancheLinking;
    }

    /**
     * Gets the value of the chargePolicyExecFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isChargePolicyExecFlag() {
        return chargePolicyExecFlag;
    }

    /**
     * Sets the value of the chargePolicyExecFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setChargePolicyExecFlag(Boolean value) {
        this.chargePolicyExecFlag = value;
    }

    /**
     * Gets the value of the applicationCharges property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the applicationCharges property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getApplicationCharges().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UpdateApplicationCharges }
     * 
     * 
     */
    public List<UpdateApplicationCharges> getApplicationCharges() {
        if (applicationCharges == null) {
            applicationCharges = new ArrayList<UpdateApplicationCharges>();
        }
        return this.applicationCharges;
    }

    /**
     * Gets the value of the personalDiscussionForm property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the personalDiscussionForm property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPersonalDiscussionForm().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PersonalDiscussionForm }
     * 
     * 
     */
    public List<PersonalDiscussionForm> getPersonalDiscussionForm() {
        if (personalDiscussionForm == null) {
            personalDiscussionForm = new ArrayList<PersonalDiscussionForm>();
        }
        return this.personalDiscussionForm;
    }

}
