
package vn.com.tpf.microservices.models.Finnone;

import vn.com.tpf.microservices.models.Finnone.cas.MoneyType;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;



/**
 * <p>Java class for invoiceDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="invoiceDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="invoiceNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="invoiceDate" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType" minOccurs="0"/>
 *         &lt;element name="invoiceValue" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="hypothentication" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="invoicetype" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
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
@XmlType(name = "invoiceDetails", propOrder = {
    "invoiceNumber",
    "invoiceDate",
    "invoiceValue",
    "hypothentication",
    "invoicetype",
    "dynamicFormDetails"
})
public class InvoiceDetails {

    @XmlElement(required = true)
    protected String invoiceNumber;
    protected XMLGregorianCalendar invoiceDate;
    protected MoneyType invoiceValue;
    protected String hypothentication;
    @XmlElement(required = true)
    protected String invoicetype;
    protected List<DynamicFormDetails> dynamicFormDetails;

    /**
     * Gets the value of the invoiceNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    /**
     * Sets the value of the invoiceNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInvoiceNumber(String value) {
        this.invoiceNumber = value;
    }

    /**
     * Gets the value of the invoiceDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getInvoiceDate() {
        return invoiceDate;
    }

    /**
     * Sets the value of the invoiceDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setInvoiceDate(XMLGregorianCalendar value) {
        this.invoiceDate = value;
    }

    /**
     * Gets the value of the invoiceValue property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getInvoiceValue() {
        return invoiceValue;
    }

    /**
     * Sets the value of the invoiceValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setInvoiceValue(MoneyType value) {
        this.invoiceValue = value;
    }

    /**
     * Gets the value of the hypothentication property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHypothentication() {
        return hypothentication;
    }

    /**
     * Sets the value of the hypothentication property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHypothentication(String value) {
        this.hypothentication = value;
    }

    /**
     * Gets the value of the invoicetype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInvoicetype() {
        return invoicetype;
    }

    /**
     * Sets the value of the invoicetype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInvoicetype(String value) {
        this.invoicetype = value;
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
