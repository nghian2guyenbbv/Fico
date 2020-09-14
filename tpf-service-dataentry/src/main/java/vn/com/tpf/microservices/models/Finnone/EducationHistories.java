
package vn.com.tpf.microservices.models.Finnone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for educationHistories complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="educationHistories">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="qualificationType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="institute" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="board" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="courseEndDate" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType" minOccurs="0"/>
 *         &lt;element name="yearOfPassing" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="numberOfAttempts" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType"/>
 *         &lt;element name="score" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="scholorship" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "educationHistories", propOrder = {
    "qualificationType",
    "institute",
    "board",
    "courseEndDate",
    "yearOfPassing",
    "numberOfAttempts",
    "score",
    "scholorship"
})
public class EducationHistories {

    protected String qualificationType;
    protected String institute;
    protected String board;
    protected XMLGregorianCalendar courseEndDate;
    protected Integer yearOfPassing;
    protected int numberOfAttempts;
    protected Integer score;
    protected String scholorship;

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
     * Gets the value of the institute property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstitute() {
        return institute;
    }

    /**
     * Sets the value of the institute property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstitute(String value) {
        this.institute = value;
    }

    /**
     * Gets the value of the board property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBoard() {
        return board;
    }

    /**
     * Sets the value of the board property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBoard(String value) {
        this.board = value;
    }

    /**
     * Gets the value of the courseEndDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCourseEndDate() {
        return courseEndDate;
    }

    /**
     * Sets the value of the courseEndDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCourseEndDate(XMLGregorianCalendar value) {
        this.courseEndDate = value;
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
     * Gets the value of the numberOfAttempts property.
     * 
     */
    public int getNumberOfAttempts() {
        return numberOfAttempts;
    }

    /**
     * Sets the value of the numberOfAttempts property.
     * 
     */
    public void setNumberOfAttempts(int value) {
        this.numberOfAttempts = value;
    }

    /**
     * Gets the value of the score property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getScore() {
        return score;
    }

    /**
     * Sets the value of the score property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setScore(Integer value) {
        this.score = value;
    }

    /**
     * Gets the value of the scholorship property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScholorship() {
        return scholorship;
    }

    /**
     * Sets the value of the scholorship property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScholorship(String value) {
        this.scholorship = value;
    }

}
