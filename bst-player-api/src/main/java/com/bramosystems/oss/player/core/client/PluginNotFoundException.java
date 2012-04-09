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

/**
 * Thrown to indicate that a required plugin cannot be found on the client's browser.
 *
 * <p>
 * On catching this exception, a message might be displayed to the user describing
 * how to download the plugin.
 *
 * @author Sikiru Braheem
 * @see PluginVersionException
 */
public class PluginNotFoundException extends Exception {

    private Plugin plugin;

    /**
     * Constructs a new PluginNotFoundException
     */
    public PluginNotFoundException() {
        super("Required Plugin is not available");
        plugin = Plugin.Auto;
    }

    /**
     * Constructs a new PluginNotFoundException for the specified plugin
     * 
     * @param plugin the required plugin
     * @since 1.1
     */
    public PluginNotFoundException(Plugin plugin) {
        super("Required Plugin is not available");
        this.plugin = plugin;
    }

    /**
     * Constructs a new PluginNotFoundException with the specified message
     *
     * @param message describes the exception
     * @since 1.1
     */
    public PluginNotFoundException(String message) {
        super(message);
        this.plugin = Plugin.Auto;
    }

    /**
     * Constructs a new PluginNotFoundException with the specified message
     * 
     * @param plugin the required plugin
     * @param message describes the exception
     * @since 1.2.1
     */
    public PluginNotFoundException(Plugin plugin, String message) {
        super(message);
        this.plugin = plugin;
    }

    /**
     * Returns the required plugin
     *
     * @return the required plugin
     * @since 1.1
     */
    public Plugin getPlugin() {
        return plugin;
    }
}
