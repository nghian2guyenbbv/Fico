
package vn.com.tpf.microservices.models.Finnone;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="appParameter" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}appParameter"/>
 *         &lt;element name="partyRelationship" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="partyRole" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType"/>
 *         &lt;element name="customerType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="isExistingCustomer" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType"/>
 *         &lt;element name="customerParameter" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}customerParameter"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "appParameter",
    "partyRelationship",
    "partyRole",
    "customerType",
    "isExistingCustomer",
    "customerParameter"
})
@XmlRootElement(name = "addExistingCustomerRequest")
public class AddExistingCustomerRequest {

    @XmlElement(required = true)
    protected AppParameter appParameter;
    @XmlElement(required = true)
    protected String partyRelationship;
    protected int partyRole;
    @XmlElement(required = true)
    protected String customerType;
    protected int isExistingCustomer;
    @XmlElement(required = true)
    protected CustomerParameter customerParameter;

    /**
     * Gets the value of the appParameter property.
     *
     * @return
     *     possible object is
     *     {@link AppParameter }
     *
     */
    public AppParameter getAppParameter() {
        return appParameter;
    }

    /**
     * Sets the value of the appParameter property.
     *
     * @param value
     *     allowed object is
     *     {@link AppParameter }
     *
     */
    public void setAppParameter(AppParameter value) {
        this.appParameter = value;
    }

    /**
     * Gets the value of the partyRelationship property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPartyRelationship() {
        return partyRelationship;
    }

    /**
     * Sets the value of the partyRelationship property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPartyRelationship(String value) {
        this.partyRelationship = value;
    }

    /**
     * Gets the value of the partyRole property.
     *
     */
    public int getPartyRole() {
        return partyRole;
    }

    /**
     * Sets the value of the partyRole property.
     *
     */
    public void setPartyRole(int value) {
        this.partyRole = value;
    }

    /**
     * Gets the value of the customerType property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCustomerType() {
        return customerType;
    }

    /**
     * Sets the value of the customerType property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCustomerType(String value) {
        this.customerType = value;
    }

    /**
     * Gets the value of the isExistingCustomer property.
     *
     */
    public int getIsExistingCustomer() {
        return isExistingCustomer;
    }

    /**
     * Sets the value of the isExistingCustomer property.
     *
     */
    public void setIsExistingCustomer(int value) {
        this.isExistingCustomer = value;
    }

    /**
     * Gets the value of the customerParameter property.
     *
     * @return
     *     possible object is
     *     {@link CustomerParameter }
     *
     */
    public CustomerParameter getCustomerParameter() {
        return customerParameter;
    }

    /**
     * Sets the value of the customerParameter property.
     *
     * @param value
     *     allowed object is
     *     {@link CustomerParameter }
     *
     */
    public void setCustomerParameter(CustomerParameter value) {
        this.customerParameter = value;
    }

}
