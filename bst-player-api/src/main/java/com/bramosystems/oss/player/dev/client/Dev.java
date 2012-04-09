/*
 * Copyright 2009 Sikirulai Braheem
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a footer of the License at
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

//import com.bramosystems.oss.player.capsule.client.Capsule;
import com.bramosystems.oss.player.core.client.playlist.MRL;
import com.bramosystems.oss.player.core.client.AbstractMediaPlayer;
import com.bramosystems.oss.player.core.client.ConfigParameter;
import com.bramosystems.oss.player.core.client.LoadException;
import com.bramosystems.oss.player.core.client.PlayerInfo;
import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.bramosystems.oss.player.core.client.PlaylistSupport;
import com.bramosystems.oss.player.core.client.Plugin;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.bramosystems.oss.player.core.client.ui.FlashMediaPlayer;
import com.bramosystems.oss.player.core.client.ui.QuickTimePlayer;
import com.bramosystems.oss.player.core.client.ui.WinMediaPlayer;
import com.bramosystems.oss.player.core.event.client.PlayStateEvent;
import com.bramosystems.oss.player.core.event.client.PlayStateHandler;
//import com.bramosystems.oss.player.flat.client.FlatVideoPlayer;
import com.bramosystems.oss.player.playlist.client.asx.ASXEntry;
import com.bramosystems.oss.player.playlist.client.asx.ASXPlaylist;
import com.bramosystems.oss.player.playlist.client.spf.SPFPlaylist;
import com.bramosystems.oss.player.playlist.client.spf.Track;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 * 
 * TODO: 2.x - Remove mediaURL from constructor methods, use with playlist
 * TODO: 2.x - Remove LoadException from constructor methods
 * TODO: Fix WMP display on Firefox
 */
public class Dev extends FlowPanel implements EntryPoint {

    private final String HEIGHT = "50px", WIDTH = "60%";
    private AbstractMediaPlayer mmp;
    private ArrayList<MRL> mrls;

    public Dev() {
//        setSpacing(20);
        setWidth("80%");

        GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {

            @Override
            public void onUncaughtException(Throwable e) {
                GWT.log(e.getMessage(), e);
                Window.alert("Dev Uncaught : " + e.getMessage());
            }
        });

        mrls = new ArrayList<MRL>();
        mrls.add(new MRL(GWT.getModuleBaseURL() + "applause.mp3"));
//        mrl.addURL(GWT.getModuleBaseURL() + "DSCF1780.AVI");
 //       mrls.add(new MRL(GWT.getModuleBaseURL() + "big-buck-bunny.mp4"));
//        mrls.add(new MRL(GWT.getModuleBaseURL() + "u2intro.mp4"));
//        mrls.add(new MRL(GWT.getModuleBaseURL() + "traffic.flv"));
//        mrls.add(new MRL(GWT.getModuleBaseURL() + "traffic.avi"));
//        mrl.addURL("applause.mp3");
    }

//TODO: test resizeToVideoSize feature for plugins ...
    @Override
    public void onModuleLoad() {
        //        RootPanel.get().add(new ScrollPanel(this));
        RootPanel.get().add(this);
        addPlayer(Plugin.WinMediaPlayer);

//                    add(new MimeStuffs());
//                addUTube();
        //               issueDialog();
//         add(pb.createAndBindUi(this));
/*
        try {
        RequestBuilder rb = new RequestBuilder(RequestBuilder.GET, GWT.getModuleBaseURL() + "jspf.json");
        //            RequestBuilder rb = new RequestBuilder(RequestBuilder.GET, GWT.getModuleBaseURL() + "xspf.xml");
        //           RequestBuilder rb = new RequestBuilder(RequestBuilder.GET, GWT.getModuleBaseURL() + "asx.xml");
        rb.sendRequest(null, new RequestCallback() {
        
        @Override
        public void onResponseReceived(Request request, Response response) {
        showPlaylist(PlaylistFactory.parseJspfPlaylist(response.getText()));
        //                    showPlaylist(SPFParser.parseAsxPlaylist(response.getText()));
        //                    showPlaylist(SPFParser.parseXspfPlaylist(response.getText()));               
        }
        
        @Override
        public void onError(Request request, Throwable exception) {
        }
        });
        } catch (RequestException ex) {
        GWT.log("error ", ex);
        }
         */
    }

    private void showPlaylist(SPFPlaylist pl) {
        add(new Label("<<<<<<<<<<<<<<<<<<<<<<<<<< Playlist >>>>>>>>>>>>>>>>>>>>>>>>>>>>>"));
        add(new Label(pl.getTitle()));
        add(new Label(pl.getInfo()));
        add(new Label(pl.getCreator()));

        JsArray<Track> ts = pl.getTracks();
        for (int i = 0; i < ts.length(); i++) {
            Track t = ts.get(i);
            add(new Label("<<<<<<<<<<<<<<<<<<< Track " + i + " >>>>>>>>>>>>>>>>>"));
            add(new Label(t.getTitle()));
            add(new Label(t.getCreator()));
            add(new Label(t.getAlbum()));
        }
    }

    private void showPlaylist(ASXPlaylist pl) {
        GWT.log("<<< Entries : " + pl.getEntries().size() + " >>>");

        add(new Label("<<<<<<<<<<<<<<<<<<<<<<<<<< Playlist >>>>>>>>>>>>>>>>>>>>>>>>>>>>>"));
        add(new Label(pl.getTitle()));
        add(new Label(pl.getAbstract()));
        add(new Label(pl.getAuthor()));

        Iterator<ASXEntry> ts = pl.getEntries().iterator();
        int i = 0;
        while (ts.hasNext()) {
            ASXEntry t = ts.next();
            add(new Label("<<<<<<<<<<<<<<<<<<< Track " + i++ + " >>>>>>>>>>>>>>>>>"));
            add(new Label(t.getTitle()));
            add(new Label(t.getAuthor()));
            add(new Label(t.getRefs().toString()));
        }
    }

    private void addPlayer(Plugin plugin) {
        /*
        add(new Button("Show", new ClickHandler() {
        
        @Override
        public void onClick(ClickEvent event) {
        mmp.setControllerVisible(!mmp.isControllerVisible());
        }
        }));
         */
        try {
            PlayerInfo pi = PlayerUtil.getPlayerInfo("core", plugin.name());
            mmp = PlayerUtil.getPlayer(pi, mrls.get(0).getNextResource(true), false, HEIGHT, WIDTH);
 //           mmp = new WinMediaPlayer(mrl.getNextResource(true), false, HEIGHT, WIDTH, WinMediaPlayer.EmbedMode.EMBED_ONLY);
 //                   mmp = new Capsule(Plugin.FlashPlayer, mrl.getNextResource(true), false);
            mmp.addPlayStateHandler(new PlayStateHandler() {

                @Override
                public void onPlayStateChanged(PlayStateEvent event) {
                    GWT.log("Index : " + event.getItemIndex() + " = " + event.getPlayState());
                }
            });
            mmp.showLogger(true);
            mmp.setConfigParameter(ConfigParameter.QTScale, QuickTimePlayer.Scale.Aspect);
            mmp.setConfigParameter(ConfigParameter.BackgroundColor, "#ffffff");
//            ((PlaylistSupport) mmp).addToPlaylist(mrls);
//            mmp.setResizeToVideoSize(true);
//            mmp.setLoopCount(2);
//            mmp.setRepeatMode(RepeatMode.REPEAT_ALL);
            ((PlaylistSupport) mmp).setShuffleEnabled(false);
            mmp.setControllerVisible(true);
            add(mmp);
            /*
            final Label lbl = new Label("MM - ");
            add(lbl);
            mmp.addMouseMoveHandler(new MouseMoveHandler() {
            
            @Override
            public void onMouseMove(MouseMoveEvent event) {
            lbl.setText("MM - " + event.getX() + ", " + event.getY());
            }
            });
             */
        } catch (LoadException ex) {
            add(new Label("Load Exception"));
        } catch (PluginNotFoundException ex) {
            add(PlayerUtil.getMissingPluginNotice(ex.getPlugin()));
            GWT.log("Missing plugin >>>>", ex);
        } catch (PluginVersionException ex) {
            add(PlayerUtil.getMissingPluginNotice(ex.getPlugin(), ex.getRequiredVersion()));
            GWT.log("Missing Plugin Version >>>>>", ex);
        }
    }
/*
    private void addUTube() {
        try {
            YouTubePlayer pl = new YouTubePlayer("http://www.youtube.com/v/MrsGEz4NtBk", "100%", "350px");
//            pl.addToPlaylist("u1zgFlCw8Aw");
            pl.showLogger(true);
            add(pl);
        } catch (PluginVersionException ex) {
            add(PlayerUtil.getMissingPluginNotice(ex.getPlugin(), ex.getRequiredVersion()));
        } catch (PluginNotFoundException ex) {
            add(PlayerUtil.getMissingPluginNotice(ex.getPlugin()));
        }
    }
*/
    private String getURL(String path) {
        return Location.createUrlBuilder().setPort(8080).setPath(path).buildString();
    }

    @UiTemplate("Player.ui.xml")
    interface PlayerBinder extends UiBinder<Widget, Dev> {
    }
    PlayerBinder pb = GWT.create(PlayerBinder.class);

    public void issue32() {
        // GWT.getModuleBaseURL() + "applause.mp3";
        final String fileUrl = GWT.getModuleBaseURL() + "big-buck-bunny.mp4";
        AbstractMediaPlayer player;
        Widget mp = null;
        try {
            player = new FlashMediaPlayer(fileUrl, true, "464px", "620px");
            player.setResizeToVideoSize(true);
            player.showLogger(true);
            mp = player;
        } catch (PluginNotFoundException e) {
            mp = PlayerUtil.getMissingPluginNotice(Plugin.FlashPlayer, e.getMessage());
        } catch (PluginVersionException e) {
            mp = PlayerUtil.getMissingPluginNotice(Plugin.FlashPlayer, e.getRequiredVersion());
        } catch (LoadException e) {
            mp = PlayerUtil.getMissingPluginNotice(Plugin.FlashPlayer);
        }
        add(mp);
    }

    public void issueDialog() {
        final DialogBox panel = new DialogBox(false, true);
        panel.setSize("700px", "500px");
        AbstractMediaPlayer player;
        Widget mp = null;
        try {
            player = new WinMediaPlayer("", true, "464px", "620px");
            //           player.setResizeToVideoSize(false);
            mp = player;
        } catch (PluginNotFoundException e) {
            mp = PlayerUtil.getMissingPluginNotice(e.getPlugin(), e.getMessage());
        } catch (PluginVersionException e) {
            mp = PlayerUtil.getMissingPluginNotice(e.getPlugin(), e.getRequiredVersion());
        } catch (LoadException e) {
            mp = PlayerUtil.getMissingPluginNotice(Plugin.FlashPlayer);
        }

        FlowPanel fp = new FlowPanel();
        fp.add(new Button("close", new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                panel.hide();
            }
        }));
        fp.add(mp);
        panel.setWidget(fp);

        add(new Button("Show", new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                panel.center();
            }
        }));
    }
}
