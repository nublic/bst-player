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
import com.bramosystems.oss.player.core.client.ui.WinMediaPlayer;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * Native implementation of the WinMediaPlayer class. It is not recommended to
 * interact with this class directly.
 *
 * @author Sikirulai Braheem
 * @see WinMediaPlayer
 */
public class WinMediaPlayerImpl extends JavaScriptObject {

    protected WinMediaPlayerImpl() {
    }

    public static native WinMediaPlayerImpl getPlayer(String playerId) /*-{
    return $doc.getElementById(playerId);
    }-*/;

    public final native void setURL(String mediaURL) /*-{
    try {
    this.URL = mediaURL;
    } catch(e) {}
    }-*/;

    public final native String getURL() /*-{
    try {
    return this.URL;
    } catch(e) {return null;}
    }-*/;

    public final native void close() /*-{
    try {
    this.close();
    } catch(e){} // suppress exp...
    }-*/;

    public final native double getDuration() /*-{
    try {
    return this.currentMedia.duration * 1000;
    } catch(e) {return -1;}
    }-*/;

    public final native void setUIMode(String mode) /*-{
    try {
    this.uiMode = mode;
    } catch(e) {}
    }-*/;

    public final native String getUIMode() /*-{
    try {
    return this.uiMode || '';
    } catch(e) {return null;}
    }-*/;

    public final native String getPlayerVersion() /*-{
    try {
    return this.versionInfo;
    } catch(e) {return null;}
    }-*/;

    public final native int getPlayState() /*-{
    try {
    return this.playState || -10;
    } catch(e) {return -10;}
    }-*/;

    public final native void fillMetadata(MediaInfo info, String errorMsg) /*-{
    try {
    var plyrMedia = this.currentMedia;
    info.@com.bramosystems.oss.player.core.client.MediaInfo::title = plyrMedia.getItemInfo('Title');
    info.@com.bramosystems.oss.player.core.client.MediaInfo::copyright = plyrMedia.getItemInfo('Copyright');
    info.@com.bramosystems.oss.player.core.client.MediaInfo::duration = parseFloat(plyrMedia.getItemInfo('Duration')) * 1000;
    info.@com.bramosystems.oss.player.core.client.MediaInfo::publisher = plyrMedia.getItemInfo('WM/Publisher');
    info.@com.bramosystems.oss.player.core.client.MediaInfo::comment = plyrMedia.getItemInfo('Description');
    info.@com.bramosystems.oss.player.core.client.MediaInfo::year = plyrMedia.getItemInfo('WM/Year');
    info.@com.bramosystems.oss.player.core.client.MediaInfo::albumTitle = plyrMedia.getItemInfo('WM/AlbumTitle');
    info.@com.bramosystems.oss.player.core.client.MediaInfo::artists = plyrMedia.getItemInfo('WM/AlbumArtist');
    info.@com.bramosystems.oss.player.core.client.MediaInfo::contentProviders = plyrMedia.getItemInfo('WM/Provider');
    info.@com.bramosystems.oss.player.core.client.MediaInfo::genre = plyrMedia.getItemInfo('WM/Genre');
    info.@com.bramosystems.oss.player.core.client.MediaInfo::internetStationOwner = plyrMedia.getItemInfo('WM/RadioStationOwner');
    info.@com.bramosystems.oss.player.core.client.MediaInfo::internetStationName = plyrMedia.getItemInfo('WM/RadioStationName');
    info.@com.bramosystems.oss.player.core.client.MediaInfo::hardwareSoftwareRequirements = plyrMedia.getItemInfo('WM/EncodingSettings');
    info.@com.bramosystems.oss.player.core.client.MediaInfo::videoWidth = String(plyrMedia.imageSourceWidth);
    info.@com.bramosystems.oss.player.core.client.MediaInfo::videoHeight = String(plyrMedia.imageSourceHeight);
    } catch(e) {errorMsg = e;}
    }-*/;

    public final native String getErrorDiscription() /*-{
    try {
    var err = this.error;
    if(err == undefined) return '';
    return err.item(0).errorDescription;
    } catch(e) {return null;}
    }-*/;

    public final native double getDownloadProgress() /*-{
    try {
    if(this.network) {
    return this.network.downloadProgress / 100;
    } else {return -1;}
    } catch(e) {return -1;}
    }-*/;

    public final native double getBufferingProgress() /*-{
    try {
    if(this.network) {
    return this.network.bufferingProgress / 100;
    } else {return -1;}
    } catch(e) {return -1;}
    }-*/;

    public final native void setStretchToFit(boolean stretch) /*-{
    try {
    this.stretchToFit = stretch;
    } catch(e) {}
    }-*/;

    public final native boolean isStretchToFit() /*-{
    try {
    return this.stretchToFit;
    } catch(e) {return false;}
    }-*/;

    public final native void setWindowlessVideo(boolean windowless) /*-{
    try {
    this.windowlessVideo = windowless;
    } catch(e) {}
    }-*/;

    public final native boolean isWindowlessVideo() /*-{
    try {
    return this.windowlessVideo;
    } catch(e) {return false;}
    }-*/;

    public final native String getAspectRatio() /*-{
    try {
    return this.getItemInfo('PixelAspectRatioX')+':'+this.getItemInfo('PixelAspectRatioY');
    } catch(e) {return 0;}
    }-*/;

    public final native String getPlayerId() /*-{
    return this.id;
    }-*/;

    // CurrentMedia object ..
    public final native String getCurrentMediaURL() /*-{
    try {
    return this.currentMedia.sourceURL;
    } catch(e) {return '';}
    }-*/;

    public final native int getVideoHeight() /*-{
    try {
    return this.currentMedia.imageSourceHeight;
    } catch(e) {return 0;}
    }-*/;

    public final native int getVideoWidth() /*-{
    try {
    return this.currentMedia.imageSourceWidth;
    } catch(e) {return 0;}
    }-*/;

    // Controls object ...
    public final native void play() /*-{
    try {
    this.controls.play();
    } catch(e) {}
    }-*/;

    public final native boolean canPlay() /*-{
    try {
    return this.controls.isAvailable('play');
    } catch(e) {return false;}
    }-*/;
    
    public final native void stop() /*-{
    try {
    this.controls.stop();
    } catch(e) {}
    }-*/;

    public final native void pause() /*-{
    try {
    this.controls.pause();
    } catch(e) {}
    }-*/;

    public final native double getCurrentPosition() /*-{
    try {
    return this.controls.currentPosition * 1000;
    } catch(e) {return -1;}
    }-*/;

    public final native void setCurrentPosition(double position) /*-{
    try {
    this.controls.currentPosition = position / 1000;
    } catch(e) {}
    }-*/;

    // Settings object ...
    public final native void setPlayCount(double _count) /*-{
    try {
    this.settings.playCount = _count;
    } catch(e) {}
    }-*/;

    public final native int getPlayCount() /*-{
    try {
    return this.settings.playCount || 1;
    } catch(e) {return 0;}
    }-*/;

    public final native int getVolume() /*-{
    try {
    return this.settings.volume;
    } catch(e) {return -1;}
    }-*/;

    public final native void setVolume(int volume) /*-{
    try {
    this.settings.volume = volume;
    } catch(e) {}
    }-*/;

    public final native boolean isModeEnabled(String mode) /*-{
    try {
    return this.settings.getMode(mode) || false;
    } catch(e) {return false;}
    }-*/;

    /**
     * Not supported in non-IE browsers
     * @param _mode
     * @param _enable
     */
    public final native void enableMode(String _mode, boolean _enable) /*-{
    try {
    this.settings.setMode(_mode, _enable);
    } catch(e) {}
    }-*/;

    public final native void setRate(double rate) /*-{
    try {
    this.settings.rate = rate;
    } catch(e) {}
    }-*/;

    public final native double getRate() /*-{
    try {
    return this.settings.rate;
    } catch(e) {return 0;}
    }-*/;

    public final native boolean requestMediaAccessRight(String accessLevel) /*-{
    try {
    return this.settings.requestMediaAccessRights(accessLevel);
    } catch(e) {return false;}
    }-*/;

    public final native String getMediaAccessRight() /*-{
    try {
    return this.settings.mediaAccessRights;
    } catch(e) {return null;}
    }-*/;

    public final native void setAutoStart(boolean autoStart) /*-{
    try {
    this.settings.autoStart = autoStart;
    } catch(e) {}
    }-*/;

   public final native boolean isAutoStart() /*-{
    try {
    return this.settings.autoStart;
    } catch(e) {}
    }-*/;

    // test playlist creation ...
    public final native WMPPlaylist getCurrentPlaylist() /*-{
    try {
    return this.currentPlaylist;
    } catch(e) {return null;}
    }-*/;
    
    public final native void setCurrentPlaylist(WMPPlaylist playlist) /*-{
    try {
    this.currentPlaylist = playlist;
    } catch(e) {}
    }-*/;
    
    public final native WMPPlaylist createPlaylist(String name) /*-{
    try {
    v = this.newPlaylist(name, '');
    $wnd.alert('v ' + name + ', ' + v.toString()); 
           return v;
    } catch(e) {//$wnd.alert('e : ' + name + ', ' + e);
        return null;}
    }-*/;

    public final native Media createMedia(String mediaURL) /*-{
//    try {
    return this.newMedia(mediaURL);
 //   } catch(e) {return null;}
    }-*/;
    
    public final native void setCurrentMedia(Media media) /*-{
    try {
    this.currentMedia(media);
    } catch(e) {}
    }-*/;

    public static class WMPPlaylist extends JavaScriptObject {
        
        protected WMPPlaylist(){}

        public final native void addItem(Media item) /*-{
        try {
        this.appendItem(item);
        } catch(e) {}
        }-*/;

        public final native Media getItem(int index) /*-{
        try {
        return this.item(index);
        } catch(e) {return null;}
        }-*/;

        public final native void removeItem(Media item) /*-{
        try {
        this.removeItem(item);
        } catch(e) {}
        }-*/;

        public final native void moveItem(int oldIndex, int newIndex) /*-{
        try {
        this.moveItem(oldIdex,newIndex);
        } catch(e) {}
        }-*/;

        public final native void insertItem(int index, Media item) /*-{
        try {
        this.insertItem(index,item);
        } catch(e) {}
        }-*/;
    }

    public static class Media extends JavaScriptObject {

        protected Media() {
        }

        public final native String getName() /*-{
        try {
        return this.name;
        } catch(e) {return null;}
        }-*/;

        public final native String getSourceURL() /*-{
        try {
        return this.sourceURL;
        } catch(e) {return null;}
        }-*/;
    }
}
