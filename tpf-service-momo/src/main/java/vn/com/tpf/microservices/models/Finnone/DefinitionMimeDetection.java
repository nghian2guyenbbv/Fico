
package vn.com.tpf.microservices.models.Finnone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for definitionMimeDetection complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="definitionMimeDetection">
 *   &lt;simpleContent>
 *     &lt;restriction base="&lt;http://schema.applicationservices.ws.pro.finnone.nucleus.com>base64Binary">
 *       &lt;attribute ref="{http://schema.applicationservices.ws.pro.finnone.nucleus.com}contentType use="required""/>
 *     &lt;/restriction>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "definitionMimeDetection")
public class DefinitionMimeDetection
    extends Base64Binary
{


}
