
package vn.com.tpf.microservices.models.Finnone;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for updNonIndvCust complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="updNonIndvCust">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="institutionInfo" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}institutionInfo" minOccurs="0"/>
 *         &lt;element name="contactPerson" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}contactPerson" maxOccurs="4" minOccurs="0"/>
 *         &lt;element name="partners" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}partners" maxOccurs="4" minOccurs="0"/>
 *         &lt;element name="directors" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}directors" maxOccurs="4" minOccurs="0"/>
 *         &lt;element name="directorInfo" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}directorInfo" minOccurs="0"/>
 *         &lt;element name="businessDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}updateBusinessDetails" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="marketInformation" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}marketInformation" minOccurs="0"/>
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
@XmlType(name = "updNonIndvCust", propOrder = {
    "institutionInfo",
    "contactPerson",
    "partners",
    "directors",
    "directorInfo",
    "businessDetails",
    "marketInformation",
    "dynamicFormDetails"
})
public class UpdNonIndvCust {

    protected InstitutionInfo institutionInfo;
    protected List<ContactPerson> contactPerson;
    protected List<Partners> partners;
    protected List<Directors> directors;
    protected DirectorInfo directorInfo;
    protected List<UpdateBusinessDetails> businessDetails;
    protected MarketInformation marketInformation;
    protected List<DynamicFormDetails> dynamicFormDetails;

    /**
     * Gets the value of the institutionInfo property.
     * 
     * @return
     *     possible object is
     *     {@link InstitutionInfo }
     *     
     */
    public InstitutionInfo getInstitutionInfo() {
        return institutionInfo;
    }

    /**
     * Sets the value of the institutionInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link InstitutionInfo }
     *     
     */
    public void setInstitutionInfo(InstitutionInfo value) {
        this.institutionInfo = value;
    }

    /**
     * Gets the value of the contactPerson property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the contactPerson property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContactPerson().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ContactPerson }
     * 
     * 
     */
    public List<ContactPerson> getContactPerson() {
        if (contactPerson == null) {
            contactPerson = new ArrayList<ContactPerson>();
        }
        return this.contactPerson;
    }

    /**
     * Gets the value of the partners property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the partners property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPartners().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Partners }
     * 
     * 
     */
    public List<Partners> getPartners() {
        if (partners == null) {
            partners = new ArrayList<Partners>();
        }
        return this.partners;
    }

    /**
     * Gets the value of the directors property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the directors property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDirectors().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Directors }
     * 
     * 
     */
    public List<Directors> getDirectors() {
        if (directors == null) {
            directors = new ArrayList<Directors>();
        }
        return this.directors;
    }

    /**
     * Gets the value of the directorInfo property.
     * 
     * @return
     *     possible object is
     *     {@link DirectorInfo }
     *     
     */
    public DirectorInfo getDirectorInfo() {
        return directorInfo;
    }

    /**
     * Sets the value of the directorInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link DirectorInfo }
     *     
     */
    public void setDirectorInfo(DirectorInfo value) {
        this.directorInfo = value;
    }

    /**
     * Gets the value of the businessDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the businessDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBusinessDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UpdateBusinessDetails }
     * 
     * 
     */
    public List<UpdateBusinessDetails> getBusinessDetails() {
        if (businessDetails == null) {
            businessDetails = new ArrayList<UpdateBusinessDetails>();
        }
        return this.businessDetails;
    }

    /**
     * Gets the value of the marketInformation property.
     * 
     * @return
     *     possible object is
     *     {@link MarketInformation }
     *     
     */
    public MarketInformation getMarketInformation() {
        return marketInformation;
    }

    /**
     * Sets the value of the marketInformation property.
     * 
     * @param value
     *     allowed object is
     *     {@link MarketInformation }
     *     
     */
    public void setMarketInformation(MarketInformation value) {
        this.marketInformation = value;
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
