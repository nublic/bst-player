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
import com.bramosystems.oss.player.core.client.ui.QuickTimePlayer;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * Native implementation of the QuickTimePlayer class. It is not recommended to
 * interact with this class directly.
 *
 * @author Sikirulai Braheem
 * @see QuickTimePlayer
 */
public class QuickTimePlayerImpl extends JavaScriptObject {

    protected QuickTimePlayerImpl() {
    }

    public static native QuickTimePlayerImpl getPlayer(String playerId) /*-{
    return $doc.getElementById(playerId);
    }-*/;

    public final native String getPluginVersion() /*-{
    return this.GetPluginVersion();
    }-*/;

    public final native void resetPropertiesOnReload(boolean reset) /*-{
    this.SetResetPropertiesOnReload(reset);
    }-*/;

    public final native void play() /*-{
    this.Play();
    }-*/;

    public final native void stop() /*-{
    this.Rewind();
    this.Stop();
    }-*/;

    public final native void pause() /*-{
    this.Stop();
    }-*/;

    public final native void load(String mediaURL) /*-{
    this.SetURL(mediaURL);
    }-*/;

    public final native String getMovieURL() /*-{
    return this.GetURL();
    }-*/;

    public final native double getTime() /*-{
    return parseFloat(this.GetTime() / this.GetTimeScale()) * 1000;
    }-*/;

    public final native void setTime(double time) /*-{
    this.SetTime(parseInt(time / 1000 * this.GetTimeScale()));
    }-*/;

    public final native double getDuration() /*-{
    return parseFloat(this.GetDuration() / this.GetTimeScale() * 1000);
    }-*/;

    public final native int getMovieSize() /*-{
    return this.GetMovieSize();
    }-*/;

    public final native int getMaxBytesLoaded() /*-{
    return this.GetMaxBytesLoaded();
    }-*/;

    public final native double getVolume() /*-{
    return this.GetVolume() / 255.0;
    }-*/;

    public final native void setVolume(double volume) /*-{
    this.SetVolume(parseInt(volume * 255));
    }-*/;

    public final native void setQTNextURL(int index, String url) /*-{
    this.SetQTNEXTUrl(index, url);
    }-*/;

    public final native String getQTNextURL(int index) /*-{
    return this.GetQTNEXTUrl(index);
    }-*/;

    public final native void setLoopingImpl(boolean loop) /*-{
    this.SetIsLooping(loop);
    }-*/;

    public final native boolean isLoopingImpl() /*-{
    return this.GetIsLooping(loop);
    }-*/;

    public final native void setControllerVisible(boolean visible) /*-{
    this.SetControllerVisible(visible);
    }-*/;

    public final native boolean isControllerVisible() /*-{
    return this.GetControllerVisible();
    }-*/;

    public final native void setMute(boolean mute) /*-{
    this.SetMute(mute);
    }-*/;

    public final native boolean isMute() /*-{
    return this.GetMute();
    }-*/;

    public final native void setRate(double rate) /*-{
    this.SetRate(rate);
    }-*/;

    public final native double getRate() /*-{
    return this.GetRate();
    }-*/;

    public final native String getMatrix() /*-{
    return this.GetMatrix();
    }-*/;

    public final native void setMatrix(String matrix) /*-{
    this.SetMatrix(matrix);
    }-*/;

    public final native void setRectangle(String rect) /*-{
    this.SetRectangle(rect);
    }-*/;

    public final native String getRectangle() /*-{
    return this.GetRectangle();
    }-*/;

    public final native void setKioskMode(boolean kioskMode) /*-{
    this.SetKioskMode(kioskMode);
    }-*/;

    public final native boolean isKioskMode() /*-{
    return this.GetKioskMode();
    }-*/;

    public final native void setBackgroundColor(String color) /*-{
    this.SetBgColor(color);
    }-*/;

    public final native String getBackgroundColor() /*-{
    return this.GetBgColor();
    }-*/;

    public final native String getStatus() /*-{
    return this.GetPluginStatus();
    }-*/;

    public final native void fillMediaInfo(MediaInfo id3) /*-{
    try {
    id3.@com.bramosystems.oss.player.core.client.MediaInfo::year = this.GetUserData("&#xA9;day");
    id3.@com.bramosystems.oss.player.core.client.MediaInfo::albumTitle = this.GetUserData("name");
    id3.@com.bramosystems.oss.player.core.client.MediaInfo::duration = parseFloat(this.GetDuration() / this.GetTimeScale() * 1000);
    id3.@com.bramosystems.oss.player.core.client.MediaInfo::artists = this.GetUserData('@prf');
    id3.@com.bramosystems.oss.player.core.client.MediaInfo::comment = this.GetUserData('info');
    id3.@com.bramosystems.oss.player.core.client.MediaInfo::title = this.GetUserData('name');
    id3.@com.bramosystems.oss.player.core.client.MediaInfo::contentProviders = this.GetUserData('@src');
    id3.@com.bramosystems.oss.player.core.client.MediaInfo::copyright = this.GetUserData('cprt');
    id3.@com.bramosystems.oss.player.core.client.MediaInfo::hardwareSoftwareRequirements = this.GetUserData('@req');
    id3.@com.bramosystems.oss.player.core.client.MediaInfo::publisher = this.GetUserData('@prd');
    id3.@com.bramosystems.oss.player.core.client.MediaInfo::genre = '';
    id3.@com.bramosystems.oss.player.core.client.MediaInfo::internetStationOwner = '';
    id3.@com.bramosystems.oss.player.core.client.MediaInfo::internetStationName = '';

    var rect = this.GetRectangle().split(',');
    id3.@com.bramosystems.oss.player.core.client.MediaInfo::videoWidth = String(parseInt(rect[2]) - parseInt(rect[0]));
    id3.@com.bramosystems.oss.player.core.client.MediaInfo::videoHeight = String(parseInt(rect[3]) - parseInt(rect[1]));
    } catch(e) {
    }
    }-*/;
}
