
package vn.com.tpf.microservices.models.leadCreation;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for workAndIncomeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="workAndIncomeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="occupationType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="employerName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="workingHereForPast_months" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="workingHereForPast_years" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="totalWorkExpereince_months" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="totalWorkExpereince_years" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="natureOfProfession" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="yearsInProfession_months" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="yearsInProfession_years" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="natureOfOccuptaion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="grossMonthlySalary" type="{http://www.nucleus.com/schemas/integration/leadCreationService}amountField"/>
 *         &lt;element name="monthlyTakeHomeSalary" type="{http://www.nucleus.com/schemas/integration/leadCreationService}amountField"/>
 *         &lt;element name="totalInstallmentCurrentlyPay" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="organizationName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="natureOfBusiness" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="yearInBusiness_months" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="yearInBusiness_years" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="netProfit" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="startDateOfCurrentProfession" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="latestYearGrossTotalIncome" type="{http://www.nucleus.com/schemas/integration/leadCreationService}amountField"/>
 *         &lt;element name="others" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="startDateOfBusiness" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FieldworkAndIncome1" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FieldworkAndIncome2" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FieldworkAndIncome3" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FieldworkAndIncome4" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FieldworkAndIncome5" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FieldworkAndIncome6" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FieldworkAndIncome7" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FieldworkAndIncome8" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FieldworkAndIncome9" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FieldworkAndIncome10" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "workAndIncomeType", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", propOrder = {
    "occupationType",
    "employerName",
    "workingHereForPastMonths",
    "workingHereForPastYears",
    "totalWorkExpereinceMonths",
    "totalWorkExpereinceYears",
    "natureOfProfession",
    "yearsInProfessionMonths",
    "yearsInProfessionYears",
    "natureOfOccuptaion",
    "grossMonthlySalary",
    "monthlyTakeHomeSalary",
    "totalInstallmentCurrentlyPay",
    "organizationName",
    "natureOfBusiness",
    "yearInBusinessMonths",
    "yearInBusinessYears",
    "netProfit",
    "startDateOfCurrentProfession",
    "latestYearGrossTotalIncome",
    "others",
    "startDateOfBusiness",
    "fieldworkAndIncome1",
    "fieldworkAndIncome2",
    "fieldworkAndIncome3",
    "fieldworkAndIncome4",
    "fieldworkAndIncome5",
    "fieldworkAndIncome6",
    "fieldworkAndIncome7",
    "fieldworkAndIncome8",
    "fieldworkAndIncome9",
    "fieldworkAndIncome10"
})
public class WorkAndIncomeType {

    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String occupationType;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String employerName;
    @XmlElement(name = "workingHereForPast_months", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String workingHereForPastMonths;
    @XmlElement(name = "workingHereForPast_years", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String workingHereForPastYears;
    @XmlElement(name = "totalWorkExpereince_months", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String totalWorkExpereinceMonths;
    @XmlElement(name = "totalWorkExpereince_years", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String totalWorkExpereinceYears;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String natureOfProfession;
    @XmlElement(name = "yearsInProfession_months", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String yearsInProfessionMonths;
    @XmlElement(name = "yearsInProfession_years", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String yearsInProfessionYears;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String natureOfOccuptaion;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected AmountField grossMonthlySalary;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected AmountField monthlyTakeHomeSalary;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String totalInstallmentCurrentlyPay;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String organizationName;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String natureOfBusiness;
    @XmlElement(name = "yearInBusiness_months", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String yearInBusinessMonths;
    @XmlElement(name = "yearInBusiness_years", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String yearInBusinessYears;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String netProfit;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String startDateOfCurrentProfession;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected AmountField latestYearGrossTotalIncome;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String others;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String startDateOfBusiness;
    @XmlElement(name = "FieldworkAndIncome1", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String fieldworkAndIncome1;
    @XmlElement(name = "FieldworkAndIncome2", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String fieldworkAndIncome2;
    @XmlElement(name = "FieldworkAndIncome3", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String fieldworkAndIncome3;
    @XmlElement(name = "FieldworkAndIncome4", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String fieldworkAndIncome4;
    @XmlElement(name = "FieldworkAndIncome5", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String fieldworkAndIncome5;
    @XmlElement(name = "FieldworkAndIncome6", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String fieldworkAndIncome6;
    @XmlElement(name = "FieldworkAndIncome7", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String fieldworkAndIncome7;
    @XmlElement(name = "FieldworkAndIncome8", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String fieldworkAndIncome8;
    @XmlElement(name = "FieldworkAndIncome9", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String fieldworkAndIncome9;
    @XmlElement(name = "FieldworkAndIncome10", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String fieldworkAndIncome10;

    /**
     * Gets the value of the occupationType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOccupationType() {
        return occupationType;
    }

    /**
     * Sets the value of the occupationType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOccupationType(String value) {
        this.occupationType = value;
    }

    /**
     * Gets the value of the employerName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmployerName() {
        return employerName;
    }

    /**
     * Sets the value of the employerName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmployerName(String value) {
        this.employerName = value;
    }

    /**
     * Gets the value of the workingHereForPastMonths property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWorkingHereForPastMonths() {
        return workingHereForPastMonths;
    }

    /**
     * Sets the value of the workingHereForPastMonths property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWorkingHereForPastMonths(String value) {
        this.workingHereForPastMonths = value;
    }

    /**
     * Gets the value of the workingHereForPastYears property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWorkingHereForPastYears() {
        return workingHereForPastYears;
    }

    /**
     * Sets the value of the workingHereForPastYears property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWorkingHereForPastYears(String value) {
        this.workingHereForPastYears = value;
    }

    /**
     * Gets the value of the totalWorkExpereinceMonths property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalWorkExpereinceMonths() {
        return totalWorkExpereinceMonths;
    }

    /**
     * Sets the value of the totalWorkExpereinceMonths property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalWorkExpereinceMonths(String value) {
        this.totalWorkExpereinceMonths = value;
    }

    /**
     * Gets the value of the totalWorkExpereinceYears property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalWorkExpereinceYears() {
        return totalWorkExpereinceYears;
    }

    /**
     * Sets the value of the totalWorkExpereinceYears property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalWorkExpereinceYears(String value) {
        this.totalWorkExpereinceYears = value;
    }

    /**
     * Gets the value of the natureOfProfession property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNatureOfProfession() {
        return natureOfProfession;
    }

    /**
     * Sets the value of the natureOfProfession property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNatureOfProfession(String value) {
        this.natureOfProfession = value;
    }

    /**
     * Gets the value of the yearsInProfessionMonths property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getYearsInProfessionMonths() {
        return yearsInProfessionMonths;
    }

    /**
     * Sets the value of the yearsInProfessionMonths property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setYearsInProfessionMonths(String value) {
        this.yearsInProfessionMonths = value;
    }

    /**
     * Gets the value of the yearsInProfessionYears property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getYearsInProfessionYears() {
        return yearsInProfessionYears;
    }

    /**
     * Sets the value of the yearsInProfessionYears property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setYearsInProfessionYears(String value) {
        this.yearsInProfessionYears = value;
    }

    /**
     * Gets the value of the natureOfOccuptaion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNatureOfOccuptaion() {
        return natureOfOccuptaion;
    }

    /**
     * Sets the value of the natureOfOccuptaion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNatureOfOccuptaion(String value) {
        this.natureOfOccuptaion = value;
    }

    /**
     * Gets the value of the grossMonthlySalary property.
     * 
     * @return
     *     possible object is
     *     {@link AmountField }
     *     
     */
    public AmountField getGrossMonthlySalary() {
        return grossMonthlySalary;
    }

    /**
     * Sets the value of the grossMonthlySalary property.
     * 
     * @param value
     *     allowed object is
     *     {@link AmountField }
     *     
     */
    public void setGrossMonthlySalary(AmountField value) {
        this.grossMonthlySalary = value;
    }

    /**
     * Gets the value of the monthlyTakeHomeSalary property.
     * 
     * @return
     *     possible object is
     *     {@link AmountField }
     *     
     */
    public AmountField getMonthlyTakeHomeSalary() {
        return monthlyTakeHomeSalary;
    }

    /**
     * Sets the value of the monthlyTakeHomeSalary property.
     * 
     * @param value
     *     allowed object is
     *     {@link AmountField }
     *     
     */
    public void setMonthlyTakeHomeSalary(AmountField value) {
        this.monthlyTakeHomeSalary = value;
    }

    /**
     * Gets the value of the totalInstallmentCurrentlyPay property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalInstallmentCurrentlyPay() {
        return totalInstallmentCurrentlyPay;
    }

    /**
     * Sets the value of the totalInstallmentCurrentlyPay property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalInstallmentCurrentlyPay(String value) {
        this.totalInstallmentCurrentlyPay = value;
    }

    /**
     * Gets the value of the organizationName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrganizationName() {
        return organizationName;
    }

    /**
     * Sets the value of the organizationName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrganizationName(String value) {
        this.organizationName = value;
    }

    /**
     * Gets the value of the natureOfBusiness property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNatureOfBusiness() {
        return natureOfBusiness;
    }

    /**
     * Sets the value of the natureOfBusiness property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNatureOfBusiness(String value) {
        this.natureOfBusiness = value;
    }

    /**
     * Gets the value of the yearInBusinessMonths property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getYearInBusinessMonths() {
        return yearInBusinessMonths;
    }

    /**
     * Sets the value of the yearInBusinessMonths property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setYearInBusinessMonths(String value) {
        this.yearInBusinessMonths = value;
    }

    /**
     * Gets the value of the yearInBusinessYears property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getYearInBusinessYears() {
        return yearInBusinessYears;
    }

    /**
     * Sets the value of the yearInBusinessYears property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setYearInBusinessYears(String value) {
        this.yearInBusinessYears = value;
    }

    /**
     * Gets the value of the netProfit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNetProfit() {
        return netProfit;
    }

    /**
     * Sets the value of the netProfit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNetProfit(String value) {
        this.netProfit = value;
    }

    /**
     * Gets the value of the startDateOfCurrentProfession property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStartDateOfCurrentProfession() {
        return startDateOfCurrentProfession;
    }

    /**
     * Sets the value of the startDateOfCurrentProfession property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStartDateOfCurrentProfession(String value) {
        this.startDateOfCurrentProfession = value;
    }

    /**
     * Gets the value of the latestYearGrossTotalIncome property.
     * 
     * @return
     *     possible object is
     *     {@link AmountField }
     *     
     */
    public AmountField getLatestYearGrossTotalIncome() {
        return latestYearGrossTotalIncome;
    }

    /**
     * Sets the value of the latestYearGrossTotalIncome property.
     * 
     * @param value
     *     allowed object is
     *     {@link AmountField }
     *     
     */
    public void setLatestYearGrossTotalIncome(AmountField value) {
        this.latestYearGrossTotalIncome = value;
    }

    /**
     * Gets the value of the others property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOthers() {
        return others;
    }

    /**
     * Sets the value of the others property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOthers(String value) {
        this.others = value;
    }

    /**
     * Gets the value of the startDateOfBusiness property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStartDateOfBusiness() {
        return startDateOfBusiness;
    }

    /**
     * Sets the value of the startDateOfBusiness property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStartDateOfBusiness(String value) {
        this.startDateOfBusiness = value;
    }

    /**
     * Gets the value of the fieldworkAndIncome1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFieldworkAndIncome1() {
        return fieldworkAndIncome1;
    }

    /**
     * Sets the value of the fieldworkAndIncome1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFieldworkAndIncome1(String value) {
        this.fieldworkAndIncome1 = value;
    }

    /**
     * Gets the value of the fieldworkAndIncome2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFieldworkAndIncome2() {
        return fieldworkAndIncome2;
    }

    /**
     * Sets the value of the fieldworkAndIncome2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFieldworkAndIncome2(String value) {
        this.fieldworkAndIncome2 = value;
    }

    /**
     * Gets the value of the fieldworkAndIncome3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFieldworkAndIncome3() {
        return fieldworkAndIncome3;
    }

    /**
     * Sets the value of the fieldworkAndIncome3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFieldworkAndIncome3(String value) {
        this.fieldworkAndIncome3 = value;
    }

    /**
     * Gets the value of the fieldworkAndIncome4 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFieldworkAndIncome4() {
        return fieldworkAndIncome4;
    }

    /**
     * Sets the value of the fieldworkAndIncome4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFieldworkAndIncome4(String value) {
        this.fieldworkAndIncome4 = value;
    }

    /**
     * Gets the value of the fieldworkAndIncome5 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFieldworkAndIncome5() {
        return fieldworkAndIncome5;
    }

    /**
     * Sets the value of the fieldworkAndIncome5 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFieldworkAndIncome5(String value) {
        this.fieldworkAndIncome5 = value;
    }

    /**
     * Gets the value of the fieldworkAndIncome6 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFieldworkAndIncome6() {
        return fieldworkAndIncome6;
    }

    /**
     * Sets the value of the fieldworkAndIncome6 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFieldworkAndIncome6(String value) {
        this.fieldworkAndIncome6 = value;
    }

    /**
     * Gets the value of the fieldworkAndIncome7 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFieldworkAndIncome7() {
        return fieldworkAndIncome7;
    }

    /**
     * Sets the value of the fieldworkAndIncome7 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFieldworkAndIncome7(String value) {
        this.fieldworkAndIncome7 = value;
    }

    /**
     * Gets the value of the fieldworkAndIncome8 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFieldworkAndIncome8() {
        return fieldworkAndIncome8;
    }

    /**
     * Sets the value of the fieldworkAndIncome8 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFieldworkAndIncome8(String value) {
        this.fieldworkAndIncome8 = value;
    }

    /**
     * Gets the value of the fieldworkAndIncome9 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFieldworkAndIncome9() {
        return fieldworkAndIncome9;
    }

    /**
     * Sets the value of the fieldworkAndIncome9 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFieldworkAndIncome9(String value) {
        this.fieldworkAndIncome9 = value;
    }

    /**
     * Gets the value of the fieldworkAndIncome10 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFieldworkAndIncome10() {
        return fieldworkAndIncome10;
    }

    /**
     * Sets the value of the fieldworkAndIncome10 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFieldworkAndIncome10(String value) {
        this.fieldworkAndIncome10 = value;
    }

}
