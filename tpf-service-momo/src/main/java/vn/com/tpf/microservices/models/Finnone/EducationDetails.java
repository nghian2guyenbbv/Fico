
package vn.com.tpf.microservices.models.Finnone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for educationDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="educationDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="qualificationType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="educationQualification" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="specialization" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="university" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="yearOfPassing" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="isHighestDegree" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "educationDetails", propOrder = {
    "qualificationType",
    "educationQualification",
    "specialization",
    "university",
    "yearOfPassing",
    "isHighestDegree"
})
public class EducationDetails {

    @XmlElement(required = true)
    protected String qualificationType;
    @XmlElement(required = true)
    protected String educationQualification;
    @XmlElement(required = true)
    protected String specialization;
    @XmlElement(required = true)
    protected String university;
    protected Integer yearOfPassing;
    protected Integer isHighestDegree;

    /**
     * Gets the value of the qualificationType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQualificationType() {
        return qualificationType;
    }

    /**
     * Sets the value of the qualificationType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQualificationType(String value) {
        this.qualificationType = value;
    }

    /**
     * Gets the value of the educationQualification property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEducationQualification() {
        return educationQualification;
    }

    /**
     * Sets the value of the educationQualification property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEducationQualification(String value) {
        this.educationQualification = value;
    }

    /**
     * Gets the value of the specialization property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpecialization() {
        return specialization;
    }

    /**
     * Sets the value of the specialization property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpecialization(String value) {
        this.specialization = value;
    }

    /**
     * Gets the value of the university property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUniversity() {
        return university;
    }

    /**
     * Sets the value of the university property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUniversity(String value) {
        this.university = value;
    }

    /**
     * Gets the value of the yearOfPassing property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getYearOfPassing() {
        return yearOfPassing;
    }

    /**
     * Sets the value of the yearOfPassing property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setYearOfPassing(Integer value) {
        this.yearOfPassing = value;
    }

    /**
     * Gets the value of the isHighestDegree property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIsHighestDegree() {
        return isHighestDegree;
    }

    /**
     * Sets the value of the isHighestDegree property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIsHighestDegree(Integer value) {
        this.isHighestDegree = value;
    }

}
