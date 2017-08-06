package util.instances;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;


public class InstanceBuilder {

	/**
	 * Example method that returns an Instance object from an XML file.
	 * 
	 * @param inputPath
	 * @throws JAXBException
	 */
	public static Instance read(Path inputPath) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(Instance.class);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		return (Instance) unmarshaller.unmarshal(inputPath.toFile());
	}

	/**
	 * Example method that writes XML from an Instance object.
	 * This method also process a basic validation when marshalling.
	 * 
	 * @param instance
	 * @param outputPath
	 * @throws SAXException
	 * @throws JAXBException
	 */
	public static File write(Instance instance, Path outputPath) throws SAXException, JAXBException {
		outputPath.getParent().toFile().mkdirs();

		InputStream stream = Instance.class.getResourceAsStream("/xsd/instance.xsd");
		Source schemaSource = new StreamSource(stream);
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = sf.newSchema(schemaSource);

		JAXBContext jc = JAXBContext.newInstance(Instance.class);
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setSchema(schema);
		marshaller.setEventHandler(new ValidationEventHandler(){
			public boolean handleEvent(ValidationEvent event) {
				System.err.println("MESSAGE:  " + event.getMessage());
				return true;
			}});
		marshaller.marshal(instance, outputPath.toFile());

		return outputPath.toFile();
	}
}
