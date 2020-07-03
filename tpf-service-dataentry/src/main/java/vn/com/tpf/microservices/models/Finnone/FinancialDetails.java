
package vn.com.tpf.microservices.models.Finnone;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for financialDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="financialDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dynamicFinancialUpload" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}dynamicFinancialUpload" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="financialUploadFile" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}financialUploadFile" minOccurs="0"/>
 *         &lt;element name="incomeDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}incomeDetails" maxOccurs="20" minOccurs="0"/>
 *         &lt;element name="otherIncomeDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}otherIncomeDetails" maxOccurs="20" minOccurs="0"/>
 *         &lt;element name="expenseDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}expenseDetails" maxOccurs="20" minOccurs="0"/>
 *         &lt;element name="incomeAssetDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}incomeAssetDetails" maxOccurs="20" minOccurs="0"/>
 *         &lt;element name="liabilityDetail" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}liabilityDetail" maxOccurs="20" minOccurs="0"/>
 *         &lt;element name="yearlyIncomeDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}yearlyIncomeDetails" minOccurs="0"/>
 *         &lt;element name="expenditureDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}yearlyIncomeDetails" minOccurs="0"/>
 *         &lt;element name="incomeSummary" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}incomeSummary" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "financialDetails", propOrder = {
    "dynamicFinancialUpload",
    "financialUploadFile",
    "incomeDetails",
    "otherIncomeDetails",
    "expenseDetails",
    "incomeAssetDetails",
    "liabilityDetail",
    "yearlyIncomeDetails",
    "expenditureDetails",
    "incomeSummary"
})
public class FinancialDetails {

    protected List<DynamicFinancialUpload> dynamicFinancialUpload;
    protected FinancialUploadFile financialUploadFile;

    public void setIncomeDetails(List<IncomeDetails> incomeDetails) {
        this.incomeDetails = incomeDetails;
    }

    protected List<IncomeDetails> incomeDetails;
    protected List<OtherIncomeDetails> otherIncomeDetails;
    protected List<ExpenseDetails> expenseDetails;
    protected List<IncomeAssetDetails> incomeAssetDetails;
    protected List<LiabilityDetail> liabilityDetail;
    protected YearlyIncomeDetails yearlyIncomeDetails;
    protected YearlyIncomeDetails expenditureDetails;
    protected IncomeSummary incomeSummary;

    /**
     * Gets the value of the dynamicFinancialUpload property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dynamicFinancialUpload property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDynamicFinancialUpload().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DynamicFinancialUpload }
     * 
     * 
     */
    public List<DynamicFinancialUpload> getDynamicFinancialUpload() {
        if (dynamicFinancialUpload == null) {
            dynamicFinancialUpload = new ArrayList<DynamicFinancialUpload>();
        }
        return this.dynamicFinancialUpload;
    }

    /**
     * Gets the value of the financialUploadFile property.
     * 
     * @return
     *     possible object is
     *     {@link FinancialUploadFile }
     *     
     */
    public FinancialUploadFile getFinancialUploadFile() {
        return financialUploadFile;
    }

    /**
     * Sets the value of the financialUploadFile property.
     * 
     * @param value
     *     allowed object is
     *     {@link FinancialUploadFile }
     *     
     */
    public void setFinancialUploadFile(FinancialUploadFile value) {
        this.financialUploadFile = value;
    }

    /**
     * Gets the value of the incomeDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the incomeDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIncomeDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IncomeDetails }
     * 
     * 
     */
    public List<IncomeDetails> getIncomeDetails() {
        if (incomeDetails == null) {
            incomeDetails = new ArrayList<IncomeDetails>();
        }
        return this.incomeDetails;
    }

    /**
     * Gets the value of the otherIncomeDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the otherIncomeDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOtherIncomeDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OtherIncomeDetails }
     * 
     * 
     */
    public List<OtherIncomeDetails> getOtherIncomeDetails() {
        if (otherIncomeDetails == null) {
            otherIncomeDetails = new ArrayList<OtherIncomeDetails>();
        }
        return this.otherIncomeDetails;
    }

    /**
     * Gets the value of the expenseDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the expenseDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExpenseDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ExpenseDetails }
     * 
     * 
     */
    public List<ExpenseDetails> getExpenseDetails() {
        if (expenseDetails == null) {
            expenseDetails = new ArrayList<ExpenseDetails>();
        }
        return this.expenseDetails;
    }

    /**
     * Gets the value of the incomeAssetDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the incomeAssetDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIncomeAssetDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IncomeAssetDetails }
     * 
     * 
     */
    public List<IncomeAssetDetails> getIncomeAssetDetails() {
        if (incomeAssetDetails == null) {
            incomeAssetDetails = new ArrayList<IncomeAssetDetails>();
        }
        return this.incomeAssetDetails;
    }

    /**
     * Gets the value of the liabilityDetail property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the liabilityDetail property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLiabilityDetail().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LiabilityDetail }
     * 
     * 
     */
    public List<LiabilityDetail> getLiabilityDetail() {
        if (liabilityDetail == null) {
            liabilityDetail = new ArrayList<LiabilityDetail>();
        }
        return this.liabilityDetail;
    }

    /**
     * Gets the value of the yearlyIncomeDetails property.
     * 
     * @return
     *     possible object is
     *     {@link YearlyIncomeDetails }
     *     
     */
    public YearlyIncomeDetails getYearlyIncomeDetails() {
        return yearlyIncomeDetails;
    }

    /**
     * Sets the value of the yearlyIncomeDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link YearlyIncomeDetails }
     *     
     */
    public void setYearlyIncomeDetails(YearlyIncomeDetails value) {
        this.yearlyIncomeDetails = value;
    }

    /**
     * Gets the value of the expenditureDetails property.
     * 
     * @return
     *     possible object is
     *     {@link YearlyIncomeDetails }
     *     
     */
    public YearlyIncomeDetails getExpenditureDetails() {
        return expenditureDetails;
    }

    /**
     * Sets the value of the expenditureDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link YearlyIncomeDetails }
     *     
     */
    public void setExpenditureDetails(YearlyIncomeDetails value) {
        this.expenditureDetails = value;
    }

    /**
     * Gets the value of the incomeSummary property.
     * 
     * @return
     *     possible object is
     *     {@link IncomeSummary }
     *     
     */
    public IncomeSummary getIncomeSummary() {
        return incomeSummary;
    }

    /**
     * Sets the value of the incomeSummary property.
     * 
     * @param value
     *     allowed object is
     *     {@link IncomeSummary }
     *     
     */
    public void setIncomeSummary(IncomeSummary value) {
        this.incomeSummary = value;
    }

}
