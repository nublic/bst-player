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
package com.bramosystems.oss.player.core.client.ui;

import com.bramosystems.oss.player.core.client.playlist.MRL;
import com.bramosystems.oss.player.core.client.*;
import com.bramosystems.oss.player.core.client.MediaInfo.MediaInfoKey;
import com.bramosystems.oss.player.core.client.geom.TransformationMatrix;
import com.bramosystems.oss.player.core.client.geom.MatrixSupport;
import com.bramosystems.oss.player.core.client.impl.FMPStateManager;
import com.bramosystems.oss.player.core.client.impl.FlashMediaPlayerImpl;
import com.bramosystems.oss.player.core.client.spi.PlayerWidget;
import com.bramosystems.oss.player.core.client.impl.CorePlayerProvider;
import com.bramosystems.oss.player.core.event.client.DebugEvent;
import com.bramosystems.oss.player.core.event.client.DebugHandler;
import com.bramosystems.oss.player.core.event.client.MediaInfoEvent;
import com.bramosystems.oss.player.core.event.client.MediaInfoHandler;
import com.bramosystems.oss.player.core.event.client.PlayStateEvent;
import com.bramosystems.oss.player.core.event.client.PlayerStateEvent;
import com.bramosystems.oss.player.core.client.spi.Player;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Widget to embed Flash plugin for playback of flash-supported formats
 *
 * <h3>Usage Example</h3>
 *
 * <p>
 * <code><pre>
 * SimplePanel panel = new SimplePanel();   // create panel to hold the player
 * Widget player = null;
 * try {
 *      // create the player
 *      player = new FlashMediaPlayer("www.example.com/mediafile.flv", false, "200px", "250px");
 * } catch(LoadException e) {
 *      // catch loading exception and alert user
 *      Window.alert("An error occured while loading");
 * } catch(PluginVersionException e) {
 *      // catch plugin version exception and alert user to download plugin first.
 *      // An option is to use the utility method in PlayerUtil class.
 *      player = PlayerUtil.getMissingPluginNotice(Plugin.FlashMediaPlayer, "Missing Plugin",
 *              ".. some nice message telling the user to click and download plugin first ..",
 *              false);
 * } catch(PluginNotFoundException e) {
 *      // catch PluginNotFoundException and tell user to download plugin, possibly providing
 *      // a link to the plugin download page.
 *      player = new HTML(".. another kind of message telling the user to download plugin..");
 * }
 *
 * panel.setWidget(player); // add player to panel.
 * </pre></code>
 *
 * <h3>M3U Playlist Support</h3>
 * <p>
 * This player supports M3U formatted playlists.  However, each entry in the playlist MUST be
 * a flash-supported media file.
 * </p>
 *
 * @author Sikirulai Braheem
 * @since 1.0
 */
@Player(name = "FlashPlayer", providerFactory = CorePlayerProvider.class, minPluginVersion = "10.0.0")
public class FlashMediaPlayer extends AbstractMediaPlayer implements PlaylistSupport, MatrixSupport {

    private FMPStateManager manager;
    private FlashMediaPlayerImpl impl;
    private String playerId;
    private boolean isEmbedded, resizeToVideoSize;
    private Logger logger;
    private ArrayList<String> _playlistCache;
    private PlayerWidget swf;
    private String _height, _width;
    private static String DEFAULT_HEIGHT = "22px";

    private FlashMediaPlayer() throws PluginNotFoundException, PluginVersionException {
        PluginVersion req = Plugin.FlashPlayer.getVersion();
        PluginVersion v = PlayerUtil.getFlashPlayerVersion();
        if (v.compareTo(req) < 0) {
            throw new PluginVersionException(Plugin.FlashPlayer, req.toString(), v.toString());
        }

        _playlistCache = new ArrayList<String>();
        resizeToVideoSize = false;
        playerId = DOM.createUniqueId().replace("-", "");

        manager = new FMPStateManager(playerId, new FMPStateManager.FMPStateCallback() {

            @Override
            public void onInit() {
                impl = FlashMediaPlayerImpl.getPlayer(playerId);
                for (String url : _playlistCache) {
                    impl.addToPlaylist(url);
                }
                firePlayerStateEvent(PlayerStateEvent.State.Ready);
            }

            @Override
            public void onMessage(int type, String message) {
                if (type == 1) {
                    fireError(message);
                } else {
                    fireDebug(message);
                }
            }

            @Override
            public void onProgress(double progress) {
                fireLoadingProgress(progress);
            }

            @Override
            public void onMediaInfo(String info) {
                MediaInfo mi = new MediaInfo();
                manager.fillMediaInfoImpl(info, mi);
                fireMediaInfoAvailable(mi);
            }

            @Override
            public void onEvent(int type, boolean buttonDown, boolean alt, boolean ctrl,
                    boolean shift, boolean cmd, int stageX, int stageY) {
                int button = buttonDown ? NativeEvent.BUTTON_LEFT : NativeEvent.BUTTON_RIGHT;
                int screenX = -1, screenY = -1;

                Document _doc = Document.get();
                NativeEvent event = null;
                switch (type) {
                    case 1: // mouse down
                        event = _doc.createMouseDownEvent(1, screenX, screenY, stageX, stageY,
                                ctrl, alt, shift, cmd, button);
                        break;
                    case 2: // mouse up
                        event = _doc.createMouseUpEvent(1, screenX, screenY, stageX, stageY,
                                ctrl, alt, shift, cmd, button);
                        break;
                    case 3: // mouse move
                        event = _doc.createMouseMoveEvent(1, screenX, screenY, stageX, stageY,
                                ctrl, alt, shift, cmd, button);
                        break;
                    case 10: // click
                        event = _doc.createClickEvent(1, screenX, screenY, stageX, stageY,
                                ctrl, alt, shift, cmd);
                        break;
                    case 11: // double click
                        event = _doc.createDblClickEvent(1, screenX, screenY, stageX, stageY,
                                ctrl, alt, shift, cmd);
                        break;
                    case 20: // key down
                        event = _doc.createKeyDownEvent(ctrl, alt, shift, cmd, stageX);
                        break;
                    case 21: // key press
                        event = _doc.createKeyPressEvent(ctrl, alt, shift, cmd, stageX);
                        break;
                    case 22: // key up
                        event = _doc.createKeyUpEvent(ctrl, alt, shift, cmd, stageX);
                        break;
                }
                DomEvent.fireNativeEvent(event, FlashMediaPlayer.this);
            }

            @Override
            public void onStateChanged(int stateId, int listIndex) {
                switch (stateId) {
                    case 1: // loading started...
////                    listener.onPlayerReady();
                        break;
                    case 2: // play started...
                        firePlayStateEvent(PlayStateEvent.State.Started, listIndex);
                        break;
                    case 3: // play stopped...
                        firePlayStateEvent(PlayStateEvent.State.Stopped, listIndex);
                        break;
                    case 4: // play paused...
                        firePlayStateEvent(PlayStateEvent.State.Paused, listIndex);
                        break;
                    case 9: // play finished...
                        firePlayStateEvent(PlayStateEvent.State.Finished, listIndex);
                        break;
                    case 10: // loading complete ...
                        fireLoadingProgress(1.0);
                        break;
                }
            }

            @Override
            public void onFullScreen(boolean fullscreen) {
                firePlayerStateEvent(fullscreen ? PlayerStateEvent.State.FullScreenStarted : PlayerStateEvent.State.FullScreenFinished);
            }
        });
    }

    /**
     * Constructs <code>FlashMediaPlayer</code> with the specified {@code height} and
     * {@code width} to playback media located at {@code mediaURL}. Media playback
     * begins automatically if {@code autoplay} is {@code true}.
     *
     * <p> {@code height} and {@code width} are specified as CSS units. A value of {@code null}
     * for {@code height} or {@code width} puts the player in embedded mode.  When in embedded mode,
     * the player is made invisible on the page and media state events are propagated to registered
     * listeners only.  This is desired especially when used with custom sound controls.  For custom
     * video-playback control, specify valid CSS values for {@code height} and {@code width} but hide the
     * player controls with {@code setControllerVisible(false)}.
     *
     * @param mediaURL the URL of the media to playback
     * @param autoplay {@code true} to start playing automatically, {@code false} otherwise
     * @param height the height of the player
     * @param width the width of the player.
     *
     * @throws LoadException if an error occurs while loading the media.
     * @throws PluginVersionException if the required Flash plugin version is not installed on the client.
     * @throws PluginNotFoundException if the Flash plugin is not installed on the client.
     */
    public FlashMediaPlayer(final String mediaURL, final boolean autoplay, String height, String width)
            throws PluginNotFoundException, PluginVersionException, LoadException {
        this();
        _height = height;
        _width = width;

        isEmbedded = (height == null) || (width == null);
        if (isEmbedded) {
            _height = "0px";
            _width = "0px";
        }

        swf = new PlayerWidget("core", Plugin.FlashPlayer.name(), playerId, FMPStateManager.getSWFImpl(), autoplay);
        swf.addParam("flashVars", "playerId=" + playerId + "&autoplay="
                + autoplay + "&mediaURL=" + URL.encodePathSegment(mediaURL)); // encode mediaURL to avoid ampersand conflict with flashvars
        swf.addParam("allowScriptAccess", "always");
        swf.addParam("allowFullScreen", "true");
        swf.addParam("bgcolor", "#000000");

        FlowPanel panel = new FlowPanel();
        panel.add(swf);

        if (!isEmbedded) {
            logger = new Logger();
            logger.setVisible(false);
            panel.add(logger);

            addDebugHandler(new DebugHandler() {

                @Override
                public void onDebug(DebugEvent event) {
                    logger.log(event.getMessage(), false);
                }
            });
            addMediaInfoHandler(new MediaInfoHandler() {

                @Override
                public void onMediaInfoAvailable(MediaInfoEvent event) {
                    MediaInfo info = event.getMediaInfo();
                    logger.log(event.getMediaInfo().asHTMLString(), true);
                    if (info.getAvailableItems().contains(MediaInfoKey.VideoHeight)
                            || info.getAvailableItems().contains(MediaInfoKey.VideoWidth)) {
                        checkVideoSize(Integer.parseInt(info.getItem(MediaInfoKey.VideoHeight)),
                                Integer.parseInt(info.getItem(MediaInfoKey.VideoWidth)));
                    }
                }
            });
        }

        initWidget(panel);
    }

    /**
     * Constructs <code>FlashMediaPlayer</code> to automatically playback media located at
     * {@code mediaURL}.
     *
     * <p> Note: This constructor hides the video display component, the player controls are
     * however visible.
     *
     * @param mediaURL the URL of the media to playback
     *
     * @throws LoadException if an error occurs while loading the media.
     * @throws PluginVersionException if the required Flash plugin version is not installed on the client.
     * @throws PluginNotFoundException if the Flash plugin is not installed on the client.
     *
     */
    public FlashMediaPlayer(String mediaURL) throws PluginNotFoundException,
            PluginVersionException, LoadException {
        this(mediaURL, true, DEFAULT_HEIGHT, "100%");
    }

    /**
     * Constructs <code>FlashMediaPlayer</code> to playback media located at {@code mediaURL}.
     * Media playback begins automatically if {@code autoplay} is {@code true}.
     *
     * <p> Note: This constructor hides the video display component, the player controls are
     * however visible.
     *
     * @param mediaURL the URL of the media to playback
     * @param autoplay {@code true} to start playing automatically, {@code false} otherwise
     *
     * @throws LoadException if an error occurs while loading the media.
     * @throws PluginVersionException if the required Flash plugin version is not installed on the client.
     * @throws PluginNotFoundException if the Flash plugin is not installed on the client.
     */
    public FlashMediaPlayer(String mediaURL, boolean autoplay) throws PluginNotFoundException,
            PluginVersionException, LoadException {
        this(mediaURL, autoplay, DEFAULT_HEIGHT, "100%");
    }

    private void checkVideoSize(int vidHeight, int vidWidth) {
        String _h = _height, _w = _width;
        if (resizeToVideoSize) {
            if (vidHeight == 0) {
                _h = DEFAULT_HEIGHT; // suppress SWF app height for audio files ...
            } else if ((vidHeight > 0) && (vidWidth > 0)) {
                fireDebug("Resizing Player : " + vidWidth + " x " + vidHeight);
                _h = vidHeight + "px";
                _w = vidWidth + "px";
            }
        }

        swf.setSize("100%", _h);
        setWidth(_w);

        if (!_height.equals(_h) && !_width.equals(_w)) {
            firePlayerStateEvent(PlayerStateEvent.State.DimensionChangedOnVideo);
        }
    }

    private void checkAvailable() {
        if (!isPlayerOnPage(playerId)) {
            String message = "Player not available, create an instance";
            fireDebug(message);
            throw new IllegalStateException(message);
        }
    }

    /**
     * Overriden to register associated resources
     */
    @Override
    protected void onLoad() {
        swf.setSize("100%", _height);
        setWidth(_width);
    }

    /**
     * Overriden to release associated resources
     */
    @Override
    protected void onUnload() {
        impl.closeMedia();
        manager.closeMedia(playerId);
    }

    @Override
    public long getMediaDuration() {
        checkAvailable();
        return (long) impl.getMediaDuration();
    }

    @Override
    public double getPlayPosition() {
        checkAvailable();
        return impl.getPlayPosition();
    }

    @Override
    public double getVolume() {
        checkAvailable();
        return impl.getVolume();
    }

    @Override
    public void loadMedia(String mediaURL) throws LoadException {
        checkAvailable();
        impl.loadMedia(mediaURL);
    }

    @Override
    public void pauseMedia() {
        checkAvailable();
        impl.pauseMedia();
    }

    @Override
    public void playMedia() throws PlayException {
        checkAvailable();
        impl.playMedia();
    }

    @Override
    public void setPlayPosition(double position) {
        checkAvailable();
        impl.setPlayPosition(position);
    }

    @Override
    public void setVolume(double volume) {
        checkAvailable();
        impl.setVolume(volume);
    }

    @Override
    public void stopMedia() {
        checkAvailable();
        impl.stopMedia();
    }

    @Override
    public void showLogger(boolean enable) {
        if (!isEmbedded) {
            logger.setVisible(enable);
        }
    }

    /**
     * Displays or hides the player controls.
     */
    @Override
    public void setControllerVisible(final boolean show) {
        if (isPlayerOnPage(playerId)) {
            impl.setControllerVisible(show);
        } else {
            addToPlayerReadyCommandQueue("controller", new Command() {

                @Override
                public void execute() {
                    impl.setControllerVisible(show);
                }
            });
        }
    }

    /**
     * Checks whether the player controls are visible.
     */
    @Override
    public boolean isControllerVisible() {
        checkAvailable();
        return impl.isControllerVisible();
    }

    /**
     * Returns the number of times this player repeats playback before stopping.
     */
    @Override
    public int getLoopCount() {
        checkAvailable();
        return impl.getLoopCount();
    }

    /**
     * Sets the number of times the current media file should repeat playback before stopping.
     *
     * <p>As of version 1.0, if this player is not available on the panel, this method
     * call is added to the command-queue for later execution.
     */
    @Override
    public void setLoopCount(final int loop) {
        if (isPlayerOnPage(playerId)) {
            impl.setLoopCount(loop);
        } else {
            addToPlayerReadyCommandQueue("loopcount", new Command() {

                @Override
                public void execute() {
                    impl.setLoopCount(loop);
                }
            });
        }
    }

    @Override
    public void addToPlaylist(String mediaURL) {
        if (isPlayerOnPage(playerId)) {
            impl.addToPlaylist(mediaURL);
        } else {
            _playlistCache.add(mediaURL);
        }
    }

    @Override
    public void addToPlaylist(MRL mediaLocator) {
        addToPlaylist(mediaLocator.getNextResource(true));
    }

    @Override
    public void addToPlaylist(String... mediaURLs) {
        addToPlaylist(mediaURLs[0]);
    }

    @Override
    public void addToPlaylist(List<MRL> mediaLocators) {
        for (MRL mrl : mediaLocators) {
            addToPlaylist(mrl);
        }
    }

    @Override
    public boolean isShuffleEnabled() {
        checkAvailable();
        return impl.isShuffleEnabled();
    }

    @Override
    public void removeFromPlaylist(int index) {
        checkAvailable();
        impl.removeFromPlaylist(index);
    }

    /**
     * Enables or disables players' shuffle mode.
     *
     * <p>As of version 1.0, if this player is not available on the panel, this method
     * call is added to the command-queue for later execution.
     */
    @Override
    public void setShuffleEnabled(final boolean enable) {
        if (isPlayerOnPage(playerId)) {
            impl.setShuffleEnabled(enable);
        } else {
            addToPlayerReadyCommandQueue("shuffle", new Command() {

                @Override
                public void execute() {
                    impl.setShuffleEnabled(enable);
                }
            });
        }
    }

    @Override
    public void clearPlaylist() {
        checkAvailable();
        impl.clearPlaylist();
    }

    @Override
    public int getPlaylistSize() {
        checkAvailable();
        return impl.getPlaylistCount();
    }

    @Override
    public void play(int index) throws IndexOutOfBoundsException {
        checkAvailable();
        if (!impl.playMedia(index)) {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public void playNext() throws PlayException {
        checkAvailable();
        if (!impl.playNext()) {
            throw new PlayException("No more entries in playlist");
        }
    }

    @Override
    public void playPrevious() throws PlayException {
        checkAvailable();
        if (!impl.playPrevious()) {
            throw new PlayException("Beginning of playlist reached");
        }
    }

    @Override
    public int getVideoHeight() {
        checkAvailable();
        return impl.getVideoHeight();
    }

    @Override
    public int getVideoWidth() {
        checkAvailable();
        return impl.getVideoWidth();
    }

    @Override
    public void setResizeToVideoSize(boolean resize) {
        resizeToVideoSize = resize;
        if (isPlayerOnPage(playerId)) {
            // if player is on panel now update its size, otherwise
            // allow it to be handled by the MediaInfoHandler...
            checkVideoSize(impl.getVideoHeight(), impl.getVideoWidth());
        }
    }

    @Override
    public boolean isResizeToVideoSize() {
        return resizeToVideoSize;
    }

    /**
     * Sets the transformation matrix of the underlying Flash player.
     *
     * <p>If this player is not attached to a panel, this method call is added to
     * the command-queue for later execution.
     */
    @Override
    public void setMatrix(final TransformationMatrix matrix) {
        if (isPlayerOnPage(playerId)) {
            impl.setMatrix(matrix.getMatrix().getVx().getX(),
                    matrix.getMatrix().getVy().getX(), matrix.getMatrix().getVx().getY(),
                    matrix.getMatrix().getVy().getY(), matrix.getMatrix().getVx().getZ(),
                    matrix.getMatrix().getVy().getZ());

            if (resizeToVideoSize) {
                checkVideoSize(getVideoHeight(), getVideoWidth());
            }
        } else {
            addToPlayerReadyCommandQueue("matrix", new Command() {

                @Override
                public void execute() {
                    setMatrix(matrix);
                }
            });
        }
    }

    @Override
    public TransformationMatrix getMatrix() {
        checkAvailable();
        String[] elements = impl.getMatrix().split(",");

        TransformationMatrix matrix = new TransformationMatrix();
        matrix.getMatrix().getVx().setX(Double.parseDouble(elements[0]));
        matrix.getMatrix().getVy().setX(Double.parseDouble(elements[1]));
        matrix.getMatrix().getVx().setY(Double.parseDouble(elements[2]));
        matrix.getMatrix().getVy().setY(Double.parseDouble(elements[3]));
        matrix.getMatrix().getVx().setZ(Double.parseDouble(elements[4]));
        matrix.getMatrix().getVy().setZ(Double.parseDouble(elements[5]));
        return matrix;
    }

    @Override
    public <T extends ConfigValue> void setConfigParameter(ConfigParameter param, T value) {
        super.setConfigParameter(param, value);
        switch (param) {
            case TransparencyMode:
                if (value != null) {
                    swf.addParam("wmode", ((TransparencyMode) value).name().toLowerCase());
                } else {
                    swf.addParam("wmode", null);
                }
        }
    }

    @Override
    public void setConfigParameter(ConfigParameter param, String value) {
        super.setConfigParameter(param, value);
        switch (param) {
            case BackgroundColor:
                swf.addParam("bgcolor", value);

        }
    }

    @Override
    public RepeatMode getRepeatMode() {
        checkAvailable();
        return impl.getRepeatMode();
    }

    @Override
    public void setRepeatMode(final RepeatMode mode) {
        if (isPlayerOnPage(playerId)) {
            impl.setRepeatMode(mode);
        } else {
            addToPlayerReadyCommandQueue("repeatMode", new Command() {

                @Override
                public void execute() {
                    impl.setRepeatMode(mode);
                }
            });
        }
    }

    /**
     * Sets the player to automatically hide its controls.
     *
     * <p>The auto-hide effect is active IF AND ONLY IF the controller is set visible
     *
     * @param autohide {@code true} to auto-hide the controller, {@code false} otherwise
     * @since 1.2
     */
    public void setAutoHideController(final boolean autohide) {
        if (isPlayerOnPage(playerId)) {
            impl.setAutoHideController(autohide);
        } else {
            addToPlayerReadyCommandQueue("autohide", new Command() {

                @Override
                public void execute() {
                    impl.setAutoHideController(autohide);
                }
            });
        }
    }

    /**
     * Checks whether the player automatically hides its controls.
     *
     * @return {@code true} is the player autohides its controls, {@code false} otherwise
     * @since 1.2
     */
    public boolean isAutoHideController() {
        checkAvailable();
        return impl.isAutoHideController();
    }
}
