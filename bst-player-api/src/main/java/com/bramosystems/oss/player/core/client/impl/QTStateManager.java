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

import com.bramosystems.oss.player.core.client.ui.QuickTimePlayer;

/**
 * Native implementation of the QuickTimePlayer class. It is not recommended to
 * interact with this class directly.
 *
 * @author Sikirulai Braheem
 * @see QuickTimePlayer
 */
public class QTStateManager {

    public QTStateManager() {}

    public void registerMediaStateListener(QuickTimePlayerImpl impl, QTEventHandler handler, String mediaURL) {
        impl.resetPropertiesOnReload(false);
        registerMediaStateListenerImpl(impl, handler);
    }

    protected native void registerMediaStateListenerImpl(QuickTimePlayerImpl playr, QTEventHandler handler) /*-{
    playr.addEventListener('qt_begin', function(){  // plugin init complete
    handler.@com.bramosystems.oss.player.core.client.impl.QTStateManager.QTEventHandler::onStateChange(I)(1);
    }, false);
    playr.addEventListener('qt_load', function(){   // loading complete
    handler.@com.bramosystems.oss.player.core.client.impl.QTStateManager.QTEventHandler::onStateChange(I)(2);
    }, false);
    playr.addEventListener('qt_play', function(){   // play started
    handler.@com.bramosystems.oss.player.core.client.impl.QTStateManager.QTEventHandler::onStateChange(I)(3);
    }, false);
    playr.addEventListener('qt_ended', function(){  // playback finished
    handler.@com.bramosystems.oss.player.core.client.impl.QTStateManager.QTEventHandler::onStateChange(I)(4);
    }, false);
    playr.addEventListener('qt_canplay', function(){    // player ready
    handler.@com.bramosystems.oss.player.core.client.impl.QTStateManager.QTEventHandler::onStateChange(I)(5);
    }, false);
    playr.addEventListener('qt_volumechange', function(){   // volume changed
    handler.@com.bramosystems.oss.player.core.client.impl.QTStateManager.QTEventHandler::onStateChange(I)(6);
    }, false);
    playr.addEventListener('qt_progress', function(){   // progress
    handler.@com.bramosystems.oss.player.core.client.impl.QTStateManager.QTEventHandler::onStateChange(I)(7);
    }, false);
    playr.addEventListener('qt_error', function(){  // error
    handler.@com.bramosystems.oss.player.core.client.impl.QTStateManager.QTEventHandler::onStateChange(I)(8);
    }, false);
    playr.addEventListener('qt_loadedmetadata', function(){
    handler.@com.bramosystems.oss.player.core.client.impl.QTStateManager.QTEventHandler::onStateChange(I)(9);
    }, false);
    playr.addEventListener('qt_pause', function(){   // play paused
    handler.@com.bramosystems.oss.player.core.client.impl.QTStateManager.QTEventHandler::onStateChange(I)(10);
    }, false);
    playr.addEventListener('qt_waiting', function(){   // buffering
    handler.@com.bramosystems.oss.player.core.client.impl.QTStateManager.QTEventHandler::onStateChange(I)(11);
    }, false);
    playr.addEventListener('qt_stalled', function(){   // playback stalled
    handler.@com.bramosystems.oss.player.core.client.impl.QTStateManager.QTEventHandler::onStateChange(I)(12);
    }, false);
    }-*/;

    public static interface QTEventHandler {
        public void onStateChange(int newState);
    }
}
