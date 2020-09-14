
package vn.com.tpf.microservices.models.Finnone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for personalDiscussionForm complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="personalDiscussionForm">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="decision" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="pdType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="dynamicFormDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}dynamicFormDetails" minOccurs="0"/>
 *         &lt;element name="decisionReasonDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}decisionReasonDetails" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "personalDiscussionForm", propOrder = {
    "decision",
    "pdType",
    "dynamicFormDetails",
    "decisionReasonDetails"
})
public class PersonalDiscussionForm {

    protected String decision;
    protected String pdType;
    protected DynamicFormDetails dynamicFormDetails;
    protected DecisionReasonDetails decisionReasonDetails;

    /**
     * Gets the value of the decision property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDecision() {
        return decision;
    }

    /**
     * Sets the value of the decision property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDecision(String value) {
        this.decision = value;
    }

    /**
     * Gets the value of the pdType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPdType() {
        return pdType;
    }

    /**
     * Sets the value of the pdType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPdType(String value) {
        this.pdType = value;
    }

    /**
     * Gets the value of the dynamicFormDetails property.
     * 
     * @return
     *     possible object is
     *     {@link DynamicFormDetails }
     *     
     */
    public DynamicFormDetails getDynamicFormDetails() {
        return dynamicFormDetails;
    }

    /**
     * Sets the value of the dynamicFormDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link DynamicFormDetails }
     *     
     */
    public void setDynamicFormDetails(DynamicFormDetails value) {
        this.dynamicFormDetails = value;
    }

    /**
     * Gets the value of the decisionReasonDetails property.
     * 
     * @return
     *     possible object is
     *     {@link DecisionReasonDetails }
     *     
     */
    public DecisionReasonDetails getDecisionReasonDetails() {
        return decisionReasonDetails;
    }

    /**
     * Sets the value of the decisionReasonDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link DecisionReasonDetails }
     *     
     */
    public void setDecisionReasonDetails(DecisionReasonDetails value) {
        this.decisionReasonDetails = value;
    }

}
