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
package com.bramosystems.oss.player.youtube.client;

import com.bramosystems.oss.player.core.client.AbstractMediaPlayer;
import com.bramosystems.oss.player.core.client.ConfigParameter;
import com.bramosystems.oss.player.core.client.ConfigValue;
import com.bramosystems.oss.player.core.client.LoadException;
import com.bramosystems.oss.player.core.client.PlayException;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.bramosystems.oss.player.core.client.RepeatMode;
import com.bramosystems.oss.player.core.client.TransparencyMode;
import com.bramosystems.oss.player.core.client.spi.PlayerWidget;
import com.bramosystems.oss.player.core.client.spi.Player;
import com.bramosystems.oss.player.core.client.ui.Logger;
import com.bramosystems.oss.player.core.event.client.DebugEvent;
import com.bramosystems.oss.player.core.event.client.DebugHandler;
import com.bramosystems.oss.player.core.event.client.LoadingProgressEvent;
import com.bramosystems.oss.player.core.event.client.PlayStateEvent;
import com.bramosystems.oss.player.core.event.client.PlayerStateEvent;
import com.bramosystems.oss.player.core.event.client.PlayerStateHandler;
import com.bramosystems.oss.player.youtube.client.impl.YouTubeEventManager;
import com.bramosystems.oss.player.youtube.client.impl.YouTubePlayerImpl;
import com.bramosystems.oss.player.youtube.client.impl.YouTubePlayerProvider;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlowPanel;
import java.util.ArrayList;

/**
 * Widget to embed YouTube video
 *
 * <h3>Usage Example</h3>
 *
 * <p>
 * <code><pre>
 * SimplePanel panel = new SimplePanel();   // create panel to hold the player
 * Widget player = null;
 * try {
 *      // create the player
 *      player = new YouTubePlayer("http://www.youtube.com/v/VIDEO_ID&fs=1", "100%", "350px");
 * } catch(PluginVersionException e) {
 *      // catch plugin version exception and alert user to download plugin first.
 *      // An option is to use the utility method in PlayerUtil class.
 *      player = PlayerUtil.getMissingPluginNotice(e.getPlugin());
 * } catch(PluginNotFoundException e) {
 *      // catch PluginNotFoundException and tell user to download plugin, possibly providing
 *      // a link to the plugin download page.
 *      player = new HTML(".. another kind of message telling the user to download plugin..");
 * }
 *
 * panel.setWidget(player); // add player to panel.
 * </pre></code>
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems dot com>
 * @since 1.1
 */
@Player(name = "YouTube", minPluginVersion = "9.0.0", providerFactory = YouTubePlayerProvider.class)
public class YouTubePlayer extends AbstractMediaPlayer {

    private static YouTubeEventManager eventMgr = new YouTubeEventManager();
    protected YouTubePlayerImpl impl;
    protected String playerId, _width, _height, _vURL;
    private Timer bufferingTimer;
    private Logger logger;
    private PlayerWidget swf;
    private PlayerParameters pps;
    private RepeatMode repeatMode = RepeatMode.REPEAT_OFF;
//    private YouTubePlaylistManager ypm;

    /**
     * Constructs <code>YouTubePlayer</code> with the specified {@code height} and
     * {@code width} to playback video located at {@code videoURL}
     *
     * <p> {@code height} and {@code width} are specified as CSS units.
     *
     * @param videoURL the URL of the video
     * @param width the width of the player
     * @param height the height of the player
     *
     * @throws PluginNotFoundException if the required Flash player plugin is not found
     * @throws PluginVersionException if Flash player version 8 and above is not found
     * @throws NullPointerException if either {@code videoURL}, {@code height} or {@code width} is null
     */
    public YouTubePlayer(String videoURL, String width, String height)
            throws PluginNotFoundException, PluginVersionException {
        this(videoURL, new PlayerParameters(), width, height);
    }

    /**
     * Constructs <code>YouTubePlayer</code> with the specified {@code height} and
     * {@code width} to playback video located at {@code videoURL} using the specified
     * {@code playerParameters}
     *
     * <p> {@code height} and {@code width} are specified as CSS units.
     *
     * @param videoURL the URL of the video
     * @param playerParameters the parameters of the player
     * @param width the width of the player
     * @param height the height of the player
     *
     * @throws PluginNotFoundException if the required Flash player plugin is not found
     * @throws PluginVersionException if Flash player version 8 and above is not found
     * @throws NullPointerException if either {@code videoURL}, {@code height} or {@code width} is null
     */
    public YouTubePlayer(String videoURL, PlayerParameters playerParameters, String width, String height)
            throws PluginNotFoundException, PluginVersionException {
        if (height == null) {
            throw new NullPointerException("height cannot be null");
        }
        if (width == null) {
            throw new NullPointerException("width cannot be null");
        }
        if (videoURL == null) {
            throw new NullPointerException("videoURL cannot be null");
        }

        _width = width;
        _height = height;
        _vURL = videoURL;
        pps = playerParameters;
        /*
        ypm = new YouTubePlaylistManager(new YouTubePlaylistManager.CallbackHandler() {

            @Override
            public void onError(String message) {
                fireError(message);
            }

            @Override
            public YouTubePlayerImpl getPlayerImpl() {
                return impl;
            }

            @Override
            public void onInfo(String info) {
                fireDebug(info);
            }
        });
         */
        playerId = DOM.createUniqueId().replace("-", "");

        logger = new Logger();
        logger.setVisible(false);
        addDebugHandler(new DebugHandler() {

            @Override
            public void onDebug(DebugEvent event) {
                logger.log(event.getMessage(), false);
            }
        });

        initWidget(new FlowPanel());

        // register for DOM events ...
        eventMgr.init(playerId, new YouTubeEventManager.EventHandler() {

            @Override
            public void onInit() {
                impl = YouTubePlayerImpl.getPlayerImpl(playerId);
                impl.registerHandlers(playerId);
                fireDebug("YouTube Player");
//                ypm.commitPlaylist();
                playerInit();
            }

            @Override
            public void onYTStateChanged(int state) {
                switch (state) {
                    case -1: // unstarted
                        fireDebug("Waiting for video...");
                        break;
                    case 0: // ended
                        firePlayStateEvent(PlayStateEvent.State.Finished, 0);
                        fireDebug("Playback finished");
                        break;
                    case 1: // playing
                        firePlayerStateEvent(PlayerStateEvent.State.BufferingFinished);
                        firePlayStateEvent(PlayStateEvent.State.Started, 0);
                        fireDebug("Playback started");
                        break;
                    case 2: // paused
                        firePlayStateEvent(PlayStateEvent.State.Paused, 0);
                        fireDebug("Playback paused");
                        break;
                    case 3: // buffering
                        firePlayerStateEvent(PlayerStateEvent.State.BufferingStarted);
                        fireDebug("Buffering...");
                        break;
                    case 5: // video cued
                        firePlayerStateEvent(PlayerStateEvent.State.Ready);
                        fireDebug("Video ready for playback");
                        break;
                }
            }

            @Override
            public void onYTQualityChanged(String quality) {
                PlaybackQuality pq = PlaybackQuality.Default;
                for (PlaybackQuality _pq : PlaybackQuality.values()) {
                    if (_pq.name().toLowerCase().equals(quality)) {
                        pq = _pq;
                    }
                }
                PlaybackQualityChangeEvent.fire(YouTubePlayer.this, pq);
                fireDebug("Playback quality changed : " + quality);
            }

            @Override
            public void onYTError(int errorCode) {
                switch (errorCode) {
                    case 100: // video not found. Occurs when video is removed (for any reason), or marked private.
                        fireError("Video not found! It may have been removed or marked private");
                        break;
                    case 101: // video does not allow playback in the embedded players.
                    case 150: // is the same as 101, it's just 101 in disguise!
                        fireError("Video playback not allowed");
                        break;
                }
            }
        });

        // setup loading event management ...
        bufferingTimer = new Timer() {

            @Override
            public void run() {
                LoadingProgressEvent.fire(YouTubePlayer.this,
                        impl.getBytesLoaded() / impl.getBytesTotal());
            }
        };
        addPlayerStateHandler(new PlayerStateHandler() {

            @Override
            public void onPlayerStateChanged(PlayerStateEvent event) {
                switch (event.getPlayerState()) {
                    case BufferingStarted:
                        bufferingTimer.scheduleRepeating(1000);
                        break;
                    case BufferingFinished:
                        bufferingTimer.cancel();
                }
            }
        });
    }

    @Override
    protected void onLoad() {
        swf = new PlayerWidget(YouTubePlayerProvider.PROVIDER_NAME, "YouTube", playerId,
                getNormalizedVideoAppURL(_vURL, pps), false);
        swf.addParam("allowScriptAccess", "always");
        swf.addParam("bgcolor", "#000000");
        if (pps.isFullScreenEnabled()) {
            swf.addParam("allowFullScreen", "true");
        }

        FlowPanel fp = (FlowPanel) getWidget();
        fp.add(swf);
        fp.add(logger);

        swf.setSize("100%", _height);
        setWidth(_width);
    }

    @Override
    protected void onUnload() {
        if (impl != null) {
            impl.stop();
            impl.clear();
        }
        eventMgr.close(playerId);
    }

    /**
     * Returns the normalized URL of the video.
     *
     * <p>This method is called by the player Constructors.  It adjusts the parameters that may
     * be present in the <code>videoURL</code> and <code>playerParameters</code> (possibly overriding some)
     * to match the requirements of this players' internals.
     *
     * @param videoURL the URL of the YouTube&trade; video
     * @param playerParameters the parameters of the video
     * @return the normalized URL of the video
     */
    protected String getNormalizedVideoAppURL(String videoURL, PlayerParameters playerParameters) {
        parseURLParams(videoURL, playerParameters);
        playerParameters.setJSApiEnabled(true);
        playerParameters.setPlayerAPIId(playerId);
        StringBuilder vurl = new StringBuilder(videoURL).append(paramsToString(playerParameters));
        return vurl.toString();
    }

    /**
     * Called when player initialization is completed.
     */
    protected void playerInit() {
    }

    /**
     * Puts all URL parameters that may be present in the <code>videoURL</code> into the
     * <code>playerParameters</code> object.
     *
     * <p>Note: when this method returns, all parameters present in <code>videoURL</code> would have been
     * removed. For example:
     *
     * <p>If <code>videoURL</code> before method call is <em>http://www.youtube.com/v/VIDEO_ID&paramName=value</em>,
     * then <code>videoURL</code> after method call is <em>http://www.youtube.com/v/VIDEO_ID</em>.
     *
     * @param videoURL the URL of the YouTube&trade; video
     * @param playerParameters the parameters of the video
     */
    protected final void parseURLParams(String videoURL, PlayerParameters playerParameters) {
        String _params[] = videoURL.split("&");
        videoURL = _params[0];
        if (_params.length > 1) {
            for (int i = 1; i < _params.length; i++) {
                try {
                    String value[] = _params[i].split("=");
                    switch (URLParameters.valueOf(value[0])) {
                        case autohide:
                            playerParameters.setAutoHide(PlayerParameters.AutoHideMode.values()[Integer.parseInt(value[1])]);
                            break;
                        case autoplay:
                            playerParameters.setAutoplay(value[1].equals("1"));
                            break;
                        case border:
                            playerParameters.showBorder(value[1].equals("1"));
                            break;
//                        case cc_load_policy:
//                            playerParameters.showClosedCaptions(value[1].equals("1"));
//                            break;
                        case color1:
                            playerParameters.setPrimaryBorderColor(value[1]);
                            break;
                        case color2:
                            playerParameters.setSecondaryBorderColor(value[1]);
                            break;
                        case controls:
                            playerParameters.setShowControls(value[1].equals("1"));
                            break;
                        case disablekb:
                            playerParameters.setKeyboardControlsEnabled(value[1].equals("0"));
                            break;
                        case egm:
                            playerParameters.setEnhancedGenieMenuEnabled(value[1].equals("1"));
                            break;
                        case enablejsapi:
                            playerParameters.setJSApiEnabled(value[1].equals("1"));
                            break;
                        case fs:
                            playerParameters.setFullScreenEnabled(value[1].equals("1"));
                            break;
                        case hd:
                            playerParameters.setHDEnabled(value[1].equals("1"));
                            break;
                        case iv_load_policy:
                            playerParameters.showVideoAnnotations(value[1].equals("1"));
                            break;
                        case loop:
                            playerParameters.setLoopEnabled(value[1].equals("1"));
                            break;
                        case playerapiid:
                            playerParameters.setPlayerAPIId(value[1]);
                            break;
                        case rel:
                            playerParameters.setLoadRelatedVideos(value[1].equals("1"));
                            break;
                        case showinfo:
                            playerParameters.showVideoInformation(value[1].equals("1"));
                            break;
                        case showsearch:
                            playerParameters.showSearchBox(value[1].equals("1"));
                            break;
                        case start:
                            playerParameters.setStartTime(Integer.parseInt(value[1]));
                            break;
                        case modestbranding:
                            playerParameters.setModestBranding(value[1].equals("1"));
                            break;
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * Converts the PlayerParameters object into YouTube&trade; video URL parameters.
     *
     * @param playerParameters the player parameters
     * @return the parameters in YouTube&trade; video URL format
     */
    protected final String paramsToString(PlayerParameters playerParameters) {
        StringBuilder url = new StringBuilder("&version=3");
        for (URLParameters _param : URLParameters.values()) {
            url.append("&").append(_param.name()).append("=");
            switch (_param) {
                case autoplay:
                    url.append(playerParameters.autoplay);
                    break;
                case border:
                    url.append(playerParameters.showBorder);
                    break;
//                case cc_load_policy:
//                    url.append(playerParameters.() ? "1" : "0");
//                    break;
                case color1:
                    url.append(playerParameters.getPrimaryBorderColor());
                    break;
                case color2:
                    url.append(playerParameters.getSecondaryBorderColor());
                    break;
                case disablekb:
                    url.append(playerParameters.disableKeyboardControls);
                    break;
                case egm:
                    url.append(playerParameters.egm);
                    break;
                case enablejsapi:
                    url.append(playerParameters.enableJsApi);
                    break;
                case fs:
                    url.append(playerParameters.fullScreen);
                    break;
                case hd:
                    url.append(playerParameters.highDef);
                    break;
                case iv_load_policy:
                    url.append(playerParameters.ivLoadPolicy);
                    break;
                case loop:
                    url.append(playerParameters.loop);
                    break;
                case playerapiid:
                    url.append(playerParameters.getPlayerAPIId());
                    break;
                case rel:
                    url.append(playerParameters.loadRelatedVideos);
                    break;
                case showinfo:
                    url.append(playerParameters.showInfo);
                    break;
                case showsearch:
                    url.append(playerParameters.showSearch);
                    break;
                case start:
                    url.append(playerParameters.getStartTime());
                    break;
                case autohide:
                    url.append(playerParameters.getAutoHide().ordinal());
                    break;
                case controls:
                    url.append(playerParameters.showControls);
                    break;
                case modestbranding:
                    url.append(playerParameters.modestBranding);
                    break;
            }
        }
        return url.toString();
    }

    @Override
    public void loadMedia(String mediaURL) throws LoadException {
        if (impl != null) {
            impl.loadVideoByUrl(mediaURL, 0);
        }
    }

    @Override
    public void playMedia() throws PlayException {
        if (impl != null) {
            impl.play();
        }
    }

    @Override
    public void stopMedia() {
        if (impl != null) {
            impl.pause();
            impl.seekTo(0, true);
        }
    }

    @Override
    public void pauseMedia() {
        if (impl != null) {
            impl.pause();
        }
    }

    @Override
    public long getMediaDuration() {
        if (impl != null) {
            return (long) impl.getDuration();
        }
        return 0;
    }

    @Override
    public double getPlayPosition() {
        if (impl != null) {
            return impl.getCurrentTime();
        }
        return 0;
    }

    @Override
    public void setPlayPosition(double position) {
        if (impl != null) {
            impl.seekTo(position, true);
        }
    }

    @Override
    public double getVolume() {
        if (impl != null) {
            return impl.getVolume();
        }
        return 0;
    }

    @Override
    public void setVolume(double volume) {
        if (impl != null) {
            impl.setVolume(volume);
        }
    }

    @Override
    public int getLoopCount() {
        return 1;
    }

    @Override
    public void setLoopCount(int loop) {
        if((impl != null) && (loop < 0)) {
            setRepeatMode(RepeatMode.REPEAT_ALL);
        }
    }

    @Override
    public RepeatMode getRepeatMode() {
        return repeatMode;
    }

    @Override
    public void setRepeatMode(RepeatMode mode) {
        if (impl != null) {
            switch (mode) {
                case REPEAT_ALL:
                    impl.setLoop(true);
                    repeatMode = mode;
                    break;
                case REPEAT_OFF:
                    impl.setLoop(false);
                    repeatMode = mode;
            }
        }
    }

    /**
     * Checks whether the player controls are visible.  This implementation <b>always</b> return true.
     */
    @Override
    public boolean isControllerVisible() {
        return true;
    }

    @Override
    public void showLogger(boolean show) {
        logger.setVisible(show);
    }

    /**
     * Sets the suggested video quality for the current video. This method causes the video to reload
     * at its current position in the new quality.
     *
     * <p>
     * <b>Note:</b> Calling this method does not guarantee that the playback quality will actually
     * change. If the playback quality does change, it will only change for the video being played and
     * the {@linkplain PlaybackQualityChangeEvent} event will be fired.
     *
     * <p>
     * If {@code suggestedQuality} is not available for the current video, then the quality will be
     * set to the next lowest level that is available. That is, if {@code suggestedQuality} is
     * {@linkplain PlaybackQuality#hd720} and that is unavailable, then the playback quality will be
     * set to {@linkplain PlaybackQuality#large} if that quality level is available.
     *
     * @param suggestedQuality the suggested video quality for the current video
     */
    public void setPlaybackQuality(PlaybackQuality suggestedQuality) {
        if (impl != null) {
            impl.setPlaybackQuality(suggestedQuality.name().toLowerCase());
        }
    }

    /**
     * Retrieves the playback quality of the current video.
     *
     * @return the playback quality of the current video
     *
     * @throws IllegalStateException if no video is loaded in the player
     */
    public PlaybackQuality getPlaybackQuality() throws IllegalStateException {
        if (impl != null) {
            String qua = impl.getPlaybackQuality();
            if (qua.equals("undefined")) {
                throw new IllegalStateException("Player not loaded!");
            }
            return PlaybackQuality.valueOf(qua);
        }
        throw new IllegalStateException("Player not available");
    }

    /**
     * Returns the list of quality formats in which the current video is available.
     *
     * <p>An empty list is returned if no video is loaded.
     *
     * @return a list of quality formats available for the current video
     */
    public ArrayList<PlaybackQuality> getAvailableQualityLevels() {
        ArrayList<PlaybackQuality> pqs = new ArrayList<PlaybackQuality>();
        if (impl != null) {
            JsArrayString qua = impl.getAvailableQualityLevels();
            for (int i = 0; i < qua.length(); i++) {
                pqs.add(PlaybackQuality.valueOf(qua.get(i)));
            }
        }
        return pqs;
    }

    /**
     * Adds a {@link PlaybackQualityChangeHandler} handler to the player
     *
     * @param handler handler for the PlaybackQualityChangeEvent event
     * @return {@link HandlerRegistration} used to remove the handler
     */
    public HandlerRegistration addPlaybackQualityChangeHandler(PlaybackQualityChangeHandler handler) {
        return addHandler(handler, PlaybackQualityChangeEvent.TYPE);
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
/*
     * Roll into v 2.0
     * 
    @Override
    public void addToPlaylist(String mediaURL) {
         ypm.addToPlaylist(mediaURL);
    }

    @Override
    public void addToPlaylist(String... mediaURLs) {
        ypm.addToPlaylist(mediaURLs);
    }

    @Override
    public void addToPlaylist(MRL mediaLocator) {
        ypm.addToPlaylist(mediaLocator);
    }

    @Override
    public void addToPlaylist(List<MRL> mediaLocators) {
        ypm.addToPlaylist(mediaLocators);
    }

    @Override
    public void clearPlaylist() {
        ypm.clearPlaylist();
    }
    
    @Override
    public void removeFromPlaylist(int index) {
        ypm.removeFromPlaylist(index);
    }

    @Override
    public int getPlaylistSize() {
        if (impl != null) {
            return impl.getPlaylist().length();
        }
        return 0;
    }

    @Override
    public boolean isShuffleEnabled() {
        return ypm.isShuffleEnabled();
    }

    @Override
    public void play(int index) throws IndexOutOfBoundsException {
        if (impl != null) {
            impl.playVideoAt(index);
        }
    }

    @Override
    public void playNext() throws PlayException {
        if (impl != null) {
            impl.nextVideo();
        }
    }

    @Override
    public void playPrevious() throws PlayException {
        if (impl != null) {
            impl.previousVideo();
        }
    }

    @Override
    public void setShuffleEnabled(boolean enable) {
        if (impl != null) {
            ypm.setShuffleEnabled(enable);
            impl.setShuffle(enable);
        }
    }
*/
    private enum URLParameters {

        rel, autoplay, loop, enablejsapi, playerapiid, disablekb, egm, border,
        color1, color2, start, fs, hd, showsearch, showinfo, iv_load_policy, autohide,
        controls, origin, playlist, modestbranding
        //cc_load_policy
    }
}
