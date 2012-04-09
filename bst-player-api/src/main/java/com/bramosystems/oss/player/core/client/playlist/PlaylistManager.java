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
package com.bramosystems.oss.player.core.client.playlist;

import com.bramosystems.oss.player.core.client.*;
import com.bramosystems.oss.player.core.client.impl.playlist.PlaylistIndexOracle;
import com.bramosystems.oss.player.core.event.client.DebugEvent;
import com.bramosystems.oss.player.core.event.client.PlayerStateEvent;
import com.bramosystems.oss.player.core.event.client.PlayerStateHandler;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides playlist emulation support for media plugins
 *
 * @since 1.2
 * @author Sikiru Braheem
 */
public class PlaylistManager implements PlaylistSupport {

    private Playlist urls;
    private ArrayList<String> msgCache;
    private PlayerCallback callback;
    private PlaylistIndexOracle indexOracle;
    private int pIndex;
    private boolean useCache;

    /**
     * Creates the PlaylistManager object.
     */
    public PlaylistManager() {
        urls = new Playlist();
        msgCache = new ArrayList<String>();
        indexOracle = new PlaylistIndexOracle();
        useCache = true;
        callback = initCallback();
    }

    /**
     * Creates PlaylistManager for the specified player widget.
     * 
     * @param player the player widget
     */
    public PlaylistManager(final AbstractMediaPlayer player) {
        this();
        callback = new PlayerCallback() {

            @Override
            public void play() throws PlayException {
                player.playMedia();
            }

            @Override
            public void load(String url) {
                try {
                    player.loadMedia(url);
                } catch (LoadException ex) {
                    onDebug(ex.getMessage());
                }
            }

            @Override
            public void onDebug(String message) {
                DebugEvent.fire(player, DebugEvent.MessageType.Info, message);
            }
        };
        player.addPlayerStateHandler(new PlayerStateHandler() {

            @Override
            public void onPlayerStateChanged(PlayerStateEvent event) {
                switch (event.getPlayerState()) {
                    case Ready:
                        flushMessageCache();
                }
            }
        });
    }

    /**
     * Returns a player callback handler links this manager to its associated player.
     * 
     * <p>This is a convenience method that should be overridden by sub-classes.
     * 
     * @return a PlayerCallback implementation
     */
    protected PlayerCallback initCallback() {
        return new PlayerCallback() {

            @Override
            public void play() throws PlayException {
            }

            @Override
            public void load(String url) {
            }

            @Override
            public void onDebug(String message) {
            }
        };
    }

    /**
     * Fires debug events with messages that have been cached since the manager is created. 
     * 
     * <p>This method should ONLY be called by subclasses when the attached player is ready.
     */
    protected void flushMessageCache() {
        if (msgCache != null) {
            useCache = false;
            for (String msg : msgCache) {
                callback.onDebug(msg);
            }
            msgCache = null;
        }
    }

    @Override
    public boolean isShuffleEnabled() {
        return indexOracle.isRandomMode();
    }

    @Override
    public void setShuffleEnabled(boolean enable) {
        indexOracle.setRandomMode(enable);
    }

    @Override
    public void addToPlaylist(String mediaURL) {
        addToPlaylist(new MRL(mediaURL));
    }

    @Override
    public void addToPlaylist(String... mediaURLs) {
        addToPlaylist(new MRL(mediaURLs));
    }

    @Override
    public void addToPlaylist(List<MRL> mediaLocators) {
        for (MRL mrl : mediaLocators) {
            addToPlaylist(mrl);
        }
    }

    @Override
    public void addToPlaylist(MRL mediaLocator) {
        urls.add(mediaLocator);
        indexOracle.incrementIndexSize();
        _debug("Added to playlist - '" + mediaLocator + "'");
    }

    @Override
    public void removeFromPlaylist(int index) {
        _debug("Removed from playlist - '" + urls.remove(index) + "'");
        indexOracle.removeFromCache(index);
    }

    @Override
    public void clearPlaylist() {
        urls.clear();
        indexOracle.reset(false);
    }

    @Override
    public void playNext() throws PlayException {
        playNext(false);
    }

    @Override
    public void playPrevious() throws PlayException {
        playPrevious(false);
    }

    /**
     * Plays the next item in the playlist
     * 
     * @param force <code>true</code> if an end-of-playlist should roll over to the beginning, {@code false} otherwise.
     * @throws PlayException if an end-of-playlist is reached.  Only thrown if {@code force} is false
     */
    public void playNext(boolean force) throws PlayException {
        int ind = indexOracle.suggestIndex(true, force);
        if (ind < 0) {
            throw new PlayException("End of playlist");
        }
        pIndex = ind;
        _playOrLoadMedia(ind, true);
    }

    /**
     * Plays the previous item in the playlist
     * 
     * @param force <code>true</code> if a start-of-playlist should roll over to the end, {@code false} otherwise.
     * @throws PlayException if a start-of-playlist is reached.  Only thrown if {@code force} is false
     */
     public void playPrevious(boolean force) throws PlayException {
        int ind = indexOracle.suggestIndex(false, force);
        if (ind < 0) {
            throw new PlayException("Beginning of playlist reached");
        }
        pIndex = ind;
        _playOrLoadMedia(ind, true);
    }

    @Override
    public void play(int index) throws IndexOutOfBoundsException {
        try {
            indexOracle.setCurrentIndex(index);
            _playOrLoadMedia(index, true);
        } catch (PlayException ex) {
            _debug(ex.getMessage());
        }
    }

    /**
     * Play another alternative URL for the current resource index
     * 
     * @throws LoadException if no alternative URL can be found
     */
    public void loadAlternative() throws LoadException {
        try {
            callback.load(urls.get(indexOracle.getCurrentIndex()).getNextResource(false));
        } catch (Exception e) {
            throw new LoadException(e.getMessage());
        }
    }

    /**
     * Load the URL at the specified {@code index}
     * 
     * @param index the index
     */
    public void load(int index) {
        try {
            indexOracle.setCurrentIndex(index);
            _playOrLoadMedia(index, false);
        } catch (PlayException ex) {
            _debug(ex.getMessage());
        }
    }

    /**
     * Load the next URL in the playlist
     */
    public void loadNext() {
        pIndex = indexOracle.suggestIndex(true, true);
        try {
            _playOrLoadMedia(pIndex, false);
        } catch (PlayException ex) {
            _debug(ex.getMessage());
        }
    }

    /**
     * Returns the URL at the current playlist reference index
     * 
     * @return the URL at the current playlist index
     */
    public String getCurrentItem() {
        return urls.get(getPlaylistIndex()).getCurrentResource();
    }

    @Override
    public int getPlaylistSize() {
        return urls.size();
    }

    /**
     * Returns the current playlist index
     * 
     * @return the current playlist index
     */
    public int getPlaylistIndex() {
        int ind = indexOracle.getCurrentIndex();
        return ind < 0 ? pIndex : ind;
    }

    private void _playOrLoadMedia(int index, boolean play) throws PlayException {
        callback.load(urls.get(index).getNextResource(true));
        if (play) {
            callback.play();
        }
    }

    private void _debug(String msg) {
        if (useCache) {
            msgCache.add(msg);
        } else {
            callback.onDebug(msg);
        }
    }
    
    /**
     * Interface defines the methods required by the PlaylistManager to interact 
     * with a player widget
     * 
     * @author Sikirulai Braheem
     * @since 1.2
     */
    public static interface PlayerCallback {

        /**
         * Called by the playlist manager to start playback on the attached player
         * 
         * @throws PlayException if a playback error occurs. 
         */
        public void play() throws PlayException;

        /**
         * Called by the playlist manager to load the attached player with the
         * specified URL
         * 
         * @param url the media URL to load in the player
         */
        public void load(String url);

        /**
         * Called when the playlist manager needs to propagate a debug message.
         * 
         * @param message the message
         */
        public void onDebug(String message);
    }
}
