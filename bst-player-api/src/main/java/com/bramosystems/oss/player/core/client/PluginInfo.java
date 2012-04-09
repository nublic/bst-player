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

import java.io.Serializable;

/**
 * Wraps basic information about a browser media plug-in component.
 *
 * @author Sikirulai Braheem
 * @since 1.2.1
 */
public class PluginInfo implements Serializable {

    /**
     * An enum of Javascript API wrappers available for media player plugin
     *
     * @since 1.2.1
     */
    public static enum PlayerPluginWrapperType {

        /**
         * The player plugin is exposed to javascript using its native API
         */
        Native,
        /**
         * Used only for Windows Media Player plugin.  The player is exposed by the
         * Windows Media Player plugin for Firefox
         */
        WMPForFirefox,
        /**
         * The player is exposed to javascript by the Totem plugin
         */
        Totem
    }
    private PluginVersion version;
    private PlayerPluginWrapperType wrapperType;
    private Plugin plugin;

    /**
     * Creates a <code>PluginInfo</code> with the specified version and wrapperType
     *
     * @param plugin the plugin
     * @param version the plugin version
     * @param wrapperType the wrapper type in use by the plugin
     */
    public PluginInfo(Plugin plugin, PluginVersion version, PlayerPluginWrapperType wrapperType) {
        this.version = version;
        this.wrapperType = wrapperType;
        this.plugin = plugin;
    }

    /**
     * Returns the plugin version
     * 
     * @return the plugin version
     */
    public PluginVersion getVersion() {
        return version;
    }

    /**
     * Returns the wrapper type used by the plugin
     * 
     * @return the wrapper type
     */
    public PlayerPluginWrapperType getWrapperType() {
        return wrapperType;
    }

    /**
     * Returns the plugin
     * 
     * @return the plugin
     */
    public Plugin getPlugin() {
        return plugin;
    }

    /**
     * Returns the string representation of this object in the form {@code PluginInfo{plugin, version, wrapperType}}
     * 
     * @return the string representation of this object
     */
    @Override
    public String toString() {
        return "PluginInfo{plugin=" + plugin + ", version=" + version + ",  wrapperType=" + wrapperType + "} ";
    }
}
