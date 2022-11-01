package web.shop.util;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import web.shop.entity.Constraint;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class XmlSecurityParser {

    private static final Logger LOG = Logger.getLogger(XmlSecurityParser.class);
    private static final String URL_PATTERN = "url-pattern";
    private static final String ROLE = "role";
    private static final String CONSTRAINT = "constraint";
    private static final String FILE_NAME = "security.xml";

    public static List<Constraint> getXmlConstraintsAsList() {
        List<Constraint> constraints = new LinkedList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);

            factory.setFeature("http://xml.org/sax/features/validation", true);
            factory.setFeature("http://apache.org/xml/features/validation/schema", true);
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);

            DocumentBuilder builder = factory.newDocumentBuilder();
            String pathToXml = String.valueOf(XmlSecurityParser.class.getClassLoader().getResource(FILE_NAME));
            Document document = builder.parse(pathToXml);
            NodeList nodeList = document.getElementsByTagName(CONSTRAINT);
            for (int i = 0; i < nodeList.getLength(); i++) {
                constraints.add(getConstraint(nodeList.item(i)));
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            LOG.error("Cannot parse file", e);
            return Collections.emptyList();
        }
        return constraints;
    }

    private static Constraint getConstraint(Node node) {
        Element element = (Element) node;
        String urlPattern = element.getElementsByTagName(URL_PATTERN)
                .item(0)
                .getChildNodes()
                .item(0)
                .getNodeValue();

        List<String> roles = new LinkedList<>();
        NodeList nodeRoles = element.getElementsByTagName(ROLE);
        for (int i = 0; i < nodeRoles.getLength(); i++) {
            roles.add(nodeRoles.item(i).getChildNodes().item(0).getNodeValue());
        }

        return new Constraint(urlPattern, roles);
    }
}
