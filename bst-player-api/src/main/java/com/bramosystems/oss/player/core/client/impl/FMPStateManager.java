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

import com.bramosystems.oss.player.core.client.MediaInfo;
import com.bramosystems.oss.player.core.client.ui.FlashMediaPlayer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Window.Location;

/**
 * Native implementation of the FlashMediaPlayer class. It is not recommended to
 * interact with this class directly.
 *
 * @author Sikirulai Braheem
 * @see FlashMediaPlayer
 */
public class FMPStateManager {

    public FMPStateManager(String playerId, FMPStateCallback callback) {
        initCallbackImpl(CallbackUtility.getSWFCallbackHandlers(), playerId, callback);
    }

    public void closeMedia(String playerId) {
        closeMediaImpl(CallbackUtility.getSWFCallbackHandlers(), playerId);
    }

    private native void closeMediaImpl(JavaScriptObject swf, String playerId) /*-{
    delete swf[playerId];
    }-*/;

    private native void initCallbackImpl(JavaScriptObject swf, String playerId, FMPStateCallback callback) /*-{
    swf[playerId] = new Object();
    swf[playerId].onInit = function(){
    callback.@com.bramosystems.oss.player.core.client.impl.FMPStateManager.FMPStateCallback::onInit()();
    }
    swf[playerId].onEvent = function(type,buttonDown,alt,ctrl,shift,cmd,stageX_keyCode,stageY_charCode){
    callback.@com.bramosystems.oss.player.core.client.impl.FMPStateManager.FMPStateCallback::onEvent(IZZZZZII)(type,buttonDown,alt,ctrl,shift,cmd,stageX_keyCode,stageY_charCode);
    }
    swf[playerId].onStateChanged = function(state, listIndex){
    callback.@com.bramosystems.oss.player.core.client.impl.FMPStateManager.FMPStateCallback::onStateChanged(II)(state,listIndex);
    }
    swf[playerId].onLoadingProgress = function(progress){
    callback.@com.bramosystems.oss.player.core.client.impl.FMPStateManager.FMPStateCallback::onProgress(D)(progress);
    }
    swf[playerId].onMessage = function(type, message){
    callback.@com.bramosystems.oss.player.core.client.impl.FMPStateManager.FMPStateCallback::onMessage(ILjava/lang/String;)(type,message);
    }
    swf[playerId].onMetadata = function(id3){
    callback.@com.bramosystems.oss.player.core.client.impl.FMPStateManager.FMPStateCallback::onMediaInfo(Ljava/lang/String;)(id3);
    }
    swf[playerId].onFullscreen = function(fs){
    callback.@com.bramosystems.oss.player.core.client.impl.FMPStateManager.FMPStateCallback::onFullScreen(Z)(fs);
    }
    }-*/;

    public native void fillMediaInfoImpl(String infoCSV, MediaInfo mData) /*-{
    // parse from CSV like values ...
    // year[$]albumTitle[$]artists[$]comment[$]genre[$]title[$]
    // contentProviders[$]copyright[$]duration[$]hardwareSoftwareRequirements[$]
    // publisher[$]internetStationOwner[$]internetStationName[$]videoWidth[$]videoHeight
    
    csv = infoCSV.split("[$]");
    mData.@com.bramosystems.oss.player.core.client.MediaInfo::year = csv[0];
    mData.@com.bramosystems.oss.player.core.client.MediaInfo::albumTitle = csv[1];
    mData.@com.bramosystems.oss.player.core.client.MediaInfo::artists = csv[2];
    mData.@com.bramosystems.oss.player.core.client.MediaInfo::comment = csv[3];
    mData.@com.bramosystems.oss.player.core.client.MediaInfo::genre = csv[4];
    mData.@com.bramosystems.oss.player.core.client.MediaInfo::title = csv[5];
    mData.@com.bramosystems.oss.player.core.client.MediaInfo::contentProviders = csv[6];
    mData.@com.bramosystems.oss.player.core.client.MediaInfo::copyright = csv[7];
    mData.@com.bramosystems.oss.player.core.client.MediaInfo::duration = parseFloat(csv[8]);
    mData.@com.bramosystems.oss.player.core.client.MediaInfo::hardwareSoftwareRequirements = csv[9];
    mData.@com.bramosystems.oss.player.core.client.MediaInfo::publisher = csv[10];
    mData.@com.bramosystems.oss.player.core.client.MediaInfo::internetStationOwner = csv[11];
    mData.@com.bramosystems.oss.player.core.client.MediaInfo::internetStationName = csv[12];
    mData.@com.bramosystems.oss.player.core.client.MediaInfo::videoWidth = csv[13];
    mData.@com.bramosystems.oss.player.core.client.MediaInfo::videoHeight = csv[14];
    }-*/;

    public static interface FMPStateCallback {

        public void onInit();

        public void onMessage(int type, String message);

        public void onProgress(double progress);

        public void onMediaInfo(String info);

        public void onEvent(int type, boolean buttonDown, boolean alt, boolean ctrl,
                boolean shift, boolean cmd, int stageX, int stageY);

        public void onStateChanged(int stateId, int listIndex);

        public void onFullScreen(boolean fullscreen);
    }

    public static String getSWFImpl() {
        StringBuilder swf = new StringBuilder(GWT.getModuleBaseURL() + "bst-flash-player-");

        //TODO: remove b4 release ...
//        swf.append("1.3-SNAPSHOT");   // inject bst-flash-player version via maven resources filter...
        swf.append("${version}");   // inject bst-flash-player version via maven resources filter...
        if (Location.getProtocol().toLowerCase().startsWith("file")) {
            swf.append("-lo");
        }
        swf.append(".swf");
        return swf.toString();
    }
}