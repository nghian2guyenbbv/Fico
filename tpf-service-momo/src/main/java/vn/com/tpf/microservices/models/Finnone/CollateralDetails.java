
package vn.com.tpf.microservices.models.Finnone;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for collateralDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="collateralDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="collateralRole" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType"/>
 *         &lt;element name="collateralType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="collateralSubType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="description" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="collateralReferenceNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="assetType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="assetDetail" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}assetDetail" minOccurs="0"/>
 *         &lt;element name="propertyDetail" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}propertyDetail" minOccurs="0"/>
 *         &lt;element name="collateralInsuranceDetail" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}collateralInsuranceDetail" maxOccurs="10" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "collateralDetails", propOrder = {
    "collateralRole",
    "collateralType",
    "collateralSubType",
    "description",
    "collateralReferenceNumber",
    "assetType",
    "assetDetail",
    "propertyDetail",
    "collateralInsuranceDetail"
})
public class CollateralDetails {

    protected int collateralRole;
    @XmlElement(required = true)
    protected String collateralType;
    @XmlElement(required = true)
    protected String collateralSubType;
    protected String description;
    protected String collateralReferenceNumber;
    protected String assetType;
    protected AssetDetail assetDetail;
    protected PropertyDetail propertyDetail;
    protected List<CollateralInsuranceDetail> collateralInsuranceDetail;

    /**
     * Gets the value of the collateralRole property.
     * 
     */
    public int getCollateralRole() {
        return collateralRole;
    }

    /**
     * Sets the value of the collateralRole property.
     * 
     */
    public void setCollateralRole(int value) {
        this.collateralRole = value;
    }

    /**
     * Gets the value of the collateralType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCollateralType() {
        return collateralType;
    }

    /**
     * Sets the value of the collateralType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCollateralType(String value) {
        this.collateralType = value;
    }

    /**
     * Gets the value of the collateralSubType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCollateralSubType() {
        return collateralSubType;
    }

    /**
     * Sets the value of the collateralSubType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCollateralSubType(String value) {
        this.collateralSubType = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the collateralReferenceNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCollateralReferenceNumber() {
        return collateralReferenceNumber;
    }

    /**
     * Sets the value of the collateralReferenceNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCollateralReferenceNumber(String value) {
        this.collateralReferenceNumber = value;
    }

    /**
     * Gets the value of the assetType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssetType() {
        return assetType;
    }

    /**
     * Sets the value of the assetType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssetType(String value) {
        this.assetType = value;
    }

    /**
     * Gets the value of the assetDetail property.
     * 
     * @return
     *     possible object is
     *     {@link AssetDetail }
     *     
     */
    public AssetDetail getAssetDetail() {
        return assetDetail;
    }

    /**
     * Sets the value of the assetDetail property.
     * 
     * @param value
     *     allowed object is
     *     {@link AssetDetail }
     *     
     */
    public void setAssetDetail(AssetDetail value) {
        this.assetDetail = value;
    }

    /**
     * Gets the value of the propertyDetail property.
     * 
     * @return
     *     possible object is
     *     {@link PropertyDetail }
     *     
     */
    public PropertyDetail getPropertyDetail() {
        return propertyDetail;
    }

    /**
     * Sets the value of the propertyDetail property.
     * 
     * @param value
     *     allowed object is
     *     {@link PropertyDetail }
     *     
     */
    public void setPropertyDetail(PropertyDetail value) {
        this.propertyDetail = value;
    }

    /**
     * Gets the value of the collateralInsuranceDetail property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the collateralInsuranceDetail property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCollateralInsuranceDetail().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CollateralInsuranceDetail }
     * 
     * 
     */
    public List<CollateralInsuranceDetail> getCollateralInsuranceDetail() {
        if (collateralInsuranceDetail == null) {
            collateralInsuranceDetail = new ArrayList<CollateralInsuranceDetail>();
        }
        return this.collateralInsuranceDetail;
    }

}
