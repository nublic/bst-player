/*
 *  Copyright 2010 Sikiru Braheem
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package com.bramosystems.oss.player.core.client.ui;

import com.bramosystems.oss.player.core.client.spi.PlayerWidget;
import com.bramosystems.oss.player.core.client.playlist.PlaylistManager;
import com.bramosystems.oss.player.core.client.playlist.MRL;
import com.bramosystems.oss.player.core.client.*;
import com.bramosystems.oss.player.core.client.MediaInfo.MediaInfoKey;
import com.bramosystems.oss.player.core.client.impl.*;
import com.bramosystems.oss.player.core.client.impl.CorePlayerProvider;
import com.bramosystems.oss.player.core.event.client.*;
import com.bramosystems.oss.player.core.client.spi.Player;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import java.util.List;

/**
 * Widget to embed DivX&reg; Web Player plugin.
 *
 * <h3>Usage Example</h3>
 *
 * <p>
 * <code><pre>
 * SimplePanel panel = new SimplePanel();   // create panel to hold the player
 * Widget player = null;
 * try {
 *      // create the player
 *      player = new DivXPlayer("www.example.com/mediafile.divx");
 * } catch(LoadException e) {
 *      // catch loading exception and alert user
 *      Window.alert("An error occured while loading");
 * } catch(PluginVersionException e) {
 *      // catch plugin version exception and alert user to download plugin first.
 *      // An option is to use the utility method in PlayerUtil class.
 *      player = PlayerUtil.getMissingPluginNotice(Plugin.DivXPlayer, "Missing Plugin",
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
 * @since 1.2
 * @author Sikiru Braheem
 */
@Player(name = "DivXPlayer", providerFactory = CorePlayerProvider.class, minPluginVersion = "2.0.0")
public class DivXPlayer extends AbstractMediaPlayer implements PlaylistSupport {

    private DivXStateManager manager;
    private DivXPlayerImpl impl;
    private PlayerWidget playerWidget;
    private String playerId, _height, _width;
    private Logger logger;
    private LoopManager loopManager;
    private DisplayMode displayMode;
    private PlaylistManager playlistManager;
    private boolean resizeToVideoSize, isEmbedded, playing;
    private double currentPosition;

    private DivXPlayer() throws PluginNotFoundException, PluginVersionException {
        PluginVersion req = Plugin.DivXPlayer.getVersion();
        PluginVersion v = PlayerUtil.getDivXPlayerPluginVersion();
        if (v.compareTo(req) < 0) {
            throw new PluginVersionException(Plugin.DivXPlayer, req.toString(), v.toString());
        }

        playerId = DOM.createUniqueId().replace("-", "");
        loopManager = new LoopManager(new LoopManager.LoopCallback() {

            @Override
            public void onLoopFinished() {
                fireDebug("Play finished");
                firePlayStateEvent(PlayStateEvent.State.Finished, playlistManager.getPlaylistIndex());
            }

            @Override
            public void playNextItem() throws PlayException {
                playlistManager.playNext();
            }

            @Override
            public void repeatPlay() {
                playlistManager.play(playlistManager.getPlaylistIndex());
            }

            @Override
            public void playNextLoop() {
                try {
                    playlistManager.playNext(true);
                } catch (PlayException ex) {
                }
            }
        });
        manager = new DivXStateManager(playerId, new DivXStateManager.StateCallback() {

            @Override
            public void onStatusChanged(int statusId) {
                switch (statusId) {
                    case 1: // OPEN_DONE - media info available
                        fireDebug("Loading media @ '" + playlistManager.getCurrentItem() + "'");
                        fireDebug("Media Info available");
                        fireMediaInfoAvailable(manager.getFilledMediaInfo(impl.getMediaDuration(),
                                impl.getVideoWidth(), impl.getVideoHeight()));
                        break;
                    case 2: // VIDEO_END, notify loop manager...
                        fireDebug("Playback ended");
                        loopManager.notifyPlayFinished();
                        break;
                    case 6: // WINDOWED_START
                        fireDebug("Window mode started");
                        break;
                    case 7: // WINDOWED_END
                        fireDebug("Window mode ended");
                        break;
                    case 8: // FULLSCREEN_START
                        fireDebug("Fullscreen started");
                        firePlayerStateEvent(PlayerStateEvent.State.FullScreenStarted);
                        break;
                    case 9: // FULLSCREEN_END
                        fireDebug("Fullscreen ended");
                        firePlayerStateEvent(PlayerStateEvent.State.FullScreenFinished);
                        break;
                    case 10: // STATUS_PLAYING
                        fireDebug("Playback started");
                        firePlayStateEvent(PlayStateEvent.State.Started, playlistManager.getPlaylistIndex());
                        break;
                    case 11: // STATUS_PAUSED
                        fireDebug("Playback paused");
                        firePlayStateEvent(PlayStateEvent.State.Paused, playlistManager.getPlaylistIndex());
                        break;
                    case 14: // STATUS_STOPPED
                        fireDebug("Playback stopped");
                        firePlayStateEvent(PlayStateEvent.State.Stopped, playlistManager.getPlaylistIndex());
                        break;
                    case 15: // BUFFERING_START
                        fireDebug("Buffering started");
                        firePlayerStateEvent(PlayerStateEvent.State.BufferingStarted);
                        break;
                    case 16: // BUFFERING_STOP
                        fireDebug("Buffering stopped");
                        firePlayerStateEvent(PlayerStateEvent.State.BufferingFinished);
                        fireLoadingProgress(1.0);
                        break;
                    case 17: // DOWNLOAD_START
                        fireDebug("Download started");
                        fireLoadingProgress(0);
                        break;
                    case 18: // DOWNLOAD_FAILED
                        fireError("ERROR: Download failed - '"
                                + playlistManager.getCurrentItem() + "'");
                        loopManager.notifyPlayFinished();
                        break;
                    case 19: // DOWNLOAD_DONE
                        fireDebug("Download finished");
                        fireLoadingProgress(1.0);
                        break;
                    /*
                    case 4: // EMBEDDED_START
                    case 5: // EMBEDDED_END
                    case 12: // STATUS_FF
                    case 13: // STATUS_RW
                    //                        break;
                    case 0: // INIT_DONE
                    case 3: // SHUT_DONE
                    default:
                    //                        fireDebug("DEV: Status Changed : " + statusId);
                     */
                }
            }

            @Override
            public void onLoadingChanged(double current, double total) {
                fireLoadingProgress(current / total);
            }

            @Override
            public void onPositionChanged(double time) {
                currentPosition = time * 1000;
            }
        });
        playlistManager = new PlaylistManager(this);
        displayMode = DisplayMode.MINI;

        // add play state monitoring to enhance setPlayPosition method..
        addPlayStateHandler(new PlayStateHandler() {

            @Override
            public void onPlayStateChanged(PlayStateEvent event) {
                playing = event.getPlayState().equals(PlayStateEvent.State.Started);
            }
        });
    }

    /**
     * Constructs <code>DivXPlayer</code> to playback media located at {@code mediaURL} using the
     * default height of 90px and width of 100%. Media playback begins automatically if
     * {@code autoplay} is {@code true}.
     *
     * @param mediaURL the URL of the media to playback
     * @param autoplay {@code true} to play playing automatically, {@code false} otherwise
     * @param height the height of the player
     * @param width the width of the player.
     *
     * @throws LoadException if an error occurs while loading the media.
     * @throws PluginVersionException if the required DivX&reg; Web Player plugin version is not installed on the client.
     * @throws PluginNotFoundException if DivX&reg; Web Player plugin is not installed on the client.
     */
    public DivXPlayer(String mediaURL, boolean autoplay, String height, String width)
            throws LoadException, PluginNotFoundException, PluginVersionException {
        this();

        _height = height;
        _width = width;

        isEmbedded = (height == null) || (width == null);
        if (isEmbedded) {
            _height = "0px";
            _width = "0px";
        }

        playerWidget = new PlayerWidget("core", Plugin.DivXPlayer.name(), playerId, "", autoplay);
        playerWidget.addParam("statusCallback", "bstplayer.handlers.divx." + playerId + ".stateChanged");
        playerWidget.addParam("downloadCallback", "bstplayer.handlers.divx." + playerId + ".downloadState");
        playerWidget.addParam("timeCallback", "bstplayer.handlers.divx." + playerId + ".timeState");

        FlowPanel panel = new FlowPanel();
        initWidget(panel);
        panel.add(playerWidget);

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
                    if (info.getAvailableItems().contains(MediaInfoKey.VideoHeight)
                            || info.getAvailableItems().contains(MediaInfoKey.VideoWidth)) {
                        checkVideoSize(Integer.parseInt(info.getItem(MediaInfoKey.VideoHeight)),
                                Integer.parseInt(info.getItem(MediaInfoKey.VideoWidth)));
                    }
                    logger.log(info.asHTMLString(), true);
                }
            });
        }
        playerWidget.setSize("100%", _height);
        setWidth(_width);
        playlistManager.addToPlaylist(mediaURL);
    }

    /**
     * Constructs <code>DivXPlayer</code> to playback media located at {@code mediaURL} using the
     * default height of 90px and width of 100%. Media playback begins automatically if
     * {@code autoplay} is {@code true}.
     *
     * @param mediaURL the URL of the media to playback
     * @param autoplay {@code true} to play playing automatically, {@code false} otherwise
     *
     * @throws LoadException if an error occurs while loading the media.
     * @throws PluginVersionException if the required DivX&reg; Web Player plugin version is not installed on the client.
     * @throws PluginNotFoundException if DivX&reg; Web Player plugin is not installed on the client.
     */
    public DivXPlayer(String mediaURL, boolean autoplay) throws
            LoadException, PluginNotFoundException, PluginVersionException {
        this(mediaURL, autoplay, "90px", "100%");
    }

    /**
     * Constructs <code>DivXPlayer</code> to automatically playback media located at
     * {@code mediaURL} using the default height of 90px and width of 100%.
     *
     * <p>This is the same as calling {@code DivXPlayer(mediaURL, true, "90px", "100%")}
     *
     * @param mediaURL the URL of the media to playback
     *
     * @throws LoadException if an error occurs while loading the media.
     * @throws PluginVersionException if the required DivX&reg; Web Player plugin version is not installed on the client.
     * @throws PluginNotFoundException if DivX&reg; Web Player plugin is not installed on the client.
     */
    public DivXPlayer(String mediaURL) throws LoadException,
            PluginNotFoundException, PluginVersionException {
        this(mediaURL, true, "90px", "100%");
    }

    private void checkAvailable() {
        if (!isPlayerOnPage(playerId)) {
            String message = "Player not available, create an instance";
            fireDebug(message);
            throw new IllegalStateException(message);
        }
    }

    private void checkVideoSize(int vidHeight, int vidWidth) {
        String _h = _height, _w = _width;

        if (resizeToVideoSize) {
            if ((vidHeight > 0) && (vidWidth > 0)) {
                fireDebug("Resizing Player : " + vidWidth + " x " + vidHeight);
                _h = (vidHeight + displayMode.controllerHeight) + "px";
                _w = vidWidth + "px";
            }
        }

        playerWidget.setSize("100%", _h);
        setWidth(_w);

        if (!_height.equals(_h) && !_width.equals(_w)) {
            firePlayerStateEvent(PlayerStateEvent.State.DimensionChangedOnVideo);
        }
    }

    /**
     * Overridden to register player for plugin DOM events
     */
    @Override
    protected final void onLoad() {
        fireDebug("DivX Web Player plugin");
        impl = DivXPlayerImpl.getPlayer(playerId);
        fireDebug("Plugin Version : " + impl.getPluginVersion());
        firePlayerStateEvent(PlayerStateEvent.State.Ready);
        playlistManager.load(0);
    }

    /**
     * Overridden to release resources
     */
    @Override
    protected void onUnload() {
        manager.clearCallbacks(playerId);
    }

    @Override
    public void loadMedia(String mediaURL) throws LoadException {
        checkAvailable();
        impl.loadMedia(mediaURL);
    }

    @Override
    public void playMedia() throws PlayException {
        checkAvailable();
        impl.playMedia();
    }

    @Override
    public void stopMedia() {
        checkAvailable();
        impl.stopMedia();
    }

    @Override
    public void pauseMedia() {
        checkAvailable();
        impl.pauseMedia();
    }

    @Override
    public long getMediaDuration() {
        checkAvailable();
        return (long) impl.getMediaDuration();
    }

    @Override
    public double getPlayPosition() {
        checkAvailable();
        return currentPosition;
    }

    @Override
    public void setPlayPosition(double position) {
        checkAvailable();
        impl.seek(SeekMethod.DOWN.name(), Math.round(position / impl.getMediaDuration() * 100));
        if (playing) {  // seek mthd pauses playback, so start playback if playing b4 ...
            impl.playMedia();
        }
    }

    @Override
    public double getVolume() {
        checkAvailable();
        return impl.getVolume();
    }

    @Override
    public void setVolume(double volume) {
        checkAvailable();
        impl.setVolume(volume);
        fireDebug("Volume set to " + (impl.getVolume() * 100) + "%");
    }

    @Override
    public void showLogger(boolean enable) {
        if (!isEmbedded) {
            logger.setVisible(enable);
        }
    }

    /**
     * Displays or hides the player controls.
     *
     * <p>If this player is not available on the panel, this method
     * call is added to the command-queue for later execution.
     */
    @Override
    public void setControllerVisible(boolean show) {
        if (show && displayMode.equals(DisplayMode.NULL)) {
            setDisplayMode(DisplayMode.MINI);
        } else {
            setDisplayMode(show ? displayMode : DisplayMode.NULL);
        }
    }

    /**
     * Checks whether the player controls are visible.
     */
    @Override
    public boolean isControllerVisible() {
        checkAvailable();
        return !displayMode.equals(DisplayMode.NULL);
    }

    /**
     * Returns the number of times this player repeats playback before stopping.
     */
    @Override
    public int getLoopCount() {
        checkAvailable();
        return loopManager.getLoopCount();
    }

    /**
     * Sets the number of times the current media file should repeat playback before stopping.
     *
     * <p>If this player is not available on the panel, this method
     * call is added to the command-queue for later execution.
     */
    @Override
    public void setLoopCount(final int loop) {
        if (isPlayerOnPage(playerId)) {
            loopManager.setLoopCount(loop);
        } else {
            addToPlayerReadyCommandQueue("loopcount", new Command() {

                @Override
                public void execute() {
                    loopManager.setLoopCount(loop);
                }
            });
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
    }

    @Override
    public boolean isResizeToVideoSize() {
        return resizeToVideoSize;
    }

    /**
     * Specifies whether the player should display the DivX advertisement banner
     * at the end of playback.
     *
     * <p>If this player is not available on the panel, this method
     * call is added to the command-queue for later execution.
     *
     * @param enable {@code true} to enable, {@code false} otherwise
     */
    public void setBannerEnabled(final boolean enable) {
        if (isPlayerOnPage(playerId)) {
            impl.setBannerEnabled(enable);
        } else {
            addToPlayerReadyCommandQueue("banner", new Command() {

                @Override
                public void execute() {
                    impl.setBannerEnabled(enable);
                }
            });
        }
    }

    /**
     * Specify whether the player should display a contextual (right-click) menu
     * when the user presses the right mouse button or the menu buttons on the skin.
     *
     * <p>If this player is not available on the panel, this method
     * call is added to the command-queue for later execution.
     *
     * @param allow {@code true} to allow, {@code false} otherwise
     */
    public void setAllowContextMenu(final boolean allow) {
        if (isPlayerOnPage(playerId)) {
            impl.setAllowContextMenu(allow);
        } else {
            addToPlayerReadyCommandQueue("context", new Command() {

                @Override
                public void execute() {
                    impl.setAllowContextMenu(allow);
                }
            });
        }
    }

    /**
     * Specify how the player should buffer downloaded data before attempting
     * to start playback.
     *
     * <p>If this player is not available on the panel, this method
     * call is added to the command-queue for later execution.
     *
     * @param mode the mode
     */
    public void setBufferingMode(final BufferingMode mode) {
        if (isPlayerOnPage(playerId)) {
            impl.setBufferingMode(mode.name().toLowerCase());
        } else {
            addToPlayerReadyCommandQueue("buffering", new Command() {

                @Override
                public void execute() {
                    impl.setBufferingMode(mode.name().toLowerCase());
                }
            });
        }
    }

    /**
     * Specifies which skin mode the player should use to display playback controls.
     *
     * <p>If this player is not available on the panel, this method
     * call is added to the command-queue for later execution.
     *
     * @param mode the display mode
     */
    public void setDisplayMode(final DisplayMode mode) {
        if (isPlayerOnPage(playerId)) {
            impl.setMode(mode.name().toLowerCase());
            displayMode = mode;
        } else {
            playerWidget.addParam("mode", mode.name().toLowerCase());
            displayMode = mode;
        }
    }

    /**
     * Returns the skin mode of the player which is used to display playback controls.
     *
     * @return the display mode
     * @since 1.3
     */
    public DisplayMode getDisplayMode() {
        return displayMode;
    }

    /**
     * Specifies an image, text and text size to use as a preview for the video. 
     * 
     * <p>The image file which should be in PNG, JPEG or GIF format is used as a preview
     * image and is displayed in place of the DivX&reg; logo.  The <code>message</code> is
     * displayed on top of the image at the size specified by <code>messageFontSize</code>.
     *
     * <p>Note: <code>autoplay</code> should be set to <code>false</code> for this method to
     * take effect.
     *
     * <p>If this player is not available on the panel, this method call is added to the
     * command-queue for later execution.
     *
     * @param imageURL the URL of the preview image
     * @param message the text displayed on top of the image
     * @param messageFontSize the font size of the displayed text
     */
    public void setPreview(final String imageURL, final String message, final int messageFontSize) {
        if (isPlayerOnPage(playerId)) {
            impl.setPreviewImage(imageURL);
            impl.setPreviewMessage(message);
            impl.setPreviewMessageFontSize(messageFontSize);
        } else {
            addToPlayerReadyCommandQueue("preview", new Command() {

                @Override
                public void execute() {
                    impl.setPreviewImage(imageURL);
                    impl.setPreviewMessage(message);
                    impl.setPreviewMessageFontSize(messageFontSize);
                }
            });
        }
    }

    @Override
    public void setShuffleEnabled(final boolean enable) {
        if (isPlayerOnPage(playerId)) {
            playlistManager.setShuffleEnabled(enable);
        } else {
            addToPlayerReadyCommandQueue("shuffle", new Command() {

                @Override
                public void execute() {
                    playlistManager.setShuffleEnabled(enable);
                }
            });
        }
    }

    @Override
    public boolean isShuffleEnabled() {
        checkAvailable();
        return playlistManager.isShuffleEnabled();
    }

    @Override
    public void addToPlaylist(String mediaURL) {
        playlistManager.addToPlaylist(mediaURL);
    }

    @Override
    public void addToPlaylist(MRL mediaLocator) {
        playlistManager.addToPlaylist(mediaLocator);
    }

    @Override
    public void addToPlaylist(String... mediaURLs) {
        playlistManager.addToPlaylist(mediaURLs);
    }

    @Override
    public void addToPlaylist(List<MRL> mediaLocators) {
        playlistManager.addToPlaylist(mediaLocators);
    }

    @Override
    public void removeFromPlaylist(int index) {
        checkAvailable();
        playlistManager.removeFromPlaylist(index);
    }

    @Override
    public void clearPlaylist() {
        checkAvailable();
        playlistManager.clearPlaylist();
    }

    @Override
    public void playNext() throws PlayException {
        checkAvailable();
        playlistManager.playNext();
    }

    @Override
    public void playPrevious() throws PlayException {
        checkAvailable();
        playlistManager.playPrevious();
    }

    @Override
    public void play(int index) throws IndexOutOfBoundsException {
        checkAvailable();
        playlistManager.play(index);
    }

    @Override
    public int getPlaylistSize() {
        checkAvailable();
        return playlistManager.getPlaylistSize();
    }

    @Override
    public RepeatMode getRepeatMode() {
        return loopManager.getRepeatMode();
    }

    @Override
    public void setRepeatMode(RepeatMode mode) {
        loopManager.setRepeatMode(mode);
    }

    private static enum SeekMethod {

        DOWN, UP, DRAG
    }

    /**
     * An enum of buffering modes for DivXPlayer widget.  The mode is used to specify how the DivX Web Player should
     * buffer downloaded data before attempting to start playback.
     */
    public static enum BufferingMode {

        /**
         * The player does only very minimal buffering and starts playing as soon as data is available.
         * This mode does not guarantee a very good user experience unless on a very fast internet connection.
         */
        NULL,
        /**
         * The player analyses the download speed and tries to buffer just enough so
         * that uninterrupted progressive playback can happen at the end of the buffer period.
         */
        AUTO,
        /**
         * The player will always buffer the full video file before starting playback.
         */
        FULL
    }

    /**
     * An enum of display modes for DivXPlayer widget.  The display mode is used to specify which skin mode
     * the plugin should use to display playback controls
     */
    public static enum DisplayMode {

        /**
         * The player shows absolutely no controls.
         */
        NULL(0),
        /**
         * The player only shows a small floating controls bar in the bottom left corner of
         * the allocated video area.
         */
        ZERO(0),
        /**
         * The player shows a more elaborate control bar at the bottom of the video area.
         */
        MINI(20),
        /**
         * The player shows a complete control bar at the bottom of the video area
         */
        LARGE(65),
        /**
         * The player displays the complete control bar at the bottom of the video area and
         * an additional control bar at the top of the video area
         */
        FULL(90);
        private int controllerHeight;

        private DisplayMode(int controllerHeight) {
            this.controllerHeight = controllerHeight;
        }
    }
}
