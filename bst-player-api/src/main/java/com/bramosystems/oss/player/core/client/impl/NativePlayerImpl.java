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
import com.bramosystems.oss.player.core.client.ui.NativePlayer;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * Native implementation of the VLCPlayer class. It is not recommended to
 * interact with this class directly.
 *
 * @author Sikirulai Braheem
 * @see NativePlayer
 */
public class NativePlayerImpl extends JavaScriptObject {

    public static native NativePlayerImpl getPlayer(String playerId) /*-{
    return $doc.getElementById(playerId);
    }-*/;

    protected NativePlayerImpl() {
    }

    public final native int getNetworkState() /*-{
    return this.networkState;
    }-*/;

    public final native int getReadyState() /*-{
    return this.readyState;
    }-*/;

    public final native int getErrorState() /*-{
    var _err = this.error;
    if(_err) {
    return _err.code
    } else {
    return 0;
    }
    }-*/;

    public final native void play() /*-{
    this.play();
    }-*/;

    public final native void pause() /*-{
    this.pause();
    }-*/;

    public final native boolean isPaused() /*-{
    return this.paused;
    }-*/;

    public final native boolean isEnded() /*-{
    return this.ended;
    }-*/;

    public final native void setMediaURL(String mediaURL) /*-{
    this.src = mediaURL;
    }-*/;

    public final native void load() /*-{
    this.load();
    }-*/;

    public final native String getMediaURL() /*-{
    return this.currentSrc;
    }-*/;

    public final native boolean isAutoPlay() /*-{
    return this.autoplay;
    }-*/;

    public final native boolean isLooping() /*-{
    return this.loop;
    }-*/;

    public final native void setLooping(boolean looping) /*-{
    this.loop = looping;
    }-*/;

    public final native double getTime() /*-{
    try {
    return this.currentTime * 1000;
    } catch(e) {
    return 0;
    }
    }-*/;

    public final native void setTime(double time) /*-{
    try {
    this.currentTime = time / 1000;
    } catch(e) {}
    }-*/;

    public final native double getDuration() /*-{
    try {
    return parseFloat(this.duration * 1000);
    } catch(e) {
    return 0;
    }
    }-*/;

    public final native TimeRange getBuffered() /*-{
    return this.buffered;
    }-*/;

    public final native double getVolume() /*-{
    try{
    return this.volume;
    } catch(e){
    return 0;
    }
    }-*/;

    public final native void setVolume(double volume) /*-{
    try{
    this.volume = volume;
    } catch(e){}
    }-*/;

    public final native boolean isMute() /*-{
    try{
    return this.muted;
    } catch(e){
    return false;
    }
    }-*/;

    public final native void setMute(boolean mute) /*-{
    try{
    this.muted = mute;
    } catch(e){}
    }-*/;

    public final native boolean isControlsVisible() /*-{
    return this.controls;
    }-*/;

    public final native void setControlsVisible(boolean visible) /*-{
    this.controls = visible;
    }-*/;

    public final native double getRate() /*-{
    return this.playbackRate;
    }-*/;

    public final native void setRate(double rate) /*-{
    this.playbackRate = rate;
    }-*/;

    public final native int getVideoWidth() /*-{
    return this.videoWidth;
    }-*/;

    public final native int getVideoHeight() /*-{
     return this.videoHeight;
    }-*/;

    public final native String getPoster() /*-{
    return this.poster;
    }-*/;

    public final native void setPoster(String _poster) /*-{
    this.poster = _poster;
    }-*/;

    public final native void fillMediaInfo(MediaInfo id3) /*-{
    try {
    //    id3.@com.bramosystems.oss.player.core.client.MediaInfo::year = ;
    //    id3.@com.bramosystems.oss.player.core.client.MediaInfo::albumTitle = ;
    //    id3.@com.bramosystems.oss.player.core.client.MediaInfo::artists = ;
    //    id3.@com.bramosystems.oss.player.core.client.MediaInfo::comment = ;
    //    id3.@com.bramosystems.oss.player.core.client.MediaInfo::title = ;
    //    id3.@com.bramosystems.oss.player.core.client.MediaInfo::contentProviders = ;
    //    id3.@com.bramosystems.oss.player.core.client.MediaInfo::copyright = ;
    //    id3.@com.bramosystems.oss.player.core.client.MediaInfo::hardwareSoftwareRequirements = ;
    //    id3.@com.bramosystems.oss.player.core.client.MediaInfo::publisher =;
    //    id3.@com.bramosystems.oss.player.core.client.MediaInfo::genre = ;
    //    id3.@com.bramosystems.oss.player.core.client.MediaInfo::internetStationOwner = '';
    //    id3.@com.bramosystems.oss.player.core.client.MediaInfo::internetStationName = '';
    id3.@com.bramosystems.oss.player.core.client.MediaInfo::duration = parseFloat(this.duration * 1000);
    id3.@com.bramosystems.oss.player.core.client.MediaInfo::videoWidth = String(this.videoWidth);
    id3.@com.bramosystems.oss.player.core.client.MediaInfo::videoHeight = String(this.videoHeight);
    } catch(e) {
    $wnd.alert(e);
    }
    }-*/;

    public final native void registerMediaStateHandler(NativePlayerUtil.NativeEventCallback _callback) /*-{
    this.addEventListener('progress', function(){  // plugin init complete
    _callback.@com.bramosystems.oss.player.core.client.impl.NativePlayerUtil.NativeEventCallback::onProgressChanged()();
    }, false);
    this.addEventListener('play', function(){  // play started
    _callback.@com.bramosystems.oss.player.core.client.impl.NativePlayerUtil.NativeEventCallback::onStateChanged(I)(1);
    }, false);
    this.addEventListener('pause', function(){  // play paused
    _callback.@com.bramosystems.oss.player.core.client.impl.NativePlayerUtil.NativeEventCallback::onStateChanged(I)(2);
    }, false);
    this.addEventListener('ended', function(){  // play finished
    _callback.@com.bramosystems.oss.player.core.client.impl.NativePlayerUtil.NativeEventCallback::onStateChanged(I)(3);
    }, false);
    this.addEventListener('waiting', function(){  // buffering
    _callback.@com.bramosystems.oss.player.core.client.impl.NativePlayerUtil.NativeEventCallback::onStateChanged(I)(4);
    }, false);
    this.addEventListener('playing', function(){  // playing again, buffering stopped
    _callback.@com.bramosystems.oss.player.core.client.impl.NativePlayerUtil.NativeEventCallback::onStateChanged(I)(5);
    }, false);
    this.addEventListener('loadedmetadata', function(){  // metadata available
    _callback.@com.bramosystems.oss.player.core.client.impl.NativePlayerUtil.NativeEventCallback::onStateChanged(I)(6);
    }, false);
    this.addEventListener('volumechange', function(){  // volume changed
    _callback.@com.bramosystems.oss.player.core.client.impl.NativePlayerUtil.NativeEventCallback::onStateChanged(I)(7);
    }, false);
    this.addEventListener('loadstart', function(){  // loading started
    _callback.@com.bramosystems.oss.player.core.client.impl.NativePlayerUtil.NativeEventCallback::onStateChanged(I)(10);
    }, false);
    this.addEventListener('load', function(){  // loading completed
    _callback.@com.bramosystems.oss.player.core.client.impl.NativePlayerUtil.NativeEventCallback::onStateChanged(I)(11);
    }, false);
    this.addEventListener('error', function(){  // loading error
    _callback.@com.bramosystems.oss.player.core.client.impl.NativePlayerUtil.NativeEventCallback::onStateChanged(I)(12);
    }, false);
    this.addEventListener('abort', function(){  // loading aborted
    _callback.@com.bramosystems.oss.player.core.client.impl.NativePlayerUtil.NativeEventCallback::onStateChanged(I)(13);
    }, false);
    }-*/;

    public static class TimeRange extends JavaScriptObject {

        protected TimeRange() {
        }

        /**
         * Return the number of ranges represented by the object.
         *
         * @return number of ranges
         */
        public final native double getLength() /*-{
        return this.length;
        }-*/;

        /**
         * Returns the position of the start of the indexth range represented by the object
         *
         * <p>
         * Throws an exception if called with an index argument greater than or equal to the
         * number of ranges represented by the object.
         *
         * @param index the index
         * @return range in seconds measured from the start of the timeline that the object covers.
         */
        public final native double getStart(double index) /*-{
        return this.start(index);
        }-*/;

        /**
         * Returns the position of the end of the range at index
         *
         * <p>
         * Throws an exception if called with an index argument greater than or equal to the
         * number of ranges represented by the object.
         *
         * @param index the index
         * @return range in seconds measured from the start of the timeline that the object covers.
         */
        public final native double getEnd(double index) /*-{
        return this.end(index);
        }-*/;
    }
}
