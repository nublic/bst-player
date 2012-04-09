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

import com.bramosystems.oss.player.youtube.client.YouTubePlayer;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;

/**
 * Native implementation of the YouTubePlayer class. It is not recommended to
 * interact with this class directly.
 *
 * @author Sikirulai Braheem
 * @see YouTubePlayer
 */
public class YouTubePlayerImpl extends JavaScriptObject {

    protected YouTubePlayerImpl() {
    }

    public static native YouTubePlayerImpl getPlayerImpl(String playerId) /*-{
    return $doc.getElementById(playerId);
    }-*/;

    public final native String getUrl() /*-{
    return this.getVideoUrl();
    }-*/;

    public final native String getEmbedCode() /*-{
    return this.getVideoEmbedCode();
    }-*/;

    public final native void cueVideoById(String vid, double startTime) /*-{
    this.cueVideoById(vid, startTime);
    }-*/;

    public final native void cueVideoByUrl(String url, double startTime) /*-{
    this.cueVideoByUrl(url, startTime);
    }-*/;

    public final native void loadVideoById(String vid, double startTime) /*-{
    this.loadVideoById(vid, startTime);
    }-*/;

    public final native void loadVideoByUrl(String url, double startTime) /*-{
    this.loadVideoByUrl(url, startTime);
    }-*/;

    public final native void play() /*-{
    this.playVideo();
    }-*/;

    public final native void pause() /*-{
    this.pauseVideo();
    }-*/;

    public final native void stop() /*-{
    this.stopVideo();
    }-*/;

    public final native void clear() /*-{
    this.clearVideo();
    }-*/;

    public final native double getBytesLoaded() /*-{
    return this.getVideoBytesLoaded();
    }-*/;

    public final native double getBytesTotal() /*-{
    return this.getVideoBytesTotal();
    }-*/;

    public final native double getStartBytes() /*-{
    return this.getVideoStartBytes();
    }-*/;

    public final native void mute() /*-{
    this.mute();
    }-*/;

    public final native void unMute() /*-{
    this.unMute();
    }-*/;

    public final native boolean isMuted() /*-{
    return this.isMuted();
    }-*/;

    public final native double getCurrentTime() /*-{
    return this.getCurrentTime() * 1000;
    }-*/;

    public final native void seekTo(double seconds, boolean allowSeekAhead) /*-{
    this.seekTo(seconds, allowSeekAhead);
    }-*/;

    public final native double getDuration() /*-{
    return this.getDuration() * 1000;
    }-*/;

    public final native double getVolume() /*-{
    return this.getVolume() / 100;
    }-*/;

    public final native void setVolume(double volume) /*-{
    this.setVolume(volume * 100);
    }-*/;

    public final native String getPlaybackQuality() /*-{
    return this.getPlaybackQuality();
    }-*/;

    public final native JsArrayString getAvailableQualityLevels() /*-{
    return this.getAvailableQualityLevels();
    }-*/;

    public final native void setPlaybackQuality(String quality) /*-{
    this.setPlaybackQuality(quality);
    }-*/;

    public final native void registerHandlers(String playerId) /*-{
    this.addEventListener("onStateChange", "bstplayer.handlers.utube." + playerId + ".onStateChanged");
    this.addEventListener("onPlaybackQualityChange", "bstplayer.handlers.utube." + playerId + ".onQualityChanged");
    this.addEventListener("onError", "bstplayer.handlers.utube." + playerId + ".onError");
    }-*/;

    public final native void cuePlaylist(JsArrayString vids) /*-{
    this.cuePlaylist(vids);
    }-*/;

    public final native void loadPlaylist(String pid) /*-{
    this.loadPlaylist(pid);
    }-*/;

    public final native void nextVideo() /*-{
    this.nextVideo();
    }-*/;

    public final native void previousVideo() /*-{
    this.previousVideo();
    }-*/;

    public final native void playVideoAt(double index) /*-{
    this.playVideoAt(index);
    }-*/;

    public final native void setLoop(boolean loop) /*-{
    this.setLoop(loop);
    }-*/;

    public final native void setShuffle(boolean shuffle) /*-{
    this.setShuffle(shuffle);
    }-*/;

    public final native JsArrayString getPlaylist() /*-{
    return this.getPlaylist();
    }-*/;

    public final native double getPlaylistIndex() /*-{
    return this.getPlaylistIndex();
    }-*/;
}
