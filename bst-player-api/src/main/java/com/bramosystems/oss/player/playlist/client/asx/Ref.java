/*
 * Copyright 2011 Sikirulai Braheem <sbraheem at bramosystems.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bramosystems.oss.player.playlist.client.asx;

import com.bramosystems.oss.player.core.client.PlayTime;

/**
 * Class specifies the URL reference of a media content
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems.com>
 * @since 1.3
 */
public class Ref {

    private String href;
    private PlayTime duration, startTime;
 
    /**
     * Constructor
     */
    public Ref() {
    }

    /**
     * Returns the playback duration of the media
     * 
     * @return the duration of playback 
     */
    public PlayTime getDuration() {
        return duration;
    }

    /**
     * Sets the playback duration of the media
     * 
     * @param duration the duration of playback
     */
    public void setDuration(PlayTime duration) {
        this.duration = duration;
    }

    /**
     * Returns the URL of the media
     * 
     * @return the URL of the media
     */
    public String getHref() {
        return href;
    }

    /**
     * Sets the URL of the media
     * 
     * @param href the URL of the media
     */
    public void setHref(String href) {
        this.href = href;
    }

    /**
     * Returns the time when playback should start
     * 
     * @return the time when playback should start
     */
    public PlayTime getStartTime() {
        return startTime;
    }

    /**
     * Sets the time when playback should start
     * 
     * @param startTime the time when playback should start
     */
    public void setStartTime(PlayTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Returns a more descriptive representation of the media reference. Useful during debugging
     */
    @Override
    public String toString() {
        return "Ref{" + "href=" + href + ", duration=" + duration + ", startTime=" + startTime + '}';
    }
}
