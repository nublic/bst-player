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

import com.bramosystems.oss.player.core.event.client.LoadingProgressEvent;
import com.bramosystems.oss.player.core.event.client.VolumeChangeHandler;
import com.bramosystems.oss.player.core.event.client.LoadingProgressHandler;
import com.bramosystems.oss.player.core.event.client.PlayStateHandler;
import com.bramosystems.oss.player.core.event.client.PlayerStateHandler;
import com.bramosystems.oss.player.core.event.client.PlayStateEvent;
import com.bramosystems.oss.player.core.event.client.PlayerStateEvent;
import com.bramosystems.oss.player.core.event.client.SeekChangeHandler;
import com.bramosystems.oss.player.core.event.client.VolumeChangeEvent;
import com.bramosystems.oss.player.core.event.client.DebugEvent;
import com.bramosystems.oss.player.core.event.client.SeekChangeEvent;
import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.bramosystems.oss.player.core.client.PlayException;
import com.bramosystems.oss.player.core.client.AbstractMediaPlayer;
import com.bramosystems.oss.player.core.client.PlaylistSupport;
import com.bramosystems.oss.player.core.client.skin.flat.FlatImagePack;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;

/**
 * Custom player control widget.
 *
 * <h4>CSS Styles</h4>
 * <code><pre>
 * .player-CustomPlayerControl { the player control }
 * .player-CustomPlayerControl-seekbar { the seekbar of the control }
 * .player-CustomPlayerControl-seekbar .loading { the seekbars' loading progress indicator }
 * .player-CustomPlayerControl-seekbar .playing { the seekbars' playing progress indicator }
 * .player-CustomPlayerControl-volumeControl { the volume controls' slider widget }
 * .player-CustomPlayerControl-volumeControl .volume { the volume level indicator }
 * .player-CustomPlayerControl-volumeControl .track  { the volume sliders' track indicator }
 * </pre></code>
 *
 * @author Sikirulai Braheem
 */
public class CustomPlayerControl extends Composite {

    private final String STYLE_NAME = "player-CustomPlayerControl";
    private ButtonBase play, stop, prev, next;
    private Timer playTimer;
    private CSSSeekBar seekbar;
    private Label timeLabel;
    private AbstractMediaPlayer player;
    private PlayState playState;
    private VolumeControl vc;

    /**
     * Contructs CustomPlayerControl object.
     *
     * @param player the player object to control
     */
    public CustomPlayerControl(AbstractMediaPlayer player) {
        this(player, ((FlatImagePack) GWT.create(FlatImagePack.class)).css());
    }

    /**
     * Constructs CustomPlayerControl to control the specified player using the
     * specified CSS resource bundle for the control buttons.
     *
     * @param player the player object to control
     * @param cssResource the CSS style bundle for the control buttons
     * @since 1.2
     */
    public CustomPlayerControl(AbstractMediaPlayer player, UIStyleResource cssResource) {
        this.player = player;
        playState = PlayState.Stop;

        initWidget(getPlayerWidget(cssResource));
        setSize("100%", "20px");
        setStylePrimaryName(STYLE_NAME);
    }

    /**
     * Constructs CustomPlayerControl to control the specified player using the
     * specified image icons for the control buttons.
     *
     * @param player the player object to control
     * @param imagePack the control button icons
     * @deprecated As of version 1.2. Will be removed in a future version
     */
    public CustomPlayerControl(AbstractMediaPlayer player, ImagePack imagePack) {
        this.player = player;
        playState = PlayState.Stop;

        initWidget(getPlayerWidget(imagePack));
        setSize("100%", "20px");
        setStylePrimaryName(STYLE_NAME);
    }

    /**
     * Sets the primary CSS style name of this widget as well as the seekbar and
     * the volume controls' slider widgets.
     *
     * @param style the CSS style name
     */
    @Override
    public void setStylePrimaryName(String style) {
        super.setStylePrimaryName(style);
        seekbar.setStylePrimaryName(style);
        vc.setPopupStyleName(style + "-volumeControl");
    }

    /**
     * Sets the CSS style name of this widget as well as the seekbar and
     * the volume controls' slider widgets.
     *
     * @param style the CSS style name
     */
    @Override
    public void setStyleName(String style) {
        super.setStyleName(style);
        seekbar.setStylePrimaryName(style);
        vc.setPopupStyleName(style + "-volumeControl");
    }

    /**
     * Overriden to release resources.
     */
    @Override
    protected void onUnload() {
        playTimer.cancel();
    }

    private void setTime(long time, long duration) {
        timeLabel.setText(PlayerUtil.formatMediaTime(time)
                + " / " + PlayerUtil.formatMediaTime(duration));
    }

    private void toPlayState(PlayState state, UIStyleResource imgPack) {
        switch (state) {
            case Playing:
                playTimer.scheduleRepeating(1000);
                vc.setVolume(player.getVolume());

                stop.setEnabled(true);
                play.setEnabled(true);
                play.setStylePrimaryName(imgPack.pause());
                break;
            case Stop:
                stop.setEnabled(false);
                seekbar.setPlayingProgress(0);
                setTime(0, player.getMediaDuration());
                playTimer.cancel();
                next.setEnabled(false);
                prev.setEnabled(false);
            case Pause:
                play.setStylePrimaryName(imgPack.play());
                play.setEnabled(true);
                break;
        }
        playState = state;
    }

    private void updateSeekState() {
        double pos = player.getPlayPosition();
        long duration = player.getMediaDuration();
        setTime((long) pos, duration);
        seekbar.setPlayingProgress(duration > 0 ? pos / duration : 0);
    }

    private enum PlayState {

        Playing, Pause, Stop;
    }

    private Widget getPlayerWidget(final UIStyleResource imgPack) {
        imgPack.ensureInjected();

        seekbar = new CSSSeekBar(5);
        seekbar.setStylePrimaryName(STYLE_NAME);
        seekbar.addStyleDependentName("seekbar");
        seekbar.setWidth("95%");
        seekbar.addSeekChangeHandler(new SeekChangeHandler() {

            @Override
            public void onSeekChanged(SeekChangeEvent event) {
                player.setPlayPosition(event.getSeekPosition() * player.getMediaDuration());
            }
        });

        playTimer = new Timer() {

            @Override
            public void run() {
                updateSeekState();
            }
        };

        play = new CPCButton(imgPack.playDisabled(), new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                switch (playState) {
                    case Stop:
                    case Pause:
                        try { // play media...
                            play.setEnabled(false); // avoid multiple clicks
                            player.playMedia();
                        } catch (PlayException ex) {
                            DebugEvent.fire(player, DebugEvent.MessageType.Error, ex.getMessage());
                        }
                        break;
                    case Playing:
                        player.pauseMedia();
                }
            }
        });
        play.setStyleName(imgPack.play());
        play.setEnabled(false);

        stop = new CPCButton(imgPack.stopDisabled(), new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                player.stopMedia();
            }
        });
        stop.setStyleName(imgPack.stop());
        stop.setEnabled(false);

        prev = new CPCButton(imgPack.prevDisabled(), new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (player instanceof PlaylistSupport) {
                    try {
                        ((PlaylistSupport) player).playPrevious();
                    } catch (PlayException ex) {
                        next.setEnabled(false);
                        DebugEvent.fire(player, DebugEvent.MessageType.Info, ex.getMessage());
                    }
                }
            }
        });
        prev.setStyleName(imgPack.prev());
        prev.setEnabled(false);

        next = new CPCButton(imgPack.nextDisabled(), new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (player instanceof PlaylistSupport) {
                    try {
                        ((PlaylistSupport) player).playNext();
                    } catch (PlayException ex) {
                        next.setEnabled(false);
                        DebugEvent.fire(player, DebugEvent.MessageType.Info, ex.getMessage());
                    }
                }
            }
        });
        next.setStyleName(imgPack.next());
        next.setEnabled(false);

        vc = new VolumeControl(5);
        vc.setStyleName(imgPack.volume());
        vc.setPopupStyleName(STYLE_NAME + "-volumeControl");
        vc.addVolumeChangeHandler(new VolumeChangeHandler() {

            @Override
            public void onVolumeChanged(VolumeChangeEvent event) {
                player.setVolume(event.getNewVolume());
            }
        });

        player.addLoadingProgressHandler(new LoadingProgressHandler() {

            @Override
            public void onLoadingProgress(LoadingProgressEvent event) {
                seekbar.setLoadingProgress(event.getProgress());
                vc.setVolume(player.getVolume());
                updateSeekState();
            }
        });
        player.addPlayStateHandler(new PlayStateHandler() {

            @Override
            public void onPlayStateChanged(PlayStateEvent event) {
                int index = event.getItemIndex();
                switch (event.getPlayState()) {
                    case Paused:
                        toPlayState(PlayState.Pause, imgPack);
                        next.setEnabled(index < (((PlaylistSupport) player).getPlaylistSize() - 1));
                        prev.setEnabled(index > 0);
                        break;
                    case Started:
                        toPlayState(PlayState.Playing, imgPack);
                        next.setEnabled(index < (((PlaylistSupport) player).getPlaylistSize() - 1));
                        prev.setEnabled(index > 0);
                        break;
                    case Stopped:
                    case Finished:
                        toPlayState(PlayState.Stop, imgPack);
                        next.setEnabled(false);
                        prev.setEnabled(false);
                        break;
                }
            }
        });
        player.addPlayerStateHandler(new PlayerStateHandler() {

            @Override
            public void onPlayerStateChanged(PlayerStateEvent event) {
                switch (event.getPlayerState()) {
                    case Ready:
                        play.setEnabled(true);
                        vc.setVolume(player.getVolume());
                }
            }
        });

        // Time label..
        timeLabel = new Label("--:-- / --:--");
        timeLabel.setWordWrap(false);
        timeLabel.setHorizontalAlignment(Label.ALIGN_CENTER);

        // build the UI...
        DockPanel face = new DockPanel();
        face.setStyleName("");
        face.setVerticalAlignment(DockPanel.ALIGN_MIDDLE);
        face.setSpacing(3);
        face.add(vc, DockPanel.WEST);
        face.add(play, DockPanel.WEST);
        face.add(stop, DockPanel.WEST);
        face.add(prev, DockPanel.WEST);
        face.add(next, DockPanel.WEST);
        face.add(timeLabel, DockPanel.EAST);
        face.add(seekbar, DockPanel.CENTER);

        face.setCellWidth(seekbar, "100%");
        face.setCellHorizontalAlignment(seekbar, DockPanel.ALIGN_CENTER);
        return face;
    }

    private class CPCButton extends Button {

        private String disabledStyle = "disabled";

        public CPCButton(String disabledStyleName, ClickHandler handler) {
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

    /**
     * Defines the CSS class names used by the {@linkplain CustomPlayerControl} widget.
     * These class names should be defined in a stylesheet and linked as required by
     * GWT's ClientBundle
     *
     * @since 1.2
     * @author Sikiru Braheem
     */
    public static interface UIStyleResource extends CssResource {

        public String play();

        public String playDisabled();

        public String pause();

        public String stop();

        public String stopDisabled();

        public String next();

        public String nextDisabled();

        public String prev();

        public String prevDisabled();

        public String volume();
    }

    /**
     * ImageBundle definition for the CustomPlayerControl class
     *
     * @deprecated As of version 1.2, replaced with {@link UIStyleResource}.
     * Will be removed in a future release.
     */
    public interface ImagePack extends ImageBundle {

        @Resource("com/bramosystems/oss/player/core/client/skin/flat/pause.png")
        public AbstractImagePrototype pause();

        @Resource("com/bramosystems/oss/player/core/client/skin/flat/pauseHover.png")
        public AbstractImagePrototype pauseHover();

        @Resource("com/bramosystems/oss/player/core/client/skin/flat/play.png")
        public AbstractImagePrototype play();

        @Resource("com/bramosystems/oss/player/core/client/skin/flat/playHover.png")
        public AbstractImagePrototype playHover();

        @Resource("com/bramosystems/oss/player/core/client/skin/flat/playDisabled.png")
        public AbstractImagePrototype playDisabled();

        @Resource("com/bramosystems/oss/player/core/client/skin/flat/stop.png")
        public AbstractImagePrototype stop();

        @Resource("com/bramosystems/oss/player/core/client/skin/flat/stopDisabled.png")
        public AbstractImagePrototype stopDisabled();

        @Resource("com/bramosystems/oss/player/core/client/skin/flat/stopHover.png")
        public AbstractImagePrototype stopHover();

        @Resource("com/bramosystems/oss/player/core/client/skin/flat/volume.png")
        public AbstractImagePrototype spk();

        @Resource("com/bramosystems/oss/player/core/client/skin/flat/prev.png")
        public AbstractImagePrototype prev();

        @Resource("com/bramosystems/oss/player/core/client/skin/flat/prevDisabled.png")
        public AbstractImagePrototype prevDisabled();

        @Resource("com/bramosystems/oss/player/core/client/skin/flat/prevHover.png")
        public AbstractImagePrototype prevHover();

        @Resource("com/bramosystems/oss/player/core/client/skin/flat/next.png")
        public AbstractImagePrototype next();

        @Resource("com/bramosystems/oss/player/core/client/skin/flat/nextDisabled.png")
        public AbstractImagePrototype nextDisabled();

        @Resource("com/bramosystems/oss/player/core/client/skin/flat/nextHover.png")
        public AbstractImagePrototype nextHover();
    }

    /**
     * Will be removed in a future version
     * @param imgPack
     * @return
     */
    private Widget getPlayerWidget(final ImagePack imgPack) {
        seekbar = new CSSSeekBar(5);
        seekbar.setStylePrimaryName(STYLE_NAME);
        seekbar.addStyleDependentName("seekbar");
        seekbar.setWidth("95%");
        seekbar.addSeekChangeHandler(new SeekChangeHandler() {

            @Override
            public void onSeekChanged(SeekChangeEvent event) {
                player.setPlayPosition(event.getSeekPosition() * player.getMediaDuration());
            }
        });

        playTimer = new Timer() {

            @Override
            public void run() {
                updateSeekState();
            }
        };

        play = new PushButton(imgPack.play().createImage(), new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                switch (playState) {
                    case Stop:
                    case Pause:
                        try { // play media...
                            play.setEnabled(false); // avoid multiple clicks
                            player.playMedia();
                        } catch (PlayException ex) {
                            DebugEvent.fire(player, DebugEvent.MessageType.Error, ex.getMessage());
                        }
                        break;
                    case Playing:
                        player.pauseMedia();
                }
            }
        });
        ((PushButton)play).getUpDisabledFace().setImage(imgPack.playDisabled().createImage());
        play.setStyleName("");
        play.setEnabled(false);

        stop = new PushButton(imgPack.stop().createImage(), new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                player.stopMedia();
            }
        });
        ((PushButton)stop).getUpDisabledFace().setImage(imgPack.stopDisabled().createImage());
        ((PushButton)stop).getUpHoveringFace().setImage(imgPack.stopHover().createImage());
        stop.setStyleName("");
        stop.setEnabled(false);

        prev = new PushButton(imgPack.prev().createImage(), new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (player instanceof PlaylistSupport) {
                    try {
                        ((PlaylistSupport) player).playPrevious();
                    } catch (PlayException ex) {
                        next.setEnabled(false);
                        DebugEvent.fire(player, DebugEvent.MessageType.Info, ex.getMessage());
                    }
                }
            }
        });
        ((PushButton)prev).getUpDisabledFace().setImage(imgPack.prevDisabled().createImage());
        ((PushButton)prev).getUpHoveringFace().setImage(imgPack.prevHover().createImage());
        prev.setStyleName("");
        prev.setEnabled(false);

        next = new PushButton(imgPack.next().createImage(), new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (player instanceof PlaylistSupport) {
                    try {
                        ((PlaylistSupport) player).playNext();
                    } catch (PlayException ex) {
                        next.setEnabled(false);
                        DebugEvent.fire(player, DebugEvent.MessageType.Info, ex.getMessage());
                    }
                }
            }
        });
        ((PushButton)next).getUpDisabledFace().setImage(imgPack.nextDisabled().createImage());
        ((PushButton)next).getUpHoveringFace().setImage(imgPack.nextHover().createImage());
        next.setStyleName("");
        next.setEnabled(false);

        vc = new VolumeControl(imgPack.spk().createImage(), 5);
        vc.setPopupStyleName(STYLE_NAME + "-volumeControl");
        vc.addVolumeChangeHandler(new VolumeChangeHandler() {

            @Override
            public void onVolumeChanged(VolumeChangeEvent event) {
                player.setVolume(event.getNewVolume());
            }
        });

        player.addLoadingProgressHandler(new LoadingProgressHandler() {

            @Override
            public void onLoadingProgress(LoadingProgressEvent event) {
                seekbar.setLoadingProgress(event.getProgress());
                vc.setVolume(player.getVolume());
                updateSeekState();
            }
        });
        player.addPlayStateHandler(new PlayStateHandler() {

            @Override
            public void onPlayStateChanged(PlayStateEvent event) {
                int index = event.getItemIndex();
                switch (event.getPlayState()) {
                    case Finished:
                        toPlayState(PlayState.Stop, imgPack);
                        next.setEnabled(index >= 1);
                        prev.setEnabled(index >= 1);
                        break;
                    case Paused:
                        toPlayState(PlayState.Pause, imgPack);
                        break;
                    case Started:
                        toPlayState(PlayState.Playing, imgPack);
                        next.setEnabled(index < (((PlaylistSupport) player).getPlaylistSize() - 1));
                        prev.setEnabled(index > 0);
                        break;
                    case Stopped:
                        toPlayState(PlayState.Stop, imgPack);
                }
            }
        });
        player.addPlayerStateHandler(new PlayerStateHandler() {

            @Override
            public void onPlayerStateChanged(PlayerStateEvent event) {
                switch (event.getPlayerState()) {
                    case Ready:
                        play.setEnabled(true);
                        vc.setVolume(player.getVolume());
                }
            }
        });

        // Time label..
        timeLabel = new Label("--:-- / --:--");
        timeLabel.setWordWrap(false);
        timeLabel.setHorizontalAlignment(Label.ALIGN_CENTER);

        // build the UI...
        DockPanel face = new DockPanel();
        face.setStyleName("");
        face.setVerticalAlignment(DockPanel.ALIGN_MIDDLE);
        face.setSpacing(3);
        face.add(vc, DockPanel.WEST);
        face.add(play, DockPanel.WEST);
        face.add(stop, DockPanel.WEST);
        face.add(prev, DockPanel.WEST);
        face.add(next, DockPanel.WEST);
        face.add(timeLabel, DockPanel.EAST);
        face.add(seekbar, DockPanel.CENTER);

        face.setCellWidth(seekbar, "100%");
        face.setCellHorizontalAlignment(seekbar, DockPanel.ALIGN_CENTER);
        return face;
    }

    /**
     * Provided for backward compatibility.  Will be removed in a future version
     * @param state
     * @param imgPack
     */
    private void toPlayState(PlayState state, ImagePack imgPack) {
        switch (state) {
            case Playing:
                playTimer.scheduleRepeating(1000);
                vc.setVolume(player.getVolume());

                stop.setEnabled(true);
                play.setEnabled(true);
                ((PushButton)play).getUpFace().setImage(imgPack.pause().createImage());
                ((PushButton)play).getUpHoveringFace().setImage(imgPack.pauseHover().createImage());
                break;
            case Stop:
                stop.setEnabled(false);
                seekbar.setPlayingProgress(0);
                setTime(0, player.getMediaDuration());
                playTimer.cancel();
                next.setEnabled(false);
                prev.setEnabled(false);
            case Pause:
                ((PushButton)play).getUpFace().setImage(imgPack.play().createImage());
                ((PushButton)play).getUpHoveringFace().setImage(imgPack.playHover().createImage());
                play.setEnabled(true);
                break;
        }
        playState = state;
    }
}
