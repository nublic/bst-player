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
package com.bramosystems.oss.player.youtube.client;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;

/**
 * The event fired when the playback quality of a YouTube video changes
 *
 * @author Sikirulai Braheem
 * @since 1.1
 */
public class PlaybackQualityChangeEvent extends GwtEvent<PlaybackQualityChangeHandler> {

    public static final Type<PlaybackQualityChangeHandler> TYPE = new Type<PlaybackQualityChangeHandler>();
    private PlaybackQuality quality;

    /**
     * Creates a new PlaybackQualityChangeEvent event
     *
     * @param quality the new playback quality
     */
    protected PlaybackQualityChangeEvent(PlaybackQuality quality) {
        this.quality = quality;
    }

    /**
     * Fires PlaybackQualityChangeEvent events on all registered handlers
     *
     * @param source the YouTubePlayer generating the event
     * @param quality the playback quality
     */
    public static void fire(YouTubePlayer source, PlaybackQuality quality) {
        source.fireEvent(new PlaybackQualityChangeEvent(quality));
    }

    @Override
    public Type<PlaybackQualityChangeHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(PlaybackQualityChangeHandler handler) {
        handler.onPlaybackQualityChanged(this);
    }

    /**
     * Retrieves the new video playback quality
     *
     * @return the new video playback quality
     */
    public PlaybackQuality getPlaybackQuality() {
        return quality;
    }
}
