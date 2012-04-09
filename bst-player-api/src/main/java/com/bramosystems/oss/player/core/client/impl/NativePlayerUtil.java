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
package com.bramosystems.oss.player.core.client.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * Utility class to get properties specific to the NativePlayer widget. This is meant
 * for those properties that are different on HTML 5 media implementation of browsers.
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems dot com>
 * @since 1.1
 */
public class NativePlayerUtil {

    public static NativePlayerUtil get = GWT.create(NativePlayerUtil.class);

    private NativePlayerUtil() {
    }

    public String getPlayerHeight() {
        return "20px";
    }

    public static class NativePlayerUtilSafari extends NativePlayerUtil {

        private boolean isChrome;

        public NativePlayerUtilSafari() {
            isChrome = isChrome();
        }

        @Override
        public String getPlayerHeight() {
            if (isChrome) {
                return "25px";
            }
            return "16px";
        }

        private native boolean isChrome() /*-{
        try {
        return navigator.userAgent.toLowerCase().indexOf('chrome') > 0;
        }catch(e){
        return false;
        }
        }-*/;
    }

    public static native TestUtil getTestUtil() /*-{
    return $doc.createElement('video');
    }-*/;

    public static class TestUtil extends JavaScriptObject {

        protected TestUtil() {
        }

        public final TestResult canPlayType(String mimeType) {
            try {
                return TestResult.valueOf(canPlayTypeImpl(mimeType));
            } catch (Exception e) {
                return TestResult.no;
            }
        }

        private native String canPlayTypeImpl(String mimeType) /*-{
        return this.canPlayType(mimeType);
        }-*/;
    }

    public static enum TestResult {

        /**
         * The browser cannot handle the specified media resource
         */
        no,
        /**
         * The browser can maybe (but not sure) handle the specified media resource
         */
        maybe,
        /**
         * The browser can confidently handle the specified media resource
         */
        probably
    }

    public static interface NativeEventCallback {

        public void onProgressChanged();

        public void onStateChanged(int code);
    }
}
