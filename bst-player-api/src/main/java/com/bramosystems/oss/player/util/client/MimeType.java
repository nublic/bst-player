/*
 * Copyright 2009 Sikirulai Braheem
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.bramosystems.oss.player.util.client;

import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

/**
 * Utility class that wraps information about a MIME type supported
 * by the browser
 *
 * @author Sikirulai Braheem
 */
public class MimeType extends JavaScriptObject {

    /**
     * Constructs a new MimeType object
     */
    protected MimeType() {
    }

    /**
     * Returns the description of the MIME type
     *
     * @return the description
     */
    public final native String getDescription() /*-{
    return this.description;
    }-*/;

    /**
     * Returns the name of the MIME type such as "video/mpeg"
     *
     * @return the name of the MIME type
     */
    public final native String getType() /*-{
    return this.type;
    }-*/;

    /**
     * Returns the plug-in configured for the MIME type
     *
     * @return {@link BrowserPlugin} configured for the MIME type
     * @throws PluginNotFoundException if no plugin is found e.g. when plugin is disabled.
     */
    public final BrowserPlugin getEnabledPlugin() throws PluginNotFoundException {
        BrowserPlugin plug = _getEnabledPlugin();
        if (plug != null) {
            return plug;
        } else {
            throw new PluginNotFoundException("No Plugin found for type - '" + getType() + "' !");
        }
    }

    private final native BrowserPlugin _getEnabledPlugin() /*-{
    return this.enabledPlugin;
    }-*/;

    /**
     * Returns a listing of the filename extensions of the MIME type
     *
     * @return listing of filename extensions
     */
    public final native String getSuffixes() /*-{
    return this.suffixes;
    }-*/;

    /**
     * Returns an array of all MIME types supported by the browser
     *
     * @return an array of {@link MimeType}s
     */
    public static final native JsArray<MimeType> getMimeTypes() /*-{
    return navigator.mimeTypes;
    }-*/;

    /**
     * Returns a MimeType object with the specified <code>type</code>
     *
     * @param type the type such as "audio/x-wav"
     *
     * @return the MimeType object
     */
    public static final native MimeType getMimeType(String type) /*-{
    return navigator.mimeTypes[type];
    }-*/;
}
