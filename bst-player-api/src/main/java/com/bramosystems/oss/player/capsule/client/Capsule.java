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
package com.bramosystems.oss.player.capsule.client;

import com.bramosystems.oss.player.capsule.client.skin.CapsuleResourcePack;
import com.bramosystems.oss.player.core.client.*;
import com.bramosystems.oss.player.core.client.skin.*;
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
import com.bramosystems.oss.player.core.event.client.SeekChangeEvent;
import com.bramosystems.oss.player.core.event.client.SeekChangeHandler;
import com.bramosystems.oss.player.core.event.client.VolumeChangeEvent;
import com.bramosystems.oss.player.core.event.client.VolumeChangeHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;
import java.util.ArrayList;

/**
 * Sample custom sound player.
 *
 * <h3>Usage Example</h3>
 *
 * <p>
 * <code><pre>
 * SimplePanel panel = new SimplePanel();   // create panel to hold the player
 * Widget player = null;
 * try {
 *      // create the player
 *      player = new Capsule("www.example.com/mediafile.wma");
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
public class Capsule extends CustomAudioPlayer {

    private CapsuleUIResource uiRes;
    private ProgressBar progress;
    private Button play, stop;
    private Timer playTimer, infoTimer;
    private PlayState playState;
    private VolumeControl vc;
    private MediaInfo mInfo;
    private ArrayList<MediaInfo.MediaInfoKey> mItems;
    private int infoIndex;

    /**
     * Constructs <code>Capsule</code> player to automatically playback the
     * media located at {@code mediaURL}.
     *
     * @param mediaURL the URL of the media to playback
     *
     * @throws LoadException if an error occurs while loading the media.
     * @throws PluginVersionException if the required plugin version is not installed on the client.
     * @throws PluginNotFoundException if the plugin is not installed on the client.
     */
    public Capsule(String mediaURL) throws PluginNotFoundException,
            PluginVersionException, LoadException {
        this(mediaURL, true);
    }

    /**
     * Constructs <code>Capsule</code> player to automatically playback the
     * media located at {@code mediaURL}, if {@code autoplay} is {@code true}.
     *
     * @param mediaURL the URL of the media to playback
     * @param autoplay {@code true} to start playing automatically, {@code false} otherwise
     *
     * @throws LoadException if an error occurs while loading the media.
     * @throws PluginVersionException if the required plugin version is not installed on the client.
     * @throws PluginNotFoundException if the plugin is not installed on the client.
     */
    public Capsule(String mediaURL, boolean autoplay) throws PluginNotFoundException,
            PluginVersionException, LoadException {
        this(Plugin.Auto, mediaURL, autoplay);
    }

    /**
     * Constructs <code>Capsule</code> player to automatically playback the
     * media located at {@code mediaURL}, if {@code autoplay} is {@code true} using
     * the specified {@code plugin}.
     *
     * <p>
     * This constructor uses the default {@link CapsuleResourcePack} to build the UI controls.
     * </p>
     *
     * @param plugin plugin to use for playback.
     * @param mediaURL the URL of the media to playback
     * @param autoplay {@code true} to start playing automatically, {@code false} otherwise
     *
     * @throws LoadException if an error occurs while loading the media.
     * @throws PluginVersionException if the required plugin version is not installed on the client.
     * @throws PluginNotFoundException if the plugin is not installed on the client.
     *
     * @see Plugin
     */
    public Capsule(Plugin plugin, String mediaURL, boolean autoplay) throws PluginNotFoundException,
            PluginVersionException, LoadException {
        this(plugin, mediaURL, autoplay,
                ((CapsuleResourcePack)GWT.create(CapsuleResourcePack.class)).uiResource());
    }

    /**
     * Constructs <code>Capsule</code> player to automatically playback the
     * media located at {@code mediaURL}, if {@code autoplay} is {@code true} using
     * the specified {@code plugin}.
     *
     * @param plugin plugin to use for playback.
     * @param mediaURL the URL of the media to playback
     * @param autoplay {@code true} to start playing automatically, {@code false} otherwise
     * @param uiResource the CSS resource to use for the UI
     *
     * @throws LoadException if an error occurs while loading the media.
     * @throws PluginVersionException if the required plugin version is not installed on the client.
     * @throws PluginNotFoundException if the plugin is not installed on the client.
     *
     * @see Plugin
     * @since 1.2
     */
    public Capsule(Plugin plugin, String mediaURL, boolean autoplay,
            CapsuleUIResource uiResource) throws PluginNotFoundException,
            PluginVersionException, LoadException {
        super(plugin, mediaURL, autoplay, "64px", "100%");
        uiRes = uiResource;
        uiRes.ensureInjected();
        playState = PlayState.Stop;
        mItems = new ArrayList<MediaInfo.MediaInfoKey>();

        progress = new ProgressBar();
        progress.setWidth("95%");

        playTimer = new Timer() {

            @Override
            public void run() {
                progress.setTime(getPlayPosition(), getMediaDuration());
            }
        };
        infoTimer = new Timer() {

            @Override
            public void run() {
                if (mItems.size() > 0) {
                    MediaInfo.MediaInfoKey item = mItems.get(infoIndex);
                    progress.setInfo(item.toString() + ": " + mInfo.getItem(item));
                    infoIndex++;
                    infoIndex %= mItems.size();
                } else {
                    cancel();
                }
            }

            @Override
            public void cancel() {
                super.cancel();
                progress.setInfo("");
            }
        };

        play = new CButton(uiRes.pauseDisabled(), new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                switch (playState) {
                    case Stop:
                    case Pause:
                        try { // play media...
                            play.setEnabled(false);
                            playMedia();
                        } catch (PlayException ex) {
                            fireError(ex.getMessage());
                        }
                        break;
                    case Playing:
                        pauseMedia();
                }
            }
        });
        play.setStyleName(uiRes.play());
        play.setEnabled(false);

        stop = new CButton(uiRes.stopDisabled(), new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                stopMedia();
            }
        });
        stop.setStyleName(uiRes.stop());
        stop.setEnabled(false);

        vc = new VolumeControl(5);
        vc.setStyleName(uiRes.volume());
        vc.addVolumeChangeHandler(new VolumeChangeHandler() {

            @Override
            public void onVolumeChanged(VolumeChangeEvent event) {
                setVolume(event.getNewVolume());
            }
        });
        vc.setPopupStyleName("player-Capsule-volumeControl");

        addDebugHandler(new DebugHandler() {

            @Override
            public void onDebug(DebugEvent event) {
                logger.log(event.getMessage(), false);
            }
        });
        addLoadingProgressHandler(new LoadingProgressHandler() {

            @Override
            public void onLoadingProgress(LoadingProgressEvent event) {
                double prog = event.getProgress();
                progress.setLoadingProgress(prog);
                if (prog == 1.0) {
                    progress.setTime(0, getMediaDuration());
                    vc.setVolume(getVolume());
                }
            }
        });
        addMediaInfoHandler(new MediaInfoHandler() {

            @Override
            public void onMediaInfoAvailable(MediaInfoEvent event) {
                mInfo = event.getMediaInfo();
                mItems = mInfo.getAvailableItems();
                mItems.remove(MediaInfo.MediaInfoKey.Comment);
                mItems.remove(MediaInfo.MediaInfoKey.Duration);
                mItems.remove(MediaInfo.MediaInfoKey.HardwareSoftwareRequirements);
                mItems.remove(MediaInfo.MediaInfoKey.VideoHeight);
                mItems.remove(MediaInfo.MediaInfoKey.VideoWidth);
                logger.log(mInfo.asHTMLString(), true);
            }
        });
        addPlayStateHandler(new PlayStateHandler() {

            @Override
            public void onPlayStateChanged(PlayStateEvent event) {
                switch (event.getPlayState()) {
                    case Stopped:
                    case Finished:
                        toPlayState(PlayState.Stop);
                        break;
                    case Paused:
                        toPlayState(PlayState.Pause);
                        break;
                    case Started:
                        toPlayState(PlayState.Playing);
                        break;
                }
            }
        });
        addPlayerStateHandler(new PlayerStateHandler() {

            @Override
            public void onPlayerStateChanged(PlayerStateEvent event) {
                switch (event.getPlayerState()) {
                    case BufferingFinished:
                    case BufferingStarted:
                        break;
                    case Ready:
                        play.setEnabled(true);
                        vc.setVolume(getVolume());
                }
            }
        });

        // build the UI...
        DockPanel main = new DockPanel();
        main.setStyleName("player-Capsule");
        main.setSpacing(0);
        main.setVerticalAlignment(DockPanel.ALIGN_MIDDLE);
        main.setSize("100%", "64px");

        main.add(new CEdge(uiRes.leftEdge()), DockPanel.WEST);
        main.add(play, DockPanel.WEST);
        main.add(stop, DockPanel.WEST);
        main.add(new CEdge(uiRes.rightEdge()), DockPanel.EAST);
        main.add(vc, DockPanel.EAST);
        main.add(progress, DockPanel.CENTER);
        main.setCellWidth(progress, "100%");
        main.setCellHorizontalAlignment(progress, DockPanel.ALIGN_CENTER);
        setPlayerControlWidget(main);
        setWidth("100%");
    }

    /**
     * Overridden to release resources.
     */
    @Override
    protected void onUnload() {
        playTimer.cancel();
        infoTimer.cancel();
    }

    private void toPlayState(PlayState state) {
        switch (state) {
            case Playing:
                play.setEnabled(true);
                stop.setEnabled(true);
                vc.setVolume(getVolume());
                playTimer.scheduleRepeating(1000);
                infoTimer.scheduleRepeating(3000);

                play.setStylePrimaryName(uiRes.pause());
                break;
            case Stop:
                progress.setTime(0, getMediaDuration());
                progress.setFinishedState();
                stop.setEnabled(false);
                playTimer.cancel();
                infoTimer.cancel();
            case Pause:
                play.setStylePrimaryName(uiRes.play());
                break;
        }
        playState = state;
    }

    private class ProgressBar extends Composite {

        private MediaSeekBar seekBar;
        private Label timeLabel, infoLabel;

        public ProgressBar() {
            timeLabel = new Label("--:-- / --:--");
            timeLabel.setStyleName("player-Capsule-info");
            timeLabel.setWordWrap(false);
            timeLabel.setHorizontalAlignment(Label.ALIGN_RIGHT);

            infoLabel = new Label();
            infoLabel.setStyleName("player-Capsule-info");
            infoLabel.setWordWrap(false);

            DockPanel dp = new DockPanel();
            dp.setVerticalAlignment(DockPanel.ALIGN_MIDDLE);
            dp.setWidth("100%");

            dp.add(timeLabel, DockPanel.EAST);
            dp.add(infoLabel, DockPanel.CENTER);

            seekBar = new CSSSeekBar(5);
            seekBar.setStylePrimaryName("player-Capsule-seekbar");
            seekBar.setWidth("100%");
            seekBar.addSeekChangeHandler(new SeekChangeHandler() {

                @Override
                public void onSeekChanged(SeekChangeEvent event) {
                    setPlayPosition(event.getSeekPosition() * getMediaDuration());
                }
            });

            VerticalPanel main = new VerticalPanel();
            main.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
            main.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
            main.setSpacing(2);
            main.setWidth("80%");
            main.add(dp);
            main.add(seekBar);
            initWidget(main);
        }

        public void setTime(double timeInMS, double duration) {
            timeLabel.setText(PlayerUtil.formatMediaTime((long) timeInMS) + " / "
                    + PlayerUtil.formatMediaTime((long) duration));
            seekBar.setPlayingProgress(timeInMS / duration);
        }

        public void setLoadingProgress(double progress) {
            seekBar.setLoadingProgress(progress);
        }

        public void setInfo(String info) {
            infoLabel.setText(info);
        }

        public void setFinishedState() {
            setTime(0, getMediaDuration());
            seekBar.setPlayingProgress(0);
        }
    }

    private enum PlayState {

        Playing, Pause, Stop;
    }

     /**
     * Defines the CSS class names used by the {@linkplain Capsule} widget.
     * These class names should be defined in a stylesheet and linked as required by
     * GWT's ClientBundle
     *
     * @since 1.2
     * @author Sikiru Braheem
     */
   public static interface CapsuleUIResource extends CssResource {

        public String leftEdge();

        public String rightEdge();

        public String pauseDisabled();

        public String pause();

        public String play();

        public String playDisabled();

        public String stop();

        public String stopDisabled();

        public String volume();
    }

    private class CButton extends Button {

        private String disabledStyle = "disabled";

        public CButton(String disabledStyleName, ClickHandler handler) {
            super();
            addClickHandler(handler);
            disabledStyle = disabledStyleName;
        }

        @Override
        public void setEnabled(boolean enabled) {
            super.setEnabled(enabled);
            if (enabled) {
                removeStyleName(disabledStyle);
            } else {
                addStyleName(disabledStyle);
            }
        }
    }

    private class CEdge extends Label {

        public CEdge(String styleName) {
            super();
            setStyleName(styleName);
        }

    }
}
