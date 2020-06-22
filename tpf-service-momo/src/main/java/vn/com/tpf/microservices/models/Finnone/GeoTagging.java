
package vn.com.tpf.microservices.models.Finnone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for geoTagging complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="geoTagging">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="latitude" type="{http://schema.base.ws.pro.finnone.nucleus.com}DoubleType"/>
 *         &lt;element name="longitude" type="{http://schema.base.ws.pro.finnone.nucleus.com}DoubleType"/>
 *         &lt;element name="altitude" type="{http://schema.base.ws.pro.finnone.nucleus.com}DoubleType"/>
 *         &lt;element name="speed" type="{http://schema.base.ws.pro.finnone.nucleus.com}DoubleType"/>
 *         &lt;element name="accuracy" type="{http://schema.base.ws.pro.finnone.nucleus.com}DoubleType"/>
 *         &lt;element name="provider" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0255Type"/>
 *         &lt;element name="timeStamp" type="{http://schema.base.ws.pro.finnone.nucleus.com}DateType"/>
 *         &lt;element name="locationAddress" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0255Type"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "geoTagging", propOrder = {
    "latitude",
    "longitude",
    "altitude",
    "speed",
    "accuracy",
    "provider",
    "timeStamp",
    "locationAddress"
})
public class GeoTagging {

    protected double latitude;
    protected double longitude;
    protected double altitude;
    protected double speed;
    protected double accuracy;
    @XmlElement(required = true)
    protected String provider;
    @XmlElement(required = true)
    protected XMLGregorianCalendar timeStamp;
    @XmlElement(required = true)
    protected String locationAddress;

    /**
     * Gets the value of the latitude property.
     * 
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Sets the value of the latitude property.
     * 
     */
    public void setLatitude(double value) {
        this.latitude = value;
    }

    /**
     * Gets the value of the longitude property.
     * 
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets the value of the longitude property.
     * 
     */
    public void setLongitude(double value) {
        this.longitude = value;
    }

    /**
     * Gets the value of the altitude property.
     * 
     */
    public double getAltitude() {
        return altitude;
    }

    /**
     * Sets the value of the altitude property.
     * 
     */
    public void setAltitude(double value) {
        this.altitude = value;
    }

    /**
     * Gets the value of the speed property.
     * 
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Sets the value of the speed property.
     * 
     */
    public void setSpeed(double value) {
        this.speed = value;
    }

    /**
     * Gets the value of the accuracy property.
     * 
     */
    public double getAccuracy() {
        return accuracy;
    }

    /**
     * Sets the value of the accuracy property.
     * 
     */
    public void setAccuracy(double value) {
        this.accuracy = value;
    }

    /**
     * Gets the value of the provider property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProvider() {
        return provider;
    }

    /**
     * Sets the value of the provider property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProvider(String value) {
        this.provider = value;
    }

    /**
     * Gets the value of the timeStamp property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTimeStamp() {
        return timeStamp;
    }

    /**
     * Sets the value of the timeStamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTimeStamp(XMLGregorianCalendar value) {
        this.timeStamp = value;
    }

    /**
     * Gets the value of the locationAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocationAddress() {
        return locationAddress;
    }

    /**
     * Sets the value of the locationAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocationAddress(String value) {
        this.locationAddress = value;
    }

}
