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

import com.bramosystems.oss.player.core.client.RepeatMode;
import com.bramosystems.oss.player.core.client.ui.FlashMediaPlayer;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * Native implementation of the FlashMediaPlayer class. It is not recommended to
 * interact with this class directly.
 *
 * @author Sikirulai Braheem
 * @see FlashMediaPlayer
 */
public class FlashMediaPlayerImpl extends JavaScriptObject {

    protected FlashMediaPlayerImpl() {
    }

    public static native FlashMediaPlayerImpl getPlayer(String playerId) /*-{
    return $doc.getElementById(playerId);
    }-*/;

    public native final String getPluginVersion() /*-{
    return this.getPluginVersion();
    }-*/;

    public native final String getPlayerVersion() /*-{
    return this.getPlayerVersion();
    }-*/;

    public native final void loadMedia(String url) /*-{
    this.load(url);
    }-*/;

    public native final void playMedia() /*-{
    this.play();
    }-*/;

    public native final boolean playMedia(int index) /*-{
    return this.playIndex(index);
    }-*/;

    public native final boolean playNext() /*-{
    return this.playNext();
    }-*/;

    public native final boolean playPrevious() /*-{
    return this.playPrev();
    }-*/;

    public native final void stopMedia() /*-{
    this.stop(true);
    }-*/;

    public native final void pauseMedia() /*-{
    this.stop(false);
    }-*/;

    public native final void closeMedia() /*-{
    try {
    this.close();
    }catch(err){}
    }-*/;

    public native final double getPlayPosition() /*-{
    return this.getPlayPosition();
    }-*/;

    public native final void setPlayPosition(double position) /*-{
    this.setPlayPosition(position);
    }-*/;

    public native final double getMediaDuration() /*-{
    return this.getDuration();
    }-*/;

    public native final void addToPlaylist(String mediaURL) /*-{
    this.addToPlaylist(mediaURL);
    }-*/;

    public native final void removeFromPlaylist(int index) /*-{
    this.removeFromPlaylist(index);
    }-*/;

    public native final void clearPlaylist() /*-{
    this.clearPlaylist();
    }-*/;

    public native final int getPlaylistCount() /*-{
    return this.getPlaylistSize();
    }-*/;

    public native final double getVolume() /*-{
    return this.getVolume();
    }-*/;

    public native final void setVolume(double volume) /*-{
    this.setVolume(volume);
    }-*/;

    public native final void setLoopCount(int count) /*-{
    this.setLoopCount(count);
    }-*/;

    public native final int getLoopCount() /*-{
    this.getLoopCount();
    }-*/;

    public native final boolean isShuffleEnabled() /*-{
    return this.isShuffleEnabled();
    }-*/;

    public native final void setShuffleEnabled(boolean enable) /*-{
    this.setShuffleEnabled(enable);
    }-*/;

    public native final int getVideoHeight() /*-{
    return this.getVideoHeight();
    }-*/;

    public native final int getVideoWidth() /*-{
    return this.getVideoWidth();
    }-*/;

    public native final String getMatrix() /*-{
    return this.getMatrix();
    }-*/;

    public native final void setMatrix(double a, double b, double c, double d, double tx, double ty) /*-{
    this.setMatrix(a, b, c, d, tx, ty);
    }-*/;

    public native final boolean isControllerVisible() /*-{
    return this.isControllerVisible();
    }-*/;

    public native final void setControllerVisible(boolean visible) /*-{
    this.setControllerVisible(visible);
    }-*/;

    public final RepeatMode getRepeatMode() {
        try {
            return RepeatMode.valueOf("REPEAT_" + getRepeatModeImpl().toUpperCase());
        } catch (Exception e) {
            return RepeatMode.REPEAT_OFF;
        }
    }

    public final void setRepeatMode(RepeatMode mode) {
        switch(mode) {
            case REPEAT_ALL:
                setRepeatModeImpl("all");
                break;
            case REPEAT_OFF:
                setRepeatModeImpl("off");
                break;
            case REPEAT_ONE:
                setRepeatModeImpl("one");
        }
    }

    private native String getRepeatModeImpl() /*-{
    return this.getRepeatMode();
    }-*/;

    private native void setRepeatModeImpl(String mode) /*-{
    this.setRepeatMode(mode);
    }-*/;

    public native final boolean isAutoHideController() /*-{
    return this.isAutoHideController();
    }-*/;

    public native final void setAutoHideController(boolean autohide) /*-{
    this.setAutoHideController(autohide);
    }-*/;
}