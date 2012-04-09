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

import com.google.gwt.user.client.ui.Label;

/**
 * CSS based implementation of the MediaSeekBar.  The playing and loading
 * progress indicators as well as the seek track can be modified via
 * CSS style names defined in stylesheets.
 *
 * <h4>CSS Styles</h4>
 * <code><pre>
 * .player-CSSSeekBar { the seekbar itself }
 * .player-CSSSeekBar .loading { the loading progress indicator }
 * .player-CSSSeekBar .playing { the playing progress indicator }
 * </pre></code>
 *
 * @author Sikirulai Braheem
 */
public class CSSSeekBar extends MediaSeekBar {

    private Label playing,  loading;

    /**
     * Constructs <code>CSSSeekBar</code> of the specified height.
     *
     * @param height the height of the seek bar in pixels
     */
    public CSSSeekBar(int height) {
        super(height);
        playing = new Label();
        playing.setHeight(height + "px");
        playing.addMouseUpHandler(this);

        loading = new Label();
        loading.setHeight(height + "px");
        loading.addMouseUpHandler(this);

        playing.setStyleName("playing");
        loading.setStyleName("loading");

        initSeekBar(loading, playing);
        setStylePrimaryName("player-CSSSeekBar");
    }
}
