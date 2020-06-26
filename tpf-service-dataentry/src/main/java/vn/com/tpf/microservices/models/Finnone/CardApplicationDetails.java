
package vn.com.tpf.microservices.models.Finnone;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for cardApplicationDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="cardApplicationDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="applicationFormNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="dateOfReceipt" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType" minOccurs="0"/>
 *         &lt;element name="applicationCreationDate" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType" minOccurs="0"/>
 *         &lt;element name="officer" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="dsa" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="dse" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="employeeName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="employeeNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="connectorCode" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="dseCode" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cardApplicationDetails", propOrder = {
    "applicationFormNumber",
    "dateOfReceipt",
    "applicationCreationDate",
    "officer",
    "dsa",
    "dse",
    "employeeName",
    "employeeNumber",
    "connectorCode",
    "dseCode"
})
public class CardApplicationDetails {

    protected String applicationFormNumber;
    protected XMLGregorianCalendar dateOfReceipt;
    protected XMLGregorianCalendar applicationCreationDate;
    @XmlElement(required = true)
    protected String officer;
    protected String dsa;
    protected String dse;
    protected String employeeName;
    protected String employeeNumber;
    protected String connectorCode;
    protected String dseCode;

    /**
     * Gets the value of the applicationFormNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApplicationFormNumber() {
        return applicationFormNumber;
    }

    /**
     * Sets the value of the applicationFormNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApplicationFormNumber(String value) {
        this.applicationFormNumber = value;
    }

    /**
     * Gets the value of the dateOfReceipt property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateOfReceipt() {
        return dateOfReceipt;
    }

    /**
     * Sets the value of the dateOfReceipt property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateOfReceipt(XMLGregorianCalendar value) {
        this.dateOfReceipt = value;
    }

    /**
     * Gets the value of the applicationCreationDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getApplicationCreationDate() {
        return applicationCreationDate;
    }

    /**
     * Sets the value of the applicationCreationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setApplicationCreationDate(XMLGregorianCalendar value) {
        this.applicationCreationDate = value;
    }

    /**
     * Gets the value of the officer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOfficer() {
        return officer;
    }

    /**
     * Sets the value of the officer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOfficer(String value) {
        this.officer = value;
    }

    /**
     * Gets the value of the dsa property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDsa() {
        return dsa;
    }

    /**
     * Sets the value of the dsa property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDsa(String value) {
        this.dsa = value;
    }

    /**
     * Gets the value of the dse property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDse() {
        return dse;
    }

    /**
     * Sets the value of the dse property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDse(String value) {
        this.dse = value;
    }

    /**
     * Gets the value of the employeeName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmployeeName() {
        return employeeName;
    }

    /**
     * Sets the value of the employeeName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmployeeName(String value) {
        this.employeeName = value;
    }

    /**
     * Gets the value of the employeeNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmployeeNumber() {
        return employeeNumber;
    }

    /**
     * Sets the value of the employeeNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmployeeNumber(String value) {
        this.employeeNumber = value;
    }

    /**
     * Gets the value of the connectorCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConnectorCode() {
        return connectorCode;
    }

    /**
     * Sets the value of the connectorCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConnectorCode(String value) {
        this.connectorCode = value;
    }

    /**
     * Gets the value of the dseCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDseCode() {
        return dseCode;
    }

    /**
     * Sets the value of the dseCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDseCode(String value) {
        this.dseCode = value;
    }

}
