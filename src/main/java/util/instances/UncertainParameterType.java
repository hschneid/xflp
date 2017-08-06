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
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * Defines a parameter which value is uncertain (e.g., stochastic travel time or stochastic demand)
 * 
 * <p>Java-Klasse für uncertain_parameter_type complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="uncertain_parameter_type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element ref="{}random_variable"/>
 *         &lt;element name="scenario" maxOccurs="unbounded" minOccurs="2">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>double">
 *                 &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *                 &lt;attribute name="probability" use="required" type="{}probability" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "uncertain_parameter_type", propOrder = {
    "randomVariable",
    "scenario"
})
public class UncertainParameterType {

    @XmlElement(name = "random_variable")
    protected RandomVariableType randomVariable;
    protected List<UncertainParameterType.Scenario> scenario;

    /**
     * Ruft den Wert der randomVariable-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link RandomVariableType }
     *     
     */
    public RandomVariableType getRandomVariable() {
        return randomVariable;
    }

    /**
     * Legt den Wert der randomVariable-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link RandomVariableType }
     *     
     */
    public void setRandomVariable(RandomVariableType value) {
        this.randomVariable = value;
    }

    /**
     * Gets the value of the scenario property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the scenario property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getScenario().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UncertainParameterType.Scenario }
     * 
     * 
     */
    public List<UncertainParameterType.Scenario> getScenario() {
        if (scenario == null) {
            scenario = new ArrayList<UncertainParameterType.Scenario>();
        }
        return this.scenario;
    }


    /**
     * <p>Java-Klasse für anonymous complex type.
     * 
     * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>double">
     *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
     *       &lt;attribute name="probability" use="required" type="{}probability" />
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "value"
    })
    public static class Scenario {

        @XmlValue
        protected double value;
        @XmlAttribute(name = "id", required = true)
        protected BigInteger id;
        @XmlAttribute(name = "probability", required = true)
        protected double probability;

        /**
         * Ruft den Wert der value-Eigenschaft ab.
         * 
         */
        public double getValue() {
            return value;
        }

        /**
         * Legt den Wert der value-Eigenschaft fest.
         * 
         */
        public void setValue(double value) {
            this.value = value;
        }

        /**
         * Ruft den Wert der id-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getId() {
            return id;
        }

        /**
         * Legt den Wert der id-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setId(BigInteger value) {
            this.id = value;
        }

        /**
         * Ruft den Wert der probability-Eigenschaft ab.
         * 
         */
        public double getProbability() {
            return probability;
        }

        /**
         * Legt den Wert der probability-Eigenschaft fest.
         * 
         */
        public void setProbability(double value) {
            this.probability = value;
        }

    }

}
