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
package com.bramosystems.oss.player.script.client;

import com.bramosystems.oss.player.core.client.AbstractMediaPlayer;
import com.bramosystems.oss.player.core.client.PlaylistSupport;
import com.bramosystems.oss.player.core.client.skin.MediaSeekBar;
import com.bramosystems.oss.player.script.client.impl.ScriptUtil.ScriptPlayer;
import com.bramosystems.oss.player.script.client.impl.ScriptUtil.ScriptSeekBar;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Utility class to export the player and seek bar widgets as Javascript objects
 * that can be used in non-GWT applications.
 *
 * <p>
 * The objects are bound to the <code>bstplayer</code> namespace.
 * </p>
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems dot com>
 * @see <a href="package-summary.html#export">Exporting the player widgets</a>
 */
public class ExportUtil {

    /**
     * Calls the <code>onBSTPlayerReady()</code> javascript function on the
     * host page.
     *
     * <p>This method should be called after all widgets have been exported</p>
     */
    public static native void signalAPIReady() /*-{
    $wnd.onBSTPlayerReady();
    }-*/;

    /**
     * Exports the {@linkplain AbstractMediaPlayer} implementation as Javascript object.
     *
     * <p>
     * The player is made available as a <code>bstplayer.Player</code> Javascript
     * object. The object supports all the public methods defined in the
     * {@linkplain AbstractMediaPlayer} and {@linkplain PlaylistSupport}.
     * </p>
     *
     * <p>
     * <b>Note:</b> The {@code bstplayer.Player} object defines an
     * {@code addEventListener(eventName, function)} method, instead of the {@code addXXXHandler()}
     * methods.
     * </p>
     */
    public static native void exportPlayer() /*-{
    if($wnd.bstplayer == null){
    $wnd.bstplayer = new Object();
    }
    
    $wnd.bstplayer.Player = function(name, mediaURL, autoplay, width, height, options){
    // init the player ...
    var _player = @com.bramosystems.oss.player.script.client.ExportUtil::getPlayer(Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)(name,mediaURL,autoplay,width,height,options);
    
    this.inject = function(containerId) {
    @com.bramosystems.oss.player.script.client.ExportUtil::inject(Lcom/google/gwt/user/client/ui/Widget;Ljava/lang/String;)(_player, containerId);
    }
    this.setResizeToVideoSize = function(resize) {
    _player.@com.bramosystems.oss.player.script.client.impl.ScriptUtil.ScriptPlayer::setResizeToVideoSize(Z)(resize);
    }
    this.isResizeToVideoSize = function() {
    return _player.@com.bramosystems.oss.player.script.client.impl.ScriptUtil.ScriptPlayer::isResizeToVideoSize()();
    }
    this.getVideoHeight = function() {
    return _player.@com.bramosystems.oss.player.script.client.impl.ScriptUtil.ScriptPlayer::getVideoHeight()();
    }
    this.getVideoWidth = function() {
    return _player.@com.bramosystems.oss.player.script.client.impl.ScriptUtil.ScriptPlayer::getVideoWidth()();
    }
    this.loadMedia = function(mediaURL) {
    _player.@com.bramosystems.oss.player.script.client.impl.ScriptUtil.ScriptPlayer::loadMedia(Ljava/lang/String;)(mediaURL);
    }
    this.playMedia = function() {
    try {
    _player.@com.bramosystems.oss.player.script.client.impl.ScriptUtil.ScriptPlayer::playMedia()();
    }catch(e){throw e;}
    }
    this.stopMedia = function() {
    _player.@com.bramosystems.oss.player.script.client.impl.ScriptUtil.ScriptPlayer::stopMedia()();
    }
    this.pauseMedia = function() {
    _player.@com.bramosystems.oss.player.script.client.impl.ScriptUtil.ScriptPlayer::pauseMedia()();
    }
    this.getMediaDuration = function() {
    return _player.@com.bramosystems.oss.player.script.client.impl.ScriptUtil.ScriptPlayer::getMediaDurationImpl()();
    }
    this.getPlayPosition = function() {
    return _player.@com.bramosystems.oss.player.script.client.impl.ScriptUtil.ScriptPlayer::getPlayPosition()();
    }
    this.setPlayPosition = function(position) {
    _player.@com.bramosystems.oss.player.script.client.impl.ScriptUtil.ScriptPlayer::setPlayPosition(D)(position);
    }
    this.getVolume = function() {
    return _player.@com.bramosystems.oss.player.script.client.impl.ScriptUtil.ScriptPlayer::getVolume()();
    }
    this.setVolume = function(volume) {
    _player.@com.bramosystems.oss.player.script.client.impl.ScriptUtil.ScriptPlayer::setVolume(D)(volume);
    }
    this.showLogger = function(show) {
    _player.@com.bramosystems.oss.player.script.client.impl.ScriptUtil.ScriptPlayer::showLogger(Z)(show);
    }
    this.setControllerVisible = function(show) {
    _player.@com.bramosystems.oss.player.script.client.impl.ScriptUtil.ScriptPlayer::setControllerVisible(Z)(show);
    }
    this.isControllerVisible = function() {
    return _player.@com.bramosystems.oss.player.script.client.impl.ScriptUtil.ScriptPlayer::isControllerVisible()();
    }
    this.setLoopCount = function(loop) {
    _player.@com.bramosystems.oss.player.script.client.impl.ScriptUtil.ScriptPlayer::setLoopCount(I)(loop);
    }
    this.getLoopCount = function() {
    return _player.@com.bramosystems.oss.player.script.client.impl.ScriptUtil.ScriptPlayer::getLoopCount()();
    }
    this.addEventListener = function(name, _function) {
    _player.@com.bramosystems.oss.player.script.client.impl.ScriptUtil.ScriptPlayer::addEventListener(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)(name,_function);
    }
    this.setConfigParameter = function(param, value) {
    _player.@com.bramosystems.oss.player.script.client.impl.ScriptUtil.ScriptPlayer::setConfigParameter(Ljava/lang/String;Ljava/lang/String;)(param,value);
    }
    this.setShuffleEnabled = function(enable) {
    _player.@com.bramosystems.oss.player.script.client.impl.ScriptUtil.ScriptPlayer::setShuffleEnabled(Z)(enable);
    }
    this.isShuffleEnabled = function() {
    return _player.@com.bramosystems.oss.player.script.client.impl.ScriptUtil.ScriptPlayer::isShuffleEnabled()();
    }
    this.addToPlaylist = function(mediaURL) {
    _player.@com.bramosystems.oss.player.script.client.impl.ScriptUtil.ScriptPlayer::addToPlaylist(Ljava/lang/String;)(mediaURL);
    }
    this.removeFromPlaylist = function(index) {
    _player.@com.bramosystems.oss.player.script.client.impl.ScriptUtil.ScriptPlayer::removeFromPlaylist(I)(index);
    }
    this.clearPlaylist = function() {
    _player.@com.bramosystems.oss.player.script.client.impl.ScriptUtil.ScriptPlayer::clearPlaylist()();
    }
    this.playNext = function() {
    _player.@com.bramosystems.oss.player.script.client.impl.ScriptUtil.ScriptPlayer::playNext()();
    }
    this.playPrevious = function() {
    _player.@com.bramosystems.oss.player.script.client.impl.ScriptUtil.ScriptPlayer::playPrevious()();
    }
    this.play = function(index) {
    _player.@com.bramosystems.oss.player.script.client.impl.ScriptUtil.ScriptPlayer::play(I)(index);
    }
    this.getPlaylistSize = function() {
    return _player.@com.bramosystems.oss.player.script.client.impl.ScriptUtil.ScriptPlayer::getPlaylistSize()();
    }
    this.getRate = function() {
    return _player.@com.bramosystems.oss.player.script.client.impl.ScriptUtil.ScriptPlayer::getRate()();
    }
    this.getRepeatMode = function() {
    return _player.@com.bramosystems.oss.player.script.client.impl.ScriptUtil.ScriptPlayer::_getRepeatMode()();
    }
    this.setRate = function(rate) {
    _player.@com.bramosystems.oss.player.script.client.impl.ScriptUtil.ScriptPlayer::setRate(D)(rate);
    }
    this.setRepeatMode = function(mode) {
    _player.@com.bramosystems.oss.player.script.client.impl.ScriptUtil.ScriptPlayer::setRepeatMode(Ljava/lang/String;)(mode);
    }
    }
    }-*/;

    /**
     * Exports a {@linkplain MediaSeekBar} implementation as Javascript object.
     *
     * <p>
     * The seekbar is made available as a <code>bstplayer.SeekBar</code> Javascript
     * object. The object supports all the public methods defined in the
     * {@linkplain MediaSeekBar} class.
     * </p>
     *
     * <p>
     * <b>Note:</b> The {@code bstplayer.SeekBar} object defines an
     * {@code addEventListener(eventName, function)} method, instead of the {@code addXXXHandler()}
     * methods.
     * </p>
     */
    public static native void exportSeekBar() /*-{
    if($wnd.bstplayer == null){
    $wnd.bstplayer = new Object();
    }
    
    $wnd.bstplayer.SeekBar = function(height, containerId, options){
    // init the seekbar ...
    var _seek = @com.bramosystems.oss.player.script.client.ExportUtil::getSeekBar(ILjava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)(height,containerId,options);
    
    this.setLoadingProgress = function(progress) {
    _seek.@com.bramosystems.oss.player.script.client.impl.ScriptUtil.ScriptSeekBar::setLoadingProgress(D)(progress);
    }
    this.setPlayingProgress = function(progress) {
    _seek.@com.bramosystems.oss.player.script.client.impl.ScriptUtil.ScriptSeekBar::setPlayingProgress(D)(progress);
    }
    this.addEventListener = function(name, _function) {
    _seek.@com.bramosystems.oss.player.script.client.impl.ScriptUtil.ScriptSeekBar::addEventListener(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)(name,_function);
    }
    }
    }-*/;

    private static ScriptPlayer getPlayer(String name, String url, boolean autoplay, String width,
            String height, JavaScriptObject options) {
        return new ScriptPlayer(name, url, autoplay, width, height, options);
    }

    private static ScriptSeekBar getSeekBar(int height, String containerId, JavaScriptObject options) {
        return new ScriptSeekBar(height, containerId, options);
    }

    private static void inject(Widget widget, String containerId) {
        RootPanel.get(containerId).add(widget);
    }
}
