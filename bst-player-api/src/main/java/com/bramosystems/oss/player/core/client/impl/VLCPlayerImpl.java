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
import com.bramosystems.oss.player.core.client.ui.VLCPlayer;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * Native implementation of the VLCPlayer class. It is not recommended to
 * interact with this class directly.
 *
 * @author Sikirulai Braheem
 * @see VLCPlayer
 */
public class VLCPlayerImpl extends JavaScriptObject {

    public static native VLCPlayerImpl getPlayer(String playerId) /*-{
    return $doc.getElementById(playerId);
    }-*/;

    protected VLCPlayerImpl() {
    }

    public final native String getPluginVersion() /*-{
    return this.VersionInfo;
    }-*/;

    public final native void play() /*-{
    this.playlist.play();
    }-*/;

    public final native void playMediaAt(int index) /*-{
    this.playlist.playItem(index);
    }-*/;

    public final native void stop() /*-{
    this.playlist.stop();
    }-*/;

    public final native void togglePause() /*-{
    this.playlist.togglePause();
    }-*/;

    public final native double getTime() /*-{
    try {
    return this.input.time;
    } catch(e) {
    return 0;
    }
    }-*/;

    public final native void setTime(double time) /*-{
    try {
    this.input.time = time;
    } catch(e) {}
    }-*/;

    public final native double getDuration() /*-{
    try {
    return parseFloat(this.input.length);
    } catch(e) {
    return 0;
    }
    }-*/;

    public final native double getVolume() /*-{
    try{
    return this.audio.volume / 100.0;
    } catch(e){
    return 0;
    }
    }-*/;

    public final native void setVolume(double volume) /*-{
    try{
    this.audio.volume = parseInt(volume * 100);
    } catch(e){}
    }-*/;

    public final native boolean isMute() /*-{
    try{
    return this.audio.mute;
    } catch(e){
    return false;
    }
    }-*/;

    public final native void setMute(boolean mute) /*-{
    try{
    this.audio.mute = mute;
    } catch(e){}
    }-*/;

    public final native double getRate() /*-{
    try{
    return this.input.rate;
    } catch(e){
    return -1;
    }
    }-*/;

    public final native void setRate(double rate) /*-{
    try{
    this.input.rate = rate;
    } catch(e){}
    }-*/;

    public final native String getAspectRatio() /*-{
    try{
    return this.video.aspectRatio;
    } catch(e){
    return '';
    }
    }-*/;

    public final native void setAspectRatio(String aspect) /*-{
    try{
    this.video.aspectRatio = aspect;
    } catch(e){}
    }-*/;

    public final native String getVideoWidth() /*-{
    try{
    if(this.input.hasVout){
    return this.video.width;
    }else {
    return 0;
    }
    } catch(e){
    return 0;
    }
    }-*/;

    public final native String getVideoHeight() /*-{
    try{
    if(this.input.hasVout) {
    return this.video.height;
    }else {
    return 0;
    }
    } catch(e){
    return 0;
    }
    }-*/;

    public final native void toggleFullScreen() /*-{
    try{
    this.video.toggleFullscreen();
    } catch(e){}
    }-*/;

    public final native boolean hasVideo() /*-{
    try{
    return this.input.hasVout;
    } catch(e){
    return false;
    }
    }-*/;

    public final native int getCurrentAudioTrack() /*-{
    try{
    return this.audio.track;
    } catch(e){
    return -1;
    }
    }-*/;

    public final native int getAudioChannelMode() /*-{
    try{
    return this.audio.channel;
    } catch(e){
    return -1;
    }
    }-*/;

    public final native void setAudioChannelMode(int mode) /*-{
    try{
    this.audio.channel = mode;
    } catch(e){}
    }-*/;

    public final native int addToPlaylist(String mediaURL) /*-{
    try{
    return this.playlist.add(mediaURL);
    } catch(e){
    return -1;
    }
    }-*/;

    public final native int addToPlaylist(String mediaURL, String options) /*-{
    try{
    var opts = new Array();
    opts.push(options);
    return this.playlist.add(mediaURL, '', opts);
    } catch(e){
    return -1;
    }
    }-*/;

    public final native void removeFromPlaylist(int index) /*-{
    try{
    this.playlist.items.remove(index);
    } catch(e){}
    }-*/;

    public final native void clearPlaylist() /*-{
    try{
    this.playlist.items.clear();
    } catch(e){}
    }-*/;

    public final native int getPlaylistCount() /*-{
    try{
    return this.playlist.items.count - 1; // [vlc://quit is also part of playlist]
    } catch(e){
    return 1;
    }
    }-*/;

    public final native int getPlayerState() /*-{
    try{
    return this.input.state;
    } catch(e){
    return -1;
    }
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
    id3.@com.bramosystems.oss.player.core.client.MediaInfo::duration = parseFloat(this.input.length);

    if(this.input.hasVout) {
    id3.@com.bramosystems.oss.player.core.client.MediaInfo::videoWidth = String(this.video.width);
    id3.@com.bramosystems.oss.player.core.client.MediaInfo::videoHeight = String(this.video.height);
    }
    } catch(e) {
    }
    }-*/;
}
