
package vn.com.tpf.microservices.models.Finnone;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for communicationDetails complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="communicationDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="phoneNumber" type="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}phoneNumber" maxOccurs="4"/>
 *         &lt;element name="primaryEmailId" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="officialEmailId" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="preferredLanguage" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="preferredModeOfCommunication" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="allowPromotionalCalls" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "communicationDetails", propOrder = { "phoneNumber", "primaryEmailId", "officialEmailId",
		"preferredLanguage", "preferredModeOfCommunication", "allowPromotionalCalls" })
public class CommunicationDetails {

	@XmlElement(required = true)
	protected List<PhoneNumber> phoneNumber;
	protected String primaryEmailId;
	protected String officialEmailId;
	protected String preferredLanguage;
	protected String preferredModeOfCommunication;
	protected Integer allowPromotionalCalls;

	/**
	 * Gets the value of the phoneNumber property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot.
	 * Therefore any modification you make to the returned list will be present
	 * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
	 * for the phoneNumber property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getPhoneNumber().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link PhoneNumber }
	 * 
	 * 
	 */

	public void setPhoneNumber(List<PhoneNumber> phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public List<PhoneNumber> getPhoneNumber() {
		if (phoneNumber == null) {
			phoneNumber = new ArrayList<PhoneNumber>();
		}
		return this.phoneNumber;
	}

	/**
	 * Gets the value of the primaryEmailId property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPrimaryEmailId() {
		return primaryEmailId;
	}

	/**
	 * Sets the value of the primaryEmailId property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setPrimaryEmailId(String value) {
		this.primaryEmailId = value;
	}

	/**
	 * Gets the value of the officialEmailId property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getOfficialEmailId() {
		return officialEmailId;
	}

	/**
	 * Sets the value of the officialEmailId property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setOfficialEmailId(String value) {
		this.officialEmailId = value;
	}

	/**
	 * Gets the value of the preferredLanguage property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPreferredLanguage() {
		return preferredLanguage;
	}

	/**
	 * Sets the value of the preferredLanguage property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setPreferredLanguage(String value) {
		this.preferredLanguage = value;
	}

	/**
	 * Gets the value of the preferredModeOfCommunication property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPreferredModeOfCommunication() {
		return preferredModeOfCommunication;
	}

	/**
	 * Sets the value of the preferredModeOfCommunication property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setPreferredModeOfCommunication(String value) {
		this.preferredModeOfCommunication = value;
	}

	/**
	 * Gets the value of the allowPromotionalCalls property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getAllowPromotionalCalls() {
		return allowPromotionalCalls;
	}

	/**
	 * Sets the value of the allowPromotionalCalls property.
	 * 
	 * @param value allowed object is {@link Integer }
	 * 
	 */
	public void setAllowPromotionalCalls(Integer value) {
		this.allowPromotionalCalls = value;
	}

}
