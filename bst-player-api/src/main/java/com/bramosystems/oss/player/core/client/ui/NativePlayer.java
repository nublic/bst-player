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

import com.bramosystems.oss.player.core.client.AbstractMediaPlayer;
import com.bramosystems.oss.player.core.client.playlist.MRL;
import com.bramosystems.oss.player.core.client.LoadException;
import com.bramosystems.oss.player.core.client.MediaInfo;
import com.bramosystems.oss.player.core.client.MediaInfo.MediaInfoKey;
import com.bramosystems.oss.player.core.client.PlayException;
import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.bramosystems.oss.player.core.client.PlaylistSupport;
import com.bramosystems.oss.player.core.client.Plugin;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.RepeatMode;
import com.bramosystems.oss.player.core.client.playlist.PlaylistManager;
import com.bramosystems.oss.player.core.client.impl.LoopManager;
import com.bramosystems.oss.player.core.client.impl.NativePlayerImpl;
import com.bramosystems.oss.player.core.client.impl.NativePlayerUtil;
import com.bramosystems.oss.player.core.client.spi.PlayerWidget;
import com.bramosystems.oss.player.core.client.impl.CorePlayerProvider;
import com.bramosystems.oss.player.core.event.client.DebugEvent;
import com.bramosystems.oss.player.core.event.client.DebugHandler;
import com.bramosystems.oss.player.core.event.client.MediaInfoEvent;
import com.bramosystems.oss.player.core.event.client.MediaInfoHandler;
import com.bramosystems.oss.player.core.event.client.PlayStateEvent;
import com.bramosystems.oss.player.core.event.client.PlayerStateEvent;
import com.bramosystems.oss.player.core.client.spi.Player;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import java.util.ArrayList;
import java.util.List;

/**
 * Widget to embed media files with HTML 5 video elements in compliant browsers.
 *
 * <h3>Usage Example</h3>
 *
 * <p>
 * <code><pre>
 * SimplePanel panel = new SimplePanel();   // create panel to hold the player
 * Widget player = null;
 * try {
 *      // create the player
 *      player = new NativePlayer("www.example.com/mediafile.ogg");
 * } catch(LoadException e) {
 *      // catch loading exception and alert user
 *      Window.alert("An error occured while loading");
 * } catch(PluginNotFoundException e) {
 *      // PluginNotFoundException thrown if browser does not support HTML 5 specs.
 *      player = PlayerUtil.getMissingPluginNotice(e.getPlugin());
 * }
 *
 * panel.setWidget(player); // add player to panel.
 * </pre></code>
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems dot com>
 */
@Player(name = "Native", providerFactory = CorePlayerProvider.class, minPluginVersion = "5.0.0")
public class NativePlayer extends AbstractMediaPlayer implements PlaylistSupport {

    private NumberFormat volFmt = NumberFormat.getPercentFormat();
    private NativePlayerImpl impl;
    private String playerId, _height, _width;
    private PlayerWidget playerWidget;
    private boolean adjustToVideoSize, isEmbedded, isWasPlaying;
    private Logger logger;
    private LoopManager loopManager;
    private PlaylistManager playlistManager;
    private NativePlayerUtil.NativeEventCallback _callback;

    private NativePlayer() throws PluginNotFoundException {
        if (!PlayerUtil.isHTML5CompliantClient()) {
            throw new PluginNotFoundException(Plugin.Native);
        }

        playerId = DOM.createUniqueId().replace("-", "");
        adjustToVideoSize = false;
        playlistManager = new PlaylistManager(this);
        loopManager = new LoopManager(new LoopManager.LoopCallback() {

            @Override
            public void playNextItem() throws PlayException {
                playlistManager.playNext();
            }

            @Override
            public void onLoopFinished() {
                isWasPlaying = false;
                fireDebug("Play finished - " + playlistManager.getPlaylistIndex());
                firePlayStateEvent(PlayStateEvent.State.Finished,
                        playlistManager.getPlaylistIndex());
            }

            @Override
            public void repeatPlay() {
                playlistManager.play(playlistManager.getPlaylistIndex());
            }

            @Override
            public void playNextLoop() {
                impl.play();
            }
        });
        _callback = new NativePlayerUtil.NativeEventCallback() {

            @Override
            public void onProgressChanged() {
                NativePlayerImpl.TimeRange time = impl.getBuffered();
                if (time != null) {
                    double i = time.getLength();
                    fireLoadingProgress((time.getEnd(i - 1) - time.getStart(0)) * 1000 / impl.getDuration());
                }
            }

            @Override
            public void onStateChanged(int code) {
                switch (code) {
                    case 1: // play started
                        isWasPlaying = true;
                        fireDebug("Play started");
                        firePlayStateEvent(PlayStateEvent.State.Started, playlistManager.getPlaylistIndex());
                        fireDebug("Playing media at '" + impl.getMediaURL() + "'");
                        break;
                    case 2: // pause
                        fireDebug("Play paused");
                        firePlayStateEvent(PlayStateEvent.State.Paused, playlistManager.getPlaylistIndex());
                        break;
                    case 3: // finished
                        // notify loop manager, it handles play finished event ...
                        loopManager.notifyPlayFinished();
                        break;
                    case 4: // buffering
                        fireDebug("Buffering started");
                        firePlayerStateEvent(PlayerStateEvent.State.BufferingStarted);
                        break;
                    case 5: // playing again, buffering stopped
                        fireDebug("Buffering stopped");
                        firePlayerStateEvent(PlayerStateEvent.State.BufferingFinished);
                        break;
                    case 6: // process metadata
                        fireDebug("Media Metadata available");
                        MediaInfo info = new MediaInfo();
                        impl.fillMediaInfo(info);
                        fireMediaInfoAvailable(info);
                        break;
                    case 7: // volume changed
                        if (impl.isMute()) {
                            fireDebug("Volume muted");
                        } else {
                            fireDebug("Volume changed : " + volFmt.format(impl.getVolume()));
                        }
                        break;
                    case 10: // loading started
                        fireDebug("Loading '" + impl.getMediaURL() + "'");
                        fireLoadingProgress(0);
                        break;
                    case 11: // loading finished
                        fireDebug("Loading completed");
                        fireLoadingProgress(1.0);
                        break;
                    case 12: // error
                        switch (MediaError.values()[impl.getErrorState()]) {
                            case Aborted:
                                fireError("ERROR: Loading aborted!");
                                break;
                            case DecodeError:
                                fireError("ERROR: Decoding error");
                                break;
                            case NetworkError:
                                fireError("ERROR: Network error");
                                break;
                            case UnsupportedMedia:
                                fireError("ERROR: Media could not be loaded or format not supported! '"
                                        + playlistManager.getCurrentItem() + "'");
                                fireDebug("Trying alternative playlist item ...");
                                try {
                                    playlistManager.loadAlternative();
                                } catch (LoadException ex) {
                                    fireError("ERROR: No alternative media available!");
                                    if (isWasPlaying) {
                                        loopManager.notifyPlayFinished();
                                    } else {
                                        playlistManager.loadNext();
                                    }
                                }
                                break;
                        }
                        break;
                    case 13: // loading aborted
                        fireDebug("Media loading aborted!");
                        break;
                }
            }
        };
    }

    private void _init(String width, String height) {
        FlowPanel panel = new FlowPanel();
        panel.add(playerWidget);
        initWidget(panel);

        if ((width == null) || (height == null)) {
            _height = "0px";
            _width = "0px";
            isEmbedded = true;
        } else {
            _height = height;
            _width = width;

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
                    logger.log(info.asHTMLString(), true);
                    if (info.getAvailableItems().contains(MediaInfoKey.VideoHeight)
                            || info.getAvailableItems().contains(MediaInfoKey.VideoWidth)) {
                        checkVideoSize(Integer.parseInt(info.getItem(MediaInfoKey.VideoHeight)),
                                Integer.parseInt(info.getItem(MediaInfoKey.VideoWidth)));
                    }
                }
            });
        }
    }

    /**
     * Constructs <code>NativePlayer</code> to playback media located at {@code mediaURL}.
     * Media playback begins automatically.
     *
     * <p>This is the same as calling {@code NativePlayer(mediaURL, true, "20px", "100%")}</p>
     *
     * @param mediaURL the URL of the media to playback
     *
     * @throws LoadException if an error occurs while loading the media.
     * @throws PluginNotFoundException if browser does not support the HTML 5 specification.
     */
    public NativePlayer(String mediaURL) throws LoadException, PluginNotFoundException {
        this(mediaURL, true, NativePlayerUtil.get.getPlayerHeight(), "100%");
    }

    /**
     * Constructs <code>NativePlayer</code> to playback media located at {@code mediaURL}.
     * Media playback begins automatically if {@code autoplay} is {@code true}.
     *
     * <p>This is the same as calling {@code NativePlayer(mediaURL, autoplay, "20px", "100%")}</p>
     *
     * @param mediaURL the URL of the media to playback
     * @param autoplay {@code true} to start playing automatically, {@code false} otherwise
     *
     * @throws LoadException if an error occurs while loading the media.
     * @throws PluginNotFoundException if browser does not support the HTML 5 specification.
     */
    public NativePlayer(String mediaURL, boolean autoplay)
            throws LoadException, PluginNotFoundException {
        this(mediaURL, autoplay, NativePlayerUtil.get.getPlayerHeight(), "100%");
    }

    // TODO: 2.x remove LoadException from throws clause ....
    /**
     * Constructs <code>NativePlayer</code> with the specified {@code height} and
     * {@code width} to playback media located at {@code mediaURL}. Media playback
     * begins automatically if {@code autoplay} is {@code true}.
     *
     * <p> {@code height} and {@code width} are specified as CSS units.</p>
     *
     * @param mediaURL the URL of the media to playback
     * @param autoplay {@code true} to start playing automatically, {@code false} otherwise
     * @param height the height of the player
     * @param width the width of the player.
     *
     * @throws LoadException if an error occurs while loading the media.
     * @throws PluginNotFoundException if browser does not support the HTML 5 specification.
     */
    public NativePlayer(String mediaURL, boolean autoplay, String height, String width)
            throws LoadException, PluginNotFoundException {
        this();
        playerWidget = new PlayerWidget("core", Plugin.Native.name(), playerId, "", autoplay);
        _init(width, height);
        playlistManager.addToPlaylist(mediaURL);
    }

    /**
     * Constructs <code>NativePlayer</code> with the specified {@code height} and
     * {@code width} to playback media located at any of the {@code mediaSources}.
     * Playback begins automatically if {@code autoplay} is {@code true}.
     *
     * <p>As per the HTML 5 specification, the browser chooses any of the {@code mediaSources}
     * it supports</p>
     *
     * <p> {@code height} and {@code width} are specified as CSS units.</p>
     *
     * @param mediaSources a list of media URLs
     * @param autoplay {@code true} to start playing automatically, {@code false} otherwise
     * @param height the height of the player
     * @param width the width of the player.
     *
     * @throws LoadException if an error occurs while loading the media.
     * @throws PluginNotFoundException if browser does not support the HTML 5 specification.
     */
    public NativePlayer(ArrayList<String> mediaSources, boolean autoplay, String height, String width)
            throws LoadException, PluginNotFoundException {
        this();
        playerWidget = new PlayerWidget("core", Plugin.Native.name(), playerId, "", autoplay);
        _init(width, height);
        playlistManager.addToPlaylist(new MRL(mediaSources));
    }

    /**
     * Overriden to register player for DOM events.
     */
    @Override
    protected void onLoad() {
        fireDebug("HTML5 Native Player");
        playerWidget.setSize("100%", _height);
        setWidth(_width);

        impl = NativePlayerImpl.getPlayer(playerId);
        impl.registerMediaStateHandler(_callback);
        firePlayerStateEvent(PlayerStateEvent.State.Ready);
        playlistManager.load(0);
    }

    @Override
    public void loadMedia(String mediaURL) throws LoadException {
        checkAvailable();
        impl.setMediaURL(mediaURL);
        impl.load();
    }

    @Override
    public void playMedia() throws PlayException {
        checkAvailable();
        impl.play();
    }

    @Override
    public void stopMedia() {
        checkAvailable();
        impl.pause();
        impl.setTime(0);
        firePlayStateEvent(PlayStateEvent.State.Stopped, playlistManager.getPlaylistIndex());
    }

    @Override
    public void pauseMedia() {
        checkAvailable();
        impl.pause();
    }

    @Override
    public long getMediaDuration() {
        checkAvailable();
        return (long) impl.getDuration();
    }

    @Override
    public double getPlayPosition() {
        checkAvailable();
        return impl.getTime();
    }

    @Override
    public void setPlayPosition(double position) {
        checkAvailable();
        impl.setTime(position);
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
    }

    @Override
    public int getLoopCount() {
        checkAvailable();
        return loopManager.getLoopCount();
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
    public boolean isControllerVisible() {
        checkAvailable();
        return impl.isControlsVisible();
    }

    @Override
    public boolean isResizeToVideoSize() {
        return adjustToVideoSize;
    }

    /**
     * Displays or hides the player controls.
     *
     * <p>If this player is not available on the panel, this method
     * call is added to the command-queue for later execution.
     */
    @Override
    public void setControllerVisible(final boolean show) {
        if (isPlayerOnPage(playerId)) {
            impl.setControlsVisible(show);
        } else {
            addToPlayerReadyCommandQueue("controller", new Command() {

                @Override
                public void execute() {
                    impl.setControlsVisible(show);
                }
            });
        }
    }

    /**
     * Sets the number of times the current media file should repeat playback before stopping.
     *
     * <p>If this player is not available on the panel, this method call is added
     * to the command-queue for later execution.
     */
    @Override
    public void setLoopCount(final int loop) {
        if (isPlayerOnPage(playerId)) {
            loopManager.setLoopCount(loop);
        } else {
            addToPlayerReadyCommandQueue("loop", new Command() {

                @Override
                public void execute() {
                    loopManager.setLoopCount(loop);
                }
            });
        }
    }

    @Override
    public void setResizeToVideoSize(boolean resize) {
        adjustToVideoSize = resize;
        if (isPlayerOnPage(playerId)) {
            // if player is on panel now update its size, otherwise
            // allow it to be handled by the MediaInfoHandler...
            checkVideoSize(getVideoHeight(), getVideoWidth());
        }
    }

    private void checkVideoSize(int vidHeight, int vidWidth) {
        String _h = _height, _w = _width;
        if (adjustToVideoSize) {
            if ((vidHeight > 0) && (vidWidth > 0)) {
                // adjust to video size ...
                fireDebug("Resizing Player : " + vidWidth + " x " + vidHeight);
                _w = vidWidth + "px";
                _h = vidHeight + "px";
            }
        }

        playerWidget.setSize("100%", _h);
        setWidth(_w);

        if (!_height.equals(_h) && !_width.equals(_w)) {
            firePlayerStateEvent(PlayerStateEvent.State.DimensionChangedOnVideo);
        }
    }

    @Override
    public void showLogger(boolean show) {
        if (!isEmbedded) {
            logger.setVisible(show);
        }
    }

    @Override
    public void setRate(final double rate) {
        if (isPlayerOnPage(playerId)) {
            impl.setRate(rate);
        } else {
            addToPlayerReadyCommandQueue("rate", new Command() {

                @Override
                public void execute() {
                    impl.setRate(rate);
                }
            });
        }
    }

    @Override
    public double getRate() {
        checkAvailable();
        return impl.getRate();
    }

    private void checkAvailable() {
        if (!isPlayerOnPage(playerId)) {
            String message = "Player not available, create an instance";
            fireDebug(message);
            throw new IllegalStateException(message);
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

    /**
     * Adds the media at the specified URLs to the players' playlist.  However, as
     * per the HTML 5 specification, the browser chooses ONLY ONE of the {@code mediaURLs}
     * it supports.
     *
     * <p>In respect of the same domain policy of some browsers, the URLs should point to
     * a destination on the same domain where the application is hosted.
     * 
     * @param mediaURLs the alternative URLs of the same media (probably in different formats).
     * @since 1.2
     */
    @Override
    public void addToPlaylist(String... mediaURLs) {
        playlistManager.addToPlaylist(mediaURLs);
    }

    @Override
    public void addToPlaylist(MRL mediaLocator) {
        playlistManager.addToPlaylist(mediaLocator);
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

    private enum NetworkState {

        Empty, Idle, Loading, Loaded, NoSource
    }

    private enum MediaError {

        NoError, Aborted, NetworkError, DecodeError, UnsupportedMedia
    }

    private enum ReadyState {

        HaveNothing, HaveMetadata, CurrentData, FutureData, EnoughData
    }
}
