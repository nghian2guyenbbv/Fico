
package vn.com.tpf.microservices.models.Finnone;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for yearlyIncomeDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="yearlyIncomeDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="year1" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType"/>
 *         &lt;element name="year2" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType"/>
 *         &lt;element name="year3" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType"/>
 *         &lt;element name="yearDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}yearDetails" maxOccurs="10"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "yearlyIncomeDetails", propOrder = {
    "year1",
    "year2",
    "year3",
    "yearDetails"
})
public class YearlyIncomeDetails {

    protected int year1;
    protected int year2;
    protected int year3;
    @XmlElement(required = true)
    protected List<YearDetails> yearDetails;

    /**
     * Gets the value of the year1 property.
     * 
     */
    public int getYear1() {
        return year1;
    }

    /**
     * Sets the value of the year1 property.
     * 
     */
    public void setYear1(int value) {
        this.year1 = value;
    }

    /**
     * Gets the value of the year2 property.
     * 
     */
    public int getYear2() {
        return year2;
    }

    /**
     * Sets the value of the year2 property.
     * 
     */
    public void setYear2(int value) {
        this.year2 = value;
    }

    /**
     * Gets the value of the year3 property.
     * 
     */
    public int getYear3() {
        return year3;
    }

    /**
     * Sets the value of the year3 property.
     * 
     */
    public void setYear3(int value) {
        this.year3 = value;
    }

    /**
     * Gets the value of the yearDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the yearDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getYearDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link YearDetails }
     * 
     * 
     */
    public List<YearDetails> getYearDetails() {
        if (yearDetails == null) {
            yearDetails = new ArrayList<YearDetails>();
        }
        return this.yearDetails;
    }

}
