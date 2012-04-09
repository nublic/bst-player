/*
 *  Copyright 2010 Sikiru.
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
package com.bramosystems.oss.player.showcase.client;

import com.bramosystems.oss.player.capsule.client.Capsule;
import com.bramosystems.oss.player.core.client.AbstractMediaPlayer;
import com.bramosystems.oss.player.core.client.ConfigParameter;
import com.bramosystems.oss.player.core.client.LoadException;
import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.bramosystems.oss.player.core.client.PlaylistSupport;
import com.bramosystems.oss.player.core.client.Plugin;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.bramosystems.oss.player.core.client.skin.CustomPlayerControl;
import com.bramosystems.oss.player.core.client.ui.NativePlayer;
import com.bramosystems.oss.player.core.event.client.DebugEvent;
import com.bramosystems.oss.player.core.event.client.DebugHandler;
import com.bramosystems.oss.player.core.event.client.HasMediaMessageHandlers;
import com.bramosystems.oss.player.core.event.client.MediaInfoHandler;
import com.bramosystems.oss.player.flat.client.FlatVideoPlayer;
import com.bramosystems.oss.player.showcase.client.event.PlayerOptionsChangeEvent;
import com.bramosystems.oss.player.showcase.client.event.PlayerOptionsChangeHandler;
import com.bramosystems.oss.player.showcase.client.event.PlaylistChangeEvent;
import com.bramosystems.oss.player.showcase.client.event.PlaylistChangeHandler;
import com.bramosystems.oss.player.showcase.client.event.PluginChangeEvent;
import com.bramosystems.oss.player.showcase.client.event.PluginChangeHandler;
import com.bramosystems.oss.player.showcase.client.res.Bundle;
import com.bramosystems.oss.player.youtube.client.ChromelessPlayer;
import com.bramosystems.oss.player.youtube.client.YouTubePlayer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ExternalTextResource;
import com.google.gwt.resources.client.ResourceCallback;
import com.google.gwt.resources.client.ResourceException;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.*;
import java.util.ArrayList;

/**
 *
 * @author Sikiru
 */
public class StageController extends Composite implements ValueChangeHandler<String>, PluginChangeHandler,
        PlayerOptionsChangeHandler, PlaylistChangeHandler, HasMediaMessageHandlers {

    private final String LOG_SEPARATOR = "---------------------------------", PLAYER_WIDTH = "100%", PLAYER_HEIGHT = "300px";
    private PlayerOptions playerOptions;
    private AbstractMediaPlayer player;
    private BrowserInfo info;
    private HTML docPane;
    private AppOptions loadedOption;
    private Plugin plugin;
    private MatrixStagePane matrix;

    @SuppressWarnings("LeakingThisInConstructor")
    public StageController() {
        initWidget(sb.createAndBindUi(this));
        info = new BrowserInfo();
        docPane = new HTML();
        loadedOption = AppOptions.home;
        plugin = Plugin.Auto;
        playerOptions = PlayerOptionsPane.singleton.getOptions();
    }

    @Override
    public HandlerRegistration addDebugHandler(DebugHandler handler) {
        return addHandler(handler, DebugEvent.TYPE);
    }

    @Override
    public HandlerRegistration addMediaInfoHandler(MediaInfoHandler handler) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onPluginChanged(PluginChangeEvent pevt) {
        plugin = pevt.getPlugin();
        switch (loadedOption) {
            case core: // load player again ...
            case capsule:
            case flat:
                loadPlayer();
                break;
            case matrix:
                loadMatrix();
                break;
            case ytube:
                loadUTube();
        }
    }

    @Override
    public void onPlayerOptionsChanged(PlayerOptionsChangeEvent pevt) {
        playerOptions = pevt.getPlayerOptions();
        switch (loadedOption) {
            case core:
            case capsule:
            case flat:
                if (pevt.isForceReloadPlayer()) {
                    loadPlayer();
                } else {
                    player.setControllerVisible(playerOptions.isShowControls());
                    player.setResizeToVideoSize(playerOptions.isResizeToVideo());
                    player.showLogger(playerOptions.isShowLogger());
                    player.setRepeatMode(playerOptions.getRepeatMode());
                    ((PlaylistSupport) player).setShuffleEnabled(playerOptions.isShuffleOn());
                }
                break;
            case ytube:
                if (pevt.isForceReloadPlayer()) {
                    loadUTube();
                } else {
                    player.setControllerVisible(playerOptions.isShowControls());
                    player.setResizeToVideoSize(playerOptions.isResizeToVideo());
                    player.showLogger(playerOptions.isShowLogger());
                }
        }
    }

    @Override
    public void onPlaylistChanged(PlaylistChangeEvent pevt) {
        switch (loadedOption) {
            case core:
            case capsule:
            case flat:
                if (pevt.isAdded()) {
                    if (player instanceof PlaylistSupport) {
                        ((PlaylistSupport) player).addToPlaylist(pevt.getPlaylistItem().get(0));
                    }
                } else {
                    if (player instanceof PlaylistSupport) {
                        ((PlaylistSupport) player).removeFromPlaylist(pevt.getIndex());
                    }
                }
        }
    }

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        loadedOption = AppOptions.home;
        try {
            loadedOption = AppOptions.valueOf(event.getValue());
        } catch (Exception e) {
        }

        switch (loadedOption) {
            case home:
                loadDoc(Bundle.bundle.home());
                break;
            case notices:
                loadDoc(Bundle.bundle.notices());
                break;
            case plugins:
            case mimes:
                loadInfo();
                break;
            case core:
            case capsule:
            case flat:
                loadPlayer();
                break;
            case matrix:
                loadMatrix();
                break;
            case ytube:
                loadUTube();
        }
        title.setText(loadedOption.toString());
    }

    private void loadPlayer() {
        try {
            panel.setWidget(null);
            DebugEvent.fire(this, DebugEvent.MessageType.Info, LOG_SEPARATOR);

            ArrayList<MRL> playlist = PlaylistPane.singleton.getEntries();
            switch (loadedOption) {
                case capsule:
                    DebugEvent.fire(this, DebugEvent.MessageType.Info, "Loading Capsule with : " + plugin);
                    DebugEvent.fire(this, DebugEvent.MessageType.Info, LOG_SEPARATOR);
                    player = new Capsule(plugin, playlist.get(0).get(0), false);
                    player.addStyleName(Bundle.bundle.css().capsule());
                    player.setWidth(PLAYER_WIDTH);
                    break;
                case core:
                    DebugEvent.fire(this, DebugEvent.MessageType.Info, "Loading Core player : " + plugin);
                    DebugEvent.fire(this, DebugEvent.MessageType.Info, LOG_SEPARATOR);
                    player = PlayerUtil.getPlayer(plugin, playlist.get(0).get(0), false, PLAYER_HEIGHT, PLAYER_WIDTH);
                    break;
                case flat:
                    DebugEvent.fire(this, DebugEvent.MessageType.Info, "Loading FlatVideoPlayer : " + plugin);
                    DebugEvent.fire(this, DebugEvent.MessageType.Info, LOG_SEPARATOR);
                    player = new FlatVideoPlayer(plugin, playlist.get(0).get(0), false, PLAYER_HEIGHT, PLAYER_WIDTH);
            }
            player.setControllerVisible(playerOptions.isShowControls());
            player.setResizeToVideoSize(playerOptions.isResizeToVideo());
            player.showLogger(playerOptions.isShowLogger());
            player.setConfigParameter(ConfigParameter.BackgroundColor, "#ffffff");
            player.setRepeatMode(playerOptions.getRepeatMode());
            ((PlaylistSupport) player).setShuffleEnabled(playerOptions.isShuffleOn());
            player.addDebugHandler(new DebugHandler() {

                @Override
                public void onDebug(DebugEvent event) {
                    fireEvent(event);
                }
            });

            if (playlist.size() > 1) {
                for (int i = 1; i < playlist.size(); i++) {
                    switch (plugin) {
                        case Native:
                            if (player instanceof NativePlayer) {
                                ((NativePlayer) player).addToPlaylist(playlist.get(i).toArray(new String[0]));
                            } else {
                                ((PlaylistSupport) player).addToPlaylist(playlist.get(i).get(0));
                            }
                            break;
                        default:
                            ((PlaylistSupport) player).addToPlaylist(playlist.get(i).get(0));
                    }
                }
            }

            title.setText(loadedOption.toString() + " - " + plugin);
            panel.setWidget(player);
        } catch (LoadException ex) {
        } catch (PluginVersionException ex) {
            panel.setWidget(PlayerUtil.getMissingPluginNotice(ex.getPlugin(), ex.getRequiredVersion()));
        } catch (PluginNotFoundException ex) {
            panel.setWidget(PlayerUtil.getMissingPluginNotice(ex.getPlugin()));
        }
    }

    private void loadDoc(ExternalTextResource res) {
        panel.setWidget(docPane);
        try {
            res.getText(new ResourceCallback<TextResource>() {

                @Override
                public void onError(ResourceException e) {
                    docPane.setHTML("<h2>Resource loading failed!</h2><br/>"
                            + e.getMessage());
                }

                @Override
                public void onSuccess(TextResource resource) {
                    docPane.setHTML(resource.getText());
                }
            });
        } catch (ResourceException ex) {
            docPane.setHTML("<h2>Resource loading failed!</h2><br/>"
                    + ex.getMessage());
        }
    }

    private void loadInfo() {
        panel.setWidget(info);
        info.update(loadedOption);
    }

    private void loadMatrix() {
        try {
            panel.setWidget(null);
            DebugEvent.fire(this, DebugEvent.MessageType.Info, LOG_SEPARATOR);
            DebugEvent.fire(this, DebugEvent.MessageType.Info, "Loading MatrixSupport : " + plugin);
            DebugEvent.fire(this, DebugEvent.MessageType.Info, LOG_SEPARATOR);
            matrix = new MatrixStagePane(plugin, PLAYER_WIDTH, PLAYER_HEIGHT);
            matrix.addDebugHandler(new DebugHandler() {

                @Override
                public void onDebug(DebugEvent event) {
                    fireEvent(event);
                }
            });
            panel.setWidget(matrix);
        } catch (LoadException ex) {
        } catch (PluginVersionException ex) {
            panel.setWidget(PlayerUtil.getMissingPluginNotice(ex.getPlugin(), ex.getRequiredVersion()));
        } catch (PluginNotFoundException ex) {
            panel.setWidget(PlayerUtil.getMissingPluginNotice(ex.getPlugin()));
        }
    }

    private void loadUTube() {
        try {
            panel.setWidget(null);
            DebugEvent.fire(this, DebugEvent.MessageType.Info, LOG_SEPARATOR);
            DebugEvent.fire(this, DebugEvent.MessageType.Info, "Loading YouTubePlayer");
            DebugEvent.fire(this, DebugEvent.MessageType.Info, LOG_SEPARATOR);

            ArrayList<MRL> playlist = PlaylistPane.singleton.getEntries();
            switch (plugin) {
                case Native:
                    player = new ChromelessPlayer(playlist.get(0).get(0), PLAYER_WIDTH, PLAYER_HEIGHT);

                    FlowPanel fp = new FlowPanel();
                    fp.add(player);
                    fp.add(new CustomPlayerControl(player));
                    panel.setWidget(fp);
                    break;
                default:
                    player = new YouTubePlayer(playlist.get(0).get(0), PLAYER_WIDTH, PLAYER_HEIGHT);
                    panel.setWidget(player);
            }

            player.addDebugHandler(new DebugHandler() {

                @Override
                public void onDebug(DebugEvent event) {
                    fireEvent(event);
                }
            });
        } catch (PluginVersionException ex) {
            panel.setWidget(PlayerUtil.getMissingPluginNotice(ex.getPlugin(), ex.getRequiredVersion()));
        } catch (PluginNotFoundException ex) {
            panel.setWidget(PlayerUtil.getMissingPluginNotice(ex.getPlugin()));
        }
    }
    @UiField
    Label title;
    @UiField
    SimplePanel panel;
    StageBinder sb = GWT.create(StageBinder.class);

    @UiTemplate("xml/Stage.ui.xml")
    interface StageBinder extends UiBinder<Widget, StageController> {
    }
}
