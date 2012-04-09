/*
 *  Copyright 2010 Sikiru Braheem.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package com.bramosystems.oss.player.core.client.impl.playlist;

import com.bramosystems.oss.player.core.client.playlist.PlaylistManager;
import com.bramosystems.oss.player.core.client.PlayException;
import com.bramosystems.oss.player.core.client.impl.VLCStateManager.VLCPlayerImplCallback;
import com.bramosystems.oss.player.core.client.impl.VLCStateManager.VLCStateCallback;
import com.bramosystems.oss.player.core.event.client.PlayStateEvent;
import com.bramosystems.oss.player.core.event.client.PlayStateEvent.State;

/**
 *
 * @author Sikiru
 */
public class VLCPlaylistManager extends PlaylistManager {

    private VLCStateCallback _callback;
    private VLCPlayerImplCallback _impl;
    private boolean stoppedByUser;
    private PlayStateEvent.State _currentState;
    private int _vlcItemIndex;

    public VLCPlaylistManager(VLCPlayerImplCallback impl, VLCStateCallback callback) {
        _impl = impl;
        _callback = callback;
        _currentState = PlayStateEvent.State.Stopped;
    }

    @Override
    protected PlayerCallback initCallback() {
        return new PlayerCallback() {

            @Override
            public void play() throws PlayException {
                _impl.getImpl().playMediaAt(_vlcItemIndex);
            }

            @Override
            public void load(String url) {
                _impl.getImpl().clearPlaylist();
                _vlcItemIndex = _impl.getImpl().addToPlaylist(url);
            }

            @Override
            public void onDebug(String message) {
                _callback.onInfo(message);
            }
        };
    }

    @Override
    public void flushMessageCache() {
        super.flushMessageCache();
    }

    public void setCurrentState(State currentState) {
        _currentState = currentState;
    }

    public void setStoppedByUser(boolean stoppedByUser) {
        this.stoppedByUser = stoppedByUser;
    }

    public boolean isStoppedByUser() {
        return stoppedByUser;
    }

    public void play() throws PlayException {
        switch (_currentState) {
            case Paused:
                _impl.getImpl().togglePause();
                break;
            case Finished:
                playNext(true);
                break;
            case Stopped:
                play(getPlaylistIndex());
        }
    }

    public void stop() {
        stoppedByUser = true;
        _impl.getImpl().stop();
    }
    /*
    public void addToPlaylist(String mediaUrl, String options) {
    _indexCache.add(options == null ? _impl.addToPlaylist(mediaUrl)
    : _impl.addToPlaylist(mediaUrl, options));
    _callback.onInfo("Added '" + mediaUrl + "' to playlist"
    + (options == null ? "" : " with options [" + options + "]"));
    _indexOracle.incrementIndexSize();
    }
     */
}
