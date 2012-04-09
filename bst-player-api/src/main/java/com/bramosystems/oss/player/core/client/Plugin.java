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
package com.bramosystems.oss.player.core.client;

import com.bramosystems.oss.player.core.client.geom.MatrixSupport;

/**
 * An enum of supported media player plugins
 *
 * @author Sikirulai Braheem
 */
public enum Plugin {

    /**
     * Specifies any media player plugin that is available on the browser and features basic
     * playback support.
     *
     * <p>Basic playback support is as defined by the {@linkplain AbstractMediaPlayer} class.
     *
     * @see PlayerUtil#getPlayer(java.lang.String, boolean, java.lang.String, java.lang.String)
     */
    Auto("", 0, 0, 0, "Auto-select"),

    /**
     * Specifies the browsers' native media handler.  HTML 5 compliant browsers
     * have support for media playback via the <code>audio</code> and <code>video</code>
     * elements.
     *
     * @since 1.1
     */
    Native("", 5, 0, 0, "HTML5 Media Player"),

    /**
     * Specifies the QuickTime Player plugin
     */
    QuickTimePlayer("http://www.apple.com/quicktime/download", 7, 2, 1, "QuickTime Player"),

    /**
     * Specifies the Flash Player plugin
     */
    FlashPlayer("http://get.adobe.com/flashplayer", 9, 0, 0, "Flash Player (BST Flash Player)"),

    /**
     * Specifies the Window Media Player&trade; plugin
     */
    WinMediaPlayer("http://www.microsoft.com/windowsmedia", 1, 1, 1, "Windows Media Player"),

    /**
     * Specifies the VLC Media Player plugin
     * @since 1.0
     */
    VLCPlayer("http://www.videolan.org", 1, 0, 0, "VLC Media Player"),

    /**
     * Specifies the DivX Web Player plugin
     * @since 1.2
     */
    DivXPlayer("http://go.divx.com/plugin/download", 2, 0, 0, "DivX Web Player"),

    /**
     * Similar to {@linkplain #Auto}, specifies any media player plugin that supports
     * client-side playlist management as defined by the {@linkplain PlaylistSupport}
     * interface.
     *
     * @see PlayerUtil#getPlayer(Plugin, java.lang.String, boolean, java.lang.String, java.lang.String)
     * @see PlaylistSupport
     * @since 1.0
     */
    PlaylistSupport("", 0, 0, 0, "Playlist Support"),

    /**
     * Similar to {@linkplain #Auto}, specifies any media player plugin that supports
     * graphics manipulation with transformation matrices as defined by the
     * {@linkplain MatrixSupport} interface.
     *
     * @see PlayerUtil#getPlayer(Plugin, java.lang.String, boolean, java.lang.String, java.lang.String)
     * @see MatrixSupport
     * @since 1.1
     */
    MatrixSupport("", 0, 0, 0, "Matrix Support");

    private String downloadURL, desc;
    private PluginVersion version;

    Plugin(String downloadURL, int majorVersion, int minorVersion, int revision, String desc) {
        this.downloadURL = downloadURL;
        version = PluginVersion.get(majorVersion, minorVersion, revision);
        this.desc = desc;
    }

    /**
     * Gets the URL of the plugins' download page
     *
     * @return the plugin download URL
     * @since 0.6
     */
    public String getDownloadURL() {
        return downloadURL;
    }

    /**
     * Gets the minimum version required by this plugin
     *
     * @return the minimum version of the plugin
     * @since 1.0
     */
    public PluginVersion getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return desc;
    }
}
