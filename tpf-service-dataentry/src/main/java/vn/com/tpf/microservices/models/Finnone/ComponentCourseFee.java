
package vn.com.tpf.microservices.models.Finnone;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for componentCourseFee complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="componentCourseFee">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="feeComponent" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="frequency" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="numberOfPayments" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="currency" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="feePerInstance" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="financeRequestedPercnt" type="{http://schema.base.ws.pro.finnone.nucleus.com}AmountType" minOccurs="0"/>
 *         &lt;element name="financeAmountRequested" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="componentDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}componentDetails" maxOccurs="10" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "componentCourseFee", propOrder = {
    "feeComponent",
    "frequency",
    "numberOfPayments",
    "currency",
    "feePerInstance",
    "financeRequestedPercnt",
    "financeAmountRequested",
    "componentDetails"
})
public class ComponentCourseFee {

    @XmlElement(required = true)
    protected String feeComponent;
    @XmlElement(required = true)
    protected String frequency;
    protected Integer numberOfPayments;
    @XmlElement(required = true)
    protected String currency;
    protected Integer feePerInstance;
    protected BigDecimal financeRequestedPercnt;
    protected Integer financeAmountRequested;
    protected List<ComponentDetails> componentDetails;

    /**
     * Gets the value of the feeComponent property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFeeComponent() {
        return feeComponent;
    }

    /**
     * Sets the value of the feeComponent property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFeeComponent(String value) {
        this.feeComponent = value;
    }

    /**
     * Gets the value of the frequency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFrequency() {
        return frequency;
    }

    /**
     * Sets the value of the frequency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFrequency(String value) {
        this.frequency = value;
    }

    /**
     * Gets the value of the numberOfPayments property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumberOfPayments() {
        return numberOfPayments;
    }

    /**
     * Sets the value of the numberOfPayments property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumberOfPayments(Integer value) {
        this.numberOfPayments = value;
    }

    /**
     * Gets the value of the currency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets the value of the currency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrency(String value) {
        this.currency = value;
    }

    /**
     * Gets the value of the feePerInstance property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getFeePerInstance() {
        return feePerInstance;
    }

    /**
     * Sets the value of the feePerInstance property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setFeePerInstance(Integer value) {
        this.feePerInstance = value;
    }

    /**
     * Gets the value of the financeRequestedPercnt property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getFinanceRequestedPercnt() {
        return financeRequestedPercnt;
    }

    /**
     * Sets the value of the financeRequestedPercnt property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setFinanceRequestedPercnt(BigDecimal value) {
        this.financeRequestedPercnt = value;
    }

    /**
     * Gets the value of the financeAmountRequested property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getFinanceAmountRequested() {
        return financeAmountRequested;
    }

    /**
     * Sets the value of the financeAmountRequested property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setFinanceAmountRequested(Integer value) {
        this.financeAmountRequested = value;
    }

    /**
     * Gets the value of the componentDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the componentDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getComponentDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ComponentDetails }
     * 
     * 
     */
    public List<ComponentDetails> getComponentDetails() {
        if (componentDetails == null) {
            componentDetails = new ArrayList<ComponentDetails>();
        }
        return this.componentDetails;
    }

}
