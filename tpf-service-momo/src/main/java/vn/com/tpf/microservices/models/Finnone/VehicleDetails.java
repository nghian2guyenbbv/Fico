
package vn.com.tpf.microservices.models.Finnone;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for vehicleDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="vehicleDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="chassisNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="engineNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="fipNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="delieveryDate" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType" minOccurs="0"/>
 *         &lt;element name="registrationNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="registrationDate" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType" minOccurs="0"/>
 *         &lt;element name="registrationNameOf" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="registrationExpiryDate" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType" minOccurs="0"/>
 *         &lt;element name="rcRecievedDate" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType" minOccurs="0"/>
 *         &lt;element name="yearOfManufacturer" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="color" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="vehicleCapacity" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="assetLife" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="assetAge" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="fuelPumpNo" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="odometerReading" type="{http://schema.base.ws.pro.finnone.nucleus.com}DecimalType" minOccurs="0"/>
 *         &lt;element name="hourMeterReading" type="{http://schema.base.ws.pro.finnone.nucleus.com}LongType" minOccurs="0"/>
 *         &lt;element name="minutesMeterReading" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="assetNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="registrationTransportOffice" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "vehicleDetails", propOrder = {
    "chassisNumber",
    "engineNumber",
    "fipNumber",
    "delieveryDate",
    "registrationNumber",
    "registrationDate",
    "registrationNameOf",
    "registrationExpiryDate",
    "rcRecievedDate",
    "yearOfManufacturer",
    "color",
    "vehicleCapacity",
    "assetLife",
    "assetAge",
    "fuelPumpNo",
    "odometerReading",
    "hourMeterReading",
    "minutesMeterReading",
    "assetNumber",
    "registrationTransportOffice"
})
public class VehicleDetails {

    protected String chassisNumber;
    protected String engineNumber;
    protected String fipNumber;
    protected XMLGregorianCalendar delieveryDate;
    protected String registrationNumber;
    protected XMLGregorianCalendar registrationDate;
    protected String registrationNameOf;
    protected XMLGregorianCalendar registrationExpiryDate;
    protected XMLGregorianCalendar rcRecievedDate;
    protected Integer yearOfManufacturer;
    protected String color;
    protected Integer vehicleCapacity;
    protected Integer assetLife;
    protected Integer assetAge;
    protected String fuelPumpNo;
    protected BigDecimal odometerReading;
    protected Long hourMeterReading;
    protected Integer minutesMeterReading;
    protected String assetNumber;
    protected String registrationTransportOffice;

    /**
     * Gets the value of the chassisNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChassisNumber() {
        return chassisNumber;
    }

    /**
     * Sets the value of the chassisNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChassisNumber(String value) {
        this.chassisNumber = value;
    }

    /**
     * Gets the value of the engineNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEngineNumber() {
        return engineNumber;
    }

    /**
     * Sets the value of the engineNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEngineNumber(String value) {
        this.engineNumber = value;
    }

    /**
     * Gets the value of the fipNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFipNumber() {
        return fipNumber;
    }

    /**
     * Sets the value of the fipNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFipNumber(String value) {
        this.fipNumber = value;
    }

    /**
     * Gets the value of the delieveryDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDelieveryDate() {
        return delieveryDate;
    }

    /**
     * Sets the value of the delieveryDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDelieveryDate(XMLGregorianCalendar value) {
        this.delieveryDate = value;
    }

    /**
     * Gets the value of the registrationNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegistrationNumber() {
        return registrationNumber;
    }

    /**
     * Sets the value of the registrationNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegistrationNumber(String value) {
        this.registrationNumber = value;
    }

    /**
     * Gets the value of the registrationDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getRegistrationDate() {
        return registrationDate;
    }

    /**
     * Sets the value of the registrationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setRegistrationDate(XMLGregorianCalendar value) {
        this.registrationDate = value;
    }

    /**
     * Gets the value of the registrationNameOf property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegistrationNameOf() {
        return registrationNameOf;
    }

    /**
     * Sets the value of the registrationNameOf property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegistrationNameOf(String value) {
        this.registrationNameOf = value;
    }

    /**
     * Gets the value of the registrationExpiryDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getRegistrationExpiryDate() {
        return registrationExpiryDate;
    }

    /**
     * Sets the value of the registrationExpiryDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setRegistrationExpiryDate(XMLGregorianCalendar value) {
        this.registrationExpiryDate = value;
    }

    /**
     * Gets the value of the rcRecievedDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getRcRecievedDate() {
        return rcRecievedDate;
    }

    /**
     * Sets the value of the rcRecievedDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setRcRecievedDate(XMLGregorianCalendar value) {
        this.rcRecievedDate = value;
    }

    /**
     * Gets the value of the yearOfManufacturer property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getYearOfManufacturer() {
        return yearOfManufacturer;
    }

    /**
     * Sets the value of the yearOfManufacturer property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setYearOfManufacturer(Integer value) {
        this.yearOfManufacturer = value;
    }

    /**
     * Gets the value of the color property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets the value of the color property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setColor(String value) {
        this.color = value;
    }

    /**
     * Gets the value of the vehicleCapacity property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getVehicleCapacity() {
        return vehicleCapacity;
    }

    /**
     * Sets the value of the vehicleCapacity property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setVehicleCapacity(Integer value) {
        this.vehicleCapacity = value;
    }

    /**
     * Gets the value of the assetLife property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAssetLife() {
        return assetLife;
    }

    /**
     * Sets the value of the assetLife property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAssetLife(Integer value) {
        this.assetLife = value;
    }

    /**
     * Gets the value of the assetAge property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAssetAge() {
        return assetAge;
    }

    /**
     * Sets the value of the assetAge property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAssetAge(Integer value) {
        this.assetAge = value;
    }

    /**
     * Gets the value of the fuelPumpNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFuelPumpNo() {
        return fuelPumpNo;
    }

    /**
     * Sets the value of the fuelPumpNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFuelPumpNo(String value) {
        this.fuelPumpNo = value;
    }

    /**
     * Gets the value of the odometerReading property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getOdometerReading() {
        return odometerReading;
    }

    /**
     * Sets the value of the odometerReading property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setOdometerReading(BigDecimal value) {
        this.odometerReading = value;
    }

    /**
     * Gets the value of the hourMeterReading property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getHourMeterReading() {
        return hourMeterReading;
    }

    /**
     * Sets the value of the hourMeterReading property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setHourMeterReading(Long value) {
        this.hourMeterReading = value;
    }

    /**
     * Gets the value of the minutesMeterReading property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMinutesMeterReading() {
        return minutesMeterReading;
    }

    /**
     * Sets the value of the minutesMeterReading property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMinutesMeterReading(Integer value) {
        this.minutesMeterReading = value;
    }

    /**
     * Gets the value of the assetNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssetNumber() {
        return assetNumber;
    }

    /**
     * Sets the value of the assetNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssetNumber(String value) {
        this.assetNumber = value;
    }

    /**
     * Gets the value of the registrationTransportOffice property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegistrationTransportOffice() {
        return registrationTransportOffice;
    }

    /**
     * Sets the value of the registrationTransportOffice property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegistrationTransportOffice(String value) {
        this.registrationTransportOffice = value;
    }

}
