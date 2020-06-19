
package vn.com.tpf.microservices.models.leadCreation;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for loanInformation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="loanInformation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="productType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="loanProduct" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="product" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="scheme" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="loanAmountRequested" type="{http://www.nucleus.com/schemas/integration/leadCreationService}amountField"/>
 *         &lt;element name="city_Inwhich_property_is_based" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="typeOfLoan" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="existingNumberOftrucksOrBuses" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="numberOfVehiclesFreeFromFinance" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="indicativePrice" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="costOfHome" type="{http://www.nucleus.com/schemas/integration/leadCreationService}amountField"/>
 *         &lt;element name="costOfContsruction" type="{http://www.nucleus.com/schemas/integration/leadCreationService}amountField"/>
 *         &lt;element name="costOfLand" type="{http://www.nucleus.com/schemas/integration/leadCreationService}amountField"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "loanInformation", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", propOrder = {
    "productType",
    "loanProduct",
    "product",
    "scheme",
    "loanAmountRequested",
    "cityInwhichPropertyIsBased",
    "typeOfLoan",
    "existingNumberOftrucksOrBuses",
    "numberOfVehiclesFreeFromFinance",
    "indicativePrice",
    "costOfHome",
    "costOfContsruction",
    "costOfLand"
})
public class LoanInformation {

    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String productType;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String loanProduct;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String product;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService")
    protected String scheme;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected AmountField loanAmountRequested;
    @XmlElement(name = "city_Inwhich_property_is_based", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String cityInwhichPropertyIsBased;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService")
    protected String typeOfLoan;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String existingNumberOftrucksOrBuses;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String numberOfVehiclesFreeFromFinance;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String indicativePrice;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected AmountField costOfHome;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected AmountField costOfContsruction;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected AmountField costOfLand;

    /**
     * Gets the value of the productType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductType() {
        return productType;
    }

    /**
     * Sets the value of the productType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductType(String value) {
        this.productType = value;
    }

    /**
     * Gets the value of the loanProduct property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLoanProduct() {
        return loanProduct;
    }

    /**
     * Sets the value of the loanProduct property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLoanProduct(String value) {
        this.loanProduct = value;
    }

    /**
     * Gets the value of the product property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProduct() {
        return product;
    }

    /**
     * Sets the value of the product property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProduct(String value) {
        this.product = value;
    }

    /**
     * Gets the value of the scheme property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScheme() {
        return scheme;
    }

    /**
     * Sets the value of the scheme property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScheme(String value) {
        this.scheme = value;
    }

    /**
     * Gets the value of the loanAmountRequested property.
     * 
     * @return
     *     possible object is
     *     {@link AmountField }
     *     
     */
    public AmountField getLoanAmountRequested() {
        return loanAmountRequested;
    }

    /**
     * Sets the value of the loanAmountRequested property.
     * 
     * @param value
     *     allowed object is
     *     {@link AmountField }
     *     
     */
    public void setLoanAmountRequested(AmountField value) {
        this.loanAmountRequested = value;
    }

    /**
     * Gets the value of the cityInwhichPropertyIsBased property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCityInwhichPropertyIsBased() {
        return cityInwhichPropertyIsBased;
    }

    /**
     * Sets the value of the cityInwhichPropertyIsBased property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCityInwhichPropertyIsBased(String value) {
        this.cityInwhichPropertyIsBased = value;
    }

    /**
     * Gets the value of the typeOfLoan property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTypeOfLoan() {
        return typeOfLoan;
    }

    /**
     * Sets the value of the typeOfLoan property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTypeOfLoan(String value) {
        this.typeOfLoan = value;
    }

    /**
     * Gets the value of the existingNumberOftrucksOrBuses property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExistingNumberOftrucksOrBuses() {
        return existingNumberOftrucksOrBuses;
    }

    /**
     * Sets the value of the existingNumberOftrucksOrBuses property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExistingNumberOftrucksOrBuses(String value) {
        this.existingNumberOftrucksOrBuses = value;
    }

    /**
     * Gets the value of the numberOfVehiclesFreeFromFinance property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumberOfVehiclesFreeFromFinance() {
        return numberOfVehiclesFreeFromFinance;
    }

    /**
     * Sets the value of the numberOfVehiclesFreeFromFinance property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumberOfVehiclesFreeFromFinance(String value) {
        this.numberOfVehiclesFreeFromFinance = value;
    }

    /**
     * Gets the value of the indicativePrice property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndicativePrice() {
        return indicativePrice;
    }

    /**
     * Sets the value of the indicativePrice property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndicativePrice(String value) {
        this.indicativePrice = value;
    }

    /**
     * Gets the value of the costOfHome property.
     * 
     * @return
     *     possible object is
     *     {@link AmountField }
     *     
     */
    public AmountField getCostOfHome() {
        return costOfHome;
    }

    /**
     * Sets the value of the costOfHome property.
     * 
     * @param value
     *     allowed object is
     *     {@link AmountField }
     *     
     */
    public void setCostOfHome(AmountField value) {
        this.costOfHome = value;
    }

    /**
     * Gets the value of the costOfContsruction property.
     * 
     * @return
     *     possible object is
     *     {@link AmountField }
     *     
     */
    public AmountField getCostOfContsruction() {
        return costOfContsruction;
    }

    /**
     * Sets the value of the costOfContsruction property.
     * 
     * @param value
     *     allowed object is
     *     {@link AmountField }
     *     
     */
    public void setCostOfContsruction(AmountField value) {
        this.costOfContsruction = value;
    }

    /**
     * Gets the value of the costOfLand property.
     * 
     * @return
     *     possible object is
     *     {@link AmountField }
     *     
     */
    public AmountField getCostOfLand() {
        return costOfLand;
    }

    /**
     * Sets the value of the costOfLand property.
     * 
     * @param value
     *     allowed object is
     *     {@link AmountField }
     *     
     */
    public void setCostOfLand(AmountField value) {
        this.costOfLand = value;
    }

}
