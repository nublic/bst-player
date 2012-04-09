/*
 * Copyright 2010 Sikirulai Braheem
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
package com.bramosystems.oss.player.uibinder.client;

import com.bramosystems.oss.player.core.client.AbstractMediaPlayer;
import com.bramosystems.oss.player.core.client.LoadException;
import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.bramosystems.oss.player.core.client.Plugin;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.google.gwt.uibinder.client.UiConstructor;

/**
 * UiBinder wrapper class for any player implementation that supports transformation matrixes
 *
 * <h3>Usage Example</h3>
 *
 * <p>
 * <code><pre>
 * &lt;ui:UiBinder xmlns:ui='urn:ui:com.google.gwt.uibinder'
 *      xmlns:g='urn:import:com.google.gwt.user.client.ui'
 *      xmlns:player='urn:import:com.bramosystems.oss.player.uibinder.client'&gt;
 *         ...
 *         &lt;player:MatrixSupport autoplay='true' height='20px' width='100%'
 *                 mediaURL='GWT-HOST::media.mp4' /&gt;
 *         ...
 * &lt;/ui:UiBinder&gt;
 * </pre></code>
 *
 * @deprecated Replaced with {@link Player}, will be removed in future.
 * @author Sikiru Braheem <sbraheem at bramosystems . com>
 * @since 1.1
 */
public class MatrixSupport extends PlayerWrapper {

    /**
     * The constructor
     *
     * @param mediaURL the URL of the media to playback
     * @param autoplay {@code true} to autoplay, {@code false} otherwise
     * @param height the height of the player (in CSS units)
     * @param width the width of the player (in CSS units)
     */
    @UiConstructor
    public MatrixSupport(String mediaURL, boolean autoplay, String height, String width) {
        super(mediaURL, autoplay, height, width);
    }

    @Override
    protected AbstractMediaPlayer initPlayerEngine(String mediaURL, boolean autoplay, String height,
            String width) throws LoadException, PluginNotFoundException, PluginVersionException {
        return PlayerUtil.getPlayer(Plugin.MatrixSupport, mediaURL, autoplay, height, width);
    }
}
