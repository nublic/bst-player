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
package com.bramosystems.oss.player.core.client.spi;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ParamElement;

/**
 * Low level helper class to create DOM-style objects for player widgets.
 * 
 * <p>This class is tied to the {@link PlayerProviderFactory} to provide 
 * browser-specific implementations via deferred binding.
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems.com>
 * @see PlayerWidget
 * @since 1.3
 */
public class PlayerElement {

    /**
     * Enumeration of HTML object element types
     * 
     * @author Sikirulai Braheem <sbraheem at bramosystems.com>
     * @since 1.3
     */
    public static enum Type {

        /**
         * HTML embed element type
         */
        EmbedElement, 
        
        /**
         * HTML object element type
         */
        ObjectElement, 
        
        /**
         * HTML object element type, specifically for Internet Explorer
         */
        ObjectElementIE, 
        
        /**
         * HTML5 video element type
         */
        VideoElement
    }
    private Document _doc = Document.get();
    private Element e;
    private Type type;

    /**
     * Creates a new PlayerElement object
     * 
     * @param type the element type
     * @param id the DOM ID of the element
     * @param typeOrClassId the HTML type (mime-type), or the CLASSID (if in Internet Explorer) of the element
     */
    public PlayerElement(Type type, String id, String typeOrClassId) {
        this.type = type;
        switch (type) {
            case EmbedElement:
                e = _doc.createElement("embed");
                e.setAttribute("type", typeOrClassId);
                break;
            case ObjectElement:
                e = _doc.createObjectElement();
                e.setPropertyString("type", typeOrClassId);
                break;
            case ObjectElementIE:
                e = _doc.createObjectElement();
                e.setPropertyString("classid", typeOrClassId);
                break;
            case VideoElement:
                e = _doc.createElement("video");
        }
        e.setId(id);
    }

    /**
     * Adds the specified parameters to the element.  The parameters are used as HTML params tags in the
     * eventual HTML DOM object
     * 
     * @param name the name of the parameter
     * @param value the value of the parameter
     */
    public final void addParam(String name, String value) {
        switch (type) {
            case ObjectElement:
            case ObjectElementIE:
                ParamElement param = _doc.createParamElement();
                param.setName(name);
                param.setValue(value);
                e.appendChild(param);
                break;
            case EmbedElement:
            case VideoElement:
                e.setAttribute(name, value);
        }
    }

    /**
     * Returns the DOM element representation of this player object
     * 
     * @return the DOM element representation
     */
    public Element getElement() {
        return e;
    }
}
