
package vn.com.tpf.microservices.models.Finnone;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for updateCollateralDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="updateCollateralDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="deleteFlag" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType" minOccurs="0"/>
 *         &lt;element name="collateralNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="collateralRole" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="collateralType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="collateralSubType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="description" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="collateralReferenceNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="assetType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="updateAssetDetail" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}updateAssetDetail" minOccurs="0"/>
 *         &lt;element name="updatePropertyDetail" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}updatePropertyDetail" minOccurs="0"/>
 *         &lt;element name="updColInsuranceDetail" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}updColInsuranceDetail" maxOccurs="10" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateCollateralDetails", propOrder = {
    "deleteFlag",
    "collateralNumber",
    "collateralRole",
    "collateralType",
    "collateralSubType",
    "description",
    "collateralReferenceNumber",
    "assetType",
    "updateAssetDetail",
    "updatePropertyDetail",
    "updColInsuranceDetail"
})
public class UpdateCollateralDetails {

    protected Boolean deleteFlag;
    protected String collateralNumber;
    protected Integer collateralRole;
    @XmlElement(required = true)
    protected String collateralType;
    @XmlElement(required = true)
    protected String collateralSubType;
    protected String description;
    protected String collateralReferenceNumber;
    protected String assetType;
    protected UpdateAssetDetail updateAssetDetail;
    protected UpdatePropertyDetail updatePropertyDetail;
    protected List<UpdColInsuranceDetail> updColInsuranceDetail;

    /**
     * Gets the value of the deleteFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDeleteFlag() {
        return deleteFlag;
    }

    /**
     * Sets the value of the deleteFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDeleteFlag(Boolean value) {
        this.deleteFlag = value;
    }

    /**
     * Gets the value of the collateralNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCollateralNumber() {
        return collateralNumber;
    }

    /**
     * Sets the value of the collateralNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCollateralNumber(String value) {
        this.collateralNumber = value;
    }

    /**
     * Gets the value of the collateralRole property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCollateralRole() {
        return collateralRole;
    }

    /**
     * Sets the value of the collateralRole property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCollateralRole(Integer value) {
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
     * Gets the value of the updateAssetDetail property.
     * 
     * @return
     *     possible object is
     *     {@link UpdateAssetDetail }
     *     
     */
    public UpdateAssetDetail getUpdateAssetDetail() {
        return updateAssetDetail;
    }

    /**
     * Sets the value of the updateAssetDetail property.
     * 
     * @param value
     *     allowed object is
     *     {@link UpdateAssetDetail }
     *     
     */
    public void setUpdateAssetDetail(UpdateAssetDetail value) {
        this.updateAssetDetail = value;
    }

    /**
     * Gets the value of the updatePropertyDetail property.
     * 
     * @return
     *     possible object is
     *     {@link UpdatePropertyDetail }
     *     
     */
    public UpdatePropertyDetail getUpdatePropertyDetail() {
        return updatePropertyDetail;
    }

    /**
     * Sets the value of the updatePropertyDetail property.
     * 
     * @param value
     *     allowed object is
     *     {@link UpdatePropertyDetail }
     *     
     */
    public void setUpdatePropertyDetail(UpdatePropertyDetail value) {
        this.updatePropertyDetail = value;
    }

    /**
     * Gets the value of the updColInsuranceDetail property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the updColInsuranceDetail property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUpdColInsuranceDetail().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UpdColInsuranceDetail }
     * 
     * 
     */
    public List<UpdColInsuranceDetail> getUpdColInsuranceDetail() {
        if (updColInsuranceDetail == null) {
            updColInsuranceDetail = new ArrayList<UpdColInsuranceDetail>();
        }
        return this.updColInsuranceDetail;
    }

}
