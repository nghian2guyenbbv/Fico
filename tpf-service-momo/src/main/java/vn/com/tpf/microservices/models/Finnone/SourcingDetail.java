
package vn.com.tpf.microservices.models.Finnone;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for sourcingDetail complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="sourcingDetail">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="loanInfo" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}loanInfo"/>
 *         &lt;element name="loanApplication" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}loanApplication"/>
 *         &lt;element name="applicationDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}applicationDetails"/>
 *         &lt;element name="bTDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}bTDetails" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="dynamicFormDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}dynamicFormDetails" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sourcingDetail", propOrder = {
    "loanInfo",
    "loanApplication",
    "applicationDetails",
    "btDetails",
    "dynamicFormDetails"
})
public class SourcingDetail {

    @XmlElement(required = true)
    protected LoanInfo loanInfo;
    @XmlElement(required = true)
    protected LoanApplication loanApplication;
    @XmlElement(required = true)
    protected ApplicationDetails applicationDetails;
    @XmlElement(name = "bTDetails")
    protected List<BTDetails> btDetails;
    protected List<DynamicFormDetails> dynamicFormDetails;

    /**
     * Gets the value of the loanInfo property.
     * 
     * @return
     *     possible object is
     *     {@link LoanInfo }
     *     
     */
    public LoanInfo getLoanInfo() {
        return loanInfo;
    }

    /**
     * Sets the value of the loanInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link LoanInfo }
     *     
     */
    public void setLoanInfo(LoanInfo value) {
        this.loanInfo = value;
    }

    /**
     * Gets the value of the loanApplication property.
     * 
     * @return
     *     possible object is
     *     {@link LoanApplication }
     *     
     */
    public LoanApplication getLoanApplication() {
        return loanApplication;
    }

    /**
     * Sets the value of the loanApplication property.
     * 
     * @param value
     *     allowed object is
     *     {@link LoanApplication }
     *     
     */
    public void setLoanApplication(LoanApplication value) {
        this.loanApplication = value;
    }

    /**
     * Gets the value of the applicationDetails property.
     * 
     * @return
     *     possible object is
     *     {@link ApplicationDetails }
     *     
     */
    public ApplicationDetails getApplicationDetails() {
        return applicationDetails;
    }

    /**
     * Sets the value of the applicationDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link ApplicationDetails }
     *     
     */
    public void setApplicationDetails(ApplicationDetails value) {
        this.applicationDetails = value;
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
     *    getBTDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BTDetails }
     * 
     * 
     */
    public List<BTDetails> getBTDetails() {
        if (btDetails == null) {
            btDetails = new ArrayList<BTDetails>();
        }
        return this.btDetails;
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

}
