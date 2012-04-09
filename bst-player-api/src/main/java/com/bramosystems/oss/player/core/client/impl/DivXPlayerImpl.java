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

import com.bramosystems.oss.player.core.client.ui.FlashMediaPlayer;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * Native implementation of the FlashMediaPlayer class. It is not recommended to
 * interact with this class directly.
 *
 * @author Sikirulai Braheem
 * @see FlashMediaPlayer
 */
public class DivXPlayerImpl extends JavaScriptObject {

    protected DivXPlayerImpl() {
    }

    public static native DivXPlayerImpl getPlayer(String playerId) /*-{
    return $doc.getElementById(playerId);
    }-*/;

    public native final String getPluginVersion() /*-{
    return this.GetVersion();
    }-*/;

    public native final void loadMedia(String url) /*-{
    this.Open(url);
    }-*/;

    public native final void playMedia() /*-{
    this.Play();
    }-*/;

    public native final void stopMedia() /*-{
    this.Stop();
    }-*/;

    public native final void pauseMedia() /*-{
    this.Pause();
    }-*/;

    public native final void fastForward() /*-{
    this.FF();
    }-*/;

    public native final void rewind() /*-{
    this.RW();
    }-*/;

    public native final void seek(String method, double value) /*-{
    this.Seek(method, value);
    }-*/;

    public native final double getMediaDuration() /*-{
    return this.GetTotalTime() * 1000;
    }-*/;

    public native final void setMute(boolean mute) /*-{
    if(mute) {
    this.Mute();
    }else {
    this.UnMute();
    }
    }-*/;

    public native final void setVolume(double volume) /*-{
    this.volume = volume;
    }-*/;

    public native final double getVolume() /*-{
    return this.volume;
    }-*/;

    public native final void setAutoplay(boolean autoplay) /*-{
    this.SetAutoPlay(autoplay);
    }-*/;

    public native final void setBufferingMode(String mode) /*-{
    this.SetBufferingMode(mode);
    }-*/;

    public native final void setLoop(boolean loop) /*-{
    this.SetLoop(loop);
    }-*/;

    public native final int getVideoHeight() /*-{
    return this.GetVideoHeight();
    }-*/;

    public native final int getVideoWidth() /*-{
    return this.GetVideoWidth();
    }-*/;

    public native final void setPreviewImage(String imageUrl) /*-{
    this.SetPreviewImage(imageUrl);
    }-*/;

    public native final void setPreviewMessage(String message) /*-{
    this.SetPreviewMessage(message);
    }-*/;

    public native final void setPreviewMessageFontSize(int fontSize) /*-{
    this.SetPreviewMessageFontSize(fontSize);
    }-*/;

    public native final void setBannerEnabled(boolean enable) /*-{
    this.SetBannerEnabled(enable);
    }-*/;

    public native final void setMovieTitle(String title) /*-{
    this.SetMovieTitle(title);
    }-*/;

    public native final void setAllowContextMenu(boolean allow) /*-{
    this.SetAllowContextMenu(allow);
    }-*/;

    public native final void setMode(String mode) /*-{
    this.SetMode(mode);
    }-*/;

    public native final void setSize(int width, int height) /*-{
    this.Resize(width, height);
    }-*/;
}
