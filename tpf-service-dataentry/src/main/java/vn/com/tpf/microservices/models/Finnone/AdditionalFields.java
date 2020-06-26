
package vn.com.tpf.microservices.models.Finnone;
import vn.com.tpf.microservices.models.Finnone.cas.MoneyType;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;



/**
 * <p>Java class for additionalFields complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="additionalFields">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="additionalField1" type="{http://schema.base.ws.pro.finnone.nucleus.com}DecimalType" minOccurs="0"/>
 *         &lt;element name="additionalField2" type="{http://schema.base.ws.pro.finnone.nucleus.com}DecimalType" minOccurs="0"/>
 *         &lt;element name="additionalField3" type="{http://schema.base.ws.pro.finnone.nucleus.com}DecimalType" minOccurs="0"/>
 *         &lt;element name="additionalField4" type="{http://schema.base.ws.pro.finnone.nucleus.com}DecimalType" minOccurs="0"/>
 *         &lt;element name="additionalField5" type="{http://schema.base.ws.pro.finnone.nucleus.com}DecimalType" minOccurs="0"/>
 *         &lt;element name="additionalField6" type="{http://schema.base.ws.pro.finnone.nucleus.com}DecimalType" minOccurs="0"/>
 *         &lt;element name="additionalField7" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0255Type" minOccurs="0"/>
 *         &lt;element name="additionalField8" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0255Type" minOccurs="0"/>
 *         &lt;element name="additionalField9" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0255Type" minOccurs="0"/>
 *         &lt;element name="additionalField10" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0255Type" minOccurs="0"/>
 *         &lt;element name="additionalField11" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0255Type" minOccurs="0"/>
 *         &lt;element name="additionalField12" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0255Type" minOccurs="0"/>
 *         &lt;element name="additionalField13" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0255Type" minOccurs="0"/>
 *         &lt;element name="additionalField14" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0255Type" minOccurs="0"/>
 *         &lt;element name="additionalField15" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0255Type" minOccurs="0"/>
 *         &lt;element name="additionalField16" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0255Type" minOccurs="0"/>
 *         &lt;element name="additionalField17" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0255Type" minOccurs="0"/>
 *         &lt;element name="additionalField18" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0255Type" minOccurs="0"/>
 *         &lt;element name="additionalField19" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0255Type" minOccurs="0"/>
 *         &lt;element name="additionalField20" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0255Type" minOccurs="0"/>
 *         &lt;element name="additionalField21" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateTimeType" minOccurs="0"/>
 *         &lt;element name="additionalField22" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateTimeType" minOccurs="0"/>
 *         &lt;element name="additionalField23" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateTimeType" minOccurs="0"/>
 *         &lt;element name="additionalField24" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateTimeType" minOccurs="0"/>
 *         &lt;element name="additionalField25" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="additionalField26" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="additionalField27" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="additionalField28" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="additionalField29" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="additionalField30" type="{http://schema.cas.common.base.ws.pro.finnone.nucleus.com}MoneyType" minOccurs="0"/>
 *         &lt;element name="field1N" type="{http://schema.base.ws.pro.finnone.nucleus.com}DecimalType" minOccurs="0"/>
 *         &lt;element name="field2N" type="{http://schema.base.ws.pro.finnone.nucleus.com}DecimalType" minOccurs="0"/>
 *         &lt;element name="field3N" type="{http://schema.base.ws.pro.finnone.nucleus.com}DecimalType" minOccurs="0"/>
 *         &lt;element name="field4N" type="{http://schema.base.ws.pro.finnone.nucleus.com}DecimalType" minOccurs="0"/>
 *         &lt;element name="field5N" type="{http://schema.base.ws.pro.finnone.nucleus.com}DecimalType" minOccurs="0"/>
 *         &lt;element name="field1S" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0255Type" minOccurs="0"/>
 *         &lt;element name="field2S" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0255Type" minOccurs="0"/>
 *         &lt;element name="field3S" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0255Type" minOccurs="0"/>
 *         &lt;element name="field4S" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0255Type" minOccurs="0"/>
 *         &lt;element name="field5S" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0255Type" minOccurs="0"/>
 *         &lt;element name="field6S" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0255Type" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "additionalFields", propOrder = {
    "additionalField1",
    "additionalField2",
    "additionalField3",
    "additionalField4",
    "additionalField5",
    "additionalField6",
    "additionalField7",
    "additionalField8",
    "additionalField9",
    "additionalField10",
    "additionalField11",
    "additionalField12",
    "additionalField13",
    "additionalField14",
    "additionalField15",
    "additionalField16",
    "additionalField17",
    "additionalField18",
    "additionalField19",
    "additionalField20",
    "additionalField21",
    "additionalField22",
    "additionalField23",
    "additionalField24",
    "additionalField25",
    "additionalField26",
    "additionalField27",
    "additionalField28",
    "additionalField29",
    "additionalField30",
    "field1N",
    "field2N",
    "field3N",
    "field4N",
    "field5N",
    "field1S",
    "field2S",
    "field3S",
    "field4S",
    "field5S",
    "field6S"
})
public class AdditionalFields {

    protected BigDecimal additionalField1;
    protected BigDecimal additionalField2;
    protected BigDecimal additionalField3;
    protected BigDecimal additionalField4;
    protected BigDecimal additionalField5;
    protected BigDecimal additionalField6;
    protected String additionalField7;
    protected String additionalField8;
    protected String additionalField9;
    protected String additionalField10;
    protected String additionalField11;
    protected String additionalField12;
    protected String additionalField13;
    protected String additionalField14;
    protected String additionalField15;
    protected String additionalField16;
    protected String additionalField17;
    protected String additionalField18;
    protected String additionalField19;
    protected String additionalField20;
    protected XMLGregorianCalendar additionalField21;
    protected XMLGregorianCalendar additionalField22;
    protected XMLGregorianCalendar additionalField23;
    protected XMLGregorianCalendar additionalField24;
    protected MoneyType additionalField25;
    protected MoneyType additionalField26;
    protected MoneyType additionalField27;
    protected MoneyType additionalField28;
    protected MoneyType additionalField29;
    protected MoneyType additionalField30;
    protected BigDecimal field1N;
    protected BigDecimal field2N;
    protected BigDecimal field3N;
    protected BigDecimal field4N;
    protected BigDecimal field5N;
    protected String field1S;
    protected String field2S;
    protected String field3S;
    protected String field4S;
    protected String field5S;
    protected String field6S;

    /**
     * Gets the value of the additionalField1 property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAdditionalField1() {
        return additionalField1;
    }

    /**
     * Sets the value of the additionalField1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAdditionalField1(BigDecimal value) {
        this.additionalField1 = value;
    }

    /**
     * Gets the value of the additionalField2 property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAdditionalField2() {
        return additionalField2;
    }

    /**
     * Sets the value of the additionalField2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAdditionalField2(BigDecimal value) {
        this.additionalField2 = value;
    }

    /**
     * Gets the value of the additionalField3 property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAdditionalField3() {
        return additionalField3;
    }

    /**
     * Sets the value of the additionalField3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAdditionalField3(BigDecimal value) {
        this.additionalField3 = value;
    }

    /**
     * Gets the value of the additionalField4 property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAdditionalField4() {
        return additionalField4;
    }

    /**
     * Sets the value of the additionalField4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAdditionalField4(BigDecimal value) {
        this.additionalField4 = value;
    }

    /**
     * Gets the value of the additionalField5 property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAdditionalField5() {
        return additionalField5;
    }

    /**
     * Sets the value of the additionalField5 property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAdditionalField5(BigDecimal value) {
        this.additionalField5 = value;
    }

    /**
     * Gets the value of the additionalField6 property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAdditionalField6() {
        return additionalField6;
    }

    /**
     * Sets the value of the additionalField6 property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAdditionalField6(BigDecimal value) {
        this.additionalField6 = value;
    }

    /**
     * Gets the value of the additionalField7 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdditionalField7() {
        return additionalField7;
    }

    /**
     * Sets the value of the additionalField7 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdditionalField7(String value) {
        this.additionalField7 = value;
    }

    /**
     * Gets the value of the additionalField8 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdditionalField8() {
        return additionalField8;
    }

    /**
     * Sets the value of the additionalField8 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdditionalField8(String value) {
        this.additionalField8 = value;
    }

    /**
     * Gets the value of the additionalField9 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdditionalField9() {
        return additionalField9;
    }

    /**
     * Sets the value of the additionalField9 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdditionalField9(String value) {
        this.additionalField9 = value;
    }

    /**
     * Gets the value of the additionalField10 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdditionalField10() {
        return additionalField10;
    }

    /**
     * Sets the value of the additionalField10 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdditionalField10(String value) {
        this.additionalField10 = value;
    }

    /**
     * Gets the value of the additionalField11 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdditionalField11() {
        return additionalField11;
    }

    /**
     * Sets the value of the additionalField11 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdditionalField11(String value) {
        this.additionalField11 = value;
    }

    /**
     * Gets the value of the additionalField12 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdditionalField12() {
        return additionalField12;
    }

    /**
     * Sets the value of the additionalField12 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdditionalField12(String value) {
        this.additionalField12 = value;
    }

    /**
     * Gets the value of the additionalField13 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdditionalField13() {
        return additionalField13;
    }

    /**
     * Sets the value of the additionalField13 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdditionalField13(String value) {
        this.additionalField13 = value;
    }

    /**
     * Gets the value of the additionalField14 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdditionalField14() {
        return additionalField14;
    }

    /**
     * Sets the value of the additionalField14 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdditionalField14(String value) {
        this.additionalField14 = value;
    }

    /**
     * Gets the value of the additionalField15 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdditionalField15() {
        return additionalField15;
    }

    /**
     * Sets the value of the additionalField15 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdditionalField15(String value) {
        this.additionalField15 = value;
    }

    /**
     * Gets the value of the additionalField16 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdditionalField16() {
        return additionalField16;
    }

    /**
     * Sets the value of the additionalField16 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdditionalField16(String value) {
        this.additionalField16 = value;
    }

    /**
     * Gets the value of the additionalField17 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdditionalField17() {
        return additionalField17;
    }

    /**
     * Sets the value of the additionalField17 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdditionalField17(String value) {
        this.additionalField17 = value;
    }

    /**
     * Gets the value of the additionalField18 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdditionalField18() {
        return additionalField18;
    }

    /**
     * Sets the value of the additionalField18 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdditionalField18(String value) {
        this.additionalField18 = value;
    }

    /**
     * Gets the value of the additionalField19 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdditionalField19() {
        return additionalField19;
    }

    /**
     * Sets the value of the additionalField19 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdditionalField19(String value) {
        this.additionalField19 = value;
    }

    /**
     * Gets the value of the additionalField20 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdditionalField20() {
        return additionalField20;
    }

    /**
     * Sets the value of the additionalField20 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdditionalField20(String value) {
        this.additionalField20 = value;
    }

    /**
     * Gets the value of the additionalField21 property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getAdditionalField21() {
        return additionalField21;
    }

    /**
     * Sets the value of the additionalField21 property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setAdditionalField21(XMLGregorianCalendar value) {
        this.additionalField21 = value;
    }

    /**
     * Gets the value of the additionalField22 property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getAdditionalField22() {
        return additionalField22;
    }

    /**
     * Sets the value of the additionalField22 property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setAdditionalField22(XMLGregorianCalendar value) {
        this.additionalField22 = value;
    }

    /**
     * Gets the value of the additionalField23 property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getAdditionalField23() {
        return additionalField23;
    }

    /**
     * Sets the value of the additionalField23 property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setAdditionalField23(XMLGregorianCalendar value) {
        this.additionalField23 = value;
    }

    /**
     * Gets the value of the additionalField24 property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getAdditionalField24() {
        return additionalField24;
    }

    /**
     * Sets the value of the additionalField24 property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setAdditionalField24(XMLGregorianCalendar value) {
        this.additionalField24 = value;
    }

    /**
     * Gets the value of the additionalField25 property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getAdditionalField25() {
        return additionalField25;
    }

    /**
     * Sets the value of the additionalField25 property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setAdditionalField25(MoneyType value) {
        this.additionalField25 = value;
    }

    /**
     * Gets the value of the additionalField26 property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getAdditionalField26() {
        return additionalField26;
    }

    /**
     * Sets the value of the additionalField26 property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setAdditionalField26(MoneyType value) {
        this.additionalField26 = value;
    }

    /**
     * Gets the value of the additionalField27 property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getAdditionalField27() {
        return additionalField27;
    }

    /**
     * Sets the value of the additionalField27 property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setAdditionalField27(MoneyType value) {
        this.additionalField27 = value;
    }

    /**
     * Gets the value of the additionalField28 property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getAdditionalField28() {
        return additionalField28;
    }

    /**
     * Sets the value of the additionalField28 property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setAdditionalField28(MoneyType value) {
        this.additionalField28 = value;
    }

    /**
     * Gets the value of the additionalField29 property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getAdditionalField29() {
        return additionalField29;
    }

    /**
     * Sets the value of the additionalField29 property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setAdditionalField29(MoneyType value) {
        this.additionalField29 = value;
    }

    /**
     * Gets the value of the additionalField30 property.
     * 
     * @return
     *     possible object is
     *     {@link MoneyType }
     *     
     */
    public MoneyType getAdditionalField30() {
        return additionalField30;
    }

    /**
     * Sets the value of the additionalField30 property.
     * 
     * @param value
     *     allowed object is
     *     {@link MoneyType }
     *     
     */
    public void setAdditionalField30(MoneyType value) {
        this.additionalField30 = value;
    }

    /**
     * Gets the value of the field1N property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getField1N() {
        return field1N;
    }

    /**
     * Sets the value of the field1N property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setField1N(BigDecimal value) {
        this.field1N = value;
    }

    /**
     * Gets the value of the field2N property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getField2N() {
        return field2N;
    }

    /**
     * Sets the value of the field2N property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setField2N(BigDecimal value) {
        this.field2N = value;
    }

    /**
     * Gets the value of the field3N property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getField3N() {
        return field3N;
    }

    /**
     * Sets the value of the field3N property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setField3N(BigDecimal value) {
        this.field3N = value;
    }

    /**
     * Gets the value of the field4N property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getField4N() {
        return field4N;
    }

    /**
     * Sets the value of the field4N property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setField4N(BigDecimal value) {
        this.field4N = value;
    }

    /**
     * Gets the value of the field5N property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getField5N() {
        return field5N;
    }

    /**
     * Sets the value of the field5N property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setField5N(BigDecimal value) {
        this.field5N = value;
    }

    /**
     * Gets the value of the field1S property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getField1S() {
        return field1S;
    }

    /**
     * Sets the value of the field1S property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setField1S(String value) {
        this.field1S = value;
    }

    /**
     * Gets the value of the field2S property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getField2S() {
        return field2S;
    }

    /**
     * Sets the value of the field2S property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setField2S(String value) {
        this.field2S = value;
    }

    /**
     * Gets the value of the field3S property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getField3S() {
        return field3S;
    }

    /**
     * Sets the value of the field3S property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setField3S(String value) {
        this.field3S = value;
    }

    /**
     * Gets the value of the field4S property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getField4S() {
        return field4S;
    }

    /**
     * Sets the value of the field4S property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setField4S(String value) {
        this.field4S = value;
    }

    /**
     * Gets the value of the field5S property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getField5S() {
        return field5S;
    }

    /**
     * Sets the value of the field5S property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setField5S(String value) {
        this.field5S = value;
    }

    /**
     * Gets the value of the field6S property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getField6S() {
        return field6S;
    }

    /**
     * Sets the value of the field6S property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setField6S(String value) {
        this.field6S = value;
    }

}
