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
import com.bramosystems.oss.player.core.client.geom.MatrixSupport;
import com.bramosystems.oss.player.core.client.geom.TransformationMatrix;
import com.bramosystems.oss.player.core.client.*;
import com.bramosystems.oss.player.core.client.ui.FlashMediaPlayer;
import com.bramosystems.oss.player.core.client.ui.QuickTimePlayer;
import com.bramosystems.oss.player.core.client.ui.WinMediaPlayer;
import com.bramosystems.oss.player.core.event.client.*;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.List;

/**
 * Abstract base class for HTML based custom video players.
 * 
 * <p>The actual player plugin used to playback media files is wrapped by
 * this player and hidden on the browser.  This ensures that the player
 * is controlled via the HTML controls provided by implementation classes.
 *
 * @author Sikirulai Braheem
 */
public abstract class CustomVideoPlayer extends AbstractMediaPlayer implements PlaylistSupport,
        MatrixSupport {

    private AbstractMediaPlayer engine;
    private SimplePanel controller;

    /**
     * Constructs <code>CustomVideoPlayer</code> with the specified {@code height} and
     * {@code width} using the specified {@code playerPlugin} to playback media located
     * at {@code mediaURL}. Media playback begins automatically if {@code autoplay} is {@code true}.
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
     * @throws NullPointerException if {@code height} or {@code width} is {@code null}
     *
     * @see Plugin
     * @see QuickTimePlayer
     * @see WinMediaPlayer
     * @see FlashMediaPlayer
     */
    public CustomVideoPlayer(Plugin playerPlugin, String mediaURL, boolean autoplay,
            String height, String width)
            throws PluginNotFoundException, PluginVersionException, LoadException {
        if (height == null) {
            throw new NullPointerException("height cannot be null");
        }
        if (width == null) {
            throw new NullPointerException("width cannot be null");
        }

        engine = PlayerUtil.getPlayer(playerPlugin, mediaURL, autoplay, height, "100%");
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
                switch (event.getPlayerState()) {
                    case DimensionChangedOnVideo:
                        onVideoDimensionChanged(engine.getOffsetWidth(),
                                engine.getOffsetHeight());
                        break;
                    default:
                        fireEvent(event);
                }
            }
        });

        engine.setConfigParameter(ConfigParameter.WMPUIMode, WinMediaPlayer.UIMode.NONE);
        engine.setControllerVisible(false);
        engine.showLogger(false);

        controller = new SimplePanel();
        controller.setWidth("100%");

        FlowPanel hp = new FlowPanel();
        hp.add(engine);
        hp.add(controller);

        super.initWidget(hp);
        setWidth(width);
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
    public int getVideoHeight() {
        return engine.getVideoHeight();
    }

    @Override
    public int getVideoWidth() {
        return engine.getVideoWidth();
    }

    @Override
    public boolean isControllerVisible() {
        return controller.isVisible();
    }

    @Override
    public boolean isResizeToVideoSize() {
        return engine.isResizeToVideoSize();
    }

    @Override
    public void setControllerVisible(boolean show) {
        controller.setVisible(show);
    }

    @Override
    public void setResizeToVideoSize(boolean resize) {
        engine.setResizeToVideoSize(resize);
    }

    @Override
    public void showLogger(boolean show) {
        engine.showLogger(show);
    }

    @Override
    public TransformationMatrix getMatrix() {
        if (engine instanceof MatrixSupport) {
            return ((MatrixSupport) engine).getMatrix();
        }
        return null;
    }

    @Override
    public void setMatrix(TransformationMatrix matrix) {
        if (engine instanceof MatrixSupport) {
            ((MatrixSupport) engine).setMatrix(matrix);
        }
    }

    /**
     * Called when the size of the embedded player changes to match the dimension of
     * the media (especially video)
     *
     * <p>This method is called whenever the {@link PlayerStateEvent} event is fired
     * with <code>State.DimensionChangedOnVideo</code> state.
     *
     * @param width the width of the media (in pixels)
     * @param height the height of the media (in pixels)
     */
    protected abstract void onVideoDimensionChanged(int width, int height);

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
