//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.08.05 um 06:28:02 PM CEST 
//


package util.instances;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * Variable capacity (e.g. MC-VRP)
 * 
 * <p>Java-Klasse für anonymous complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;sequence>
 *             &lt;element name="min_capacity" type="{}positive_double"/>
 *             &lt;element name="max_capacity" type="{}positive_double"/>
 *           &lt;/sequence>
 *           &lt;element name="fix_capacity" type="{}positive_double"/>
 *         &lt;/choice>
 *         &lt;element name="compatible_request_type" type="{http://www.w3.org/2001/XMLSchema}integer" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;choice minOccurs="0">
 *           &lt;sequence>
 *             &lt;element name="min_dimensions" type="{}dimensions_type"/>
 *             &lt;element name="max_dimensions" type="{}dimensions_type"/>
 *           &lt;/sequence>
 *           &lt;element ref="{}dimensions"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *       &lt;attribute name="number" use="required" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "minCapacity",
    "maxCapacity",
    "fixCapacity",
    "compatibleRequestType",
    "minDimensions",
    "maxDimensions",
    "dimensions"
})
@XmlRootElement(name = "compartment")
public class Compartment {

    @XmlElement(name = "min_capacity")
    protected Double minCapacity;
    @XmlElement(name = "max_capacity")
    protected Double maxCapacity;
    @XmlElement(name = "fix_capacity")
    protected Double fixCapacity;
    @XmlElement(name = "compatible_request_type")
    protected List<BigInteger> compatibleRequestType;
    @XmlElement(name = "min_dimensions")
    protected DimensionsType minDimensions;
    @XmlElement(name = "max_dimensions")
    protected DimensionsType maxDimensions;
    protected DimensionsType dimensions;
    @XmlAttribute(name = "number", required = true)
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger number;

    /**
     * Ruft den Wert der minCapacity-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getMinCapacity() {
        return minCapacity;
    }

    /**
     * Legt den Wert der minCapacity-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setMinCapacity(Double value) {
        this.minCapacity = value;
    }

    /**
     * Ruft den Wert der maxCapacity-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getMaxCapacity() {
        return maxCapacity;
    }

    /**
     * Legt den Wert der maxCapacity-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setMaxCapacity(Double value) {
        this.maxCapacity = value;
    }

    /**
     * Ruft den Wert der fixCapacity-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getFixCapacity() {
        return fixCapacity;
    }

    /**
     * Legt den Wert der fixCapacity-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setFixCapacity(Double value) {
        this.fixCapacity = value;
    }

    /**
     * Gets the value of the compatibleRequestType property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the compatibleRequestType property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCompatibleRequestType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BigInteger }
     * 
     * 
     */
    public List<BigInteger> getCompatibleRequestType() {
        if (compatibleRequestType == null) {
            compatibleRequestType = new ArrayList<BigInteger>();
        }
        return this.compatibleRequestType;
    }

    /**
     * Ruft den Wert der minDimensions-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DimensionsType }
     *     
     */
    public DimensionsType getMinDimensions() {
        return minDimensions;
    }

    /**
     * Legt den Wert der minDimensions-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DimensionsType }
     *     
     */
    public void setMinDimensions(DimensionsType value) {
        this.minDimensions = value;
    }

    /**
     * Ruft den Wert der maxDimensions-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DimensionsType }
     *     
     */
    public DimensionsType getMaxDimensions() {
        return maxDimensions;
    }

    /**
     * Legt den Wert der maxDimensions-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DimensionsType }
     *     
     */
    public void setMaxDimensions(DimensionsType value) {
        this.maxDimensions = value;
    }

    /**
     * Fix compartment dimensions
     * 
     * @return
     *     possible object is
     *     {@link DimensionsType }
     *     
     */
    public DimensionsType getDimensions() {
        return dimensions;
    }

    /**
     * Legt den Wert der dimensions-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DimensionsType }
     *     
     */
    public void setDimensions(DimensionsType value) {
        this.dimensions = value;
    }

    /**
     * Ruft den Wert der number-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumber() {
        return number;
    }

    /**
     * Legt den Wert der number-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumber(BigInteger value) {
        this.number = value;
    }

}
