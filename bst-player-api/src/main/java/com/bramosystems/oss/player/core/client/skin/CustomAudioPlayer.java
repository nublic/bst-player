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
package com.bramosystems.oss.player.core.client.skin;

import com.bramosystems.oss.player.core.client.playlist.MRL;
import com.bramosystems.oss.player.core.client.*;
import com.bramosystems.oss.player.core.client.ui.*;
import com.bramosystems.oss.player.core.event.client.DebugEvent;
import com.bramosystems.oss.player.core.event.client.DebugHandler;
import com.bramosystems.oss.player.core.event.client.LoadingProgressEvent;
import com.bramosystems.oss.player.core.event.client.LoadingProgressHandler;
import com.bramosystems.oss.player.core.event.client.MediaInfoEvent;
import com.bramosystems.oss.player.core.event.client.MediaInfoHandler;
import com.bramosystems.oss.player.core.event.client.PlayStateEvent;
import com.bramosystems.oss.player.core.event.client.PlayStateHandler;
import com.bramosystems.oss.player.core.event.client.PlayerStateEvent;
import com.bramosystems.oss.player.core.event.client.PlayerStateHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.List;

/**
 * Abstract base class for HTML based custom audio players.
 * 
 * <p>The actual player plugin used to playback media files is wrapped by
 * this player and hidden on the browser.  This ensures that the player
 * is controlled via the HTML controls provided by implementation classes.
 *
 * @author Sikirulai Braheem
 * @since 0.6
 */
public abstract class CustomAudioPlayer extends AbstractMediaPlayer implements PlaylistSupport {

    private AbstractMediaPlayer engine;
    private SimplePanel controller;
    /**
     * The Logger widget attached to this player
     */
    protected final Logger logger = new Logger();

    /**
     * Constructs <code>CustomAudioPlayer</code> with the specified {@code height} and
     * {@code width} which uses the specified {@code playerPlugin} to playback media
     * located at {@code mediaURL}. Media playback begins automatically if
     * {@code autoplay} is {@code true}.
     *
     * @param playerPlugin the plugin to use for playback.
     * @param mediaURL the URL of the media to playback
     * @param autoplay {@code true} to start playing automatically, {@code false} otherwise
     * @param height the height of the player
     * @param width the width of the player.
     *
     * @throws LoadException if an error occurs while loading the media.
     * @throws PluginVersionException if the required player plugin version is not installed on the client.
     * @throws PluginNotFoundException if the player plugin is not installed on the client.
     *
     * @see Plugin
     * @see QuickTimePlayer
     * @see WinMediaPlayer
     * @see FlashMediaPlayer
     *
     */
    public CustomAudioPlayer(Plugin playerPlugin, String mediaURL, boolean autoplay,
            String height, String width) throws PluginNotFoundException,
            PluginVersionException, LoadException {
        engine = PlayerUtil.getPlayer(playerPlugin, mediaURL, autoplay, null, null);
        engine.addDebugHandler(new DebugHandler() {

            @Override
            public void onDebug(DebugEvent event) {
                fireEvent(event);
            }
        });
        engine.addLoadingProgressHandler(new LoadingProgressHandler() {

            @Override
            public void onLoadingProgress(LoadingProgressEvent event) {
                fireEvent(event);
            }
        });
        engine.addMediaInfoHandler(new MediaInfoHandler() {

            @Override
            public void onMediaInfoAvailable(MediaInfoEvent event) {
                fireEvent(event);
            }
        });
        engine.addPlayStateHandler(new PlayStateHandler() {

            @Override
            public void onPlayStateChanged(PlayStateEvent event) {
                fireEvent(event);
            }
        });
        engine.addPlayerStateHandler(new PlayerStateHandler() {

            @Override
            public void onPlayerStateChanged(PlayerStateEvent event) {
                fireEvent(event);
            }
        });

        controller = new SimplePanel();
        controller.setWidth("100%");

        logger.setVisible(false);

        AbsolutePanel hp = new AbsolutePanel();
        hp.setSize(width, height);
        hp.add(engine, 0, 0);
        hp.add(controller, 0, 0);

        DockPanel vp = new DockPanel();
        vp.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
        vp.setWidth("100%");
        vp.add(logger, DockPanel.SOUTH);
        vp.add(hp, DockPanel.SOUTH);
        vp.setCellHorizontalAlignment(hp, VerticalPanel.ALIGN_CENTER);
        super.initWidget(vp);
    }

    /**
     * Overridden to prevent subclasses from changing the wrapped widget.
     * Subclass should call <code>setPlayerControlWidget</code> instead.
     *
     * @see #setPlayerControlWidget(com.google.gwt.user.client.ui.Widget)
     */
    @Override
    protected final void initWidget(Widget widget) {
    }

    /**
     * Sets the widget that will be used to control the player plugin.
     * <p>Subclasses should call this method before calling any method that
     * targets this widget.
     *
     * @param widget the player control widget
     */
    protected final void setPlayerControlWidget(Widget widget) {
        controller.setWidget(widget);
    }

    @Override
    public long getMediaDuration() {
        return engine.getMediaDuration();
    }

    @Override
    public double getPlayPosition() {
        return engine.getPlayPosition();
    }

    @Override
    public void setPlayPosition(double position) {
        engine.setPlayPosition(position);
    }

    @Override
    public void loadMedia(String mediaURL) throws LoadException {
        engine.loadMedia(mediaURL);
    }

    @Override
    public void pauseMedia() {
        engine.pauseMedia();
    }

    @Override
    public void playMedia() throws PlayException {
        engine.playMedia();
    }

    @Override
    public void stopMedia() {
        engine.stopMedia();
    }

    @Override
    public double getVolume() {
        return engine.getVolume();
    }

    @Override
    public void setVolume(double volume) {
        engine.setVolume(volume);
    }

    /**
     * Returns the remaining number of times this player loops playback before stopping.
     */
    @Override
    public int getLoopCount() {
        return engine.getLoopCount();
    }

    /**
     * Sets the number of times the current media file should loop playback before stopping.
     */
    @Override
    public void setLoopCount(int loop) {
        engine.setLoopCount(loop);
    }

    @Override
    public void showLogger(boolean show) {
        logger.setVisible(show);
    }

    @Override
    public void addToPlaylist(String mediaURL) {
        if (engine instanceof PlaylistSupport) {
            ((PlaylistSupport) engine).addToPlaylist(mediaURL);
        }
    }

    @Override
    public void addToPlaylist(MRL mediaLocator) {
        if (engine instanceof PlaylistSupport) {
            ((PlaylistSupport) engine).addToPlaylist(mediaLocator);
        }
    }

    @Override
    public void addToPlaylist(String... mediaURLs) {
        if (engine instanceof PlaylistSupport) {
            ((PlaylistSupport) engine).addToPlaylist(mediaURLs);
        }
    }

    @Override
    public void addToPlaylist(List<MRL> mediaLocators) {
        if (engine instanceof PlaylistSupport) {
            ((PlaylistSupport) engine).addToPlaylist(mediaLocators);
        }
    }

    @Override
    public boolean isShuffleEnabled() {
        if (engine instanceof PlaylistSupport) {
            return ((PlaylistSupport) engine).isShuffleEnabled();
        }
        return false;
    }

    @Override
    public void removeFromPlaylist(int index) {
        if (engine instanceof PlaylistSupport) {
            ((PlaylistSupport) engine).removeFromPlaylist(index);
        }

    }

    @Override
    public void setShuffleEnabled(boolean enable) {
        if (engine instanceof PlaylistSupport) {
            ((PlaylistSupport) engine).setShuffleEnabled(enable);
        }
    }

    @Override
    public void clearPlaylist() {
        if (engine instanceof PlaylistSupport) {
            ((PlaylistSupport) engine).clearPlaylist();
        }
    }

    @Override
    public int getPlaylistSize() {
        if (engine instanceof PlaylistSupport) {
            return ((PlaylistSupport) engine).getPlaylistSize();
        }
        return 1;
    }

    @Override
    public void play(int index) throws IndexOutOfBoundsException {
        if (engine instanceof PlaylistSupport) {
            ((PlaylistSupport) engine).play(index);
        }
    }

    @Override
    public void playNext() throws PlayException {
        if (engine instanceof PlaylistSupport) {
            ((PlaylistSupport) engine).playNext();
        }
    }

    @Override
    public void playPrevious() throws PlayException {
        if (engine instanceof PlaylistSupport) {
            ((PlaylistSupport) engine).playPrevious();
        }
    }

    @Override
    public double getRate() {
        return engine.getRate();
    }

    @Override
    public RepeatMode getRepeatMode() {
        return engine.getRepeatMode();
    }

    @Override
    public <T extends ConfigValue> void setConfigParameter(ConfigParameter param, T value) {
        engine.setConfigParameter(param, value);
    }

    @Override
    public void setConfigParameter(ConfigParameter param, Number value) {
        engine.setConfigParameter(param, value);
    }

    @Override
    public void setConfigParameter(ConfigParameter param, String value) {
        engine.setConfigParameter(param, value);
    }

    @Override
    public void setRate(double rate) {
        engine.setRate(rate);
    }

    @Override
    public void setRepeatMode(RepeatMode mode) {
        engine.setRepeatMode(mode);
    }
}
