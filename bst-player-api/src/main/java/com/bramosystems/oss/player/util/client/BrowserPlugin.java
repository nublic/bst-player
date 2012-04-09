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

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

/**
 * Utility class that wraps the <code>navigator.plugins</code> information of
 * the clients' browser
 *
 * @author Sikirulai Braheem
 */
public class BrowserPlugin extends JavaScriptObject {

    /**
     * Create a new BrowserPlugin object
     */
    protected BrowserPlugin() {
    }

    /**
     * Returns the description of the plug-in
     *
     * @return the description of the plug-in
     */
    public final native String getDescription() /*-{
    return this.description;
    }-*/;

    /**
     * Returns the name of the plug-in
     *
     * @return the name of the plug-in
     */
    public final native String getName() /*-{
    return this.name;
    }-*/;

    /**
     * Returns the plug-in filename on the client
     *
     * @return the filename of the plug-in
     */
    public final native String getFileName() /*-{
    return this.filename;
    }-*/;

    /**
     * Returns the number of mime-types supported by the plug-in
     *
     * @return the number of supported mime-types
     */
    public final native int getMimeTypeCount() /*-{
    return this.length;
    }-*/;

    /**
     * Returns the mime type at the specified index
     *
     * @param index the index
     * @return the {@link MimeType}
     */
    public final native MimeType getMimeType(int index) /*-{
    return this[index];
    }-*/;

    /**
     * Returns an array of all plug-in's on the client
     *
     * @return an array of {@link BrowserPlugin}'s
     */
    public static final native JsArray<BrowserPlugin> getPlugins() /*-{
    return navigator.plugins;
    }-*/;
}
