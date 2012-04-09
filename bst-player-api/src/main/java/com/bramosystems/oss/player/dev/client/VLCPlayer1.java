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
package com.bramosystems.oss.player.dev.client;

import com.bramosystems.oss.player.core.client.playlist.MRL;
import com.bramosystems.oss.player.core.event.client.PlayerStateHandler;
import com.bramosystems.oss.player.core.event.client.PlayerStateEvent;
import com.bramosystems.oss.player.core.event.client.MediaInfoEvent;
import com.bramosystems.oss.player.core.event.client.DebugEvent;
import com.bramosystems.oss.player.core.event.client.MediaInfoHandler;
import com.bramosystems.oss.player.core.event.client.DebugHandler;
import com.bramosystems.oss.player.core.client.*;
import com.bramosystems.oss.player.core.client.MediaInfo.MediaInfoKey;
import com.bramosystems.oss.player.core.client.impl.VLCPlayerImpl;
import com.bramosystems.oss.player.core.client.spi.PlayerWidget;
import com.bramosystems.oss.player.core.client.skin.CustomPlayerControl;
import com.bramosystems.oss.player.core.client.ui.Logger;
import com.bramosystems.oss.player.core.event.client.PlayStateEvent.State;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Widget to embed VLC Media Player&trade; plugin.
 *
 * <h3>Usage Example</h3>
 *
 * <p>
 * <code><pre>
 * SimplePanel panel = new SimplePanel();   // create panel to hold the player
 * Widget player = null;
 * try {
 *      // create the player
 *      player = new VLCPlayer("www.example.com/mediafile.vob");
 * } catch(LoadException e) {
 *      // catch loading exception and alert user
 *      Window.alert("An error occured while loading");
 * } catch(PluginVersionException e) {
 *      // catch plugin version exception and alert user, possibly providing a link
 *      // to the plugin download page.
 *      player = new HTML(".. some nice message telling the user to download plugin first ..");
 * } catch(PluginNotFoundException e) {
 *      // catch PluginNotFoundException and tell user to download plugin, possibly providing
 *      // a link to the plugin download page.
 *      player = new HTML(".. another kind of message telling the user to download plugin..");
 * }
 *
 * panel.setWidget(player); // add player to panel.
 * </pre></code>
 *
 * @author Sikirulai Braheem
 */
public class VLCPlayer1 extends AbstractMediaPlayer implements PlaylistSupport {

    private VLCPlayerImpl impl;
    private PlayerWidget playerWidget;
    private VLCStateManager1 stateHandler;
    private VLCStateManager1.VLCEventCallback eventCallback;
    private String playerId, mediaUrl, _width, _height;
    private Logger logger;
    private boolean isEmbedded, autoplay, resizeToVideoSize, shuffleOn;
    private HandlerRegistration initListHandler;
    private ArrayList<_MRL> _playlistCache;
    private CustomPlayerControl control;

    VLCPlayer1() throws PluginNotFoundException, PluginVersionException {
        PluginVersion req = Plugin.VLCPlayer.getVersion();
        PluginVersion v = PlayerUtil.getVLCPlayerPluginVersion();
        if (v.compareTo(req) < 0) {
            throw new PluginVersionException(Plugin.VLCPlayer, req.toString(), v.toString());
        }

        _playlistCache = new ArrayList<_MRL>();
        playerId = DOM.createUniqueId().replace("-", "");
        stateHandler = new VLCStateManager1();
        shuffleOn = false;

        eventCallback = new VLCStateManager1.VLCEventCallback() {

            @Override
            public void onIdle() {
                fireDebug("player idle");
            }

            @Override
            public void onOpening() {
                fireDebug("opening");
            }

            @Override
            public void onBuffering() {
                fireDebug("buffering");
            }

            @Override
            public void onPlaying() {
                fireDebug("playing");
                firePlayStateEvent(State.Started, 0);
            }

            @Override
            public void onPaused() {
                fireDebug("paused");
                firePlayStateEvent(State.Paused, 0);
            }

            @Override
            public void onForward() {
                fireDebug("forward");
            }

            @Override
            public void onBackward() {
                fireDebug("backward");
            }

            @Override
            public void onError() {
                fireError("Not supported yet.");
            }

            @Override
            public void onEndReached() {
                firePlayStateEvent(State.Finished, 0);
            }

            @Override
            public void onPositionChanged() {
                fireDebug("position");
            }

            @Override
            public void onTimeChanged() {
                fireDebug("time changed");
            }

            @Override
            public void onMouseGrabed(double x, double y) {
                fireDebug("mouse grabed " + x + "," + y);
            }
        };
        stateHandler.initHandlers(playerId, eventCallback);
    }

    /**
     * Constructs <code>VLCPlayer</code> with the specified {@code height} and
     * {@code width} to playback media located at {@code mediaURL}. Media playback
     * begins automatically if {@code autoplay} is {@code true}.
     *
     * <p> {@code height} and {@code width} are specified as CSS units. A value of {@code null}
     * for {@code height} or {@code width} puts the player in embedded mode.  When in embedded mode,
     * the player is made invisible on the page and media state events are propagated to registered
     * listeners only.  This is desired especially when used with custom sound controls.  For custom
     * video control, specify valid CSS values for {@code height} and {@code width} but hide the
     * player controls with {@code setControllerVisible(false)}.
     *
     * @param mediaURL the URL of the media to playback
     * @param autoplay {@code true} to play playing automatically, {@code false} otherwise
     * @param height the height of the player
     * @param width the width of the player.
     *
     * @throws LoadException if an error occurs while loading the media.
     * @throws PluginVersionException if the required VLCPlayer plugin version is not installed on the client.
     * @throws PluginNotFoundException if the VLCPlayer plugin is not installed on the client.
     */
    public VLCPlayer1(String mediaURL, final boolean autoplay, String height, String width)
            throws LoadException, PluginVersionException, PluginNotFoundException {
        this();

        mediaUrl = mediaURL;
        this.autoplay = autoplay;
        _height = height;
        _width = width;

        FlowPanel panel = new FlowPanel();
        initWidget(panel);

        playerWidget = new PlayerWidget("core", Plugin.VLCPlayer.name(), playerId, mediaURL, autoplay);
//        playerWidget.getElement().getStyle().setProperty("backgroundColor", "#000000");   // IE workaround
        panel.add(playerWidget);

        isEmbedded = (height == null) || (width == null);
        if (!isEmbedded) {
            control = new CustomPlayerControl(this);
            panel.add(control);

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
                    logger.log(event.getMediaInfo().asHTMLString(), true);
                }
            });
        } else {
            _height = "0px";
            _width = "0px";
        }
    }

    /**
     * Constructs <code>VLCPlayer</code> to automatically playback media located at
     * {@code mediaURL} using the default height of 20px and width of 100%.
     *
     * @param mediaURL the URL of the media to playback
     *
     * @throws LoadException if an error occurs while loading the media.
     * @throws PluginVersionException if the required VLCPlayer plugin version is not installed on the client.
     * @throws PluginNotFoundException if the VLCPlayer plugin is not installed on the client.
     *
     */
    public VLCPlayer1(String mediaURL) throws LoadException, PluginVersionException,
            PluginNotFoundException {
        this(mediaURL, true, "0px", "100%");
    }

    /**
     * Constructs <code>VLCPlayer</code> to playback media located at {@code mediaURL}
     * using the default height of 20px and width of 100%. Media playback begins
     * automatically if {@code autoplay} is {@code true}.
     *
     * @param mediaURL the URL of the media to playback
     * @param autoplay {@code true} to play playing automatically, {@code false} otherwise
     *
     * @throws LoadException if an error occurs while loading the media.
     * @throws PluginVersionException if the required VLCPlayer plugin version is not installed on the client.
     * @throws PluginNotFoundException if the VLCPlayer plugin is not installed on the client.
     */
    public VLCPlayer1(String mediaURL, boolean autoplay) throws LoadException,
            PluginVersionException, PluginNotFoundException {
        this(mediaURL, autoplay, "0px", "100%");
    }

    /**
     * Overridden to register player for plugin events
     */
    @Override
    protected final void onLoad() {
        playerWidget.setSize(_width, _height);
        setWidth(_width);

        impl = VLCPlayerImpl.getPlayer(playerId);
        fireDebug("VLC Media Player plugin");
        fireDebug("Version : " + impl.getPluginVersion());

//        Timer t = new Timer() {

//            @Override
//            public void run() {
//        stateHandler.start(this, impl);   // start state pooling ...
        stateHandler.registerEventCallbacksIE(impl, playerId);

        // load player ...
//        stateHandler.addToPlaylist(mediaUrl, null);
        fireDebug("add : " + impl.addToPlaylist(mediaUrl));

        // fire player ready ...
        firePlayerStateEvent(PlayerStateEvent.State.Ready);

        // and play if required ...
        if (autoplay) {
//            try {
//                stateHandler.play();
//            } catch (PlayException ex) {
//            }
            impl.play();
        }
//            }
//        };
//        t.schedule(300);
    }

    @Override
    public void loadMedia(String mediaURL) throws LoadException {
        checkAvailable();
//        stateHandler.clearPlaylist();
//        stateHandler.addToPlaylist(mediaUrl, null);
    }

    @Override
    public void playMedia() throws PlayException {
        checkAvailable();
//        stateHandler.play();
        impl.play();
    }

    @Override
    public void play(int index) throws IndexOutOfBoundsException {
        checkAvailable();
//        stateHandler.playItemAt(index);
    }

    @Override
    public void playNext() throws PlayException {
        checkAvailable();
//        stateHandler.next(true); // play next and play over if end-of-playlist
    }

    @Override
    public void playPrevious() throws PlayException {
        checkAvailable();
 //       stateHandler.previous(true);
    }

    @Override
    public void stopMedia() {
        checkAvailable();
 //       stateHandler.stop();
    }

    @Override
    public void pauseMedia() {
        checkAvailable();
        impl.togglePause();
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
        fireDebug("Volume set to " + (volume * 100) + "%");
    }

    private void checkAvailable() {
        if (!isPlayerOnPage(playerId)) {
            String message = "Player not available, create an instance";
            fireDebug(message);
            throw new IllegalStateException(message);
        }
    }

    @Override
    public void showLogger(boolean enable) {
        if (!isEmbedded) {
            logger.setVisible(enable);
        }
    }

    @Override
    public void setControllerVisible(boolean show) {
        if (!isEmbedded) {
            control.setVisible(show);
        }
    }

    @Override
    public boolean isControllerVisible() {
        return control.isVisible();
    }

    @Override
    public int getLoopCount() {
        checkAvailable();
//        return stateHandler.getLoopCount();
        return 1;
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
//            stateHandler.setLoopCount(loop);
        } else {
            addToPlayerReadyCommandQueue("loopcount", new Command() {

                @Override
                public void execute() {
//                    stateHandler.setLoopCount(loop);
                }
            });
        }
    }

    @Override
    public void addToPlaylist(String mediaURL) {
        if (isPlayerOnPage(playerId)) {
//            stateHandler.addToPlaylist(mediaURL, null);
        } else {
            if (initListHandler == null) {
                initListHandler = addPlayerStateHandler(new PlayerStateHandler() {

                    @Override
                    public void onPlayerStateChanged(PlayerStateEvent event) {
                        switch (event.getPlayerState()) {
                            case Ready:
                                for (_MRL mrl : _playlistCache) {
//                                    stateHandler.addToPlaylist(mrl.getUrl(), mrl.getOption());
                                }
                                break;
                        }
                        initListHandler.removeHandler();
                    }
                });
            }
            _playlistCache.add(new _MRL(mediaURL, null));
        }
    }
    @Override
    public void addToPlaylist(MRL mediaLocator) {
    }

    @Override
    public void addToPlaylist(String... mediaURLs) {
    }
    @Override
    public boolean isShuffleEnabled() {
        checkAvailable();
        return shuffleOn;
    }

    @Override
    public void setShuffleEnabled(boolean enable) {
        shuffleOn = enable;
        if (enable) {
//            stateHandler.shuffle();
        }
    }

    @Override
    public void removeFromPlaylist(int index) {
        checkAvailable();
//        stateHandler.removeFromPlaylist(index);
    }

    @Override
    public void clearPlaylist() {
        checkAvailable();
//        stateHandler.clearPlaylist();
    }

    @Override
    public int getPlaylistSize() {
        checkAvailable();
        return impl.getPlaylistCount();
    }

    /**
     * Sets the audio channel mode of the player
     *
     * <p>Use {@linkplain #getAudioChannelMode()} to check if setting of the audio channel
     * is succeessful
     *
     * @param mode the audio channel mode
     * @see #getAudioChannelMode()
     */
    public void setAudioChannelMode(AudioChannelMode mode) {
        checkAvailable();
        impl.setAudioChannelMode(mode.ordinal() + 1);
    }

    /**
     * Gets the current audio channel mode of the player
     *
     * @return the current mode of the audio channel
     * @see #setAudioChannelMode(AudioChannelMode)
     */
    public AudioChannelMode getAudioChannelMode() {
        checkAvailable();
        return AudioChannelMode.values()[impl.getAudioChannelMode() - 1];
    }

    @Override
    public int getVideoHeight() {
        checkAvailable();
        return Integer.parseInt(impl.getVideoHeight());
    }

    @Override
    public int getVideoWidth() {
        checkAvailable();
        return Integer.parseInt(impl.getVideoWidth());
    }

    public void toggleFullScreen() {
        checkAvailable();
        impl.toggleFullScreen();
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

    /*
    public AspectRatio getAspectRatio() {
    checkAvailable();
    if (impl.hasVideo(playerId)) {
    return AspectRatio.parse(impl.getAspectRatio(playerId));
    } else {
    throw new IllegalStateException("No video input can be found");
    }
    }

    public void setAspectRatio(AspectRatio aspectRatio) {
    checkAvailable();
    if (impl.hasVideo(playerId)) {
    impl.setAspectRatio(playerId, aspectRatio.toString());
    } else {
    throw new IllegalStateException("No video input can be found");
    }
    }
     */
    @Override
    public void setResizeToVideoSize(boolean resize) {
        resizeToVideoSize = resize;
        if (isPlayerOnPage(playerId)) {
            // if player is on panel now update its size, otherwise
            // allow it to be handled by the MediaInfoHandler...
            checkVideoSize(getVideoHeight(), getVideoWidth());
        }
    }

    @Override
    public boolean isResizeToVideoSize() {
        return resizeToVideoSize;
    }

    private void checkVideoSize(int vidHeight, int vidWidth) {
        String _h = _height, _w = _width;
        if (vidHeight == 0) {
//            _h = "0px";
        }

        if (resizeToVideoSize) {
            if ((vidHeight > 0) && (vidWidth > 0)) {
                // adjust to video size ...
                fireDebug("Resizing Player : " + vidWidth + " x " + vidHeight);
                _h = vidHeight + "px";
                _w = vidWidth + "px";
            }
        }

        playerWidget.setSize(_w, _h);
        setWidth(_w);

        if (!_height.equals(_h) && !_width.equals(_w)) {
            firePlayerStateEvent(PlayerStateEvent.State.DimensionChangedOnVideo);
        }
    }

    @Override
    public void addToPlaylist(List<MRL> mediaLocators) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private class _MRL {

        private String url, option;

        public _MRL(String url, String option) {
            this.url = url;
            this.option = option;
        }

        public String getUrl() {
            return url;
        }

        public String getOption() {
            return option;
        }
    }

    /**
     * An enum of Audio Channel modes for VLC Media Player&trade;
     */
    public static enum AudioChannelMode {

        /**
         * Stereo mode
         */
        Stereo,
        /**
         * Reverse Stereo mode
         */
        ReverseStereo,
        /**
         * Left only mode
         */
        Left,
        /**
         * Right only mode
         */
        Right,
        /**
         * Dolby mode
         */
        Dolby
    }
}
