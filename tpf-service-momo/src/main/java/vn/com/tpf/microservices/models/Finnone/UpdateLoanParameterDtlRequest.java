
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
 *         &lt;element name="loanParameterDetail" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}loanParameterDetail"/>
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
    "loanParameterDetail"
})
@XmlRootElement(name = "updateLoanParameterDtlRequest")
public class UpdateLoanParameterDtlRequest {

    @XmlElement(required = true)
    protected AppParameter appParameter;
    @XmlElement(required = true)
    protected LoanParameterDetail loanParameterDetail;

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
     * Gets the value of the loanParameterDetail property.
     * 
     * @return
     *     possible object is
     *     {@link LoanParameterDetail }
     *     
     */
    public LoanParameterDetail getLoanParameterDetail() {
        return loanParameterDetail;
    }

    /**
     * Sets the value of the loanParameterDetail property.
     * 
     * @param value
     *     allowed object is
     *     {@link LoanParameterDetail }
     *     
     */
    public void setLoanParameterDetail(LoanParameterDetail value) {
        this.loanParameterDetail = value;
    }

}
