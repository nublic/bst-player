/*
 * Copyright 2011 Sikirulai Braheem <sbraheem at bramosystems.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bramosystems.oss.player.youtube.client.impl;

import com.bramosystems.oss.player.core.client.AbstractMediaPlayer;
import com.bramosystems.oss.player.core.client.LoadException;
import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersion;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.bramosystems.oss.player.core.client.spi.PlayerElement;
import com.bramosystems.oss.player.core.client.spi.PlayerProvider;
import com.bramosystems.oss.player.core.client.spi.PlayerProviderFactory;
import com.bramosystems.oss.player.youtube.client.ChromelessPlayer;
import com.bramosystems.oss.player.youtube.client.PlayerParameters;
import com.bramosystems.oss.player.youtube.client.YouTubePlayer;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems.com>
 */
@PlayerProvider(YouTubePlayerProvider.PROVIDER_NAME)
public class YouTubePlayerProvider implements PlayerProviderFactory {

    public static final String PROVIDER_NAME = "yt";

    @Override
    public AbstractMediaPlayer getPlayer(String playerName, String mediaURL, boolean autoplay, String height, String width)
            throws LoadException, PluginNotFoundException, PluginVersionException {
        AbstractMediaPlayer player = null;
        PlayerParameters pp = new PlayerParameters();
        pp.setAutoplay(autoplay);

        if (playerName.equals("YouTube")) {
            player = new YouTubePlayer(mediaURL, pp, width, height);
        } else if (playerName.equals("Chromeless")) {
            player = new ChromelessPlayer(mediaURL, pp, width, height);
        } else {
            throw new IllegalArgumentException("Unknown player - '" + playerName + "'");
        }
        return player;
    }

    @Override
    public AbstractMediaPlayer getPlayer(String playerName, String mediaURL, boolean autoplay)
            throws LoadException, PluginNotFoundException, PluginVersionException {
        AbstractMediaPlayer player = null;
        PlayerParameters pp = new PlayerParameters();
        pp.setAutoplay(autoplay);
        String w = "100%", h = "350px";
        if (playerName.equals("YouTube")) {
            player = new YouTubePlayer(mediaURL, pp, w, h);
        } else if (playerName.equals("Chromeless")) {
            player = new ChromelessPlayer(mediaURL, pp, w, h);
        } else {
            throw new IllegalArgumentException("Unknown player - '" + playerName + "'");
        }
        return player;
    }

    @Override
    public PluginVersion getDetectedPluginVersion(String playerName) throws PluginNotFoundException {
        if (playerName.equals("YouTube") || playerName.equals("Chromeless")) {
            return PlayerUtil.getFlashPlayerVersion();
        } else {
            throw new IllegalArgumentException("Unknown player - '" + playerName + "'");
        }
    }

    @Override
    public PlayerElement getPlayerElement(String playerName, String playerId, String mediaURL, boolean autoplay, HashMap<String, String> params) {
        if (playerName.equals("YouTube") || playerName.equals("Chromeless")) {
            PlayerElement e = new PlayerElement(PlayerElement.Type.EmbedElement, playerId, "application/x-shockwave-flash");
            e.addParam("src", mediaURL);
            e.addParam("name", playerId);

            Iterator<String> keys = params.keySet().iterator();
            while (keys.hasNext()) {
                String name = keys.next();
                e.addParam(name, params.get(name));
            }
            return e;
        } else {
            throw new IllegalArgumentException("Unknown player - '" + playerName + "'");
        }
    }
}
