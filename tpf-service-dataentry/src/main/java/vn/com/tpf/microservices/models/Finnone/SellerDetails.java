
package vn.com.tpf.microservices.models.Finnone;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for sellerDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="sellerDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="sellerName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="presentRegisteredOwner" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="tctNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="lotNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="percentageShare" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="currentPropertyUsage" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="otherRemarks" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
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
@XmlType(name = "sellerDetails", propOrder = {
    "sellerName",
    "presentRegisteredOwner",
    "tctNumber",
    "lotNumber",
    "percentageShare",
    "currentPropertyUsage",
    "otherRemarks",
    "dynamicFormDetails"
})
public class SellerDetails {

    protected String sellerName;
    protected String presentRegisteredOwner;
    protected Integer tctNumber;
    protected Integer lotNumber;
    protected Integer percentageShare;
    protected String currentPropertyUsage;
    protected String otherRemarks;
    protected List<DynamicFormDetails> dynamicFormDetails;

    /**
     * Gets the value of the sellerName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSellerName() {
        return sellerName;
    }

    /**
     * Sets the value of the sellerName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSellerName(String value) {
        this.sellerName = value;
    }

    /**
     * Gets the value of the presentRegisteredOwner property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPresentRegisteredOwner() {
        return presentRegisteredOwner;
    }

    /**
     * Sets the value of the presentRegisteredOwner property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPresentRegisteredOwner(String value) {
        this.presentRegisteredOwner = value;
    }

    /**
     * Gets the value of the tctNumber property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTctNumber() {
        return tctNumber;
    }

    /**
     * Sets the value of the tctNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTctNumber(Integer value) {
        this.tctNumber = value;
    }

    /**
     * Gets the value of the lotNumber property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getLotNumber() {
        return lotNumber;
    }

    /**
     * Sets the value of the lotNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setLotNumber(Integer value) {
        this.lotNumber = value;
    }

    /**
     * Gets the value of the percentageShare property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPercentageShare() {
        return percentageShare;
    }

    /**
     * Sets the value of the percentageShare property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPercentageShare(Integer value) {
        this.percentageShare = value;
    }

    /**
     * Gets the value of the currentPropertyUsage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrentPropertyUsage() {
        return currentPropertyUsage;
    }

    /**
     * Sets the value of the currentPropertyUsage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrentPropertyUsage(String value) {
        this.currentPropertyUsage = value;
    }

    /**
     * Gets the value of the otherRemarks property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOtherRemarks() {
        return otherRemarks;
    }

    /**
     * Sets the value of the otherRemarks property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOtherRemarks(String value) {
        this.otherRemarks = value;
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
