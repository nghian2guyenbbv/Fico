
package vn.com.tpf.microservices.models.Finnone;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for loanDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="loanDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="sourcingDetail" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}sourcingDetail"/>
 *         &lt;element name="collateralDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}collateralDetails" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="loanParameterDetail" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}loanParameterDetail"/>
 *         &lt;element name="assetDetailList" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}multiAsset" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="vapDetail" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}vapDetail" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="courseDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}courseDetails" minOccurs="0"/>
 *         &lt;element name="chargePolicyExecFlag" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType" minOccurs="0"/>
 *         &lt;element name="applicationCharges" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}applicationCharges" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "loanDetails", propOrder = {
    "sourcingDetail",
    "collateralDetails",
    "loanParameterDetail",
    "assetDetailList",
    "vapDetail",
    "courseDetails",
    "chargePolicyExecFlag",
    "applicationCharges",
    "personalDiscussionForm"
})
public class LoanDetails {

    @XmlElement(required = true)
    protected SourcingDetail sourcingDetail;
    protected List<CollateralDetails> collateralDetails;
    @XmlElement(required = true)
    protected LoanParameterDetail loanParameterDetail;
    protected List<MultiAsset> assetDetailList;
    protected List<VapDetail> vapDetail;
    protected CourseDetails courseDetails;
    protected Boolean chargePolicyExecFlag;
    protected List<ApplicationCharges> applicationCharges;
    protected List<PersonalDiscussionForm> personalDiscussionForm;

    /**
     * Gets the value of the sourcingDetail property.
     * 
     * @return
     *     possible object is
     *     {@link SourcingDetail }
     *     
     */
    public SourcingDetail getSourcingDetail() {
        return sourcingDetail;
    }

    /**
     * Sets the value of the sourcingDetail property.
     * 
     * @param value
     *     allowed object is
     *     {@link SourcingDetail }
     *     
     */
    public void setSourcingDetail(SourcingDetail value) {
        this.sourcingDetail = value;
    }

    /**
     * Gets the value of the collateralDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the collateralDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCollateralDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CollateralDetails }
     * 
     * 
     */
    public List<CollateralDetails> getCollateralDetails() {
        if (collateralDetails == null) {
            collateralDetails = new ArrayList<CollateralDetails>();
        }
        return this.collateralDetails;
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
     * {@link MultiAsset }
     * 
     * 
     */
    public List<MultiAsset> getAssetDetailList() {
        if (assetDetailList == null) {
            assetDetailList = new ArrayList<MultiAsset>();
        }
        return this.assetDetailList;
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
     * {@link VapDetail }
     * 
     * 
     */
    public List<VapDetail> getVapDetail() {
        if (vapDetail == null) {
            vapDetail = new ArrayList<VapDetail>();
        }
        return this.vapDetail;
    }

    /**
     * Gets the value of the courseDetails property.
     * 
     * @return
     *     possible object is
     *     {@link CourseDetails }
     *     
     */
    public CourseDetails getCourseDetails() {
        return courseDetails;
    }

    /**
     * Sets the value of the courseDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link CourseDetails }
     *     
     */
    public void setCourseDetails(CourseDetails value) {
        this.courseDetails = value;
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
     * {@link ApplicationCharges }
     * 
     * 
     */
    public List<ApplicationCharges> getApplicationCharges() {
        if (applicationCharges == null) {
            applicationCharges = new ArrayList<ApplicationCharges>();
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

    public void setVapDetail(List<VapDetail> vapDetail) {
        this.vapDetail = vapDetail;
    }
}
