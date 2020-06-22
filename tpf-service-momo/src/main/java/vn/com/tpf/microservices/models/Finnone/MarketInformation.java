
package vn.com.tpf.microservices.models.Finnone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for marketInformation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="marketInformation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="competitiorDetails" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="demSupAnalysis" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="substituteAvl" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="govPoliciies" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="comments" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "marketInformation", propOrder = {
    "competitiorDetails",
    "demSupAnalysis",
    "substituteAvl",
    "govPoliciies",
    "comments"
})
public class MarketInformation {

    protected String competitiorDetails;
    protected String demSupAnalysis;
    protected String substituteAvl;
    protected String govPoliciies;
    protected String comments;

    /**
     * Gets the value of the competitiorDetails property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompetitiorDetails() {
        return competitiorDetails;
    }

    /**
     * Sets the value of the competitiorDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompetitiorDetails(String value) {
        this.competitiorDetails = value;
    }

    /**
     * Gets the value of the demSupAnalysis property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDemSupAnalysis() {
        return demSupAnalysis;
    }

    /**
     * Sets the value of the demSupAnalysis property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDemSupAnalysis(String value) {
        this.demSupAnalysis = value;
    }

    /**
     * Gets the value of the substituteAvl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubstituteAvl() {
        return substituteAvl;
    }

    /**
     * Sets the value of the substituteAvl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubstituteAvl(String value) {
        this.substituteAvl = value;
    }

    /**
     * Gets the value of the govPoliciies property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGovPoliciies() {
        return govPoliciies;
    }

    /**
     * Sets the value of the govPoliciies property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGovPoliciies(String value) {
        this.govPoliciies = value;
    }

    /**
     * Gets the value of the comments property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComments() {
        return comments;
    }

    /**
     * Sets the value of the comments property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComments(String value) {
        this.comments = value;
    }

}
