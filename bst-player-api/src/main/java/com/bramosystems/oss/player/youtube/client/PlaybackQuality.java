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

/**
 * An enum of YouTube video playback qualities. 
 * 
 * <p>The playback quality usually correspond to the size of the video player.
 * For example, if a page displays a 640px by 360px video player, a medium quality video
 * will look better than a large quality video.
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems dot com>
 * @since 1.1
 * @see YouTubePlayer#setPlaybackQuality(PlaybackQuality)
 */
public enum PlaybackQuality {

    /**
     * Instructs YouTube to select the most appropriate playback quality,
     * which will vary for different users, videos, systems and other playback
     * conditions.
     */
    Default,

    /**
     * Quality level for player resolution less than 640px by 360px.
     */
    small,

    /**
     * Quality level for minimum player resolution of 640px by 360px.
     */
    medium,

    /**
     * Quality level for minimum player resolution of 854px by 480px.
     */
    large,

    /**
     * Quality level for minimum player resolution of 1280px by 720px.
     */
    hd720,

    /**
     * Quality level for minimum player height of 1080px i.e. player dimensions are 1920px by 1080px (for 16:9 aspect ratio) or
     * 1440px by 1080px (for 4:3 aspect ratio)
     *
     * @since 1.2
     */
    hd1080,

    /**
     * Quality level for player resolution greater than 1920px by 1080px
     *
     * @since 1.2
     */
    highres
}
