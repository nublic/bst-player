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
package com.bramosystems.oss.player.core.client.spi;

import com.bramosystems.oss.player.core.client.AbstractMediaPlayer;
import com.bramosystems.oss.player.core.client.LoadException;
import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersion;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import java.util.HashMap;

/**
 * Interface defines the methods required of player provider implementations.  The implementation
 * classes are consulted by the API framework during player widget creation.
 *
 * <p><b>Note:</b> Classes that implement this interface should be annotated with {@link PlayerProvider} 
 * to be recognized during compilation
 * 
 * @author Sikirulai Braheem <sbraheem at bramosystems.com>
 * @since 1.3
 */
public interface PlayerProviderFactory {

    /**
     * Returns the DOM element structure for the specified {@code playerName}.  The DOM element structure 
     * will be inserted into the page when required.
     * 
     * <p>Implementation classes should throw {@link IllegalArgumentException} if the {@code playerName}
     * is not supported by the factory
     *
     * @param playerName the name of the required player
     * @param playerId the HTML element {@code id/name} that should be used for the player
     * @param mediaURL the first URL of the media to be loaded by the player
     * @param autoplay {@code true} if playback should start immediately, {@code false} otherwise
     * @param params other HTML parameters that should be associated with the DOM element
     * @return the DOM element structure for the specified {@code playerName}
     * @throws IllegalArgumentException if {@code playerName} does not exist in this factory
     */
    public PlayerElement getPlayerElement(String playerName, String playerId, String mediaURL, boolean autoplay,
            HashMap<String, String> params);

    /**
     * Returns the version of the plugin required by the specified {@code playerName} that is currently 
     * installed AND enabled on the browser
     * 
     * <p>The detection methods in the {@link PlayerUtil} class may be used by implementation classes if applicable. 
     * 
     * <p>Implementation classes should throw {@link IllegalArgumentException} if the {@code playerName}
     * is not supported by the factory
     *
     * @param playerName the name of the player
     * @return the version of the required plugin that is installed and enabled on the browser
     * @throws PluginNotFoundException if the required plugin is not installed AND enabled
     * @throws IllegalArgumentException if {@code playerName} does not exist in this factory
     */
    public PluginVersion getDetectedPluginVersion(String playerName) throws PluginNotFoundException;

    /**
     * Returns the player with the specified {@code playerName}.
     * 
     * <p>Implementation classes should throw {@link IllegalArgumentException} if the {@code playerName}
     * is not supported by the factory
     *
     * @param playerName
     * @param mediaURL the URL of the media to playback
     * @param autoplay {@code true} to start playing automatically, {@code false} otherwise
     * @param height the height of the player
     * @param width the width of the player.
     *
     * @return the player implementation
     *
     * @throws LoadException if an error occurs while loading the media.
     * @throws PluginVersionException if the required plugin version is not installed on the client.
     * @throws PluginNotFoundException if the required plugin is not installed on the client.
     * @throws IllegalArgumentException if {@code playerName} does not exist in this factory
     */
    public AbstractMediaPlayer getPlayer(String playerName, String mediaURL,
            boolean autoplay, String height, String width) throws LoadException, PluginNotFoundException, PluginVersionException;

    /**
     * Returns the player with the specified {@code playerName}.
     *
     * <p>Implementation classes should throw {@link IllegalArgumentException} if the {@code playerName}
     * is not supported by the factory
     *
     * @param playerName
     * @param mediaURL the URL of the media to playback
     * @param autoplay {@code true} to start playing automatically, {@code false} otherwise
     * 
     * @return the player implementation
     * 
     * @throws LoadException if an error occurs while loading the media.
     * @throws PluginVersionException if the required plugin version is not installed on the client.
     * @throws PluginNotFoundException if the required plugin is not installed on the client.
     * @throws IllegalArgumentException if {@code playerName} does not exist in this factory
     */
    public AbstractMediaPlayer getPlayer(String playerName, String mediaURL,
            boolean autoplay) throws LoadException, PluginNotFoundException, PluginVersionException;
}
