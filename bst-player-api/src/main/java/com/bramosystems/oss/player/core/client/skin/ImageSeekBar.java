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
package com.bramosystems.oss.player.core.client.skin;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.*;

/**
 * Image based implementation of the MediaSeekBar.  The playing and loading
 * progress indicators are loaded as images.
 *
 * @author Sikirulai Braheem
 */
public class ImageSeekBar extends MediaSeekBar {

    private Image playing,  loading;

    /**
     * Constructs <code>ImageeekBar</code> of the specified height using blank
     * images as loading and playback progress indicators.
     *
     * @param height the height of the seek bar in pixels
     */
    public ImageSeekBar(int height) {
        super(height);
        playing = new Image();
        playing.setStyleName("");
        playing.addMouseUpHandler(this);

        loading = new Image();
        loading.setStyleName("");
        loading.addMouseUpHandler(this);

        DOM.setStyleAttribute(playing.getElement(), "cursor", "pointer");
        DOM.setStyleAttribute(loading.getElement(), "cursor", "pointer");

        initSeekBar(loading, playing);
    }

    /**
     * Uses the images at the specified URLs as loading and playback progress
     * indicators.
     *
     * <p>In respect of the <code>same domain</code> policy, the URL should
     * point to a destination on the same domain where the application is hosted.
     *
     * @param loadingImageUrl URL of the loading progress image
     * @param playingImageUrl URL of the playback progress image
     */
    public void setProgressImages(String loadingImageUrl, String playingImageUrl) {
        loading.setUrl(loadingImageUrl);
        playing.setUrl(playingImageUrl);
    }
}
