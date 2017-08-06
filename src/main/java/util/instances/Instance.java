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
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * <p>Java-Klasse für anonymous complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="info">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="dataset" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="network">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="nodes">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="node" maxOccurs="unbounded" minOccurs="2">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;choice minOccurs="0">
 *                                         &lt;sequence>
 *                                           &lt;element name="cx" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *                                           &lt;element name="cy" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *                                           &lt;element name="cz" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *                                         &lt;/sequence>
 *                                         &lt;sequence>
 *                                           &lt;element name="latitude" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *                                           &lt;element name="longitude" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *                                         &lt;/sequence>
 *                                       &lt;/choice>
 *                                       &lt;element name="compatible_vehicle" type="{http://www.w3.org/2001/XMLSchema}integer" maxOccurs="unbounded" minOccurs="0"/>
 *                                       &lt;element ref="{}custom" minOccurs="0"/>
 *                                     &lt;/sequence>
 *                                     &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *                                     &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *                                     &lt;attribute name="trailer" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;choice>
 *                     &lt;element name="links">
 *                       &lt;complexType>
 *                         &lt;complexContent>
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                             &lt;sequence>
 *                               &lt;element name="link" maxOccurs="unbounded">
 *                                 &lt;complexType>
 *                                   &lt;complexContent>
 *                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                       &lt;sequence>
 *                                         &lt;element name="length" type="{}positive_double" minOccurs="0"/>
 *                                         &lt;choice minOccurs="0">
 *                                           &lt;element name="travel_time" type="{}positive_double"/>
 *                                           &lt;element name="td_travel_time" type="{}time_dependent_parameter_type" maxOccurs="unbounded"/>
 *                                           &lt;element name="uncertain_travel_time" type="{}uncertain_parameter_type"/>
 *                                         &lt;/choice>
 *                                         &lt;choice minOccurs="0">
 *                                           &lt;element name="travel_cost" type="{}positive_double"/>
 *                                           &lt;element name="td_travel_cost" type="{}time_dependent_parameter_type" maxOccurs="unbounded"/>
 *                                           &lt;element name="uncertain_travel_cost" type="{}uncertain_parameter_type"/>
 *                                         &lt;/choice>
 *                                         &lt;element ref="{}tw" maxOccurs="unbounded" minOccurs="0"/>
 *                                         &lt;element ref="{}custom" minOccurs="0"/>
 *                                       &lt;/sequence>
 *                                       &lt;attribute name="tail" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *                                       &lt;attribute name="head" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *                                       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *                                       &lt;attribute name="directed" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *                                       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *                                     &lt;/restriction>
 *                                   &lt;/complexContent>
 *                                 &lt;/complexType>
 *                               &lt;/element>
 *                             &lt;/sequence>
 *                             &lt;attribute name="symmetric" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *                           &lt;/restriction>
 *                         &lt;/complexContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                     &lt;sequence>
 *                       &lt;choice>
 *                         &lt;element name="euclidean">
 *                           &lt;complexType>
 *                             &lt;complexContent>
 *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                               &lt;/restriction>
 *                             &lt;/complexContent>
 *                           &lt;/complexType>
 *                         &lt;/element>
 *                         &lt;element name="manhattan">
 *                           &lt;complexType>
 *                             &lt;complexContent>
 *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                               &lt;/restriction>
 *                             &lt;/complexContent>
 *                           &lt;/complexType>
 *                         &lt;/element>
 *                         &lt;element name="distance_calculator" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                       &lt;/choice>
 *                       &lt;choice>
 *                         &lt;element name="ceil">
 *                           &lt;complexType>
 *                             &lt;complexContent>
 *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                               &lt;/restriction>
 *                             &lt;/complexContent>
 *                           &lt;/complexType>
 *                         &lt;/element>
 *                         &lt;element name="floor">
 *                           &lt;complexType>
 *                             &lt;complexContent>
 *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                               &lt;/restriction>
 *                             &lt;/complexContent>
 *                           &lt;/complexType>
 *                         &lt;/element>
 *                         &lt;element name="decimals" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *                       &lt;/choice>
 *                     &lt;/sequence>
 *                   &lt;/choice>
 *                   &lt;element ref="{}custom" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="fleet">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="vehicle_profile" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;choice>
 *                               &lt;element name="departure_from_any_node">
 *                                 &lt;complexType>
 *                                   &lt;complexContent>
 *                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;/restriction>
 *                                   &lt;/complexContent>
 *                                 &lt;/complexType>
 *                               &lt;/element>
 *                               &lt;element name="departure_node" type="{http://www.w3.org/2001/XMLSchema}integer" maxOccurs="unbounded"/>
 *                             &lt;/choice>
 *                             &lt;choice>
 *                               &lt;element name="arrival_at_any_node">
 *                                 &lt;complexType>
 *                                   &lt;complexContent>
 *                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;/restriction>
 *                                   &lt;/complexContent>
 *                                 &lt;/complexType>
 *                               &lt;/element>
 *                               &lt;element name="arrival_node" type="{http://www.w3.org/2001/XMLSchema}integer" maxOccurs="unbounded"/>
 *                             &lt;/choice>
 *                             &lt;group ref="{}storage"/>
 *                             &lt;element name="max_travel_time" type="{}positive_double" minOccurs="0"/>
 *                             &lt;element name="max_travel_distance" type="{}positive_double" minOccurs="0"/>
 *                             &lt;element name="speed_factor" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *                             &lt;sequence>
 *                               &lt;element name="fix_cost" type="{}positive_double" minOccurs="0"/>
 *                               &lt;element name="cost_x_distance" type="{}positive_double" minOccurs="0"/>
 *                               &lt;element name="cost_x_time" type="{}positive_double" minOccurs="0"/>
 *                             &lt;/sequence>
 *                             &lt;element name="resource" maxOccurs="unbounded" minOccurs="0">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="start" type="{}positive_double" minOccurs="0"/>
 *                                       &lt;element name="end" type="{}positive_double" minOccurs="0"/>
 *                                       &lt;element name="max" type="{}positive_double"/>
 *                                     &lt;/sequence>
 *                                     &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="trailer" maxOccurs="unbounded" minOccurs="0">
 *                               &lt;complexType>
 *                                 &lt;simpleContent>
 *                                   &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>integer">
 *                                     &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *                                   &lt;/extension>
 *                                 &lt;/simpleContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element ref="{}custom" minOccurs="0"/>
 *                           &lt;/sequence>
 *                           &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *                           &lt;attribute name="number" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="trailer_profile" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;group ref="{}storage"/>
 *                             &lt;sequence>
 *                               &lt;element name="fix_cost" type="{}positive_double" minOccurs="0"/>
 *                               &lt;element name="cost_x_distance" type="{}positive_double" minOccurs="0"/>
 *                               &lt;element name="cost_x_time" type="{}positive_double" minOccurs="0"/>
 *                             &lt;/sequence>
 *                             &lt;element ref="{}custom" minOccurs="0"/>
 *                           &lt;/sequence>
 *                           &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *                           &lt;attribute name="number" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element ref="{}custom" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="requests">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="request" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="release" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *                             &lt;element name="priority" type="{}positive_double" minOccurs="0"/>
 *                             &lt;element name="prize" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *                             &lt;element ref="{}tw" maxOccurs="unbounded" minOccurs="0"/>
 *                             &lt;choice minOccurs="0">
 *                               &lt;element name="quantity" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *                               &lt;element name="td_quantity" type="{}time_dependent_parameter_type"/>
 *                               &lt;element name="uncertain_quantity" type="{}uncertain_parameter_type"/>
 *                             &lt;/choice>
 *                             &lt;choice minOccurs="0">
 *                               &lt;element name="service_time" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *                               &lt;element name="td_service_time" type="{}time_dependent_parameter_type"/>
 *                               &lt;element name="uncertain_service_time" type="{}uncertain_parameter_type"/>
 *                             &lt;/choice>
 *                             &lt;element ref="{}dimensions" minOccurs="0"/>
 *                             &lt;element name="predecessors" minOccurs="0">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="request" type="{http://www.w3.org/2001/XMLSchema}integer" maxOccurs="unbounded"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="successors" minOccurs="0">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="request" type="{http://www.w3.org/2001/XMLSchema}integer" maxOccurs="unbounded"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="skill" type="{http://www.w3.org/2001/XMLSchema}integer" maxOccurs="unbounded" minOccurs="0"/>
 *                             &lt;element name="resource" maxOccurs="unbounded" minOccurs="0">
 *                               &lt;complexType>
 *                                 &lt;simpleContent>
 *                                   &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>double">
 *                                     &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *                                   &lt;/extension>
 *                                 &lt;/simpleContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element ref="{}custom" minOccurs="0"/>
 *                           &lt;/sequence>
 *                           &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *                           &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *                           &lt;attribute name="node" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *                           &lt;attribute name="link" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="request_incompatibility" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;choice>
 *                             &lt;sequence>
 *                               &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *                               &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *                             &lt;/sequence>
 *                             &lt;sequence>
 *                               &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *                               &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *                             &lt;/sequence>
 *                           &lt;/choice>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="resources" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="resource" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;simpleContent>
 *                         &lt;extension base="&lt;>positive_double">
 *                           &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *                           &lt;attribute name="renewable" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *                           &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                         &lt;/extension>
 *                       &lt;/simpleContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="drivers" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="driver_profile" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;choice>
 *                               &lt;element name="compatible_with_all_vehicles">
 *                                 &lt;complexType>
 *                                   &lt;complexContent>
 *                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;/restriction>
 *                                   &lt;/complexContent>
 *                                 &lt;/complexType>
 *                               &lt;/element>
 *                               &lt;element name="compatible_vehicle_type" type="{http://www.w3.org/2001/XMLSchema}integer" maxOccurs="unbounded"/>
 *                             &lt;/choice>
 *                             &lt;element name="skill" maxOccurs="unbounded" minOccurs="0">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="workload_profile" minOccurs="0">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="max_work_time" type="{}positive_double" minOccurs="0"/>
 *                                       &lt;element name="max_driving_time" type="{}positive_double" minOccurs="0"/>
 *                                       &lt;element ref="{}tw" maxOccurs="unbounded" minOccurs="0"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element ref="{}custom" minOccurs="0"/>
 *                           &lt;/sequence>
 *                           &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "instance")
public class Instance {

    @XmlElement(required = true)
    protected Instance.Info info;
    @XmlElement(required = true)
    protected Instance.Network network;
    @XmlElement(required = true)
    protected Instance.Fleet fleet;
    @XmlElement(required = true)
    protected Instance.Requests requests;
    protected Instance.Resources resources;
    protected Instance.Drivers drivers;

    /**
     * Ruft den Wert der info-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Instance.Info }
     *     
     */
    public Instance.Info getInfo() {
        return info;
    }

    /**
     * Legt den Wert der info-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Instance.Info }
     *     
     */
    public void setInfo(Instance.Info value) {
        this.info = value;
    }

    /**
     * Ruft den Wert der network-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Instance.Network }
     *     
     */
    public Instance.Network getNetwork() {
        return network;
    }

    /**
     * Legt den Wert der network-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Instance.Network }
     *     
     */
    public void setNetwork(Instance.Network value) {
        this.network = value;
    }

    /**
     * Ruft den Wert der fleet-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Instance.Fleet }
     *     
     */
    public Instance.Fleet getFleet() {
        return fleet;
    }

    /**
     * Legt den Wert der fleet-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Instance.Fleet }
     *     
     */
    public void setFleet(Instance.Fleet value) {
        this.fleet = value;
    }

    /**
     * Ruft den Wert der requests-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Instance.Requests }
     *     
     */
    public Instance.Requests getRequests() {
        return requests;
    }

    /**
     * Legt den Wert der requests-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Instance.Requests }
     *     
     */
    public void setRequests(Instance.Requests value) {
        this.requests = value;
    }

    /**
     * Ruft den Wert der resources-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Instance.Resources }
     *     
     */
    public Instance.Resources getResources() {
        return resources;
    }

    /**
     * Legt den Wert der resources-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Instance.Resources }
     *     
     */
    public void setResources(Instance.Resources value) {
        this.resources = value;
    }

    /**
     * Ruft den Wert der drivers-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Instance.Drivers }
     *     
     */
    public Instance.Drivers getDrivers() {
        return drivers;
    }

    /**
     * Legt den Wert der drivers-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Instance.Drivers }
     *     
     */
    public void setDrivers(Instance.Drivers value) {
        this.drivers = value;
    }


    /**
     * <p>Java-Klasse für anonymous complex type.
     * 
     * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="driver_profile" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;choice>
     *                     &lt;element name="compatible_with_all_vehicles">
     *                       &lt;complexType>
     *                         &lt;complexContent>
     *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;/restriction>
     *                         &lt;/complexContent>
     *                       &lt;/complexType>
     *                     &lt;/element>
     *                     &lt;element name="compatible_vehicle_type" type="{http://www.w3.org/2001/XMLSchema}integer" maxOccurs="unbounded"/>
     *                   &lt;/choice>
     *                   &lt;element name="skill" maxOccurs="unbounded" minOccurs="0">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="workload_profile" minOccurs="0">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="max_work_time" type="{}positive_double" minOccurs="0"/>
     *                             &lt;element name="max_driving_time" type="{}positive_double" minOccurs="0"/>
     *                             &lt;element ref="{}tw" maxOccurs="unbounded" minOccurs="0"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element ref="{}custom" minOccurs="0"/>
     *                 &lt;/sequence>
     *                 &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "driverProfile"
    })
    public static class Drivers {

        @XmlElement(name = "driver_profile", required = true)
        protected List<Instance.Drivers.DriverProfile> driverProfile;

        /**
         * Gets the value of the driverProfile property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the driverProfile property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getDriverProfile().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Instance.Drivers.DriverProfile }
         * 
         * 
         */
        public List<Instance.Drivers.DriverProfile> getDriverProfile() {
            if (driverProfile == null) {
                driverProfile = new ArrayList<Instance.Drivers.DriverProfile>();
            }
            return this.driverProfile;
        }


        /**
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
         *           &lt;element name="compatible_with_all_vehicles">
         *             &lt;complexType>
         *               &lt;complexContent>
         *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;/restriction>
         *               &lt;/complexContent>
         *             &lt;/complexType>
         *           &lt;/element>
         *           &lt;element name="compatible_vehicle_type" type="{http://www.w3.org/2001/XMLSchema}integer" maxOccurs="unbounded"/>
         *         &lt;/choice>
         *         &lt;element name="skill" maxOccurs="unbounded" minOccurs="0">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="workload_profile" minOccurs="0">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="max_work_time" type="{}positive_double" minOccurs="0"/>
         *                   &lt;element name="max_driving_time" type="{}positive_double" minOccurs="0"/>
         *                   &lt;element ref="{}tw" maxOccurs="unbounded" minOccurs="0"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element ref="{}custom" minOccurs="0"/>
         *       &lt;/sequence>
         *       &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "compatibleWithAllVehicles",
            "compatibleVehicleType",
            "skill",
            "workloadProfile",
            "custom"
        })
        public static class DriverProfile {

            @XmlElement(name = "compatible_with_all_vehicles")
            protected Instance.Drivers.DriverProfile.CompatibleWithAllVehicles compatibleWithAllVehicles;
            @XmlElement(name = "compatible_vehicle_type")
            protected List<BigInteger> compatibleVehicleType;
            protected List<Instance.Drivers.DriverProfile.Skill> skill;
            @XmlElement(name = "workload_profile")
            protected Instance.Drivers.DriverProfile.WorkloadProfile workloadProfile;
            protected Custom custom;
            @XmlAttribute(name = "type", required = true)
            protected BigInteger type;

            /**
             * Ruft den Wert der compatibleWithAllVehicles-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link Instance.Drivers.DriverProfile.CompatibleWithAllVehicles }
             *     
             */
            public Instance.Drivers.DriverProfile.CompatibleWithAllVehicles getCompatibleWithAllVehicles() {
                return compatibleWithAllVehicles;
            }

            /**
             * Legt den Wert der compatibleWithAllVehicles-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link Instance.Drivers.DriverProfile.CompatibleWithAllVehicles }
             *     
             */
            public void setCompatibleWithAllVehicles(Instance.Drivers.DriverProfile.CompatibleWithAllVehicles value) {
                this.compatibleWithAllVehicles = value;
            }

            /**
             * Gets the value of the compatibleVehicleType property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the compatibleVehicleType property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getCompatibleVehicleType().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link BigInteger }
             * 
             * 
             */
            public List<BigInteger> getCompatibleVehicleType() {
                if (compatibleVehicleType == null) {
                    compatibleVehicleType = new ArrayList<BigInteger>();
                }
                return this.compatibleVehicleType;
            }

            /**
             * Gets the value of the skill property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the skill property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getSkill().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link Instance.Drivers.DriverProfile.Skill }
             * 
             * 
             */
            public List<Instance.Drivers.DriverProfile.Skill> getSkill() {
                if (skill == null) {
                    skill = new ArrayList<Instance.Drivers.DriverProfile.Skill>();
                }
                return this.skill;
            }

            /**
             * Ruft den Wert der workloadProfile-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link Instance.Drivers.DriverProfile.WorkloadProfile }
             *     
             */
            public Instance.Drivers.DriverProfile.WorkloadProfile getWorkloadProfile() {
                return workloadProfile;
            }

            /**
             * Legt den Wert der workloadProfile-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link Instance.Drivers.DriverProfile.WorkloadProfile }
             *     
             */
            public void setWorkloadProfile(Instance.Drivers.DriverProfile.WorkloadProfile value) {
                this.workloadProfile = value;
            }

            /**
             * Custom element. Use this element to define driver profile attributes that are not pre-defined in the specification
             * 
             * @return
             *     possible object is
             *     {@link Custom }
             *     
             */
            public Custom getCustom() {
                return custom;
            }

            /**
             * Legt den Wert der custom-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link Custom }
             *     
             */
            public void setCustom(Custom value) {
                this.custom = value;
            }

            /**
             * Ruft den Wert der type-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link BigInteger }
             *     
             */
            public BigInteger getType() {
                return type;
            }

            /**
             * Legt den Wert der type-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link BigInteger }
             *     
             */
            public void setType(BigInteger value) {
                this.type = value;
            }


            /**
             * <p>Java-Klasse für anonymous complex type.
             * 
             * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class CompatibleWithAllVehicles {


            }


            /**
             * <p>Java-Klasse für anonymous complex type.
             * 
             * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class Skill {

                @XmlAttribute(name = "id", required = true)
                protected BigInteger id;

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

            }


            /**
             * <p>Java-Klasse für anonymous complex type.
             * 
             * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="max_work_time" type="{}positive_double" minOccurs="0"/>
             *         &lt;element name="max_driving_time" type="{}positive_double" minOccurs="0"/>
             *         &lt;element ref="{}tw" maxOccurs="unbounded" minOccurs="0"/>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "maxWorkTime",
                "maxDrivingTime",
                "tw"
            })
            public static class WorkloadProfile {

                @XmlElement(name = "max_work_time")
                protected Double maxWorkTime;
                @XmlElement(name = "max_driving_time")
                protected Double maxDrivingTime;
                protected List<Tw> tw;

                /**
                 * Ruft den Wert der maxWorkTime-Eigenschaft ab.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Double }
                 *     
                 */
                public Double getMaxWorkTime() {
                    return maxWorkTime;
                }

                /**
                 * Legt den Wert der maxWorkTime-Eigenschaft fest.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Double }
                 *     
                 */
                public void setMaxWorkTime(Double value) {
                    this.maxWorkTime = value;
                }

                /**
                 * Ruft den Wert der maxDrivingTime-Eigenschaft ab.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Double }
                 *     
                 */
                public Double getMaxDrivingTime() {
                    return maxDrivingTime;
                }

                /**
                 * Legt den Wert der maxDrivingTime-Eigenschaft fest.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Double }
                 *     
                 */
                public void setMaxDrivingTime(Double value) {
                    this.maxDrivingTime = value;
                }

                /**
                 * Driver's availability time windows. May model either breaks or availability times Gets the value of the tw property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the tw property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getTw().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link Tw }
                 * 
                 * 
                 */
                public List<Tw> getTw() {
                    if (tw == null) {
                        tw = new ArrayList<Tw>();
                    }
                    return this.tw;
                }

            }

        }

    }


    /**
     * <p>Java-Klasse für anonymous complex type.
     * 
     * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="vehicle_profile" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;choice>
     *                     &lt;element name="departure_from_any_node">
     *                       &lt;complexType>
     *                         &lt;complexContent>
     *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;/restriction>
     *                         &lt;/complexContent>
     *                       &lt;/complexType>
     *                     &lt;/element>
     *                     &lt;element name="departure_node" type="{http://www.w3.org/2001/XMLSchema}integer" maxOccurs="unbounded"/>
     *                   &lt;/choice>
     *                   &lt;choice>
     *                     &lt;element name="arrival_at_any_node">
     *                       &lt;complexType>
     *                         &lt;complexContent>
     *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;/restriction>
     *                         &lt;/complexContent>
     *                       &lt;/complexType>
     *                     &lt;/element>
     *                     &lt;element name="arrival_node" type="{http://www.w3.org/2001/XMLSchema}integer" maxOccurs="unbounded"/>
     *                   &lt;/choice>
     *                   &lt;group ref="{}storage"/>
     *                   &lt;element name="max_travel_time" type="{}positive_double" minOccurs="0"/>
     *                   &lt;element name="max_travel_distance" type="{}positive_double" minOccurs="0"/>
     *                   &lt;element name="speed_factor" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
     *                   &lt;sequence>
     *                     &lt;element name="fix_cost" type="{}positive_double" minOccurs="0"/>
     *                     &lt;element name="cost_x_distance" type="{}positive_double" minOccurs="0"/>
     *                     &lt;element name="cost_x_time" type="{}positive_double" minOccurs="0"/>
     *                   &lt;/sequence>
     *                   &lt;element name="resource" maxOccurs="unbounded" minOccurs="0">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="start" type="{}positive_double" minOccurs="0"/>
     *                             &lt;element name="end" type="{}positive_double" minOccurs="0"/>
     *                             &lt;element name="max" type="{}positive_double"/>
     *                           &lt;/sequence>
     *                           &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="trailer" maxOccurs="unbounded" minOccurs="0">
     *                     &lt;complexType>
     *                       &lt;simpleContent>
     *                         &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>integer">
     *                           &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
     *                         &lt;/extension>
     *                       &lt;/simpleContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element ref="{}custom" minOccurs="0"/>
     *                 &lt;/sequence>
     *                 &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
     *                 &lt;attribute name="number" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="trailer_profile" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;group ref="{}storage"/>
     *                   &lt;sequence>
     *                     &lt;element name="fix_cost" type="{}positive_double" minOccurs="0"/>
     *                     &lt;element name="cost_x_distance" type="{}positive_double" minOccurs="0"/>
     *                     &lt;element name="cost_x_time" type="{}positive_double" minOccurs="0"/>
     *                   &lt;/sequence>
     *                   &lt;element ref="{}custom" minOccurs="0"/>
     *                 &lt;/sequence>
     *                 &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
     *                 &lt;attribute name="number" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element ref="{}custom" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "vehicleProfile",
        "trailerProfile",
        "custom"
    })
    public static class Fleet {

        @XmlElement(name = "vehicle_profile", required = true)
        protected List<Instance.Fleet.VehicleProfile> vehicleProfile;
        @XmlElement(name = "trailer_profile")
        protected List<Instance.Fleet.TrailerProfile> trailerProfile;
        protected Custom custom;

        /**
         * Gets the value of the vehicleProfile property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the vehicleProfile property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getVehicleProfile().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Instance.Fleet.VehicleProfile }
         * 
         * 
         */
        public List<Instance.Fleet.VehicleProfile> getVehicleProfile() {
            if (vehicleProfile == null) {
                vehicleProfile = new ArrayList<Instance.Fleet.VehicleProfile>();
            }
            return this.vehicleProfile;
        }

        /**
         * Gets the value of the trailerProfile property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the trailerProfile property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getTrailerProfile().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Instance.Fleet.TrailerProfile }
         * 
         * 
         */
        public List<Instance.Fleet.TrailerProfile> getTrailerProfile() {
            if (trailerProfile == null) {
                trailerProfile = new ArrayList<Instance.Fleet.TrailerProfile>();
            }
            return this.trailerProfile;
        }

        /**
         * Custom element. Use this element to define trailer attributes that are not pre-defined in the specification
         * 
         * @return
         *     possible object is
         *     {@link Custom }
         *     
         */
        public Custom getCustom() {
            return custom;
        }

        /**
         * Legt den Wert der custom-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Custom }
         *     
         */
        public void setCustom(Custom value) {
            this.custom = value;
        }


        /**
         * <p>Java-Klasse für anonymous complex type.
         * 
         * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;group ref="{}storage"/>
         *         &lt;sequence>
         *           &lt;element name="fix_cost" type="{}positive_double" minOccurs="0"/>
         *           &lt;element name="cost_x_distance" type="{}positive_double" minOccurs="0"/>
         *           &lt;element name="cost_x_time" type="{}positive_double" minOccurs="0"/>
         *         &lt;/sequence>
         *         &lt;element ref="{}custom" minOccurs="0"/>
         *       &lt;/sequence>
         *       &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
         *       &lt;attribute name="number" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "capacity",
            "maxWeight",
            "maxVolume",
            "dimensions",
            "compartment",
            "fixCost",
            "costXDistance",
            "costXTime",
            "custom"
        })
        public static class TrailerProfile {

            protected Double capacity;
            @XmlElement(name = "max_weight")
            protected Double maxWeight;
            @XmlElement(name = "max_volume")
            protected Double maxVolume;
            protected DimensionsType dimensions;
            protected List<Compartment> compartment;
            @XmlElement(name = "fix_cost")
            protected Double fixCost;
            @XmlElement(name = "cost_x_distance")
            protected Double costXDistance;
            @XmlElement(name = "cost_x_time")
            protected Double costXTime;
            protected Custom custom;
            @XmlAttribute(name = "type", required = true)
            protected BigInteger type;
            @XmlAttribute(name = "number")
            @XmlSchemaType(name = "positiveInteger")
            protected BigInteger number;

            /**
             * Ruft den Wert der capacity-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link Double }
             *     
             */
            public Double getCapacity() {
                return capacity;
            }

            /**
             * Legt den Wert der capacity-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link Double }
             *     
             */
            public void setCapacity(Double value) {
                this.capacity = value;
            }

            /**
             * Ruft den Wert der maxWeight-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link Double }
             *     
             */
            public Double getMaxWeight() {
                return maxWeight;
            }

            /**
             * Legt den Wert der maxWeight-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link Double }
             *     
             */
            public void setMaxWeight(Double value) {
                this.maxWeight = value;
            }

            /**
             * Ruft den Wert der maxVolume-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link Double }
             *     
             */
            public Double getMaxVolume() {
                return maxVolume;
            }

            /**
             * Legt den Wert der maxVolume-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link Double }
             *     
             */
            public void setMaxVolume(Double value) {
                this.maxVolume = value;
            }

            /**
             * Defines the dimensions of the storage unit
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
             * Defines a compartment. Use this element to model multicompartment VRPs Gets the value of the compartment property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the compartment property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getCompartment().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link Compartment }
             * 
             * 
             */
            public List<Compartment> getCompartment() {
                if (compartment == null) {
                    compartment = new ArrayList<Compartment>();
                }
                return this.compartment;
            }

            /**
             * Ruft den Wert der fixCost-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link Double }
             *     
             */
            public Double getFixCost() {
                return fixCost;
            }

            /**
             * Legt den Wert der fixCost-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link Double }
             *     
             */
            public void setFixCost(Double value) {
                this.fixCost = value;
            }

            /**
             * Ruft den Wert der costXDistance-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link Double }
             *     
             */
            public Double getCostXDistance() {
                return costXDistance;
            }

            /**
             * Legt den Wert der costXDistance-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link Double }
             *     
             */
            public void setCostXDistance(Double value) {
                this.costXDistance = value;
            }

            /**
             * Ruft den Wert der costXTime-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link Double }
             *     
             */
            public Double getCostXTime() {
                return costXTime;
            }

            /**
             * Legt den Wert der costXTime-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link Double }
             *     
             */
            public void setCostXTime(Double value) {
                this.costXTime = value;
            }

            /**
             * Custom element. Use this element to define trailer attributes that are not pre-defined in the specification
             * 
             * @return
             *     possible object is
             *     {@link Custom }
             *     
             */
            public Custom getCustom() {
                return custom;
            }

            /**
             * Legt den Wert der custom-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link Custom }
             *     
             */
            public void setCustom(Custom value) {
                this.custom = value;
            }

            /**
             * Ruft den Wert der type-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link BigInteger }
             *     
             */
            public BigInteger getType() {
                return type;
            }

            /**
             * Legt den Wert der type-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link BigInteger }
             *     
             */
            public void setType(BigInteger value) {
                this.type = value;
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


        /**
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
         *           &lt;element name="departure_from_any_node">
         *             &lt;complexType>
         *               &lt;complexContent>
         *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;/restriction>
         *               &lt;/complexContent>
         *             &lt;/complexType>
         *           &lt;/element>
         *           &lt;element name="departure_node" type="{http://www.w3.org/2001/XMLSchema}integer" maxOccurs="unbounded"/>
         *         &lt;/choice>
         *         &lt;choice>
         *           &lt;element name="arrival_at_any_node">
         *             &lt;complexType>
         *               &lt;complexContent>
         *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;/restriction>
         *               &lt;/complexContent>
         *             &lt;/complexType>
         *           &lt;/element>
         *           &lt;element name="arrival_node" type="{http://www.w3.org/2001/XMLSchema}integer" maxOccurs="unbounded"/>
         *         &lt;/choice>
         *         &lt;group ref="{}storage"/>
         *         &lt;element name="max_travel_time" type="{}positive_double" minOccurs="0"/>
         *         &lt;element name="max_travel_distance" type="{}positive_double" minOccurs="0"/>
         *         &lt;element name="speed_factor" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
         *         &lt;sequence>
         *           &lt;element name="fix_cost" type="{}positive_double" minOccurs="0"/>
         *           &lt;element name="cost_x_distance" type="{}positive_double" minOccurs="0"/>
         *           &lt;element name="cost_x_time" type="{}positive_double" minOccurs="0"/>
         *         &lt;/sequence>
         *         &lt;element name="resource" maxOccurs="unbounded" minOccurs="0">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="start" type="{}positive_double" minOccurs="0"/>
         *                   &lt;element name="end" type="{}positive_double" minOccurs="0"/>
         *                   &lt;element name="max" type="{}positive_double"/>
         *                 &lt;/sequence>
         *                 &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="trailer" maxOccurs="unbounded" minOccurs="0">
         *           &lt;complexType>
         *             &lt;simpleContent>
         *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>integer">
         *                 &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
         *               &lt;/extension>
         *             &lt;/simpleContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element ref="{}custom" minOccurs="0"/>
         *       &lt;/sequence>
         *       &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
         *       &lt;attribute name="number" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "departureFromAnyNode",
            "departureNode",
            "arrivalAtAnyNode",
            "arrivalNode",
            "capacity",
            "maxWeight",
            "maxVolume",
            "dimensions",
            "compartment",
            "maxTravelTime",
            "maxTravelDistance",
            "speedFactor",
            "fixCost",
            "costXDistance",
            "costXTime",
            "resource",
            "trailer",
            "custom"
        })
        public static class VehicleProfile {

            @XmlElement(name = "departure_from_any_node")
            protected Instance.Fleet.VehicleProfile.DepartureFromAnyNode departureFromAnyNode;
            @XmlElement(name = "departure_node")
            protected List<BigInteger> departureNode;
            @XmlElement(name = "arrival_at_any_node")
            protected Instance.Fleet.VehicleProfile.ArrivalAtAnyNode arrivalAtAnyNode;
            @XmlElement(name = "arrival_node")
            protected List<BigInteger> arrivalNode;
            protected Double capacity;
            @XmlElement(name = "max_weight")
            protected Double maxWeight;
            @XmlElement(name = "max_volume")
            protected Double maxVolume;
            protected DimensionsType dimensions;
            protected List<Compartment> compartment;
            @XmlElement(name = "max_travel_time")
            protected Double maxTravelTime;
            @XmlElement(name = "max_travel_distance")
            protected Double maxTravelDistance;
            @XmlElement(name = "speed_factor")
            protected Double speedFactor;
            @XmlElement(name = "fix_cost")
            protected Double fixCost;
            @XmlElement(name = "cost_x_distance")
            protected Double costXDistance;
            @XmlElement(name = "cost_x_time")
            protected Double costXTime;
            protected List<Instance.Fleet.VehicleProfile.Resource> resource;
            protected List<Instance.Fleet.VehicleProfile.Trailer> trailer;
            protected Custom custom;
            @XmlAttribute(name = "type", required = true)
            protected BigInteger type;
            @XmlAttribute(name = "number")
            @XmlSchemaType(name = "positiveInteger")
            protected BigInteger number;

            /**
             * Ruft den Wert der departureFromAnyNode-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link Instance.Fleet.VehicleProfile.DepartureFromAnyNode }
             *     
             */
            public Instance.Fleet.VehicleProfile.DepartureFromAnyNode getDepartureFromAnyNode() {
                return departureFromAnyNode;
            }

            /**
             * Legt den Wert der departureFromAnyNode-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link Instance.Fleet.VehicleProfile.DepartureFromAnyNode }
             *     
             */
            public void setDepartureFromAnyNode(Instance.Fleet.VehicleProfile.DepartureFromAnyNode value) {
                this.departureFromAnyNode = value;
            }

            /**
             * Gets the value of the departureNode property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the departureNode property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getDepartureNode().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link BigInteger }
             * 
             * 
             */
            public List<BigInteger> getDepartureNode() {
                if (departureNode == null) {
                    departureNode = new ArrayList<BigInteger>();
                }
                return this.departureNode;
            }

            /**
             * Ruft den Wert der arrivalAtAnyNode-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link Instance.Fleet.VehicleProfile.ArrivalAtAnyNode }
             *     
             */
            public Instance.Fleet.VehicleProfile.ArrivalAtAnyNode getArrivalAtAnyNode() {
                return arrivalAtAnyNode;
            }

            /**
             * Legt den Wert der arrivalAtAnyNode-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link Instance.Fleet.VehicleProfile.ArrivalAtAnyNode }
             *     
             */
            public void setArrivalAtAnyNode(Instance.Fleet.VehicleProfile.ArrivalAtAnyNode value) {
                this.arrivalAtAnyNode = value;
            }

            /**
             * Gets the value of the arrivalNode property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the arrivalNode property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getArrivalNode().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link BigInteger }
             * 
             * 
             */
            public List<BigInteger> getArrivalNode() {
                if (arrivalNode == null) {
                    arrivalNode = new ArrayList<BigInteger>();
                }
                return this.arrivalNode;
            }

            /**
             * Ruft den Wert der capacity-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link Double }
             *     
             */
            public Double getCapacity() {
                return capacity;
            }

            /**
             * Legt den Wert der capacity-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link Double }
             *     
             */
            public void setCapacity(Double value) {
                this.capacity = value;
            }

            /**
             * Ruft den Wert der maxWeight-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link Double }
             *     
             */
            public Double getMaxWeight() {
                return maxWeight;
            }

            /**
             * Legt den Wert der maxWeight-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link Double }
             *     
             */
            public void setMaxWeight(Double value) {
                this.maxWeight = value;
            }

            /**
             * Ruft den Wert der maxVolume-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link Double }
             *     
             */
            public Double getMaxVolume() {
                return maxVolume;
            }

            /**
             * Legt den Wert der maxVolume-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link Double }
             *     
             */
            public void setMaxVolume(Double value) {
                this.maxVolume = value;
            }

            /**
             * Defines the dimensions of the storage unit
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
             * Defines a compartment. Use this element to model multicompartment VRPs Gets the value of the compartment property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the compartment property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getCompartment().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link Compartment }
             * 
             * 
             */
            public List<Compartment> getCompartment() {
                if (compartment == null) {
                    compartment = new ArrayList<Compartment>();
                }
                return this.compartment;
            }

            /**
             * Ruft den Wert der maxTravelTime-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link Double }
             *     
             */
            public Double getMaxTravelTime() {
                return maxTravelTime;
            }

            /**
             * Legt den Wert der maxTravelTime-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link Double }
             *     
             */
            public void setMaxTravelTime(Double value) {
                this.maxTravelTime = value;
            }

            /**
             * Ruft den Wert der maxTravelDistance-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link Double }
             *     
             */
            public Double getMaxTravelDistance() {
                return maxTravelDistance;
            }

            /**
             * Legt den Wert der maxTravelDistance-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link Double }
             *     
             */
            public void setMaxTravelDistance(Double value) {
                this.maxTravelDistance = value;
            }

            /**
             * Ruft den Wert der speedFactor-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link Double }
             *     
             */
            public Double getSpeedFactor() {
                return speedFactor;
            }

            /**
             * Legt den Wert der speedFactor-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link Double }
             *     
             */
            public void setSpeedFactor(Double value) {
                this.speedFactor = value;
            }

            /**
             * Ruft den Wert der fixCost-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link Double }
             *     
             */
            public Double getFixCost() {
                return fixCost;
            }

            /**
             * Legt den Wert der fixCost-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link Double }
             *     
             */
            public void setFixCost(Double value) {
                this.fixCost = value;
            }

            /**
             * Ruft den Wert der costXDistance-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link Double }
             *     
             */
            public Double getCostXDistance() {
                return costXDistance;
            }

            /**
             * Legt den Wert der costXDistance-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link Double }
             *     
             */
            public void setCostXDistance(Double value) {
                this.costXDistance = value;
            }

            /**
             * Ruft den Wert der costXTime-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link Double }
             *     
             */
            public Double getCostXTime() {
                return costXTime;
            }

            /**
             * Legt den Wert der costXTime-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link Double }
             *     
             */
            public void setCostXTime(Double value) {
                this.costXTime = value;
            }

            /**
             * Gets the value of the resource property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the resource property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getResource().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link Instance.Fleet.VehicleProfile.Resource }
             * 
             * 
             */
            public List<Instance.Fleet.VehicleProfile.Resource> getResource() {
                if (resource == null) {
                    resource = new ArrayList<Instance.Fleet.VehicleProfile.Resource>();
                }
                return this.resource;
            }

            /**
             * Gets the value of the trailer property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the trailer property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getTrailer().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link Instance.Fleet.VehicleProfile.Trailer }
             * 
             * 
             */
            public List<Instance.Fleet.VehicleProfile.Trailer> getTrailer() {
                if (trailer == null) {
                    trailer = new ArrayList<Instance.Fleet.VehicleProfile.Trailer>();
                }
                return this.trailer;
            }

            /**
             * Custom element. Use this element to define vehicle attributes that are not pre-defined in the specification
             * 
             * @return
             *     possible object is
             *     {@link Custom }
             *     
             */
            public Custom getCustom() {
                return custom;
            }

            /**
             * Legt den Wert der custom-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link Custom }
             *     
             */
            public void setCustom(Custom value) {
                this.custom = value;
            }

            /**
             * Ruft den Wert der type-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link BigInteger }
             *     
             */
            public BigInteger getType() {
                return type;
            }

            /**
             * Legt den Wert der type-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link BigInteger }
             *     
             */
            public void setType(BigInteger value) {
                this.type = value;
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


            /**
             * <p>Java-Klasse für anonymous complex type.
             * 
             * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class ArrivalAtAnyNode {


            }


            /**
             * <p>Java-Klasse für anonymous complex type.
             * 
             * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class DepartureFromAnyNode {


            }


            /**
             * <p>Java-Klasse für anonymous complex type.
             * 
             * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="start" type="{}positive_double" minOccurs="0"/>
             *         &lt;element name="end" type="{}positive_double" minOccurs="0"/>
             *         &lt;element name="max" type="{}positive_double"/>
             *       &lt;/sequence>
             *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "start",
                "end",
                "max"
            })
            public static class Resource {

                protected Double start;
                protected Double end;
                protected double max;
                @XmlAttribute(name = "id", required = true)
                protected BigInteger id;

                /**
                 * Ruft den Wert der start-Eigenschaft ab.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Double }
                 *     
                 */
                public Double getStart() {
                    return start;
                }

                /**
                 * Legt den Wert der start-Eigenschaft fest.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Double }
                 *     
                 */
                public void setStart(Double value) {
                    this.start = value;
                }

                /**
                 * Ruft den Wert der end-Eigenschaft ab.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Double }
                 *     
                 */
                public Double getEnd() {
                    return end;
                }

                /**
                 * Legt den Wert der end-Eigenschaft fest.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Double }
                 *     
                 */
                public void setEnd(Double value) {
                    this.end = value;
                }

                /**
                 * Ruft den Wert der max-Eigenschaft ab.
                 * 
                 */
                public double getMax() {
                    return max;
                }

                /**
                 * Legt den Wert der max-Eigenschaft fest.
                 * 
                 */
                public void setMax(double value) {
                    this.max = value;
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

            }


            /**
             * <p>Java-Klasse für anonymous complex type.
             * 
             * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;simpleContent>
             *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>integer">
             *       &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
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
            public static class Trailer {

                @XmlValue
                protected BigInteger value;
                @XmlAttribute(name = "type", required = true)
                protected BigInteger type;

                /**
                 * Ruft den Wert der value-Eigenschaft ab.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BigInteger }
                 *     
                 */
                public BigInteger getValue() {
                    return value;
                }

                /**
                 * Legt den Wert der value-Eigenschaft fest.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BigInteger }
                 *     
                 */
                public void setValue(BigInteger value) {
                    this.value = value;
                }

                /**
                 * Ruft den Wert der type-Eigenschaft ab.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BigInteger }
                 *     
                 */
                public BigInteger getType() {
                    return type;
                }

                /**
                 * Legt den Wert der type-Eigenschaft fest.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BigInteger }
                 *     
                 */
                public void setType(BigInteger value) {
                    this.type = value;
                }

            }

        }

    }


    /**
     * <p>Java-Klasse für anonymous complex type.
     * 
     * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="dataset" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "dataset",
        "name"
    })
    public static class Info {

        @XmlElement(required = true)
        protected String dataset;
        @XmlElement(required = true)
        protected String name;

        /**
         * Ruft den Wert der dataset-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDataset() {
            return dataset;
        }

        /**
         * Legt den Wert der dataset-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDataset(String value) {
            this.dataset = value;
        }

        /**
         * Ruft den Wert der name-Eigenschaft ab.
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
         * Legt den Wert der name-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setName(String value) {
            this.name = value;
        }

    }


    /**
     * <p>Java-Klasse für anonymous complex type.
     * 
     * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="nodes">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="node" maxOccurs="unbounded" minOccurs="2">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;choice minOccurs="0">
     *                               &lt;sequence>
     *                                 &lt;element name="cx" type="{http://www.w3.org/2001/XMLSchema}double"/>
     *                                 &lt;element name="cy" type="{http://www.w3.org/2001/XMLSchema}double"/>
     *                                 &lt;element name="cz" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
     *                               &lt;/sequence>
     *                               &lt;sequence>
     *                                 &lt;element name="latitude" type="{http://www.w3.org/2001/XMLSchema}double"/>
     *                                 &lt;element name="longitude" type="{http://www.w3.org/2001/XMLSchema}double"/>
     *                               &lt;/sequence>
     *                             &lt;/choice>
     *                             &lt;element name="compatible_vehicle" type="{http://www.w3.org/2001/XMLSchema}integer" maxOccurs="unbounded" minOccurs="0"/>
     *                             &lt;element ref="{}custom" minOccurs="0"/>
     *                           &lt;/sequence>
     *                           &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
     *                           &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
     *                           &lt;attribute name="trailer" type="{http://www.w3.org/2001/XMLSchema}boolean" />
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;choice>
     *           &lt;element name="links">
     *             &lt;complexType>
     *               &lt;complexContent>
     *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                   &lt;sequence>
     *                     &lt;element name="link" maxOccurs="unbounded">
     *                       &lt;complexType>
     *                         &lt;complexContent>
     *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                             &lt;sequence>
     *                               &lt;element name="length" type="{}positive_double" minOccurs="0"/>
     *                               &lt;choice minOccurs="0">
     *                                 &lt;element name="travel_time" type="{}positive_double"/>
     *                                 &lt;element name="td_travel_time" type="{}time_dependent_parameter_type" maxOccurs="unbounded"/>
     *                                 &lt;element name="uncertain_travel_time" type="{}uncertain_parameter_type"/>
     *                               &lt;/choice>
     *                               &lt;choice minOccurs="0">
     *                                 &lt;element name="travel_cost" type="{}positive_double"/>
     *                                 &lt;element name="td_travel_cost" type="{}time_dependent_parameter_type" maxOccurs="unbounded"/>
     *                                 &lt;element name="uncertain_travel_cost" type="{}uncertain_parameter_type"/>
     *                               &lt;/choice>
     *                               &lt;element ref="{}tw" maxOccurs="unbounded" minOccurs="0"/>
     *                               &lt;element ref="{}custom" minOccurs="0"/>
     *                             &lt;/sequence>
     *                             &lt;attribute name="tail" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
     *                             &lt;attribute name="head" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
     *                             &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}integer" />
     *                             &lt;attribute name="directed" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
     *                             &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}integer" />
     *                           &lt;/restriction>
     *                         &lt;/complexContent>
     *                       &lt;/complexType>
     *                     &lt;/element>
     *                   &lt;/sequence>
     *                   &lt;attribute name="symmetric" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
     *                 &lt;/restriction>
     *               &lt;/complexContent>
     *             &lt;/complexType>
     *           &lt;/element>
     *           &lt;sequence>
     *             &lt;choice>
     *               &lt;element name="euclidean">
     *                 &lt;complexType>
     *                   &lt;complexContent>
     *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                     &lt;/restriction>
     *                   &lt;/complexContent>
     *                 &lt;/complexType>
     *               &lt;/element>
     *               &lt;element name="manhattan">
     *                 &lt;complexType>
     *                   &lt;complexContent>
     *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                     &lt;/restriction>
     *                   &lt;/complexContent>
     *                 &lt;/complexType>
     *               &lt;/element>
     *               &lt;element name="distance_calculator" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *             &lt;/choice>
     *             &lt;choice>
     *               &lt;element name="ceil">
     *                 &lt;complexType>
     *                   &lt;complexContent>
     *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                     &lt;/restriction>
     *                   &lt;/complexContent>
     *                 &lt;/complexType>
     *               &lt;/element>
     *               &lt;element name="floor">
     *                 &lt;complexType>
     *                   &lt;complexContent>
     *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                     &lt;/restriction>
     *                   &lt;/complexContent>
     *                 &lt;/complexType>
     *               &lt;/element>
     *               &lt;element name="decimals" type="{http://www.w3.org/2001/XMLSchema}integer"/>
     *             &lt;/choice>
     *           &lt;/sequence>
     *         &lt;/choice>
     *         &lt;element ref="{}custom" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "nodes",
        "links",
        "euclidean",
        "manhattan",
        "distanceCalculator",
        "ceil",
        "floor",
        "decimals",
        "custom"
    })
    public static class Network {

        @XmlElement(required = true)
        protected Instance.Network.Nodes nodes;
        protected Instance.Network.Links links;
        protected Instance.Network.Euclidean euclidean;
        protected Instance.Network.Manhattan manhattan;
        @XmlElement(name = "distance_calculator")
        protected String distanceCalculator;
        protected Instance.Network.Ceil ceil;
        protected Instance.Network.Floor floor;
        protected BigInteger decimals;
        protected Custom custom;

        /**
         * Ruft den Wert der nodes-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link Instance.Network.Nodes }
         *     
         */
        public Instance.Network.Nodes getNodes() {
            return nodes;
        }

        /**
         * Legt den Wert der nodes-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Instance.Network.Nodes }
         *     
         */
        public void setNodes(Instance.Network.Nodes value) {
            this.nodes = value;
        }

        /**
         * Ruft den Wert der links-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link Instance.Network.Links }
         *     
         */
        public Instance.Network.Links getLinks() {
            return links;
        }

        /**
         * Legt den Wert der links-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Instance.Network.Links }
         *     
         */
        public void setLinks(Instance.Network.Links value) {
            this.links = value;
        }

        /**
         * Ruft den Wert der euclidean-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link Instance.Network.Euclidean }
         *     
         */
        public Instance.Network.Euclidean getEuclidean() {
            return euclidean;
        }

        /**
         * Legt den Wert der euclidean-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Instance.Network.Euclidean }
         *     
         */
        public void setEuclidean(Instance.Network.Euclidean value) {
            this.euclidean = value;
        }

        /**
         * Ruft den Wert der manhattan-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link Instance.Network.Manhattan }
         *     
         */
        public Instance.Network.Manhattan getManhattan() {
            return manhattan;
        }

        /**
         * Legt den Wert der manhattan-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Instance.Network.Manhattan }
         *     
         */
        public void setManhattan(Instance.Network.Manhattan value) {
            this.manhattan = value;
        }

        /**
         * Ruft den Wert der distanceCalculator-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDistanceCalculator() {
            return distanceCalculator;
        }

        /**
         * Legt den Wert der distanceCalculator-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDistanceCalculator(String value) {
            this.distanceCalculator = value;
        }

        /**
         * Ruft den Wert der ceil-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link Instance.Network.Ceil }
         *     
         */
        public Instance.Network.Ceil getCeil() {
            return ceil;
        }

        /**
         * Legt den Wert der ceil-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Instance.Network.Ceil }
         *     
         */
        public void setCeil(Instance.Network.Ceil value) {
            this.ceil = value;
        }

        /**
         * Ruft den Wert der floor-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link Instance.Network.Floor }
         *     
         */
        public Instance.Network.Floor getFloor() {
            return floor;
        }

        /**
         * Legt den Wert der floor-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Instance.Network.Floor }
         *     
         */
        public void setFloor(Instance.Network.Floor value) {
            this.floor = value;
        }

        /**
         * Ruft den Wert der decimals-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getDecimals() {
            return decimals;
        }

        /**
         * Legt den Wert der decimals-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setDecimals(BigInteger value) {
            this.decimals = value;
        }

        /**
         * Ruft den Wert der custom-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link Custom }
         *     
         */
        public Custom getCustom() {
            return custom;
        }

        /**
         * Legt den Wert der custom-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Custom }
         *     
         */
        public void setCustom(Custom value) {
            this.custom = value;
        }


        /**
         * <p>Java-Klasse für anonymous complex type.
         * 
         * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Ceil {


        }


        /**
         * <p>Java-Klasse für anonymous complex type.
         * 
         * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Euclidean {


        }


        /**
         * <p>Java-Klasse für anonymous complex type.
         * 
         * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Floor {


        }


        /**
         * <p>Java-Klasse für anonymous complex type.
         * 
         * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="link" maxOccurs="unbounded">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="length" type="{}positive_double" minOccurs="0"/>
         *                   &lt;choice minOccurs="0">
         *                     &lt;element name="travel_time" type="{}positive_double"/>
         *                     &lt;element name="td_travel_time" type="{}time_dependent_parameter_type" maxOccurs="unbounded"/>
         *                     &lt;element name="uncertain_travel_time" type="{}uncertain_parameter_type"/>
         *                   &lt;/choice>
         *                   &lt;choice minOccurs="0">
         *                     &lt;element name="travel_cost" type="{}positive_double"/>
         *                     &lt;element name="td_travel_cost" type="{}time_dependent_parameter_type" maxOccurs="unbounded"/>
         *                     &lt;element name="uncertain_travel_cost" type="{}uncertain_parameter_type"/>
         *                   &lt;/choice>
         *                   &lt;element ref="{}tw" maxOccurs="unbounded" minOccurs="0"/>
         *                   &lt;element ref="{}custom" minOccurs="0"/>
         *                 &lt;/sequence>
         *                 &lt;attribute name="tail" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
         *                 &lt;attribute name="head" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
         *                 &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}integer" />
         *                 &lt;attribute name="directed" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
         *                 &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}integer" />
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *       &lt;attribute name="symmetric" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "link"
        })
        public static class Links {

            @XmlElement(required = true)
            protected List<Instance.Network.Links.Link> link;
            @XmlAttribute(name = "symmetric", required = true)
            protected boolean symmetric;

            /**
             * Gets the value of the link property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the link property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getLink().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link Instance.Network.Links.Link }
             * 
             * 
             */
            public List<Instance.Network.Links.Link> getLink() {
                if (link == null) {
                    link = new ArrayList<Instance.Network.Links.Link>();
                }
                return this.link;
            }

            /**
             * Ruft den Wert der symmetric-Eigenschaft ab.
             * 
             */
            public boolean isSymmetric() {
                return symmetric;
            }

            /**
             * Legt den Wert der symmetric-Eigenschaft fest.
             * 
             */
            public void setSymmetric(boolean value) {
                this.symmetric = value;
            }


            /**
             * <p>Java-Klasse für anonymous complex type.
             * 
             * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="length" type="{}positive_double" minOccurs="0"/>
             *         &lt;choice minOccurs="0">
             *           &lt;element name="travel_time" type="{}positive_double"/>
             *           &lt;element name="td_travel_time" type="{}time_dependent_parameter_type" maxOccurs="unbounded"/>
             *           &lt;element name="uncertain_travel_time" type="{}uncertain_parameter_type"/>
             *         &lt;/choice>
             *         &lt;choice minOccurs="0">
             *           &lt;element name="travel_cost" type="{}positive_double"/>
             *           &lt;element name="td_travel_cost" type="{}time_dependent_parameter_type" maxOccurs="unbounded"/>
             *           &lt;element name="uncertain_travel_cost" type="{}uncertain_parameter_type"/>
             *         &lt;/choice>
             *         &lt;element ref="{}tw" maxOccurs="unbounded" minOccurs="0"/>
             *         &lt;element ref="{}custom" minOccurs="0"/>
             *       &lt;/sequence>
             *       &lt;attribute name="tail" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
             *       &lt;attribute name="head" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
             *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}integer" />
             *       &lt;attribute name="directed" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
             *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}integer" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "length",
                "travelTime",
                "tdTravelTime",
                "uncertainTravelTime",
                "travelCost",
                "tdTravelCost",
                "uncertainTravelCost",
                "tw",
                "custom"
            })
            public static class Link {

                protected Double length;
                @XmlElement(name = "travel_time")
                protected Double travelTime;
                @XmlElement(name = "td_travel_time")
                protected List<TimeDependentParameterType> tdTravelTime;
                @XmlElement(name = "uncertain_travel_time")
                protected UncertainParameterType uncertainTravelTime;
                @XmlElement(name = "travel_cost")
                protected Double travelCost;
                @XmlElement(name = "td_travel_cost")
                protected List<TimeDependentParameterType> tdTravelCost;
                @XmlElement(name = "uncertain_travel_cost")
                protected UncertainParameterType uncertainTravelCost;
                protected List<Tw> tw;
                protected Custom custom;
                @XmlAttribute(name = "tail", required = true)
                protected BigInteger tail;
                @XmlAttribute(name = "head", required = true)
                protected BigInteger head;
                @XmlAttribute(name = "id")
                protected BigInteger id;
                @XmlAttribute(name = "directed")
                protected Boolean directed;
                @XmlAttribute(name = "type")
                protected BigInteger type;

                /**
                 * Ruft den Wert der length-Eigenschaft ab.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Double }
                 *     
                 */
                public Double getLength() {
                    return length;
                }

                /**
                 * Legt den Wert der length-Eigenschaft fest.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Double }
                 *     
                 */
                public void setLength(Double value) {
                    this.length = value;
                }

                /**
                 * Ruft den Wert der travelTime-Eigenschaft ab.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Double }
                 *     
                 */
                public Double getTravelTime() {
                    return travelTime;
                }

                /**
                 * Legt den Wert der travelTime-Eigenschaft fest.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Double }
                 *     
                 */
                public void setTravelTime(Double value) {
                    this.travelTime = value;
                }

                /**
                 * Gets the value of the tdTravelTime property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the tdTravelTime property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getTdTravelTime().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link TimeDependentParameterType }
                 * 
                 * 
                 */
                public List<TimeDependentParameterType> getTdTravelTime() {
                    if (tdTravelTime == null) {
                        tdTravelTime = new ArrayList<TimeDependentParameterType>();
                    }
                    return this.tdTravelTime;
                }

                /**
                 * Ruft den Wert der uncertainTravelTime-Eigenschaft ab.
                 * 
                 * @return
                 *     possible object is
                 *     {@link UncertainParameterType }
                 *     
                 */
                public UncertainParameterType getUncertainTravelTime() {
                    return uncertainTravelTime;
                }

                /**
                 * Legt den Wert der uncertainTravelTime-Eigenschaft fest.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link UncertainParameterType }
                 *     
                 */
                public void setUncertainTravelTime(UncertainParameterType value) {
                    this.uncertainTravelTime = value;
                }

                /**
                 * Ruft den Wert der travelCost-Eigenschaft ab.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Double }
                 *     
                 */
                public Double getTravelCost() {
                    return travelCost;
                }

                /**
                 * Legt den Wert der travelCost-Eigenschaft fest.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Double }
                 *     
                 */
                public void setTravelCost(Double value) {
                    this.travelCost = value;
                }

                /**
                 * Gets the value of the tdTravelCost property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the tdTravelCost property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getTdTravelCost().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link TimeDependentParameterType }
                 * 
                 * 
                 */
                public List<TimeDependentParameterType> getTdTravelCost() {
                    if (tdTravelCost == null) {
                        tdTravelCost = new ArrayList<TimeDependentParameterType>();
                    }
                    return this.tdTravelCost;
                }

                /**
                 * Ruft den Wert der uncertainTravelCost-Eigenschaft ab.
                 * 
                 * @return
                 *     possible object is
                 *     {@link UncertainParameterType }
                 *     
                 */
                public UncertainParameterType getUncertainTravelCost() {
                    return uncertainTravelCost;
                }

                /**
                 * Legt den Wert der uncertainTravelCost-Eigenschaft fest.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link UncertainParameterType }
                 *     
                 */
                public void setUncertainTravelCost(UncertainParameterType value) {
                    this.uncertainTravelCost = value;
                }

                /**
                 * Time windows. Use these elements to model the link's availability Gets the value of the tw property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the tw property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getTw().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link Tw }
                 * 
                 * 
                 */
                public List<Tw> getTw() {
                    if (tw == null) {
                        tw = new ArrayList<Tw>();
                    }
                    return this.tw;
                }

                /**
                 * Custom element. Use this element to define link attributes that are not pre-defined in the specification
                 * 
                 * @return
                 *     possible object is
                 *     {@link Custom }
                 *     
                 */
                public Custom getCustom() {
                    return custom;
                }

                /**
                 * Legt den Wert der custom-Eigenschaft fest.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Custom }
                 *     
                 */
                public void setCustom(Custom value) {
                    this.custom = value;
                }

                /**
                 * Ruft den Wert der tail-Eigenschaft ab.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BigInteger }
                 *     
                 */
                public BigInteger getTail() {
                    return tail;
                }

                /**
                 * Legt den Wert der tail-Eigenschaft fest.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BigInteger }
                 *     
                 */
                public void setTail(BigInteger value) {
                    this.tail = value;
                }

                /**
                 * Ruft den Wert der head-Eigenschaft ab.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BigInteger }
                 *     
                 */
                public BigInteger getHead() {
                    return head;
                }

                /**
                 * Legt den Wert der head-Eigenschaft fest.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BigInteger }
                 *     
                 */
                public void setHead(BigInteger value) {
                    this.head = value;
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
                 * Ruft den Wert der directed-Eigenschaft ab.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Boolean }
                 *     
                 */
                public boolean isDirected() {
                    if (directed == null) {
                        return false;
                    } else {
                        return directed;
                    }
                }

                /**
                 * Legt den Wert der directed-Eigenschaft fest.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Boolean }
                 *     
                 */
                public void setDirected(Boolean value) {
                    this.directed = value;
                }

                /**
                 * Ruft den Wert der type-Eigenschaft ab.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BigInteger }
                 *     
                 */
                public BigInteger getType() {
                    return type;
                }

                /**
                 * Legt den Wert der type-Eigenschaft fest.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BigInteger }
                 *     
                 */
                public void setType(BigInteger value) {
                    this.type = value;
                }

            }

        }


        /**
         * <p>Java-Klasse für anonymous complex type.
         * 
         * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Manhattan {


        }


        /**
         * <p>Java-Klasse für anonymous complex type.
         * 
         * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="node" maxOccurs="unbounded" minOccurs="2">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;choice minOccurs="0">
         *                     &lt;sequence>
         *                       &lt;element name="cx" type="{http://www.w3.org/2001/XMLSchema}double"/>
         *                       &lt;element name="cy" type="{http://www.w3.org/2001/XMLSchema}double"/>
         *                       &lt;element name="cz" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
         *                     &lt;/sequence>
         *                     &lt;sequence>
         *                       &lt;element name="latitude" type="{http://www.w3.org/2001/XMLSchema}double"/>
         *                       &lt;element name="longitude" type="{http://www.w3.org/2001/XMLSchema}double"/>
         *                     &lt;/sequence>
         *                   &lt;/choice>
         *                   &lt;element name="compatible_vehicle" type="{http://www.w3.org/2001/XMLSchema}integer" maxOccurs="unbounded" minOccurs="0"/>
         *                   &lt;element ref="{}custom" minOccurs="0"/>
         *                 &lt;/sequence>
         *                 &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
         *                 &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
         *                 &lt;attribute name="trailer" type="{http://www.w3.org/2001/XMLSchema}boolean" />
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "node"
        })
        public static class Nodes {

            @XmlElement(required = true)
            protected List<Instance.Network.Nodes.Node> node;

            /**
             * Gets the value of the node property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the node property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getNode().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link Instance.Network.Nodes.Node }
             * 
             * 
             */
            public List<Instance.Network.Nodes.Node> getNode() {
                if (node == null) {
                    node = new ArrayList<Instance.Network.Nodes.Node>();
                }
                return this.node;
            }


            /**
             * <p>Java-Klasse für anonymous complex type.
             * 
             * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;choice minOccurs="0">
             *           &lt;sequence>
             *             &lt;element name="cx" type="{http://www.w3.org/2001/XMLSchema}double"/>
             *             &lt;element name="cy" type="{http://www.w3.org/2001/XMLSchema}double"/>
             *             &lt;element name="cz" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
             *           &lt;/sequence>
             *           &lt;sequence>
             *             &lt;element name="latitude" type="{http://www.w3.org/2001/XMLSchema}double"/>
             *             &lt;element name="longitude" type="{http://www.w3.org/2001/XMLSchema}double"/>
             *           &lt;/sequence>
             *         &lt;/choice>
             *         &lt;element name="compatible_vehicle" type="{http://www.w3.org/2001/XMLSchema}integer" maxOccurs="unbounded" minOccurs="0"/>
             *         &lt;element ref="{}custom" minOccurs="0"/>
             *       &lt;/sequence>
             *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
             *       &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
             *       &lt;attribute name="trailer" type="{http://www.w3.org/2001/XMLSchema}boolean" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "cx",
                "cy",
                "cz",
                "latitude",
                "longitude",
                "compatibleVehicle",
                "custom"
            })
            public static class Node {

                protected Double cx;
                protected Double cy;
                protected Double cz;
                protected Double latitude;
                protected Double longitude;
                @XmlElement(name = "compatible_vehicle")
                protected List<BigInteger> compatibleVehicle;
                protected Custom custom;
                @XmlAttribute(name = "id", required = true)
                protected BigInteger id;
                @XmlAttribute(name = "type", required = true)
                protected BigInteger type;
                @XmlAttribute(name = "trailer")
                protected Boolean trailer;

                /**
                 * Ruft den Wert der cx-Eigenschaft ab.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Double }
                 *     
                 */
                public Double getCx() {
                    return cx;
                }

                /**
                 * Legt den Wert der cx-Eigenschaft fest.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Double }
                 *     
                 */
                public void setCx(Double value) {
                    this.cx = value;
                }

                /**
                 * Ruft den Wert der cy-Eigenschaft ab.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Double }
                 *     
                 */
                public Double getCy() {
                    return cy;
                }

                /**
                 * Legt den Wert der cy-Eigenschaft fest.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Double }
                 *     
                 */
                public void setCy(Double value) {
                    this.cy = value;
                }

                /**
                 * Ruft den Wert der cz-Eigenschaft ab.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Double }
                 *     
                 */
                public Double getCz() {
                    return cz;
                }

                /**
                 * Legt den Wert der cz-Eigenschaft fest.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Double }
                 *     
                 */
                public void setCz(Double value) {
                    this.cz = value;
                }

                /**
                 * Ruft den Wert der latitude-Eigenschaft ab.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Double }
                 *     
                 */
                public Double getLatitude() {
                    return latitude;
                }

                /**
                 * Legt den Wert der latitude-Eigenschaft fest.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Double }
                 *     
                 */
                public void setLatitude(Double value) {
                    this.latitude = value;
                }

                /**
                 * Ruft den Wert der longitude-Eigenschaft ab.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Double }
                 *     
                 */
                public Double getLongitude() {
                    return longitude;
                }

                /**
                 * Legt den Wert der longitude-Eigenschaft fest.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Double }
                 *     
                 */
                public void setLongitude(Double value) {
                    this.longitude = value;
                }

                /**
                 * Gets the value of the compatibleVehicle property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the compatibleVehicle property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getCompatibleVehicle().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link BigInteger }
                 * 
                 * 
                 */
                public List<BigInteger> getCompatibleVehicle() {
                    if (compatibleVehicle == null) {
                        compatibleVehicle = new ArrayList<BigInteger>();
                    }
                    return this.compatibleVehicle;
                }

                /**
                 * Custom element. Use this element to define VRP features that are not pre-defined in the specification
                 * 
                 * @return
                 *     possible object is
                 *     {@link Custom }
                 *     
                 */
                public Custom getCustom() {
                    return custom;
                }

                /**
                 * Legt den Wert der custom-Eigenschaft fest.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Custom }
                 *     
                 */
                public void setCustom(Custom value) {
                    this.custom = value;
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
                 * Ruft den Wert der type-Eigenschaft ab.
                 * 
                 * @return
                 *     possible object is
                 *     {@link BigInteger }
                 *     
                 */
                public BigInteger getType() {
                    return type;
                }

                /**
                 * Legt den Wert der type-Eigenschaft fest.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link BigInteger }
                 *     
                 */
                public void setType(BigInteger value) {
                    this.type = value;
                }

                /**
                 * Ruft den Wert der trailer-Eigenschaft ab.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Boolean }
                 *     
                 */
                public Boolean isTrailer() {
                    return trailer;
                }

                /**
                 * Legt den Wert der trailer-Eigenschaft fest.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Boolean }
                 *     
                 */
                public void setTrailer(Boolean value) {
                    this.trailer = value;
                }

            }

        }

    }


    /**
     * <p>Java-Klasse für anonymous complex type.
     * 
     * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="request" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="release" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
     *                   &lt;element name="priority" type="{}positive_double" minOccurs="0"/>
     *                   &lt;element name="prize" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
     *                   &lt;element ref="{}tw" maxOccurs="unbounded" minOccurs="0"/>
     *                   &lt;choice minOccurs="0">
     *                     &lt;element name="quantity" type="{http://www.w3.org/2001/XMLSchema}double"/>
     *                     &lt;element name="td_quantity" type="{}time_dependent_parameter_type"/>
     *                     &lt;element name="uncertain_quantity" type="{}uncertain_parameter_type"/>
     *                   &lt;/choice>
     *                   &lt;choice minOccurs="0">
     *                     &lt;element name="service_time" type="{http://www.w3.org/2001/XMLSchema}double"/>
     *                     &lt;element name="td_service_time" type="{}time_dependent_parameter_type"/>
     *                     &lt;element name="uncertain_service_time" type="{}uncertain_parameter_type"/>
     *                   &lt;/choice>
     *                   &lt;element ref="{}dimensions" minOccurs="0"/>
     *                   &lt;element name="predecessors" minOccurs="0">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="request" type="{http://www.w3.org/2001/XMLSchema}integer" maxOccurs="unbounded"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="successors" minOccurs="0">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="request" type="{http://www.w3.org/2001/XMLSchema}integer" maxOccurs="unbounded"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="skill" type="{http://www.w3.org/2001/XMLSchema}integer" maxOccurs="unbounded" minOccurs="0"/>
     *                   &lt;element name="resource" maxOccurs="unbounded" minOccurs="0">
     *                     &lt;complexType>
     *                       &lt;simpleContent>
     *                         &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>double">
     *                           &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
     *                         &lt;/extension>
     *                       &lt;/simpleContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element ref="{}custom" minOccurs="0"/>
     *                 &lt;/sequence>
     *                 &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
     *                 &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}integer" />
     *                 &lt;attribute name="node" type="{http://www.w3.org/2001/XMLSchema}integer" />
     *                 &lt;attribute name="link" type="{http://www.w3.org/2001/XMLSchema}integer" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="request_incompatibility" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;choice>
     *                   &lt;sequence>
     *                     &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}integer"/>
     *                     &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}integer"/>
     *                   &lt;/sequence>
     *                   &lt;sequence>
     *                     &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}integer"/>
     *                     &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}integer"/>
     *                   &lt;/sequence>
     *                 &lt;/choice>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "request",
        "requestIncompatibility"
    })
    public static class Requests {

        @XmlElement(required = true)
        protected List<Instance.Requests.Request> request;
        @XmlElement(name = "request_incompatibility")
        protected List<Instance.Requests.RequestIncompatibility> requestIncompatibility;

        /**
         * Gets the value of the request property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the request property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getRequest().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Instance.Requests.Request }
         * 
         * 
         */
        public List<Instance.Requests.Request> getRequest() {
            if (request == null) {
                request = new ArrayList<Instance.Requests.Request>();
            }
            return this.request;
        }

        /**
         * Gets the value of the requestIncompatibility property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the requestIncompatibility property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getRequestIncompatibility().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Instance.Requests.RequestIncompatibility }
         * 
         * 
         */
        public List<Instance.Requests.RequestIncompatibility> getRequestIncompatibility() {
            if (requestIncompatibility == null) {
                requestIncompatibility = new ArrayList<Instance.Requests.RequestIncompatibility>();
            }
            return this.requestIncompatibility;
        }


        /**
         * <p>Java-Klasse für anonymous complex type.
         * 
         * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="release" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
         *         &lt;element name="priority" type="{}positive_double" minOccurs="0"/>
         *         &lt;element name="prize" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
         *         &lt;element ref="{}tw" maxOccurs="unbounded" minOccurs="0"/>
         *         &lt;choice minOccurs="0">
         *           &lt;element name="quantity" type="{http://www.w3.org/2001/XMLSchema}double"/>
         *           &lt;element name="td_quantity" type="{}time_dependent_parameter_type"/>
         *           &lt;element name="uncertain_quantity" type="{}uncertain_parameter_type"/>
         *         &lt;/choice>
         *         &lt;choice minOccurs="0">
         *           &lt;element name="service_time" type="{http://www.w3.org/2001/XMLSchema}double"/>
         *           &lt;element name="td_service_time" type="{}time_dependent_parameter_type"/>
         *           &lt;element name="uncertain_service_time" type="{}uncertain_parameter_type"/>
         *         &lt;/choice>
         *         &lt;element ref="{}dimensions" minOccurs="0"/>
         *         &lt;element name="predecessors" minOccurs="0">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="request" type="{http://www.w3.org/2001/XMLSchema}integer" maxOccurs="unbounded"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="successors" minOccurs="0">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="request" type="{http://www.w3.org/2001/XMLSchema}integer" maxOccurs="unbounded"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="skill" type="{http://www.w3.org/2001/XMLSchema}integer" maxOccurs="unbounded" minOccurs="0"/>
         *         &lt;element name="resource" maxOccurs="unbounded" minOccurs="0">
         *           &lt;complexType>
         *             &lt;simpleContent>
         *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>double">
         *                 &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
         *               &lt;/extension>
         *             &lt;/simpleContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element ref="{}custom" minOccurs="0"/>
         *       &lt;/sequence>
         *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
         *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}integer" />
         *       &lt;attribute name="node" type="{http://www.w3.org/2001/XMLSchema}integer" />
         *       &lt;attribute name="link" type="{http://www.w3.org/2001/XMLSchema}integer" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "release",
            "priority",
            "prize",
            "tw",
            "quantity",
            "tdQuantity",
            "uncertainQuantity",
            "serviceTime",
            "tdServiceTime",
            "uncertainServiceTime",
            "dimensions",
            "predecessors",
            "successors",
            "skill",
            "resource",
            "custom"
        })
        public static class Request {

            protected Double release;
            protected Double priority;
            protected Double prize;
            protected List<Tw> tw;
            protected Double quantity;
            @XmlElement(name = "td_quantity")
            protected TimeDependentParameterType tdQuantity;
            @XmlElement(name = "uncertain_quantity")
            protected UncertainParameterType uncertainQuantity;
            @XmlElement(name = "service_time")
            protected Double serviceTime;
            @XmlElement(name = "td_service_time")
            protected TimeDependentParameterType tdServiceTime;
            @XmlElement(name = "uncertain_service_time")
            protected UncertainParameterType uncertainServiceTime;
            protected DimensionsType dimensions;
            protected Instance.Requests.Request.Predecessors predecessors;
            protected Instance.Requests.Request.Successors successors;
            protected List<BigInteger> skill;
            protected List<Instance.Requests.Request.Resource> resource;
            protected Custom custom;
            @XmlAttribute(name = "id", required = true)
            protected BigInteger id;
            @XmlAttribute(name = "type")
            protected BigInteger type;
            @XmlAttribute(name = "node")
            protected BigInteger node;
            @XmlAttribute(name = "link")
            protected BigInteger link;

            /**
             * Ruft den Wert der release-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link Double }
             *     
             */
            public Double getRelease() {
                return release;
            }

            /**
             * Legt den Wert der release-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link Double }
             *     
             */
            public void setRelease(Double value) {
                this.release = value;
            }

            /**
             * Ruft den Wert der priority-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link Double }
             *     
             */
            public Double getPriority() {
                return priority;
            }

            /**
             * Legt den Wert der priority-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link Double }
             *     
             */
            public void setPriority(Double value) {
                this.priority = value;
            }

            /**
             * Ruft den Wert der prize-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link Double }
             *     
             */
            public Double getPrize() {
                return prize;
            }

            /**
             * Legt den Wert der prize-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link Double }
             *     
             */
            public void setPrize(Double value) {
                this.prize = value;
            }

            /**
             * Model request time windows (e.g., VRPTW) Gets the value of the tw property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the tw property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getTw().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link Tw }
             * 
             * 
             */
            public List<Tw> getTw() {
                if (tw == null) {
                    tw = new ArrayList<Tw>();
                }
                return this.tw;
            }

            /**
             * Ruft den Wert der quantity-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link Double }
             *     
             */
            public Double getQuantity() {
                return quantity;
            }

            /**
             * Legt den Wert der quantity-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link Double }
             *     
             */
            public void setQuantity(Double value) {
                this.quantity = value;
            }

            /**
             * Ruft den Wert der tdQuantity-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link TimeDependentParameterType }
             *     
             */
            public TimeDependentParameterType getTdQuantity() {
                return tdQuantity;
            }

            /**
             * Legt den Wert der tdQuantity-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link TimeDependentParameterType }
             *     
             */
            public void setTdQuantity(TimeDependentParameterType value) {
                this.tdQuantity = value;
            }

            /**
             * Ruft den Wert der uncertainQuantity-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link UncertainParameterType }
             *     
             */
            public UncertainParameterType getUncertainQuantity() {
                return uncertainQuantity;
            }

            /**
             * Legt den Wert der uncertainQuantity-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link UncertainParameterType }
             *     
             */
            public void setUncertainQuantity(UncertainParameterType value) {
                this.uncertainQuantity = value;
            }

            /**
             * Ruft den Wert der serviceTime-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link Double }
             *     
             */
            public Double getServiceTime() {
                return serviceTime;
            }

            /**
             * Legt den Wert der serviceTime-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link Double }
             *     
             */
            public void setServiceTime(Double value) {
                this.serviceTime = value;
            }

            /**
             * Ruft den Wert der tdServiceTime-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link TimeDependentParameterType }
             *     
             */
            public TimeDependentParameterType getTdServiceTime() {
                return tdServiceTime;
            }

            /**
             * Legt den Wert der tdServiceTime-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link TimeDependentParameterType }
             *     
             */
            public void setTdServiceTime(TimeDependentParameterType value) {
                this.tdServiceTime = value;
            }

            /**
             * Ruft den Wert der uncertainServiceTime-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link UncertainParameterType }
             *     
             */
            public UncertainParameterType getUncertainServiceTime() {
                return uncertainServiceTime;
            }

            /**
             * Legt den Wert der uncertainServiceTime-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link UncertainParameterType }
             *     
             */
            public void setUncertainServiceTime(UncertainParameterType value) {
                this.uncertainServiceTime = value;
            }

            /**
             * Ruft den Wert der dimensions-Eigenschaft ab.
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
             * Ruft den Wert der predecessors-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link Instance.Requests.Request.Predecessors }
             *     
             */
            public Instance.Requests.Request.Predecessors getPredecessors() {
                return predecessors;
            }

            /**
             * Legt den Wert der predecessors-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link Instance.Requests.Request.Predecessors }
             *     
             */
            public void setPredecessors(Instance.Requests.Request.Predecessors value) {
                this.predecessors = value;
            }

            /**
             * Ruft den Wert der successors-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link Instance.Requests.Request.Successors }
             *     
             */
            public Instance.Requests.Request.Successors getSuccessors() {
                return successors;
            }

            /**
             * Legt den Wert der successors-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link Instance.Requests.Request.Successors }
             *     
             */
            public void setSuccessors(Instance.Requests.Request.Successors value) {
                this.successors = value;
            }

            /**
             * Gets the value of the skill property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the skill property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getSkill().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link BigInteger }
             * 
             * 
             */
            public List<BigInteger> getSkill() {
                if (skill == null) {
                    skill = new ArrayList<BigInteger>();
                }
                return this.skill;
            }

            /**
             * Gets the value of the resource property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the resource property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getResource().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link Instance.Requests.Request.Resource }
             * 
             * 
             */
            public List<Instance.Requests.Request.Resource> getResource() {
                if (resource == null) {
                    resource = new ArrayList<Instance.Requests.Request.Resource>();
                }
                return this.resource;
            }

            /**
             * Custom element. Use this element to define request attributes that are not pre-defined in the specification
             * 
             * @return
             *     possible object is
             *     {@link Custom }
             *     
             */
            public Custom getCustom() {
                return custom;
            }

            /**
             * Legt den Wert der custom-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link Custom }
             *     
             */
            public void setCustom(Custom value) {
                this.custom = value;
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
             * Ruft den Wert der type-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link BigInteger }
             *     
             */
            public BigInteger getType() {
                return type;
            }

            /**
             * Legt den Wert der type-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link BigInteger }
             *     
             */
            public void setType(BigInteger value) {
                this.type = value;
            }

            /**
             * Ruft den Wert der node-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link BigInteger }
             *     
             */
            public BigInteger getNode() {
                return node;
            }

            /**
             * Legt den Wert der node-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link BigInteger }
             *     
             */
            public void setNode(BigInteger value) {
                this.node = value;
            }

            /**
             * Ruft den Wert der link-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link BigInteger }
             *     
             */
            public BigInteger getLink() {
                return link;
            }

            /**
             * Legt den Wert der link-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link BigInteger }
             *     
             */
            public void setLink(BigInteger value) {
                this.link = value;
            }


            /**
             * <p>Java-Klasse für anonymous complex type.
             * 
             * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="request" type="{http://www.w3.org/2001/XMLSchema}integer" maxOccurs="unbounded"/>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "request"
            })
            public static class Predecessors {

                @XmlElement(required = true)
                protected List<BigInteger> request;

                /**
                 * Gets the value of the request property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the request property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getRequest().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link BigInteger }
                 * 
                 * 
                 */
                public List<BigInteger> getRequest() {
                    if (request == null) {
                        request = new ArrayList<BigInteger>();
                    }
                    return this.request;
                }

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
            public static class Resource {

                @XmlValue
                protected double value;
                @XmlAttribute(name = "id", required = true)
                protected BigInteger id;

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

            }


            /**
             * <p>Java-Klasse für anonymous complex type.
             * 
             * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="request" type="{http://www.w3.org/2001/XMLSchema}integer" maxOccurs="unbounded"/>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "request"
            })
            public static class Successors {

                @XmlElement(required = true)
                protected List<BigInteger> request;

                /**
                 * Gets the value of the request property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the request property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getRequest().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link BigInteger }
                 * 
                 * 
                 */
                public List<BigInteger> getRequest() {
                    if (request == null) {
                        request = new ArrayList<BigInteger>();
                    }
                    return this.request;
                }

            }

        }


        /**
         * <p>Java-Klasse für anonymous complex type.
         * 
         * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;choice>
         *         &lt;sequence>
         *           &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}integer"/>
         *           &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}integer"/>
         *         &lt;/sequence>
         *         &lt;sequence>
         *           &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}integer"/>
         *           &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}integer"/>
         *         &lt;/sequence>
         *       &lt;/choice>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "content"
        })
        public static class RequestIncompatibility {

            @XmlElementRefs({
                @XmlElementRef(name = "type", type = JAXBElement.class, required = false),
                @XmlElementRef(name = "id", type = JAXBElement.class, required = false)
            })
            protected List<JAXBElement<BigInteger>> content;

            /**
             * Ruft das restliche Contentmodell ab. 
             * 
             * <p>
             * Sie rufen diese "catch-all"-Eigenschaft aus folgendem Grund ab: 
             * Der Feldname "Type" wird von zwei verschiedenen Teilen eines Schemas verwendet. Siehe: 
             * Zeile 813 von file:/Users/hschneid/Desktop/XJC/vrp-rep-instance-specification-0.5.0.xsd
             * Zeile 808 von file:/Users/hschneid/Desktop/XJC/vrp-rep-instance-specification-0.5.0.xsd
             * <p>
             * Um diese Eigenschaft zu entfernen, wenden Sie eine Eigenschaftenanpassung für eine
             * der beiden folgenden Deklarationen an, um deren Namen zu ändern: 
             * Gets the value of the content property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the content property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getContent().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link JAXBElement }{@code <}{@link BigInteger }{@code >}
             * {@link JAXBElement }{@code <}{@link BigInteger }{@code >}
             * 
             * 
             */
            public List<JAXBElement<BigInteger>> getContent() {
                if (content == null) {
                    content = new ArrayList<JAXBElement<BigInteger>>();
                }
                return this.content;
            }

        }

    }


    /**
     * <p>Java-Klasse für anonymous complex type.
     * 
     * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="resource" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;simpleContent>
     *               &lt;extension base="&lt;>positive_double">
     *                 &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
     *                 &lt;attribute name="renewable" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
     *                 &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *               &lt;/extension>
     *             &lt;/simpleContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "resource"
    })
    public static class Resources {

        @XmlElement(required = true)
        protected List<Instance.Resources.Resource> resource;

        /**
         * Gets the value of the resource property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the resource property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getResource().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Instance.Resources.Resource }
         * 
         * 
         */
        public List<Instance.Resources.Resource> getResource() {
            if (resource == null) {
                resource = new ArrayList<Instance.Resources.Resource>();
            }
            return this.resource;
        }


        /**
         * <p>Java-Klasse für anonymous complex type.
         * 
         * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;simpleContent>
         *     &lt;extension base="&lt;>positive_double">
         *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
         *       &lt;attribute name="renewable" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
         *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
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
        public static class Resource {

            @XmlValue
            protected double value;
            @XmlAttribute(name = "id", required = true)
            protected BigInteger id;
            @XmlAttribute(name = "renewable", required = true)
            protected boolean renewable;
            @XmlAttribute(name = "name", required = true)
            protected String name;

            /**
             * Double restricted to be greater than or equal to 0
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
             * Ruft den Wert der renewable-Eigenschaft ab.
             * 
             */
            public boolean isRenewable() {
                return renewable;
            }

            /**
             * Legt den Wert der renewable-Eigenschaft fest.
             * 
             */
            public void setRenewable(boolean value) {
                this.renewable = value;
            }

            /**
             * Ruft den Wert der name-Eigenschaft ab.
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
             * Legt den Wert der name-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setName(String value) {
                this.name = value;
            }

        }

    }

}
