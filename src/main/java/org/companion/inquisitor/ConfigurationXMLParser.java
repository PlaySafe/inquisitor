package org.companion.inquisitor;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

class ConfigurationXMLParser {

    Document parseFrom(File xmlFile) throws IOException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setIgnoringComments(true);
        try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse(xmlFile);
            document.normalizeDocument();
            return document;
        }
        catch (ParserConfigurationException | SAXException e) {
            throw new IllegalArgumentException("Cannot parse XML document because of invalid XML format", e);
        }
    }
}
