/*
 * Copyright 2011 Sikirulai Braheem
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
package com.bramosystems.oss.player.core.client;

import com.bramosystems.oss.player.core.client.geom.MatrixSupport;
import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

/**
 * Wraps basic information about a player widget registered with the API.
 *
 * @author Sikirulai Braheem
 * @since 1.3
 */
public class PlayerInfo implements Serializable {

    private String playerName, providerName;
    private PluginVersion requiredPluginVersion, detectedPluginVersion;
    private boolean hasPlaylistSupport, hasMatrixSupport;
    private Set<String> registeredExtensions = new TreeSet<String>(), registeredProtocols = new TreeSet<String>();

    /**
     * Creates PlayerInfo object
     * 
     * @param providerName the provider of the player
     * @param playerName the name of the player
     * @param requiredPluginVersion the minimum plugin version required by the player 
     * @param hasPlaylistSupport {@code true} if player supports playlists, {@code false} otherwise.
     * @param hasMatrixSupport {@code true} if player supports matrix, {@code false} otherwise.
     */
    public PlayerInfo(String providerName, String playerName, PluginVersion requiredPluginVersion,
            boolean hasPlaylistSupport, boolean hasMatrixSupport) {
        this.playerName = playerName;
        this.providerName = providerName;
        this.requiredPluginVersion = requiredPluginVersion;
        this.hasPlaylistSupport = hasPlaylistSupport;
        this.hasMatrixSupport = hasMatrixSupport;
        detectedPluginVersion = new PluginVersion();
    }

    /**
     * Returns the provider name of the player
     * 
     * @return the provider name
     */
    public String getProviderName() {
        return providerName;
    }

    /**
     * Returns the name of the player
     * 
     * @return the player name
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Returns the minimum plugin version required by the player
     * 
     * @return the required minimum plugin version
     */
    public PluginVersion getRequiredPluginVersion() {
        return requiredPluginVersion;
    }

    /**
     * Returns {@code true} if player has matrix support, {@code false} otherwise
     * 
     * @return {@code true} if player has matrix support, {@code false} otherwise
     * @see MatrixSupport
     */
    public boolean isHasMatrixSupport() {
        return hasMatrixSupport;
    }

    /**
     * Returns {@code true} if player has playlist support, {@code false} otherwise
     * 
     * @return @code true} if player has playlist support, {@code false} otherwise
     * @see PlaylistSupport
     */
    public boolean isHasPlaylistSupport() {
        return hasPlaylistSupport;
    }

    /**
     * Returns the version of the required player plugin detected on the browser
     * 
     * @return the player plugin version detected on the browser
     */
    public PluginVersion getDetectedPluginVersion() {
        return detectedPluginVersion;
    }

    /**
     * Sets the version of the required player plugin detected on the browser
     * 
     * @param detectedPluginVersion the player plugin version detected on the browser
     */
    public void setDetectedPluginVersion(PluginVersion detectedPluginVersion) {
        this.detectedPluginVersion = detectedPluginVersion;
    }

    /**
     * Returns the media format file extensions supported by the player
     * 
     * @return media file extensions supported by the player
     */
    public Set<String> getRegisteredExtensions() {
        return registeredExtensions;
    }

    /**
     * Sets the media format file extensions supported by the player
     * 
     * @param registeredExtensions the supported media file format extensions
     */
    protected void setRegisteredExtensions(Set<String> registeredExtensions) {
        this.registeredExtensions = registeredExtensions;
    }

    /**
     * Returns the streaming protocols supported by the player
     * 
     * @return the supported streaming protocols
     */
    public Set<String> getRegisteredProtocols() {
        return registeredProtocols;
    }

    /**
     * Sets the streaming protocols supported by the player
     * 
     * @param registeredProtocols the supported streaming protocols
     */
    protected void setRegisteredProtocols(Set<String> registeredProtocols) {
        this.registeredProtocols = registeredProtocols;
    }
}
