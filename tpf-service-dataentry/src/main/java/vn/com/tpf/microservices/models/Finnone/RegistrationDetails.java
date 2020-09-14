
package vn.com.tpf.microservices.models.Finnone;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import vn.com.tpf.microservices.models.Finnone.cas.MoneyType;


/**
 * <p>Java class for registrationDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="registrationDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="registeredProperty" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType" minOccurs="0"/>
 *         &lt;element name="totalAgrmntAmount" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="agreementDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}agreementDetails" maxOccurs="10" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "registrationDetails", propOrder = {
    "registeredProperty",
    "totalAgrmntAmount",
    "agreementDetails"
})
public class RegistrationDetails {

    protected Boolean registeredProperty;
    protected MoneyType totalAgrmntAmount;
    protected List<AgreementDetails> agreementDetails;

    /**
     * Gets the value of the registeredProperty property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRegisteredProperty() {
        return registeredProperty;
    }

    /**
     * Sets the value of the registeredProperty property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRegisteredProperty(Boolean value) {
        this.registeredProperty = value;
    }

    /**
     * Gets the value of the totalAgrmntAmount property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getTotalAgrmntAmount() {
        return totalAgrmntAmount;
    }

    /**
     * Sets the value of the totalAgrmntAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setTotalAgrmntAmount(MoneyType value) {
        this.totalAgrmntAmount = value;
    }

    /**
     * Gets the value of the agreementDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the agreementDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAgreementDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AgreementDetails }
     * 
     * 
     */
    public List<AgreementDetails> getAgreementDetails() {
        if (agreementDetails == null) {
            agreementDetails = new ArrayList<AgreementDetails>();
        }
        return this.agreementDetails;
    }

}
