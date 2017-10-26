
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

import peoplestore.generated.*;

public class HealthProfileWriter {

    Document doc;
    XPath xpath;
    //public static PeopleStore people = new PeopleStore();

    public void loadXML() throws ParserConfigurationException, SAXException, IOException {

        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true);
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        doc = builder.parse("people.xml");

        //creating xpath object
        getXPathObj();
    }

    public XPath getXPathObj() {

        XPathFactory factory = XPathFactory.newInstance();
        xpath = factory.newXPath();
        return xpath;
    }
    
    public Node getActivityDescription(String id) throws XPathExpressionException {
    	
    	XPathExpression expr = xpath.compile("/people/person[@id='"+id+"']/activitypreference/description");
    	Node node = (Node) expr.evaluate(doc, XPathConstants.NODE);
    	return node;
    }
    
    
    public Node getActivityPlace(String id) throws XPathExpressionException {
    	
    	XPathExpression expr = xpath.compile("/people/person[@id='"+id+"']/activitypreference/place");
    	Node node = (Node) expr.evaluate(doc, XPathConstants.NODE);
    	return node;
    }
    
    public NodeList getActivityPreference(String id) throws XPathExpressionException {
    	
    	XPathExpression expr = xpath.compile("/people/person[@id='"+id+"']/activitypreference");
    	NodeList node = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
    	return node;
    }
    
    public NodeList getPeopleList() throws XPathExpressionException {

        XPathExpression expr = xpath.compile("/people/person");
        NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        return nodes;

    }
    
    public NodeList filterActivities(String date, String condition) throws XPathExpressionException {
    	String x = "/people/person[translate(activitypreference/startdate, \"-:T\",\"\") " + condition + " translate(\"" + date + "\",\"-:T\",\"\")]";
        XPathExpression expr = xpath.compile(x);
        NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        return nodes;

    }

    public static void main(String[] args) throws ParserConfigurationException, SAXException,
            IOException, XPathExpressionException, ParseException, JAXBException {
    	
    	String choose;
    	if (args.length == 1) {
    		choose = args[0];
    	} else return;
    	
    	try {

        	HealthProfileWriter test = new HealthProfileWriter();
            test.loadXML();
            
            Node node;
            NodeList nodes;
            
            if (choose.equals("1")) {

	            nodes = test.getPeopleList();
	            for (int i=0; i<nodes.getLength(); i++) {
	            	NodeList children = nodes.item(i).getChildNodes();
	            	System.out.println("Person-"+i);
	                System.out.println("- " + children.item(1).getNodeName() + " => " + children.item(1).getTextContent());
	                System.out.println("- " + children.item(3).getNodeName() + " => " + children.item(3).getTextContent());
	                System.out.println("- " + children.item(5).getNodeName() + " => " + children.item(5).getTextContent());
	                System.out.println("\n");
	            }
            }
            
            if (choose.equals("2")) {
            
	            nodes = test.getActivityPreference("5");
	            nodes = nodes.item(0).getChildNodes();
	            for (int i=1; i<nodes.getLength(); i=i+2) {
	            	System.out.println("- " + nodes.item(i).getNodeName() + " ==> " + nodes.item(i).getTextContent());
	            }
	            System.out.println("\n");
            }
            
            if (choose.equals("3")) {

	            nodes = test.filterActivities("2017-13-10", ">");
	            for (int i=0; i<nodes.getLength(); i++) {
	            	NodeList children = nodes.item(i).getChildNodes();
	            	System.out.println("Person-"+i);
	                System.out.println("- " + children.item(1).getNodeName() + " => " + children.item(1).getTextContent());
	                System.out.println("- " + children.item(3).getNodeName() + " => " + children.item(3).getTextContent());
	                System.out.println("\n");
	            }
            
            }
            
            JAXBContext jaxbContext = JAXBContext.newInstance("peoplestore.generated");
            
            if (choose.equals("4")) {
				Marshaller marshaller = jaxbContext.createMarshaller();
				marshaller.setProperty("jaxb.formatted.output", new Boolean(true));
				peoplestore.generated.ObjectFactory factory = new peoplestore.generated.ObjectFactory();
	
				PeopleStore people = factory.createPeopleStore();
				List<peoplestore.generated.Person> list = people.getPerson();
				
				
				ActivitypreferenceType ap7 = factory.createActivitypreferenceType();
				ap7.setId(100);
				ap7.setName("Running");
				ap7.setDescription("Running to the metro");
				ap7.setPlace("Milano");
				ap7.setStartdate(getBirth("2012-10-05T15:23:01.0"));
				peoplestore.generated.Person silvia = factory.createPerson();
				silvia.setId(0001);
				silvia.setFirstname("Silvia");
				silvia.setLastname("Simpa");
				silvia.setBirthdate(getBirth("1998-05-12"));
				silvia.setActivitypreference(ap7);
				
				
				ActivitypreferenceType ap8 = factory.createActivitypreferenceType();
				ap8.setId(200);
				ap8.setName("Walking");
				ap8.setDescription("Walking under the bridge");
				ap8.setPlace("Firenze");
				ap8.setStartdate(getBirth("2014-10-05T15:23:01.0"));
				peoplestore.generated.Person ariana = factory.createPerson();
				ariana.setId(0002);
				ariana.setFirstname("Ariana");
				ariana.setLastname("Grande");
				ariana.setBirthdate(getBirth("1995-12-23"));
				ariana.setActivitypreference(ap8);
				
				
				ActivitypreferenceType ap9 = factory.createActivitypreferenceType();
				ap9.setId(300);
				ap9.setName("Running");
				ap9.setDescription("Running to the hills");
				ap9.setPlace("Rovereto");
				ap9.setStartdate(getBirth("2014-10-05T15:23:01.0"));
				peoplestore.generated.Person sofia = factory.createPerson();
				sofia.setId(0003);
				sofia.setFirstname("Sofia");
				sofia.setLastname("Cacao");
				sofia.setBirthdate(getBirth("1993-03-12"));
				sofia.setActivitypreference(ap9);
				
				list.add(silvia);
				list.add(ariana);
				list.add(sofia);
				
				JAXBElement<peoplestore.generated.PeopleStore> peopleElement = factory.createPeople(people);
				marshaller.marshal(peopleElement, new FileOutputStream("marshalled.xml"));
				
				marshaller.marshal(peopleElement, System.out);
			
            }
			
			if (choose.equals("5")) {
				Unmarshaller unMarshaller = jaxbContext.createUnmarshaller();
				SchemaFactory schemaFactory = SchemaFactory
						.newInstance("http://www.w3.org/2001/XMLSchema");
				Schema schema = schemaFactory.newSchema(new File(
						"people.xsd"));
				unMarshaller.setSchema(schema);
				unMarshaller.setEventHandler(new CustomValidationEventHandler());
				
				@SuppressWarnings("unchecked")
				JAXBElement<PeopleStore> catalogElement = (JAXBElement<PeopleStore>) unMarshaller.unmarshal(new File("marshalled.xml"));
				
				PeopleStore catalog = catalogElement.getValue();
				
				List<peoplestore.generated.Person> p2 = catalog.getPerson();
				
				for (int i=0; i<p2.size(); i++) {
	            	peoplestore.generated.Person p = p2.get(i);
	            	ActivitypreferenceType ap = p.getActivitypreference();
	            	System.out.println("Person-"+i);
	                System.out.println("- firstname => " + p.getFirstname());
	                System.out.println("- lastname  => " + p.getLastname());
	                System.out.println("- birthdate => " + p.getBirthdate());
	                System.out.println("- activityPreference.name => " + ap.getName());
	                System.out.println("- activityPreference.description => " + ap.getDescription());
	                System.out.println("- activityPreference.place => " + ap.getPlace());
	                System.out.println("- activityPreference.startdate => " + ap.getStartdate());
	                System.out.println("\n");
	            }
			
			}
			
			if (choose.equals("6")) {
				
				Marshaller marshaller = jaxbContext.createMarshaller();
				marshaller.setProperty("jaxb.formatted.output", new Boolean(true));
				peoplestore.generated.ObjectFactory factory = new peoplestore.generated.ObjectFactory();
				
	
				PeopleStore people = factory.createPeopleStore();
List<peoplestore.generated.Person> list = people.getPerson();
				
				
				ActivitypreferenceType ap7 = factory.createActivitypreferenceType();
				ap7.setId(100);
				ap7.setName("Running");
				ap7.setDescription("Running to the metro");
				ap7.setPlace("Milano");
				ap7.setStartdate(getBirth("2012-10-05T15:23:01.0"));
				peoplestore.generated.Person silvia = factory.createPerson();
				silvia.setId(0001);
				silvia.setFirstname("Silvia");
				silvia.setLastname("Simpa");
				silvia.setBirthdate(getBirth("1998-05-12"));
				silvia.setActivitypreference(ap7);
				
				
				ActivitypreferenceType ap8 = factory.createActivitypreferenceType();
				ap8.setId(200);
				ap8.setName("Walking");
				ap8.setDescription("Walking under the bridge");
				ap8.setPlace("Firenze");
				ap8.setStartdate(getBirth("2014-10-05T15:23:01.0"));
				peoplestore.generated.Person ariana = factory.createPerson();
				ariana.setId(0002);
				ariana.setFirstname("Ariana");
				ariana.setLastname("Grande");
				ariana.setBirthdate(getBirth("1995-12-23"));
				ariana.setActivitypreference(ap8);
				
				
				ActivitypreferenceType ap9 = factory.createActivitypreferenceType();
				ap9.setId(300);
				ap9.setName("Running");
				ap9.setDescription("Running to the hills");
				ap9.setPlace("Rovereto");
				ap9.setStartdate(getBirth("2014-10-05T15:23:01.0"));
				peoplestore.generated.Person sofia = factory.createPerson();
				sofia.setId(0003);
				sofia.setFirstname("Sofia");
				sofia.setLastname("Cacao");
				sofia.setBirthdate(getBirth("1993-03-12"));
				sofia.setActivitypreference(ap9);
				
				list.add(silvia);
				list.add(ariana);
				list.add(sofia);
				
				
				ObjectMapper mapper = new ObjectMapper();
		        JaxbAnnotationModule module = new JaxbAnnotationModule();
		        
				mapper.registerModule(module);
				mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
				mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		        mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
		        
		        String result = mapper.writeValueAsString(people);
		        mapper.writeValue(new File("marshallJson.json"), people);
		        
		        System.out.println(result);
	        
			}
			
            
        } catch (Exception e) {
        	System.out.println("ERROR: "+e.getMessage());
        }
    }
    
    private static XMLGregorianCalendar getBirth(String d) throws ParseException, DatatypeConfigurationException {
    	DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = format.parse(d);
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		XMLGregorianCalendar xmlGregCal =  DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
		return xmlGregCal;
    }
}
