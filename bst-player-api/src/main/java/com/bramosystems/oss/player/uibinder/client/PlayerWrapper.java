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
package com.bramosystems.oss.player.uibinder.client;

import com.bramosystems.oss.player.core.client.playlist.MRL;
import com.bramosystems.oss.player.core.client.*;
import com.bramosystems.oss.player.core.client.geom.MatrixSupport;
import com.bramosystems.oss.player.core.client.geom.TransformationMatrix;
import com.bramosystems.oss.player.core.event.client.*;
import com.bramosystems.oss.player.util.client.RegExp;
import com.bramosystems.oss.player.util.client.RegExp.RegexException;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.List;

/**
 * Provides the base implementation of player widgets with UiBinder support
 *
 * @author Sikiru Braheem <sbraheem at bramosystems . com>
 * @param <T> the player implementation type
 * @since 1.1
 * @deprecated Replaced with {@link Player}, will be removed in future.
 */
public abstract class PlayerWrapper<T extends AbstractMediaPlayer> extends AbstractMediaPlayer
        implements MatrixSupport, com.bramosystems.oss.player.core.client.PlaylistSupport {

    private T _engine;
    private static String GWT_HOST_URL_ID = "(gwt-host::)\\S", GWT_MODULE_URL_ID = "(gwt-module::)\\S";
    protected Widget missingPluginNotice, missingPluginVersionNotice;
    private String _url, _height, _width;
    private boolean _autoplay;
    private SimplePanel _panel;

    /**
     * Parses the {@code mediaURL} for {@code gwt-host::} and {@code gwt-module::} keywords.
     * The keywords are effectively replaced with their corresponding relative URLs.
     *
     * <p>
     * {@code gwt-host::} is replaced with {@code GWT.getHostPageBaseURL()} while {@code gwt-module::}
     * is replaced with {@code GWT.getModuleBaseURL()}
     * </p>
     * 
     * @param mediaURL the mediaURL
     * @return the resulting absolute URL
     */
    public static String resolveMediaURL(String mediaURL) {
        RegExp.RegexResult rr = null;
        RegExp re = null;
        try {
            re = RegExp.getRegExp(GWT_HOST_URL_ID, "i");
            if (re.test(mediaURL)) {
                rr = re.exec(mediaURL);
                mediaURL = mediaURL.replaceAll(rr.getMatch(1), GWT.getHostPageBaseURL());
            }

            re = RegExp.getRegExp(GWT_MODULE_URL_ID, "i");
            if (re.test(mediaURL)) {
                rr = re.exec(mediaURL);
                mediaURL = mediaURL.replaceAll(rr.getMatch(1), GWT.getModuleBaseURL());
            }
        } catch (RegexException ex) {
        } finally {
            re = null;
            rr = null;
        }
        return mediaURL;
    }

    /**
     * The constructor
     *
     * @param mediaURL the URL of the media to playback
     * @param autoplay {@code true} to autoplay, {@code false} otherwise
     * @param height the height of the player (in CSS units)
     * @param width the width of the player (in CSS units)
     */
    protected PlayerWrapper(String mediaURL, boolean autoplay, String height, String width) {
        initWidget(_panel = new SimplePanel());
        _url = resolveMediaURL(mediaURL);
        _height = height;
        _width = width;
        _autoplay = autoplay;
    }

    @Override
    public void onLoad() {
        try {
            _engine = initPlayerEngine(_url, _autoplay, _height, _width);
            _engine.addDebugHandler(new DebugHandler() {

                @Override
                public void onDebug(DebugEvent event) {
                    fireEvent(event);
                }
            });
            _engine.addLoadingProgressHandler(new LoadingProgressHandler() {

                @Override
                public void onLoadingProgress(LoadingProgressEvent event) {
                    fireEvent(event);
                }
            });
            _engine.addMediaInfoHandler(new MediaInfoHandler() {

                @Override
                public void onMediaInfoAvailable(MediaInfoEvent event) {
                    fireEvent(event);
                }
            });
            _engine.addPlayStateHandler(new PlayStateHandler() {

                @Override
                public void onPlayStateChanged(PlayStateEvent event) {
                    fireEvent(event);
                }
            });
            _engine.addPlayerStateHandler(new PlayerStateHandler() {

                @Override
                public void onPlayerStateChanged(PlayerStateEvent event) {
                    fireEvent(event);
                }
            });
            _panel.setWidget(_engine);
        } catch (LoadException ex) {
        } catch (PluginNotFoundException ex) {
            _panel.setWidget(missingPluginNotice == null ? PlayerUtil.getMissingPluginNotice(ex.getPlugin()) : missingPluginNotice);
        } catch (PluginVersionException ex) {
            _panel.setWidget(missingPluginVersionNotice == null
                    ? PlayerUtil.getMissingPluginNotice(ex.getPlugin(), ex.getRequiredVersion()) : missingPluginVersionNotice);
        }
    }

    /**
     * Returns the underlying player implementation
     *
     * @return the underlying player
     */
    public T getEngine() {
        return _engine;
    }

    /**
     * Called by the constructor to create the player implementation wrapped by this widget
     *
     * @param mediaURL the resolved URL of the media to playback
     * @param autoplay {@code true} to autoplay, {@code false} otherwise
     * @param height the height of the player (in CSS units)
     * @param width the width of the player (in CSS units)
     *
     * @return the player implementation
     *
     * @throws LoadException if an error occurs while loading the media.
     * @throws PluginNotFoundException if the required plugin is not installed on the client.
     * @throws PluginVersionException if the required plugin version is not installed on the client.
     */
    protected abstract T initPlayerEngine(String mediaURL, boolean autoplay, String height, String width)
            throws LoadException, PluginNotFoundException, PluginVersionException;

    @Override
    public long getMediaDuration() {
        if (_engine == null) {
            return 0;
        }
        return _engine.getMediaDuration();
    }

    @Override
    public double getPlayPosition() {
        if (_engine == null) {
            return 0;
        }
        return _engine.getPlayPosition();
    }

    @Override
    public void setPlayPosition(double position) {
        if (_engine != null) {
            _engine.setPlayPosition(position);
        }
    }

    @Override
    public void loadMedia(String mediaURL) throws LoadException {
        if (_engine != null) {
            _engine.loadMedia(mediaURL);
        }
    }

    @Override
    public void pauseMedia() {
        if (_engine != null) {
            _engine.pauseMedia();
        }
    }

    @Override
    public void playMedia() throws PlayException {
        if (_engine != null) {
            _engine.playMedia();
        }
    }

    @Override
    public void stopMedia() {
        if (_engine != null) {
            _engine.stopMedia();
        }
    }

    @Override
    public double getVolume() {
        if (_engine == null) {
            return 0;
        }
        return _engine.getVolume();
    }

    @Override
    public void setVolume(double volume) {
        if (_engine != null) {
            _engine.setVolume(volume);
        }
    }

    /**
     * Returns the remaining number of times this player loops playback before stopping.
     */
    @Override
    public int getLoopCount() {
        if (_engine == null) {
            return 0;
        }
        return _engine.getLoopCount();
    }

    /**
     * Sets the number of times the current media file should loop playback before stopping.
     */
    @Override
    public void setLoopCount(int loop) {
        if (_engine != null) {
            _engine.setLoopCount(loop);
        }
    }

    @Override
    public void showLogger(boolean show) {
        if (_engine == null) {
            _engine.showLogger(show);
        }
    }

    /**
     * Convenience method for UiBinder support. Displays or hides the players' logger widget.
     *
     * @param show <code>true</code> to make the logger widget visible, <code>false</code> otherwise.
     * @see #showLogger(boolean)
     */
    public void setShowLogger(boolean show) {
        if (_engine != null) {
            _engine.showLogger(show);
        }
    }

    @Override
    public int getVideoHeight() {
        if (_engine == null) {
            return 0;
        }
        return _engine.getVideoHeight();
    }

    @Override
    public int getVideoWidth() {
        if (_engine == null) {
            return 0;
        }
        return _engine.getVideoWidth();
    }

    @Override
    public boolean isControllerVisible() {
        if (_engine == null) {
            return false;
        }
        return _engine.isControllerVisible();
    }

    @Override
    public boolean isResizeToVideoSize() {
        if (_engine == null) {
            return false;
        }
        return _engine.isResizeToVideoSize();
    }

    @Override
    public <T extends ConfigValue> void setConfigParameter(ConfigParameter param, T value) {
        if (_engine != null) {
            _engine.setConfigParameter(param, value);
        }
    }

    @Override
    public void setControllerVisible(boolean show) {
        if (_engine != null) {
            _engine.setControllerVisible(show);
        }
    }

    @Override
    public void setResizeToVideoSize(boolean resize) {
        if (_engine != null) {
            _engine.setResizeToVideoSize(resize);
        }
    }

    @Override
    public void addToPlaylist(String mediaURL) {
        if ((_engine != null) && (_engine instanceof com.bramosystems.oss.player.core.client.PlaylistSupport)) {
            ((com.bramosystems.oss.player.core.client.PlaylistSupport) _engine).addToPlaylist(mediaURL);
        }
    }

    @Override
    public void addToPlaylist(MRL mediaLocator) {
        if ((_engine != null) && (_engine instanceof com.bramosystems.oss.player.core.client.PlaylistSupport)) {
            ((com.bramosystems.oss.player.core.client.PlaylistSupport) _engine).addToPlaylist(mediaLocator);
        }
    }

    @Override
    public void addToPlaylist(String... mediaURLs) {
        if ((_engine != null) && (_engine instanceof com.bramosystems.oss.player.core.client.PlaylistSupport)) {
            ((com.bramosystems.oss.player.core.client.PlaylistSupport) _engine).addToPlaylist(mediaURLs);
        }
    }

    @Override
    public void addToPlaylist(List<MRL> mediaLocators) {
        if (_engine instanceof com.bramosystems.oss.player.core.client.PlaylistSupport) {
            ((com.bramosystems.oss.player.core.client.PlaylistSupport) _engine).addToPlaylist(mediaLocators);
        }
    }

    @Override
    public boolean isShuffleEnabled() {
        if (_engine == null) {
            return false;
        }
        if (_engine instanceof com.bramosystems.oss.player.core.client.PlaylistSupport) {
            return ((com.bramosystems.oss.player.core.client.PlaylistSupport) _engine).isShuffleEnabled();
        }
        return false;
    }

    @Override
    public void removeFromPlaylist(int index) {
        if ((_engine != null) && (_engine instanceof com.bramosystems.oss.player.core.client.PlaylistSupport)) {
            ((com.bramosystems.oss.player.core.client.PlaylistSupport) _engine).removeFromPlaylist(index);
        }
    }

    @Override
    public void setShuffleEnabled(boolean enable) {
        if ((_engine != null) && (_engine instanceof com.bramosystems.oss.player.core.client.PlaylistSupport)) {
            ((com.bramosystems.oss.player.core.client.PlaylistSupport) _engine).setShuffleEnabled(enable);
        }
    }

    @Override
    public void clearPlaylist() {
        if ((_engine != null) && (_engine instanceof com.bramosystems.oss.player.core.client.PlaylistSupport)) {
            ((com.bramosystems.oss.player.core.client.PlaylistSupport) _engine).clearPlaylist();
        }
    }

    @Override
    public int getPlaylistSize() {
        if (_engine == null) {
            return 0;
        }
        if (_engine instanceof com.bramosystems.oss.player.core.client.PlaylistSupport) {
            return ((com.bramosystems.oss.player.core.client.PlaylistSupport) _engine).getPlaylistSize();
        }
        return 1;
    }

    @Override
    public void play(int index) throws IndexOutOfBoundsException {
        if ((_engine != null) && (_engine instanceof com.bramosystems.oss.player.core.client.PlaylistSupport)) {
            ((com.bramosystems.oss.player.core.client.PlaylistSupport) _engine).play(index);
        }
    }

    @Override
    public void playNext() throws PlayException {
        if ((_engine != null) && (_engine instanceof com.bramosystems.oss.player.core.client.PlaylistSupport)) {
            ((com.bramosystems.oss.player.core.client.PlaylistSupport) _engine).playNext();
        }
    }

    @Override
    public void playPrevious() throws PlayException {
        if ((_engine != null) && (_engine instanceof com.bramosystems.oss.player.core.client.PlaylistSupport)) {
            ((com.bramosystems.oss.player.core.client.PlaylistSupport) _engine).playPrevious();
        }
    }

    @Override
    public void setMatrix(TransformationMatrix matrix) {
        if (_engine == null) {
            return;
        }
        if (_engine instanceof MatrixSupport) {
            ((MatrixSupport) _engine).setMatrix(matrix);
        }
    }

    @Override
    public TransformationMatrix getMatrix() {
        if (_engine == null) {
            return null;
        }
        if (_engine instanceof MatrixSupport) {
            return ((MatrixSupport) _engine).getMatrix();
        }
        return null;
    }
}
