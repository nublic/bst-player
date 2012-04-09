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
package com.bramosystems.oss.player.youtube.client.impl;

import com.bramosystems.oss.player.core.client.impl.CallbackUtility;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * Handles global events for the YouTubePlayer
 *
 * @author Sikirulai Braheem
 */
public class YouTubeEventManager {

    public YouTubeEventManager() {
    }

    public final void init(String playerApiId, EventHandler handler) {
        initImpl(playerApiId, CallbackUtility.getUTubeCallbackHandlers(), handler);
    }

    public final void close(String playerApiId) {
        closeImpl(playerApiId, CallbackUtility.getUTubeCallbackHandlers());
    }

    private native void closeImpl(String playerApiId, JavaScriptObject utube) /*-{
    delete utube[playerApiId];
    }-*/;

    private native void initImpl(String playerApiId, JavaScriptObject utube, EventHandler handler) /*-{
    utube[playerApiId] = new Object();
    utube[playerApiId].onInit = function(){
    handler.@com.bramosystems.oss.player.youtube.client.impl.YouTubeEventManager.EventHandler::onInit()();
    }
    utube[playerApiId].onStateChanged = function(changeCode){
    handler.@com.bramosystems.oss.player.youtube.client.impl.YouTubeEventManager.EventHandler::onYTStateChanged(I)(changeCode);
    }
    utube[playerApiId].onQualityChanged = function(quality){
    handler.@com.bramosystems.oss.player.youtube.client.impl.YouTubeEventManager.EventHandler::onYTQualityChanged(Ljava/lang/String;)(quality);
    }
    utube[playerApiId].onError = function(errorCode){
    handler.@com.bramosystems.oss.player.youtube.client.impl.YouTubeEventManager.EventHandler::onYTError(I)(errorCode);
    }
    }-*/;

    static {
        initGlobalCallback(CallbackUtility.getUTubeCallbackHandlers());
    }

    private static native void initGlobalCallback(JavaScriptObject utube) /*-{
    $wnd.onYouTubePlayerReady = function(playerApiId){
    utube[playerApiId].onInit();
    }
    }-*/;

    public static interface EventHandler {

        public void onInit();

        public void onYTStateChanged(int state);

        public void onYTQualityChanged(String quality);

        public void onYTError(int errorCode);
    }
}
