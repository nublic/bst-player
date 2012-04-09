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
package com.bramosystems.oss.player.core.client.impl;

import com.bramosystems.oss.player.core.client.impl.playlist.VLCPlaylistManager;
import com.bramosystems.oss.player.core.client.*;
import com.bramosystems.oss.player.core.event.client.PlayStateEvent.State;
import com.google.gwt.user.client.Timer;

public class VLCStateManager {

    protected VLCStateCallback _callback;
    protected VLCPlayerImplCallback _impl;
    private PoollingStateManager stateMgr;
    private VLCPlaylistManager playlistMgr;

    public VLCStateManager() {
    }

    public final void init(VLCStateCallback callback, VLCPlayerImplCallback impl) {
        this._callback = callback;
        this._impl = impl;
        playlistMgr = new VLCPlaylistManager(_impl, _callback);
    }

    public void registerEventCallbacks() {
        stateMgr = new PoollingStateManager();
        stateMgr.start();
        playlistMgr.flushMessageCache();
    }

    public void close() {
        stateMgr.stop();
    }

    public VLCPlaylistManager getPlaylistManager() {
        return playlistMgr;
    }

    protected class PoollingStateManager {

        private final int POOL_RATE = 300;
        private int _previousState, _metaDataWaitCount, _previousIndex;
        private boolean _isBuffering;
        private Timer _timer;

        public PoollingStateManager() {
            _previousState = -20;
            _previousIndex = -1;
            _timer = new Timer() {

                @Override
                public void run() {
                    checkState();
                }
            };
        }

        public void start() {
            _timer.scheduleRepeating(POOL_RATE);
        }

        public void stop() {
            _timer.cancel();
        }

        private void checkState() {
            int state = _impl.getImpl().getPlayerState();
            int _index = playlistMgr.getPlaylistIndex();

            if (state == _previousState) {
                if ((_metaDataWaitCount > 0) && (--_metaDataWaitCount <= 0)) {
                    MediaInfo info = new MediaInfo();
                    _impl.getImpl().fillMediaInfo(info);
                    _callback.onMediaInfo(info);
                }
                return;
            }

            switch (state) {
                case -1:   // no input yet...
                    break;
                case 0:    // idle/close
                    _callback.onIdle();
                    break;
                case 1:    // opening media
                    _callback.onOpening();
                    break;
                case 2:    // buffering
                    _isBuffering = true;
                    _callback.onBuffering(true);
                    break;
                case 3:    // playing
                    if (_isBuffering) {
                        _isBuffering = false;
                        _callback.onBuffering(false);
                    }

                    if (_index != _previousIndex) {
                        _metaDataWaitCount = 4;  // implement some kind of delay, no extra timers...
                    }
                    playlistMgr.setCurrentState(State.Started);
                    playlistMgr.setStoppedByUser(false);
                    _callback.onPlaying();
                    //                    loadingComplete();
                    break;
                case 4:    // paused
                    playlistMgr.setCurrentState(State.Paused);
                    _callback.onPaused();
                    break;
                case 5:    // stopping
                case 6:    // finished
                    if (playlistMgr.isStoppedByUser()) {
                        playlistMgr.setCurrentState(State.Stopped);
                        _callback.onStopped();
                    } else {
                        playlistMgr.setCurrentState(State.Finished);
                        _callback.onEndReached();
                    }
                    break;
                case 7:    // error
                    break;
                default:    // unknown state ...
                    _callback.onInfo("[unknown state] " + state);
            }
            _previousState = state;
            _previousIndex = _index;
        }
    }

    public static interface VLCStateCallback {

        public void onIdle();

        public void onOpening();

        public void onBuffering(boolean started);

        public void onPlaying();

        public void onPaused();

        public void onEndReached();

        public void onStopped();

        public void onError(String message);

        public void onInfo(String message);

//        public void onPositionChanged();
//        public void onTimeChanged();
//        public void onMouseGrabed(double x, double y);
//        public void onForward();
//        public void onBackward();
        public void onMediaInfo(MediaInfo info);
    }

    public static interface VLCPlayerImplCallback {

        public VLCPlayerImpl getImpl();
    }
}
