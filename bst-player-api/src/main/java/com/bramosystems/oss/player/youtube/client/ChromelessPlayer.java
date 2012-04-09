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

import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.bramosystems.oss.player.core.client.spi.Player;
import com.bramosystems.oss.player.youtube.client.impl.YouTubePlayerProvider;

/**
 * Widget to embed the chromeless YouTube video player.  The player is particularly useful
 * when embedding YouTube with custom controls.
 *
 * <h3>Usage Example</h3>
 *
 * <p>
 * <code><pre>
 * SimplePanel panel = new SimplePanel();   // create panel to hold the player
 * Widget player = null;
 * try {
 *      // create the player
 *      player = new ChromelessPlayer("http://www.youtube.com/v/VIDEO_ID&fs=1", "100%", "350px");
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
@Player(name="Chromeless", minPluginVersion="9.0.0", providerFactory=YouTubePlayerProvider.class)
public class ChromelessPlayer extends YouTubePlayer {

    private String videoURL;
    private PlayerParameters params;

    /**
     * Constructs <code>ChromelessPlayer</code> with the specified {@code height} and
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
    public ChromelessPlayer(String videoURL, String width, String height)
            throws PluginNotFoundException, PluginVersionException {
        super(videoURL, new PlayerParameters(), width, height);
    }

    /**
     * Constructs <code>ChromelessPlayer</code> with the specified {@code height} and
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
    public ChromelessPlayer(String videoURL, PlayerParameters playerParameters, String width, String height)
            throws PluginNotFoundException, PluginVersionException {
        super(videoURL, playerParameters, width, height);
    }

    @Override
    protected String getNormalizedVideoAppURL(String videoURL, PlayerParameters playerParameters) {
        parseURLParams(videoURL, playerParameters);
        params = playerParameters;
        params.setPlayerAPIId(playerId);
        this.videoURL = videoURL;
        return "http://www.youtube.com/apiplayer?version=3&enablejsapi=1&playerapiid="
                + params.getPlayerAPIId();
    }

    @Override
    protected void playerInit() {
        if (params.isAutoplay()) {
            impl.loadVideoByUrl(videoURL, 0);
        } else {
            impl.cueVideoByUrl(videoURL, 0);
        }
    }

    /**
     * Checks whether the player controls are visible.  This implementation <b>always</b> return false.
     */
    @Override
    public boolean isControllerVisible() {
        return false;
    }
}
