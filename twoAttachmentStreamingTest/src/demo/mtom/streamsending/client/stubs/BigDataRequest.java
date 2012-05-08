
package demo.mtom.streamsending.client.stubs;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for bigDataRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="bigDataRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="bigblob1" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="bigblob2" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "bigDataRequest", propOrder = {
    "name",
    "bigblob1",
    "bigblob2"
})
public class BigDataRequest {

    protected String name;
    @XmlMimeType("application/octet-stream")
    protected DataHandler bigblob1;
    @XmlMimeType("application/octet-stream")
    protected DataHandler bigblob2;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the bigblob1 property.
     * 
     * @return
     *     possible object is
     *     {@link DataHandler }
     *     
     */
    public DataHandler getBigblob1() {
        return bigblob1;
    }

    /**
     * Sets the value of the bigblob1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link DataHandler }
     *     
     */
    public void setBigblob1(DataHandler value) {
        this.bigblob1 = value;
    }

    /**
     * Gets the value of the bigblob2 property.
     * 
     * @return
     *     possible object is
     *     {@link DataHandler }
     *     
     */
    public DataHandler getBigblob2() {
        return bigblob2;
    }

    /**
     * Sets the value of the bigblob2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link DataHandler }
     *     
     */
    public void setBigblob2(DataHandler value) {
        this.bigblob2 = value;
    }

}
