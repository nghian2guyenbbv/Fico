
package vn.com.tpf.microservices.models.Finnone;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for family complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="family">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="noOfDependents" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType"/>
 *         &lt;element name="noOfDependentChildren" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType"/>
 *         &lt;element name="familyDetails" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}familyDetails" maxOccurs="4" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "family", propOrder = {
    "noOfDependents",
    "noOfDependentChildren",
    "familyDetails"
})
public class Family {

    protected int noOfDependents;

    public void setFamilyDetails(List<FamilyDetails> familyDetails) {
        this.familyDetails = familyDetails;
    }

    protected int noOfDependentChildren;
    protected List<FamilyDetails> familyDetails;

    /**
     * Gets the value of the noOfDependents property.
     * 
     */
    public int getNoOfDependents() {
        return noOfDependents;
    }

    /**
     * Sets the value of the noOfDependents property.
     * 
     */
    public void setNoOfDependents(int value) {
        this.noOfDependents = value;
    }

    /**
     * Gets the value of the noOfDependentChildren property.
     * 
     */
    public int getNoOfDependentChildren() {
        return noOfDependentChildren;
    }

    /**
     * Sets the value of the noOfDependentChildren property.
     * 
     */
    public void setNoOfDependentChildren(int value) {
        this.noOfDependentChildren = value;
    }

    /**
     * Gets the value of the familyDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the familyDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFamilyDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FamilyDetails }
     * 
     * 
     */
    public List<FamilyDetails> getFamilyDetails() {
        if (familyDetails == null) {
            familyDetails = new ArrayList<FamilyDetails>();
        }
        return this.familyDetails;
    }

}
