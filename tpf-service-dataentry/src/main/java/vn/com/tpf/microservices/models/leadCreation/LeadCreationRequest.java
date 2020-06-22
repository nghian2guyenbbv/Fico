
package vn.com.tpf.microservices.models.leadCreation;

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
 *         &lt;element name="personInfoType" type="{http://www.nucleus.com/schemas/integration/leadCreationService}personInfo" maxOccurs="unbounded"/>
 *         &lt;element name="leadReferenceNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="productProcessor" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="documents" type="{http://www.nucleus.com/schemas/integration/leadCreationService}documents" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="sourcingDetails" type="{http://www.nucleus.com/schemas/integration/leadCreationService}sourcingDetails" minOccurs="0"/>
 *         &lt;element name="loanInformation" type="{http://www.nucleus.com/schemas/integration/leadCreationService}loanInformation" minOccurs="0"/>
 *         &lt;element name="communicationDetails" type="{http://www.nucleus.com/schemas/integration/leadCreationService}communicationDetails" minOccurs="0"/>
 *         &lt;element name="moveToNextStageFlag" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="product" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="userName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="productCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="branchcode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="cardType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="logo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="networkGateway" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="promoCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="relationship" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="geoTagging" type="{http://www.nucleus.com/schemas/integration/leadCreationService}geoTagging" minOccurs="0"/>
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
    "personInfoType",
    "leadReferenceNumber",
    "productProcessor",
    "documents",
    "sourcingDetails",
    "loanInformation",
    "communicationDetails",
    "moveToNextStageFlag",
    "product",
    "userName",
    "productCode",
    "branchcode",
    "cardType",
    "logo",
    "networkGateway",
    "promoCode",
    "relationship",
    "geoTagging"
})
@XmlRootElement(name = "leadCreationRequest", namespace = "http://www.nucleus.com/schemas/integration/leadCreationService")
public class LeadCreationRequest {

    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected List<PersonInfo> personInfoType;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String leadReferenceNumber;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String productProcessor;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService")
    protected List<Documents> documents;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService")
    protected SourcingDetails sourcingDetails;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService")
    protected LoanInformation loanInformation;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService")
    protected CommunicationDetails communicationDetails;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService")
    protected boolean moveToNextStageFlag;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String product;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService")
    protected String userName;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService")
    protected String productCode;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String branchcode;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String cardType;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String logo;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String networkGateway;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String promoCode;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService", required = true)
    protected String relationship;
    @XmlElement(namespace = "http://www.nucleus.com/schemas/integration/leadCreationService")
    protected GeoTagging geoTagging;

    /**
     * Gets the value of the personInfoType property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the personInfoType property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPersonInfoType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PersonInfo }
     * 
     * 
     */
    public List<PersonInfo> getPersonInfoType() {
        if (personInfoType == null) {
            personInfoType = new ArrayList<PersonInfo>();
        }
        return this.personInfoType;
    }

    /**
     * Gets the value of the leadReferenceNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLeadReferenceNumber() {
        return leadReferenceNumber;
    }

    /**
     * Sets the value of the leadReferenceNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLeadReferenceNumber(String value) {
        this.leadReferenceNumber = value;
    }

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
     * Gets the value of the sourcingDetails property.
     * 
     * @return
     *     possible object is
     *     {@link SourcingDetails }
     *     
     */
    public SourcingDetails getSourcingDetails() {
        return sourcingDetails;
    }

    /**
     * Sets the value of the sourcingDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link SourcingDetails }
     *     
     */
    public void setSourcingDetails(SourcingDetails value) {
        this.sourcingDetails = value;
    }

    /**
     * Gets the value of the loanInformation property.
     * 
     * @return
     *     possible object is
     *     {@link LoanInformation }
     *     
     */
    public LoanInformation getLoanInformation() {
        return loanInformation;
    }

    /**
     * Sets the value of the loanInformation property.
     * 
     * @param value
     *     allowed object is
     *     {@link LoanInformation }
     *     
     */
    public void setLoanInformation(LoanInformation value) {
        this.loanInformation = value;
    }

    /**
     * Gets the value of the communicationDetails property.
     * 
     * @return
     *     possible object is
     *     {@link CommunicationDetails }
     *     
     */
    public CommunicationDetails getCommunicationDetails() {
        return communicationDetails;
    }

    /**
     * Sets the value of the communicationDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link CommunicationDetails }
     *     
     */
    public void setCommunicationDetails(CommunicationDetails value) {
        this.communicationDetails = value;
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
     * Gets the value of the productCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductCode() {
        return productCode;
    }

    /**
     * Sets the value of the productCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductCode(String value) {
        this.productCode = value;
    }

    /**
     * Gets the value of the branchcode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBranchcode() {
        return branchcode;
    }

    /**
     * Sets the value of the branchcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBranchcode(String value) {
        this.branchcode = value;
    }

    /**
     * Gets the value of the cardType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCardType() {
        return cardType;
    }

    /**
     * Sets the value of the cardType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCardType(String value) {
        this.cardType = value;
    }

    /**
     * Gets the value of the logo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLogo() {
        return logo;
    }

    /**
     * Sets the value of the logo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLogo(String value) {
        this.logo = value;
    }

    /**
     * Gets the value of the networkGateway property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNetworkGateway() {
        return networkGateway;
    }

    /**
     * Sets the value of the networkGateway property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNetworkGateway(String value) {
        this.networkGateway = value;
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
     * Gets the value of the relationship property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRelationship() {
        return relationship;
    }

    /**
     * Sets the value of the relationship property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRelationship(String value) {
        this.relationship = value;
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

//    public LeadCreationRequest(List<PersonInfo> personInfoType, String leadReferenceNumber, String productProcessor, List<Documents> documents, SourcingDetails sourcingDetails, LoanInformation loanInformation, CommunicationDetails communicationDetails, boolean moveToNextStageFlag, String product, String userName, String productCode, String branchcode, String cardType, String logo, String networkGateway, String promoCode, String relationship, GeoTagging geoTagging) {
//        this.personInfoType = personInfoType;
//        this.leadReferenceNumber = leadReferenceNumber;
//        this.productProcessor = productProcessor;
//        this.documents = documents;
//        this.sourcingDetails = sourcingDetails;
//        this.loanInformation = loanInformation;
//        this.communicationDetails = communicationDetails;
//        this.moveToNextStageFlag = moveToNextStageFlag;
//        this.product = product;
//        this.userName = userName;
//        this.productCode = productCode;
//        this.branchcode = branchcode;
//        this.cardType = cardType;
//        this.logo = logo;
//        this.networkGateway = networkGateway;
//        this.promoCode = promoCode;
//        this.relationship = relationship;
//        this.geoTagging = geoTagging;
//    }
//
//    public LeadCreationRequest() {
//    }
}
