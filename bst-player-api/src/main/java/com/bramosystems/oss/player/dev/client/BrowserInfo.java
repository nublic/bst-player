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
package com.bramosystems.oss.player.dev.client;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Utility class that wraps basic information about the clients' browser
 *
 * @author Sikirulai Braheem
 * @since 1.2
 */
public class BrowserInfo extends JavaScriptObject {

    protected BrowserInfo() {
    }

    /**
     * Returns the platform
     *
     * @return the name of the platform
     */
    public final native String getPlatform() /*-{
    return this.platform;
    }-*/;

    /**
     * Returns the browsers' code name
     *
     * @return the code name
     */
    public final native String getCodeName() /*-{
    return this.appCodeName;
    }-*/;

    /**
     * Returns the browsers' name
     *
     * @return the name
     */
    public final native String getName() /*-{
    return this.appName;
    }-*/;

    /**
     * Returns the version of the browser
     *
     * @return the version
     */
    public final native String getVersion() /*-{
    return this.appVersion;
    }-*/;

    /**
     * Returns an instance of the BrowserInfo object
     *
     * @return an instance of BrowserInfo
     */
    public static native BrowserInfo getBrowserInfo() /*-{
    return navigator;
    }-*/;
}
