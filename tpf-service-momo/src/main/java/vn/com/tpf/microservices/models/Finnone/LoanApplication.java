
package vn.com.tpf.microservices.models.Finnone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for loanApplication complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="loanApplication">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="loanBranch" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "loanApplication", propOrder = {
    "loanBranch"
})
public class LoanApplication {

    @XmlElement(required = true)
    protected String loanBranch;

    /**
     * Gets the value of the loanBranch property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLoanBranch() {
        return loanBranch;
    }

    /**
     * Sets the value of the loanBranch property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLoanBranch(String value) {
        this.loanBranch = value;
    }

}
