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
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * IE specific native implementation of the QuickTimePlayer class. It is not recommended to
 * interact with this class directly.
 *
 * @author Sikirulai Braheem
 * @see QuickTimePlayer
 */
public class QTStateManagerIE extends QTStateManager {

    public static String behaviourObjId = "bstqtevtmgr";

    public QTStateManagerIE() {
        // inject event source object...
        if (DOM.getElementById(behaviourObjId) == null) {
            Element oe = DOM.createElement("object");
            oe.setId(behaviourObjId);
            oe.setAttribute("classid", "clsid:CB927D12-4FF7-4a9e-A169-56E4B8A75598");
            RootPanel.getBodyElement().appendChild(oe);
        }
    }

    @Override
    protected native void registerMediaStateListenerImpl(QuickTimePlayerImpl playr, QTEventHandler handler) /*-{
    playr.attachEvent("onqt_begin", function(evt){
    handler.@com.bramosystems.oss.player.core.client.impl.QTStateManager.QTEventHandler::onStateChange(I)(1);
    });
    playr.attachEvent("onqt_load", function(evt){
    handler.@com.bramosystems.oss.player.core.client.impl.QTStateManager.QTEventHandler::onStateChange(I)(2);
    });
    playr.attachEvent('onqt_play', function(evt) {
    handler.@com.bramosystems.oss.player.core.client.impl.QTStateManager.QTEventHandler::onStateChange(I)(3);
    });
    playr.attachEvent('onqt_ended', function(evt) {
    handler.@com.bramosystems.oss.player.core.client.impl.QTStateManager.QTEventHandler::onStateChange(I)(4);
    });
    playr.attachEvent('onqt_canplay', function(evt) {
    handler.@com.bramosystems.oss.player.core.client.impl.QTStateManager.QTEventHandler::onStateChange(I)(5);
    });
    playr.attachEvent('onqt_volumechange', function(evt) {
    handler.@com.bramosystems.oss.player.core.client.impl.QTStateManager.QTEventHandler::onStateChange(I)(6);
    });
    playr.attachEvent('onqt_progress', function(evt) {
    handler.@com.bramosystems.oss.player.core.client.impl.QTStateManager.QTEventHandler::onStateChange(I)(7);
    });
    playr.attachEvent('onqt_error', function(evt) {
    handler.@com.bramosystems.oss.player.core.client.impl.QTStateManager.QTEventHandler::onStateChange(I)(8);
    });
    playr.attachEvent('onqt_loadedmetadata', function(evt) {
    handler.@com.bramosystems.oss.player.core.client.impl.QTStateManager.QTEventHandler::onStateChange(I)(9);
    });
    playr.attachEvent('onqt_pause', function(evt) {
    handler.@com.bramosystems.oss.player.core.client.impl.QTStateManager.QTEventHandler::onStateChange(I)(10);
    });
    playr.attachEvent('onqt_waiting', function(evt) {
    handler.@com.bramosystems.oss.player.core.client.impl.QTStateManager.QTEventHandler::onStateChange(I)(11);
    });
    playr.attachEvent('onqt_stalled', function(evt) {
    handler.@com.bramosystems.oss.player.core.client.impl.QTStateManager.QTEventHandler::onStateChange(I)(12);
    });
    }-*/;
}
