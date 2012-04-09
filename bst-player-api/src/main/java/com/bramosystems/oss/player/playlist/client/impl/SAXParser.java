/*
 * Copyright 2011 Sikirulai Braheem <sbraheem at bramosystems.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bramosystems.oss.player.playlist.client.impl;

import com.bramosystems.oss.player.playlist.client.ParseException;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
import java.util.HashMap;

/**
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems.com>
 */
public class SAXParser {

    private SAXHandler handler;

    public SAXParser() {
    }

    public SAXParser(SAXHandler handler) {
        this.handler = handler;
    }

    public void setHandler(SAXHandler handler) {
        this.handler = handler;
    }

    public void parseDocument(Document doc) throws ParseException {
        XMLParser.removeWhitespace(doc);
        processNodes(doc.getChildNodes());
    }

    private void processNodes(NodeList node) throws ParseException {
        for (int i = 0; i < node.getLength(); i++) {
            Node nd = node.item(i);
            switch (nd.getNodeType()) {
                case Node.ELEMENT_NODE:
                    HashMap<String, String> attr = new HashMap<String, String>();
                    NamedNodeMap nnm = nd.getAttributes();
                    for (int j = 0; j < nnm.getLength(); j++) {
                        Node nm = nnm.item(j);
                        attr.put(nm.getNodeName(), nm.getNodeValue());
                    }
                    handler.onNodeStart(nd.getNodeName(), attr, nd.getNamespaceURI());
                    processNodes(nd.getChildNodes());
                    handler.onNodeEnd(nd.getNodeName());
                    break;
                case Node.TEXT_NODE:
                    handler.setNodeValue(nd.getParentNode().getNodeName(), nd.getNodeValue());
            }
        }
    }
}
