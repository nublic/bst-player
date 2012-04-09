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

import com.bramosystems.oss.player.core.client.playlist.MRL;
import java.util.List;

/**
 * Interface for players that have client-side playlist support.
 *
 * @author Sikiru Braheem
 * @since 1.0
 */
public interface PlaylistSupport {

    /**
     * Enables or disables players' shuffle mode. 
     *
     * @param enable {@code true} to enable shuffle, {@code false} otherwise
     */
    public void setShuffleEnabled(boolean enable);

    /**
     * Checks if this player is in shuffle mode. 
     *
     * @return {@code true} if player is in shuffle mode, {@code false} otherwise.
     */
    public boolean isShuffleEnabled();

    /**
     * Adds the media at the specified URL to the players' playlist.
     *
     * <p>In respect of the same domain policy of some plugins/browsers,
     * the URL should point to a destination on the same domain where the
     * application is hosted.
     *
     * @param mediaURL the URL of the media.
     */
    public void addToPlaylist(String mediaURL);

    /**
     * Adds the media at the specified URLs to the players' playlist.  
     * 
     * <p>The player chooses ONLY ONE of the {@code mediaURLs} it supports.
     *
     * <p>In respect of the same domain policy of some browsers, the URLs should point to
     * a destination on the same domain where the application is hosted.
     * 
     * @param mediaURLs the alternative URLs of the same media (probably in different formats).
     * @since 1.3
     */
    public void addToPlaylist(String... mediaURLs);

    /**
     * Adds the media locator to the players' playlist.  
     * 
     * @param mediaLocator specifies alternative URLs of the same media
     * @since 1.3
     */
    public void addToPlaylist(MRL mediaLocator);

    /**
     * Adds the media locators to the players' playlist.  
     * 
     * @param mediaLocators list of alternative URLs of the same media
     * @since 1.3
     */
    public void addToPlaylist(List<MRL> mediaLocators);

    /**
     * Removes the entry at the specified index from the players' playlist.
     *
     * @param index the index of the playlist entry.
     */
    public void removeFromPlaylist(int index);

    /**
     * Removes all entries in the players' playlist
     */
    public void clearPlaylist();

    /**
     * Plays the next item in the playlist
     *
     * @throws PlayException if there are no more entries in the playlist to be played. Especially if we've
     * advanced to the end of the playlist. Note: A player with a negative loop count (i.e. set to play
     * forever!) may not throw this exception
     */
    public void playNext() throws PlayException;

    /**
     * Plays the previous item in the playlist
     *
     * @throws PlayException if there are no more entries in the playlist to be played. Especially if we've
     * gotten to the beginning of the playlist. Note: A player with a negative loop count (i.e. set to play
     * forever!) may not throw this exception
     */
    public void playPrevious() throws PlayException;

    /**
     * Play playlist entry at the specified <code>index</code>
     *
     * @param index number of the playlist entry
     * @throws IndexOutOfBoundsException if <code>index</code> is outside the bounds of the playlist
     */
    public void play(int index) throws IndexOutOfBoundsException;

    /**
     * Returns the number of entries in the playlist
     *
     * @return number of entries in the playlist
     */
    public int getPlaylistSize();
}
