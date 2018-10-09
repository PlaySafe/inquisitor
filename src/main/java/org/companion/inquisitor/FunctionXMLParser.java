package org.companion.inquisitor;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Parses the attribute of function XML tag corresponds to the XML configuration spec.
 * This class focus only the tag itself, regardless the child tag, or sibling tag.
 */
class FunctionXMLParser {

    private final String attributeName;
    private final String attributeParam;

    public FunctionXMLParser(MetaData metaData) {
        this.attributeName = metaData.getAttributeFunctionName();
        this.attributeParam = metaData.getAttributeFunctionParameter();
    }

    /**
     * Parses all attributes of a function node
     *
     * @param functionNode
     * @return a builder with data of the function
     */
    FunctionDefinition.Builder parse(Node functionNode) {
        Element element = ((Element) functionNode);
        String logic = element.getAttribute(attributeName);
        String param = element.hasAttribute(attributeParam) ? element.getAttribute(attributeParam) : null;
        return new FunctionDefinition.Builder()
                .setParam(param)
                .setLogic(logic);
    }
}