
package vn.com.tpf.microservices.models.Finnone;

import vn.com.tpf.microservices.models.Finnone.cas.MoneyType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;



/**
 * <p>Java class for regionalData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="regionalData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="regionalField1" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="regionalField2" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="regionalField3" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="regionalField4" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="regionalField5" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="regionalField6" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="regionalField7" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="regionalField8" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="regionalField9" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="regionalField10" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="regionalField11" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="regionalField12" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="regionalField13" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="regionalField14" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="regionalField15" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="regionalField16" type="{http://schema.base.ws.pro.finnone.nucleus.com}LongType" minOccurs="0"/>
 *         &lt;element name="regionalField17" type="{http://schema.base.ws.pro.finnone.nucleus.com}LongType" minOccurs="0"/>
 *         &lt;element name="regionalField18" type="{http://schema.base.ws.pro.finnone.nucleus.com}LongType" minOccurs="0"/>
 *         &lt;element name="regionalField19" type="{http://schema.base.ws.pro.finnone.nucleus.com}LongType" minOccurs="0"/>
 *         &lt;element name="regionalField20" type="{http://schema.base.ws.pro.finnone.nucleus.com}LongType" minOccurs="0"/>
 *         &lt;element name="regionalField21" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateTimeType" minOccurs="0"/>
 *         &lt;element name="regionalField22" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateTimeType" minOccurs="0"/>
 *         &lt;element name="regionalField23" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateTimeType" minOccurs="0"/>
 *         &lt;element name="regionalField24" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateTimeType" minOccurs="0"/>
 *         &lt;element name="regionalField25" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateTimeType" minOccurs="0"/>
 *         &lt;element name="regionalField26" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="regionalField27" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="regionalField28" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="regionalField29" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="regionalField30" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "regionalData", propOrder = {
    "regionalField1",
    "regionalField2",
    "regionalField3",
    "regionalField4",
    "regionalField5",
    "regionalField6",
    "regionalField7",
    "regionalField8",
    "regionalField9",
    "regionalField10",
    "regionalField11",
    "regionalField12",
    "regionalField13",
    "regionalField14",
    "regionalField15",
    "regionalField16",
    "regionalField17",
    "regionalField18",
    "regionalField19",
    "regionalField20",
    "regionalField21",
    "regionalField22",
    "regionalField23",
    "regionalField24",
    "regionalField25",
    "regionalField26",
    "regionalField27",
    "regionalField28",
    "regionalField29",
    "regionalField30"
})
public class RegionalData {

    protected String regionalField1;
    protected String regionalField2;
    protected String regionalField3;
    protected String regionalField4;
    protected String regionalField5;
    protected String regionalField6;
    protected String regionalField7;
    protected String regionalField8;
    protected String regionalField9;
    protected String regionalField10;
    protected Integer regionalField11;
    protected Integer regionalField12;
    protected Integer regionalField13;
    protected Integer regionalField14;
    protected Integer regionalField15;
    protected Long regionalField16;
    protected Long regionalField17;
    protected Long regionalField18;
    protected Long regionalField19;
    protected Long regionalField20;
    protected XMLGregorianCalendar regionalField21;
    protected XMLGregorianCalendar regionalField22;
    protected XMLGregorianCalendar regionalField23;
    protected XMLGregorianCalendar regionalField24;
    protected XMLGregorianCalendar regionalField25;
    protected MoneyType regionalField26;
    protected MoneyType regionalField27;
    protected MoneyType regionalField28;
    protected MoneyType regionalField29;
    protected MoneyType regionalField30;

    /**
     * Gets the value of the regionalField1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegionalField1() {
        return regionalField1;
    }

    /**
     * Sets the value of the regionalField1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegionalField1(String value) {
        this.regionalField1 = value;
    }

    /**
     * Gets the value of the regionalField2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegionalField2() {
        return regionalField2;
    }

    /**
     * Sets the value of the regionalField2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegionalField2(String value) {
        this.regionalField2 = value;
    }

    /**
     * Gets the value of the regionalField3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegionalField3() {
        return regionalField3;
    }

    /**
     * Sets the value of the regionalField3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegionalField3(String value) {
        this.regionalField3 = value;
    }

    /**
     * Gets the value of the regionalField4 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegionalField4() {
        return regionalField4;
    }

    /**
     * Sets the value of the regionalField4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegionalField4(String value) {
        this.regionalField4 = value;
    }

    /**
     * Gets the value of the regionalField5 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegionalField5() {
        return regionalField5;
    }

    /**
     * Sets the value of the regionalField5 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegionalField5(String value) {
        this.regionalField5 = value;
    }

    /**
     * Gets the value of the regionalField6 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegionalField6() {
        return regionalField6;
    }

    /**
     * Sets the value of the regionalField6 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegionalField6(String value) {
        this.regionalField6 = value;
    }

    /**
     * Gets the value of the regionalField7 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegionalField7() {
        return regionalField7;
    }

    /**
     * Sets the value of the regionalField7 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegionalField7(String value) {
        this.regionalField7 = value;
    }

    /**
     * Gets the value of the regionalField8 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegionalField8() {
        return regionalField8;
    }

    /**
     * Sets the value of the regionalField8 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegionalField8(String value) {
        this.regionalField8 = value;
    }

    /**
     * Gets the value of the regionalField9 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegionalField9() {
        return regionalField9;
    }

    /**
     * Sets the value of the regionalField9 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegionalField9(String value) {
        this.regionalField9 = value;
    }

    /**
     * Gets the value of the regionalField10 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegionalField10() {
        return regionalField10;
    }

    /**
     * Sets the value of the regionalField10 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegionalField10(String value) {
        this.regionalField10 = value;
    }

    /**
     * Gets the value of the regionalField11 property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getRegionalField11() {
        return regionalField11;
    }

    /**
     * Sets the value of the regionalField11 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRegionalField11(Integer value) {
        this.regionalField11 = value;
    }

    /**
     * Gets the value of the regionalField12 property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getRegionalField12() {
        return regionalField12;
    }

    /**
     * Sets the value of the regionalField12 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRegionalField12(Integer value) {
        this.regionalField12 = value;
    }

    /**
     * Gets the value of the regionalField13 property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getRegionalField13() {
        return regionalField13;
    }

    /**
     * Sets the value of the regionalField13 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRegionalField13(Integer value) {
        this.regionalField13 = value;
    }

    /**
     * Gets the value of the regionalField14 property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getRegionalField14() {
        return regionalField14;
    }

    /**
     * Sets the value of the regionalField14 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRegionalField14(Integer value) {
        this.regionalField14 = value;
    }

    /**
     * Gets the value of the regionalField15 property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getRegionalField15() {
        return regionalField15;
    }

    /**
     * Sets the value of the regionalField15 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRegionalField15(Integer value) {
        this.regionalField15 = value;
    }

    /**
     * Gets the value of the regionalField16 property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getRegionalField16() {
        return regionalField16;
    }

    /**
     * Sets the value of the regionalField16 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setRegionalField16(Long value) {
        this.regionalField16 = value;
    }

    /**
     * Gets the value of the regionalField17 property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getRegionalField17() {
        return regionalField17;
    }

    /**
     * Sets the value of the regionalField17 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setRegionalField17(Long value) {
        this.regionalField17 = value;
    }

    /**
     * Gets the value of the regionalField18 property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getRegionalField18() {
        return regionalField18;
    }

    /**
     * Sets the value of the regionalField18 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setRegionalField18(Long value) {
        this.regionalField18 = value;
    }

    /**
     * Gets the value of the regionalField19 property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getRegionalField19() {
        return regionalField19;
    }

    /**
     * Sets the value of the regionalField19 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setRegionalField19(Long value) {
        this.regionalField19 = value;
    }

    /**
     * Gets the value of the regionalField20 property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getRegionalField20() {
        return regionalField20;
    }

    /**
     * Sets the value of the regionalField20 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setRegionalField20(Long value) {
        this.regionalField20 = value;
    }

    /**
     * Gets the value of the regionalField21 property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getRegionalField21() {
        return regionalField21;
    }

    /**
     * Sets the value of the regionalField21 property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setRegionalField21(XMLGregorianCalendar value) {
        this.regionalField21 = value;
    }

    /**
     * Gets the value of the regionalField22 property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getRegionalField22() {
        return regionalField22;
    }

    /**
     * Sets the value of the regionalField22 property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setRegionalField22(XMLGregorianCalendar value) {
        this.regionalField22 = value;
    }

    /**
     * Gets the value of the regionalField23 property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getRegionalField23() {
        return regionalField23;
    }

    /**
     * Sets the value of the regionalField23 property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setRegionalField23(XMLGregorianCalendar value) {
        this.regionalField23 = value;
    }

    /**
     * Gets the value of the regionalField24 property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getRegionalField24() {
        return regionalField24;
    }

    /**
     * Sets the value of the regionalField24 property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setRegionalField24(XMLGregorianCalendar value) {
        this.regionalField24 = value;
    }

    /**
     * Gets the value of the regionalField25 property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getRegionalField25() {
        return regionalField25;
    }

    /**
     * Sets the value of the regionalField25 property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setRegionalField25(XMLGregorianCalendar value) {
        this.regionalField25 = value;
    }

    /**
     * Gets the value of the regionalField26 property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getRegionalField26() {
        return regionalField26;
    }

    /**
     * Sets the value of the regionalField26 property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setRegionalField26(MoneyType value) {
        this.regionalField26 = value;
    }

    /**
     * Gets the value of the regionalField27 property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getRegionalField27() {
        return regionalField27;
    }

    /**
     * Sets the value of the regionalField27 property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setRegionalField27(MoneyType value) {
        this.regionalField27 = value;
    }

    /**
     * Gets the value of the regionalField28 property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getRegionalField28() {
        return regionalField28;
    }

    /**
     * Sets the value of the regionalField28 property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setRegionalField28(MoneyType value) {
        this.regionalField28 = value;
    }

    /**
     * Gets the value of the regionalField29 property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getRegionalField29() {
        return regionalField29;
    }

    /**
     * Sets the value of the regionalField29 property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setRegionalField29(MoneyType value) {
        this.regionalField29 = value;
    }

    /**
     * Gets the value of the regionalField30 property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getRegionalField30() {
        return regionalField30;
    }

    /**
     * Sets the value of the regionalField30 property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setRegionalField30(MoneyType value) {
        this.regionalField30 = value;
    }

}
