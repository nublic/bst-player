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
import com.bramosystems.oss.player.core.event.client.DebugEvent;
import com.bramosystems.oss.player.core.event.client.PlayerStateEvent;
import com.bramosystems.oss.player.core.event.client.PlayerStateHandler;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

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
    private boolean useCache;

    private int _index;
    private boolean shuffleOn;
    private ArrayList<Integer> nublicShuffleIndices;
    private int nublicShufflePosition;
    private boolean repeat;

    /**
     * Creates the PlaylistManager object.
     */
    public PlaylistManager() {
        urls = new Playlist();
        msgCache = new ArrayList<String>();
        callback = initCallback();
        useCache = false;

        _index = -1;
        shuffleOn = false;
        nublicShuffleIndices = new ArrayList<Integer>();
        nublicShufflePosition = -1;
        repeat = false;
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

    private <A> ArrayList<A> shuffle(List<A> source) {
        // Initialize copy of source
        ArrayList<A> perm = new ArrayList();
        perm.addAll(source);
        // Implement Fisher-Yates shuffle
        // From http://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle
        Random r = new Random();
        for (int i = 1; i < perm.size(); i++) {
            int j = r.nextInt(i + 1);
            perm.set(i, perm.get(j));
            perm.set(j, source.get(i));
        }
        return perm;
    }
    
    @Override
    public boolean isShuffleEnabled() {
        return this.shuffleOn;
    }

    @Override
    public void setShuffleEnabled(boolean enable) {
        if (enable != this.shuffleOn) {
            this.shuffleOn = enable;

            if (shuffleOn) {
                nublicShuffleIndices = shuffle(nublicShuffleIndices);
                if (_index != -1) {
                    int i = nublicShuffleIndices.indexOf(_index);
                    if (i != -1) {
                        nublicShuffleIndices.remove(i);
                        nublicShuffleIndices.add(0, _index);
                    }
                    nublicShufflePosition = 0;                    
                }
            }
        }
    }

    public boolean isRepeatEnabled() {
        return this.repeat;
    }

    public void setRepeatEnabled(boolean repeat) {
        this.repeat = repeat;
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
        _debug("Added to playlist - '" + mediaLocator + "'");

        // NUBLIC SHUFFLING
        int newIndex;
        Random r = new Random();
        if (shuffleOn) {
            newIndex = nublicShufflePosition + 1 + r.nextInt(nublicShuffleIndices.size() - nublicShufflePosition);
        } else {
            newIndex = r.nextInt(nublicShuffleIndices.size() + 1);
        }
        nublicShuffleIndices.add(newIndex, nublicShuffleIndices.size());
    }
    
    @Override
    public void insertIntoPlaylist(int index, String mediaURL) {
    	insertIntoPlaylist(index, new MRL(mediaURL));
    }

    @Override
    public void insertIntoPlaylist(int index, String... mediaURLs) {
    	insertIntoPlaylist(index, new MRL(mediaURLs));
    }
    
    @Override
    public void insertIntoPlaylist(int index, MRL mediaLocator) {
        urls.add(index, mediaLocator);
        _debug("Added to playlist - '" + mediaLocator + "'");

        // NUBLIC SHUFFLING
        int newIndex;
        Random r = new Random();
        if (shuffleOn) {
            newIndex = nublicShufflePosition + 1 + r.nextInt(nublicShuffleIndices.size() - nublicShufflePosition);
        } else {
            newIndex = r.nextInt(nublicShuffleIndices.size() + 1);
        }
        nublicShuffleIndices.add(newIndex, nublicShuffleIndices.size());
    }
    
    @Override
    public void reorderPlaylist(int from, int to) {
    	if (from != to) {
            // Update playing index
            if (_index == from) {
                _index = to > from ? to - 1 : to;
            } else if (from > to && _index >= to && _index < from) {  // to, ..., from
                _index++;
            } else if (from < to && _index > from && _index < to) {  // from, ..., to
                _index--;
            }
            // Save the element and remove it
            MRL toMove = urls.get(from);
            urls.add(to, toMove);
            if (from > to) { // The element was later in the list
            	urls.remove(from + 1);
            } else if (from < to) { // The element was before in the list
            	urls.remove(from);
            }
        }
    }

    @Override
    public void removeFromPlaylist(int index) {
        _debug("Removed from playlist - '" + urls.remove(index) + "'");

        if (_index >= index) {
            _index--;
        }

        int greatest = urls.size() - 1;
        ArrayList<Integer> newIndices = new ArrayList<Integer>();
        for (int item : nublicShuffleIndices) {
            if (item < greatest) {
                newIndices.add(item);
            }
        }
        nublicShuffleIndices = newIndices;
        nublicShufflePosition--;
    }

    @Override
    public void clearPlaylist() {
        urls.clear();
        _index = -1;

        nublicShuffleIndices.clear();
        nublicShufflePosition = -1;
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
        if (!computeIndex(true, force)) {
            _playOrLoadMedia(_index, true);
        } else {
            throw new PlayException("End of playlist");
        }
    }

    /**
     * Plays the previous item in the playlist
     * 
     * @param force <code>true</code> if a start-of-playlist should roll over to the end, {@code false} otherwise.
     * @throws PlayException if a start-of-playlist is reached.  Only thrown if {@code force} is false
     */
     public void playPrevious(boolean force) throws PlayException {
         if (!computeIndex(true, force)) {
             _playOrLoadMedia(_index, true);
         } else {
             throw new PlayException("End of playlist");
         }
    }

    @Override
    public void play(int index) throws IndexOutOfBoundsException {
        try {
            if (shuffleOn) {
                nublicShuffleIndices = shuffle(nublicShuffleIndices);
                if (index != -1) {
                    int i = nublicShuffleIndices.indexOf(index);
                    if (i != -1) {
                        nublicShuffleIndices.remove(i);
                        nublicShuffleIndices.add(0, index);
                    }
                    nublicShufflePosition = 0;                    
                }
            }
            _index = index;
            nublicShufflePosition = nublicShuffleIndices.indexOf(index);
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
            callback.load(urls.get(_index).getNextResource(false));
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
            _index = index;
            nublicShufflePosition = nublicShuffleIndices.indexOf(index);
            _playOrLoadMedia(index, false);
        } catch (PlayException ex) {
            _debug(ex.getMessage());
        }
    }

    /**
     * Load the next URL in the playlist
     */
    public void loadNext() {
        try {
            if (!computeIndex(true, true)) {
                _playOrLoadMedia(_index, false);
            } else {
                _debug("End of playlist");
            }
        } catch (PlayException ex) {
            _debug(ex.getMessage());
        }
    }

    public String getItem(int index) {
        return urls.get(index).getCurrentResource();
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
        return _index;
    }

    public void setPlaylistIndex(int index) {
        this._index = index;

        nublicShufflePosition = nublicShuffleIndices.indexOf(index);
    }

    public int getShuffleIndex() {
        return this.nublicShufflePosition;
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

    private boolean computeIndex(boolean up, boolean force) {
        return _suggestIndex(up, repeat || force);
    }

    private boolean _suggestIndex(boolean up, boolean canRepeat) {
        int size = urls.size();
        if (!shuffleOn) {
            if (_index < 0 && canRepeat) {  // prepare for another iteration ...
                _index = up ? 0 : size;
            } else {
                _index = up ? ++_index : --_index;
            }
            
            if (_index == size) {
                _index = -1;
            }
            
            if (_index < 0 && canRepeat) {  // prepare for another iteration ...
                _index = up ? 0 : size;
            }
            
            if (_index >= 0) { // keep the used index ...
                return false; // valid index
            }
            
            return true;  // end of list
        } else {
            if (up) {
                nublicShufflePosition++;
                if (nublicShufflePosition >= size) {
                    if (canRepeat) {
                        nublicShuffleIndices = shuffle(nublicShuffleIndices);
                        nublicShufflePosition = 0;
                    } else {
                        nublicShufflePosition = size;
                    }
                } 
            } else {
                nublicShufflePosition--;
                if (nublicShufflePosition < 0) {
                    if (canRepeat) {
                        nublicShufflePosition = size - 1;
                    } else {
                        nublicShufflePosition = -1;
                    }
                }
            }
            
            if (nublicShufflePosition >= size) {
                nublicShufflePosition = size - 1;
                _index = nublicShuffleIndices.get(nublicShufflePosition);
                return true;
            } else if (nublicShufflePosition < 0) {
                nublicShufflePosition = 0;
                _index = nublicShuffleIndices.get(nublicShufflePosition);
                return true;
            } else {
                _index = nublicShuffleIndices.get(nublicShufflePosition);
                return false;
            }
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
