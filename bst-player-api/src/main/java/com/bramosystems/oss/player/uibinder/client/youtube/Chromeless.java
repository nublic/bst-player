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
package com.bramosystems.oss.player.uibinder.client.youtube;

import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.bramosystems.oss.player.uibinder.client.Player;
import com.bramosystems.oss.player.uibinder.client.PlayerWrapper;
import com.bramosystems.oss.player.youtube.client.ChromelessPlayer;
import com.bramosystems.oss.player.youtube.client.PlayerParameters;
import com.google.gwt.uibinder.client.UiConstructor;

/**
 * Wrapper class for {@link com.bramosystems.oss.player.youtube.client.ChromelessPlayer}
 * providing UiBinder support.
 *
 * <h3>Usage Example</h3>
 *
 * <p>
 * <code><pre>
 * &lt;ui:UiBinder xmlns:ui='urn:ui:com.google.gwt.uibinder'
 *      xmlns:g='urn:import:com.google.gwt.user.client.ui'
 *      xmlns:player='urn:import:com.bramosystems.oss.player.uibinder.client.youtube'&gt;
 *         ...
 *         &lt;player:Chromeless autoplay='true' height='250px' width='100%'
 *                 videoURL='www.youtube.com/v/video-id' /&gt;
 *         ...
 * &lt;/ui:UiBinder&gt;
 * </pre></code>
 *
 * @deprecated Replaced with {@link Player}, will be removed in future.
 * @author Sikiru Braheem <sbraheem at bramosystems . com>
 * @since 1.1
 */
public class Chromeless extends PlayerWrapper<ChromelessPlayer> {

    /**
     * The constructor
     *
     * @param videoURL the URL of the video to playback
     * @param autoplay {@code true} to autoplay, {@code false} otherwise
     * @param height the height of the player (in CSS units)
     * @param width the width of the player (in CSS units)
     */
    @UiConstructor
    public Chromeless(String videoURL, boolean autoplay, String width, String height) {
        super(videoURL, autoplay, height, width);
    }

    @Override
    protected ChromelessPlayer initPlayerEngine(String mediaURL, boolean autoplay, String height, String width)
            throws PluginVersionException, PluginNotFoundException {
        PlayerParameters pp = new PlayerParameters();
        pp.setAutoplay(autoplay);

        return new ChromelessPlayer(mediaURL, pp, width, height);
    }

}
