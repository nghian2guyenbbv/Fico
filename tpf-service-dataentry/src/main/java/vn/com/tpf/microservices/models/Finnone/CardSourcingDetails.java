
package vn.com.tpf.microservices.models.Finnone;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for cardSourcingDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="cardSourcingDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="requestType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="product" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="sourceBranch" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="sourcingBranch" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="sourcingChannel" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="channelType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="priorityType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="ccType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="promoCode" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="searchedCardNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="searchedCardAccountNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="crossSellReferenceNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="leadProviderId" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="leadfulfillerId" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="documentsToBeChecked" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType" minOccurs="0"/>
 *         &lt;element name="cardApplicationDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}cardApplicationDetails"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cardSourcingDetails", propOrder = {
    "requestType",
    "product",
    "sourceBranch",
    "sourcingBranch",
    "sourcingChannel",
    "channelType",
    "priorityType",
    "ccType",
    "promoCode",
    "searchedCardNumber",
    "searchedCardAccountNumber",
    "crossSellReferenceNumber",
    "leadProviderId",
    "leadfulfillerId",
    "documentsToBeChecked",
    "cardApplicationDetails"
})
public class CardSourcingDetails {

    @XmlElement(required = true)
    protected String requestType;
    @XmlElement(required = true)
    protected String product;
    @XmlElement(required = true)
    protected String sourceBranch;
    protected String sourcingBranch;
    @XmlElement(required = true)
    protected String sourcingChannel;
    protected String channelType;
    protected String priorityType;
    protected String ccType;
    @XmlElement(required = true)
    protected String promoCode;
    protected String searchedCardNumber;
    protected String searchedCardAccountNumber;
    protected String crossSellReferenceNumber;
    protected String leadProviderId;
    protected String leadfulfillerId;
    protected Boolean documentsToBeChecked;
    @XmlElement(required = true)
    protected CardApplicationDetails cardApplicationDetails;

    /**
     * Gets the value of the requestType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestType() {
        return requestType;
    }

    /**
     * Sets the value of the requestType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestType(String value) {
        this.requestType = value;
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
     * Gets the value of the sourceBranch property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourceBranch() {
        return sourceBranch;
    }

    /**
     * Sets the value of the sourceBranch property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourceBranch(String value) {
        this.sourceBranch = value;
    }

    /**
     * Gets the value of the sourcingBranch property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourcingBranch() {
        return sourcingBranch;
    }

    /**
     * Sets the value of the sourcingBranch property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourcingBranch(String value) {
        this.sourcingBranch = value;
    }

    /**
     * Gets the value of the sourcingChannel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourcingChannel() {
        return sourcingChannel;
    }

    /**
     * Sets the value of the sourcingChannel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourcingChannel(String value) {
        this.sourcingChannel = value;
    }

    /**
     * Gets the value of the channelType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChannelType() {
        return channelType;
    }

    /**
     * Sets the value of the channelType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChannelType(String value) {
        this.channelType = value;
    }

    /**
     * Gets the value of the priorityType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPriorityType() {
        return priorityType;
    }

    /**
     * Sets the value of the priorityType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPriorityType(String value) {
        this.priorityType = value;
    }

    /**
     * Gets the value of the ccType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCcType() {
        return ccType;
    }

    /**
     * Sets the value of the ccType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCcType(String value) {
        this.ccType = value;
    }

    /**
     * Gets the value of the promoCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPromoCode() {
        return promoCode;
    }

    /**
     * Sets the value of the promoCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPromoCode(String value) {
        this.promoCode = value;
    }

    /**
     * Gets the value of the searchedCardNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSearchedCardNumber() {
        return searchedCardNumber;
    }

    /**
     * Sets the value of the searchedCardNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSearchedCardNumber(String value) {
        this.searchedCardNumber = value;
    }

    /**
     * Gets the value of the searchedCardAccountNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSearchedCardAccountNumber() {
        return searchedCardAccountNumber;
    }

    /**
     * Sets the value of the searchedCardAccountNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSearchedCardAccountNumber(String value) {
        this.searchedCardAccountNumber = value;
    }

    /**
     * Gets the value of the crossSellReferenceNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCrossSellReferenceNumber() {
        return crossSellReferenceNumber;
    }

    /**
     * Sets the value of the crossSellReferenceNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCrossSellReferenceNumber(String value) {
        this.crossSellReferenceNumber = value;
    }

    /**
     * Gets the value of the leadProviderId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLeadProviderId() {
        return leadProviderId;
    }

    /**
     * Sets the value of the leadProviderId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLeadProviderId(String value) {
        this.leadProviderId = value;
    }

    /**
     * Gets the value of the leadfulfillerId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLeadfulfillerId() {
        return leadfulfillerId;
    }

    /**
     * Sets the value of the leadfulfillerId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLeadfulfillerId(String value) {
        this.leadfulfillerId = value;
    }

    /**
     * Gets the value of the documentsToBeChecked property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDocumentsToBeChecked() {
        return documentsToBeChecked;
    }

    /**
     * Sets the value of the documentsToBeChecked property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDocumentsToBeChecked(Boolean value) {
        this.documentsToBeChecked = value;
    }

    /**
     * Gets the value of the cardApplicationDetails property.
     * 
     * @return
     *     possible object is
     *     {@link CardApplicationDetails }
     *     
     */
    public CardApplicationDetails getCardApplicationDetails() {
        return cardApplicationDetails;
    }

    /**
     * Sets the value of the cardApplicationDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link CardApplicationDetails }
     *     
     */
    public void setCardApplicationDetails(CardApplicationDetails value) {
        this.cardApplicationDetails = value;
    }

}
