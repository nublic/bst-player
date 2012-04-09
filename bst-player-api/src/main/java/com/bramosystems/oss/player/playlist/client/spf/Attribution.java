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
package com.bramosystems.oss.player.playlist.client.spf;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Represents an XSPF/JSPF playlist attribution
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems.com>
 * @since 1.3
 */
public final class Attribution extends JavaScriptObject {

    /**
     * Constructor
     */
    protected Attribution() {
    }

    /**
     * Returns the identifier of the original playlist
     * 
     * @return the identifier of the original playlist
     */
    public native String getIdentifier() /*-{
        return this.identifier;
    }-*/;

    /**
     * Sets the identifier of the original playlist
     * 
     * @param identifier the identifier of the original playlist
     */
    public native void setIdentifier(String identifier) /*-{
        this.identifier = identifier;
    }-*/;

    /**
     * Returns the location of the original playlist
     * 
     * @return the location of the original playlist
     */
    public native String getLocation() /*-{
        return this.location;
    }-*/;

    /**
     * Sets the location of the original playlist
     * 
     * @param location the location of the original playlist
     */
    public native void setLocation(String location) /*-{
        this.location = location;
    }-*/;    
}
