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
package com.bramosystems.oss.player.script.client.impl;

import com.bramosystems.oss.player.core.client.AbstractMediaPlayer;
import com.bramosystems.oss.player.core.client.LoadException;
import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.bramosystems.oss.player.core.client.Plugin;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersion;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.bramosystems.oss.player.core.client.skin.CSSSeekBar;
import com.bramosystems.oss.player.core.client.skin.MediaSeekBar;
import com.bramosystems.oss.player.script.client.AbstractExportProvider;
import com.google.gwt.user.client.ui.Widget;
import java.util.HashMap;

/**
 * Exports all player widgets registered with the API.
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems dot com>
 * @since 1.3
 */
public class DefaultExportProvider extends AbstractExportProvider {

    @Override
    public AbstractMediaPlayer getPlayer(String playerProvider, String playerName, String mediaURL,
            boolean autoplay, String width, String height, HashMap<String, String> options)
            throws LoadException, PluginNotFoundException, PluginVersionException {
        return PlayerUtil.getPlayer(PlayerUtil.getPlayerInfo(playerProvider, playerName), mediaURL, autoplay, height, width);
    }

    @Override
    public Widget getMissingPluginWidget(Plugin plugin) {
        return PlayerUtil.getMissingPluginNotice(plugin);
    }

    @Override
    public Widget getMissingPluginVersionWidget(Plugin plugin, PluginVersion requiredVersion) {
        return PlayerUtil.getMissingPluginNotice(plugin, requiredVersion.toString());
    }

    @Override
    public MediaSeekBar getSeekBar(int height, HashMap<String, String> options) {
        return new CSSSeekBar(height);
    }
}
