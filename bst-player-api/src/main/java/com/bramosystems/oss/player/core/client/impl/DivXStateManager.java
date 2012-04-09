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
import com.google.gwt.core.client.JavaScriptObject;

/**
 * @author Sikirulai Braheem
 */
public class DivXStateManager {

    public DivXStateManager(String playerId, StateCallback callback) {
        initCallbacks(CallbackUtility.getDivxCallbackHandlers(), playerId, callback);
    }

    public MediaInfo getFilledMediaInfo(double duration, double videoWidth, double videoHeight) {
        MediaInfo mi = new MediaInfo();
        fillMediaInfoImpl(mi, duration, videoWidth, videoHeight);
        return mi;
    }

    private native void initCallbacks(JavaScriptObject divx, String playerId, StateCallback callback) /*-{
    divx[playerId] = new Object();
    divx[playerId].stateChanged = function(eventId){
    callback.@com.bramosystems.oss.player.core.client.impl.DivXStateManager.StateCallback::onStatusChanged(I)(parseInt(eventId));
    }
    divx[playerId].downloadState = function(current, total){
    callback.@com.bramosystems.oss.player.core.client.impl.DivXStateManager.StateCallback::onLoadingChanged(DD)(parseFloat(current),parseFloat(total));
    }
    divx[playerId].timeState = function(time){
    callback.@com.bramosystems.oss.player.core.client.impl.DivXStateManager.StateCallback::onPositionChanged(D)(parseFloat(time));
    }
    }-*/;

    public void clearCallbacks(String playerId) {
        clearCallbackImpl(CallbackUtility.getDivxCallbackHandlers(), playerId);
    }

    private native void clearCallbackImpl(JavaScriptObject divx, String playerId) /*-{
    delete divx[playerId];
    }-*/;

    private native void fillMediaInfoImpl(MediaInfo mData, double duration, double vWidth, double vHeight) /*-{
    mData.@com.bramosystems.oss.player.core.client.MediaInfo::duration = duration;
    mData.@com.bramosystems.oss.player.core.client.MediaInfo::videoWidth = '' + vWidth;
    mData.@com.bramosystems.oss.player.core.client.MediaInfo::videoHeight = '' + vHeight;
                                                            
//    mData.@com.bramosystems.oss.player.core.client.MediaInfo::year = csv[0];
//    mData.@com.bramosystems.oss.player.core.client.MediaInfo::albumTitle = csv[1];
//    mData.@com.bramosystems.oss.player.core.client.MediaInfo::artists = csv[2];
//    mData.@com.bramosystems.oss.player.core.client.MediaInfo::comment = csv[3];
//    mData.@com.bramosystems.oss.player.core.client.MediaInfo::genre = csv[4];
//    mData.@com.bramosystems.oss.player.core.client.MediaInfo::title = csv[5];
//    mData.@com.bramosystems.oss.player.core.client.MediaInfo::contentProviders = csv[6];
//    mData.@com.bramosystems.oss.player.core.client.MediaInfo::copyright = csv[7];
//    mData.@com.bramosystems.oss.player.core.client.MediaInfo::hardwareSoftwareRequirements = csv[9];
//    mData.@com.bramosystems.oss.player.core.client.MediaInfo::publisher = csv[10];
//    mData.@com.bramosystems.oss.player.core.client.MediaInfo::internetStationOwner = csv[11];
//    mData.@com.bramosystems.oss.player.core.client.MediaInfo::internetStationName = csv[12];
    }-*/;

    public static interface StateCallback {

        /**
         * Called to notify of current status
         * @param statusId
         */
        public void onStatusChanged(int statusId);

        /**
         * Called to notify on current progress
         * @param current
         * @param total
         */
        public void onLoadingChanged(double current, double total);

        /**
         * Called every second to inform of current playback position
         * @param time time in seconds
         */
        public void onPositionChanged(double time);
    }
}
