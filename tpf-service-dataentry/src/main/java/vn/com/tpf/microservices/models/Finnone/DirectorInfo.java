
package vn.com.tpf.microservices.models.Finnone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for directorInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="directorInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="maximumNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="minimumNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="presentNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="promoters" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="independent" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="professional" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="nominees" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="outside" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="others" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "directorInfo", propOrder = {
    "maximumNumber",
    "minimumNumber",
    "presentNumber",
    "promoters",
    "independent",
    "professional",
    "nominees",
    "outside",
    "others"
})
public class DirectorInfo {

    protected Integer maximumNumber;
    protected Integer minimumNumber;
    protected Integer presentNumber;
    protected Integer promoters;
    protected Integer independent;
    protected Integer professional;
    protected Integer nominees;
    protected Integer outside;
    protected Integer others;

    /**
     * Gets the value of the maximumNumber property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMaximumNumber() {
        return maximumNumber;
    }

    /**
     * Sets the value of the maximumNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMaximumNumber(Integer value) {
        this.maximumNumber = value;
    }

    /**
     * Gets the value of the minimumNumber property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMinimumNumber() {
        return minimumNumber;
    }

    /**
     * Sets the value of the minimumNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMinimumNumber(Integer value) {
        this.minimumNumber = value;
    }

    /**
     * Gets the value of the presentNumber property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPresentNumber() {
        return presentNumber;
    }

    /**
     * Sets the value of the presentNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPresentNumber(Integer value) {
        this.presentNumber = value;
    }

    /**
     * Gets the value of the promoters property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPromoters() {
        return promoters;
    }

    /**
     * Sets the value of the promoters property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPromoters(Integer value) {
        this.promoters = value;
    }

    /**
     * Gets the value of the independent property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIndependent() {
        return independent;
    }

    /**
     * Sets the value of the independent property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIndependent(Integer value) {
        this.independent = value;
    }

    /**
     * Gets the value of the professional property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getProfessional() {
        return professional;
    }

    /**
     * Sets the value of the professional property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setProfessional(Integer value) {
        this.professional = value;
    }

    /**
     * Gets the value of the nominees property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNominees() {
        return nominees;
    }

    /**
     * Sets the value of the nominees property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNominees(Integer value) {
        this.nominees = value;
    }

    /**
     * Gets the value of the outside property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOutside() {
        return outside;
    }

    /**
     * Sets the value of the outside property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOutside(Integer value) {
        this.outside = value;
    }

    /**
     * Gets the value of the others property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOthers() {
        return others;
    }

    /**
     * Sets the value of the others property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOthers(Integer value) {
        this.others = value;
    }

}
