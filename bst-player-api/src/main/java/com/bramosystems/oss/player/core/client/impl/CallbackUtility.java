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

import com.google.gwt.core.client.JavaScriptObject;

/**
 * General utility class for functions common to player implementation classes
 *
 * @author Sikirulai Braheem
 * @since 1.2
 */
public class CallbackUtility {
    
    static {
        initHandlers();
    }
    
    private static native void initHandlers() /*-{
    if($wnd.bstplayer == null){
    $wnd.bstplayer = new Object();
    }
    $wnd.bstplayer.handlers = new Object();
    $wnd.bstplayer.handlers.swf = new Object();
    $wnd.bstplayer.handlers.divx = new Object();
    $wnd.bstplayer.handlers.vlc = new Object();
    $wnd.bstplayer.handlers.utube = new Object();
    }-*/;

    public static native JavaScriptObject getSWFCallbackHandlers() /*-{
    return $wnd.bstplayer.handlers.swf;
    }-*/;

    public static native JavaScriptObject getDivxCallbackHandlers() /*-{
    return $wnd.bstplayer.handlers.divx;
    }-*/;

    public static native JavaScriptObject getVLCCallbackHandlers() /*-{
    return $wnd.bstplayer.handlers.vlc;
    }-*/;

    public static native JavaScriptObject getUTubeCallbackHandlers() /*-{
    return $wnd.bstplayer.handlers.utube;
    }-*/;
}
