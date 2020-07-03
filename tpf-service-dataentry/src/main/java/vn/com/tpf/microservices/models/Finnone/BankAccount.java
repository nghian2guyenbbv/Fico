
package vn.com.tpf.microservices.models.Finnone;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for bankAccount complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="bankAccount">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="bankName" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="bankBranch" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="accountNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="modeOfPaymnet" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="subPaymentMode" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="accountType" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *         &lt;element name="defaultAccount" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "bankAccount", propOrder = {
    "bankName",
    "bankBranch",
    "accountNumber",
    "modeOfPaymnet",
    "subPaymentMode",
    "accountType",
    "defaultAccount"
})
public class BankAccount {

    @XmlElement(required = true)
    protected String bankName;
    @XmlElement(required = true)
    protected String bankBranch;
    @XmlElement(required = true)
    protected String accountNumber;
    @XmlElement(required = true)
    protected String modeOfPaymnet;
    @XmlElement(required = true)
    protected String subPaymentMode;
    @XmlElement(required = true)
    protected String accountType;
    protected boolean defaultAccount;

    /**
     * Gets the value of the bankName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBankName() {
        return bankName;
    }

    /**
     * Sets the value of the bankName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBankName(String value) {
        this.bankName = value;
    }

    /**
     * Gets the value of the bankBranch property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBankBranch() {
        return bankBranch;
    }

    /**
     * Sets the value of the bankBranch property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBankBranch(String value) {
        this.bankBranch = value;
    }

    /**
     * Gets the value of the accountNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * Sets the value of the accountNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountNumber(String value) {
        this.accountNumber = value;
    }

    /**
     * Gets the value of the modeOfPaymnet property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModeOfPaymnet() {
        return modeOfPaymnet;
    }

    /**
     * Sets the value of the modeOfPaymnet property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModeOfPaymnet(String value) {
        this.modeOfPaymnet = value;
    }

    /**
     * Gets the value of the subPaymentMode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubPaymentMode() {
        return subPaymentMode;
    }

    /**
     * Sets the value of the subPaymentMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubPaymentMode(String value) {
        this.subPaymentMode = value;
    }

    /**
     * Gets the value of the accountType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountType() {
        return accountType;
    }

    /**
     * Sets the value of the accountType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountType(String value) {
        this.accountType = value;
    }

    /**
     * Gets the value of the defaultAccount property.
     * 
     */
    public boolean isDefaultAccount() {
        return defaultAccount;
    }

    /**
     * Sets the value of the defaultAccount property.
     * 
     */
    public void setDefaultAccount(boolean value) {
        this.defaultAccount = value;
    }

}
