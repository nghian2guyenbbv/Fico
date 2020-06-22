
package vn.com.tpf.microservices.models.Finnone;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for builderDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="builderDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="isBuilderConstructed" type="{http://schema.base.ws.pro.finnone.nucleus.com}BooleanType" minOccurs="0"/>
 *         &lt;element name="builderCompany" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="builderProject" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="building" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="buildingWing" type="{http://schema.base.ws.pro.finnone.nucleus.com}String_0100Type" minOccurs="0"/>
 *         &lt;element name="floorNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *         &lt;element name="flatNumber" type="{http://schema.base.ws.pro.finnone.nucleus.com}IntType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "builderDetails", propOrder = {
    "isBuilderConstructed",
    "builderCompany",
    "builderProject",
    "building",
    "buildingWing",
    "floorNumber",
    "flatNumber"
})
public class BuilderDetails {

    protected Boolean isBuilderConstructed;
    protected String builderCompany;
    protected String builderProject;
    protected String building;
    protected String buildingWing;
    protected Integer floorNumber;
    protected Integer flatNumber;

    /**
     * Gets the value of the isBuilderConstructed property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsBuilderConstructed() {
        return isBuilderConstructed;
    }

    /**
     * Sets the value of the isBuilderConstructed property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsBuilderConstructed(Boolean value) {
        this.isBuilderConstructed = value;
    }

    /**
     * Gets the value of the builderCompany property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBuilderCompany() {
        return builderCompany;
    }

    /**
     * Sets the value of the builderCompany property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBuilderCompany(String value) {
        this.builderCompany = value;
    }

    /**
     * Gets the value of the builderProject property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBuilderProject() {
        return builderProject;
    }

    /**
     * Sets the value of the builderProject property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBuilderProject(String value) {
        this.builderProject = value;
    }

    /**
     * Gets the value of the building property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBuilding() {
        return building;
    }

    /**
     * Sets the value of the building property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBuilding(String value) {
        this.building = value;
    }

    /**
     * Gets the value of the buildingWing property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBuildingWing() {
        return buildingWing;
    }

    /**
     * Sets the value of the buildingWing property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBuildingWing(String value) {
        this.buildingWing = value;
    }

    /**
     * Gets the value of the floorNumber property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getFloorNumber() {
        return floorNumber;
    }

    /**
     * Sets the value of the floorNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setFloorNumber(Integer value) {
        this.floorNumber = value;
    }

    /**
     * Gets the value of the flatNumber property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getFlatNumber() {
        return flatNumber;
    }

    /**
     * Sets the value of the flatNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setFlatNumber(Integer value) {
        this.flatNumber = value;
    }

}
