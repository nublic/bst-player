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
package com.bramosystems.oss.player.script.client;

import com.bramosystems.oss.player.core.client.AbstractMediaPlayer;
import com.bramosystems.oss.player.core.client.LoadException;
import com.bramosystems.oss.player.core.client.Plugin;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersion;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.bramosystems.oss.player.core.client.skin.MediaSeekBar;
import com.google.gwt.user.client.ui.Widget;
import java.util.HashMap;

/**
 * Abstract implementation for providers of the player and seekbar widgets exported as Javascript
 * objects.
 * 
 * <p>All methods in the class throw {@linkplain UnsupportedOperationException} by default.  Subclasses
 * should override and implement as required.
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems dot com>
 * @since 1.3
 */
public class AbstractExportProvider {

    /**
     * Called to retrieve the player implementation that is exported as Javascript object.
     * 
     * @param playerProvider the provider of the player
     * @param playerName the name of the player
     * @param mediaURL the URL of the media file
     * @param autoplay <code>true</code> to start playback automatically, <code>false</code> otherwise
     * @param width the width of the player (in CSS units)
     * @param height the height of the player (in CSS units)
     * @param options user-defined options supplied during Javascript-object creation
     *
     * @return a suitable player implementation
     *
     * @throws LoadException if an error occurs while loading the media.
     * @throws PluginNotFoundException if the required plugin is not found
     * @throws PluginVersionException if the required plugin version is missing
     */
    public AbstractMediaPlayer getPlayer(String playerProvider, String playerName, String mediaURL,
            boolean autoplay, String width, String height, HashMap<String, String> options)
            throws LoadException, PluginNotFoundException, PluginVersionException {
        throw new UnsupportedOperationException("Please override this method in a subclass !");
    }

    /**
     * Called to retrieve the seek bar implementation that is exported as Javascript object.
     *
     * @param height the height of the seek bar (in pixels)
     * @param options user-defined options supplied during Javascript-object creation.
     * @return seek bar implementation to be exported as Javascript object
     */
    public MediaSeekBar getSeekBar(int height, HashMap<String, String> options) {
        throw new UnsupportedOperationException("Please override this method in a subclass !");
    }

    /**
     * Called to retrieve the widget that may be used when the required plugin is not
     * found.
     *
     * @param plugin the required plugin
     * @return the widget used when required plugin is missing
     */
    public Widget getMissingPluginWidget(Plugin plugin) {
        throw new UnsupportedOperationException("Please override this method in a subclass !");
    }

    /**
     * Called to retrieve the widget that may be used when the required version of a
     * plugin is not found.
     *
     * @param plugin the required plugin
     * @param requiredVersion the required plugin version
     * @return the widget used when required plugin version is missing
     */
    public Widget getMissingPluginVersionWidget(Plugin plugin, PluginVersion requiredVersion) {
        throw new UnsupportedOperationException("Please override this method in a subclass !");
    }
}