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

import com.bramosystems.oss.player.core.client.ui.WinMediaPlayer;
import com.bramosystems.oss.player.core.event.client.PlayerStateEvent;
import com.bramosystems.oss.player.core.event.client.LoadingProgressEvent;
import com.google.gwt.user.client.Timer;

/**
 * Webkit browser specific implementation of the WMPStateManager class. It is not recommended to
 * interact with this class directly.
 *
 * @author Sikirulai Braheem
 */
public class WMPStateManagerWebkit extends WMPStateManager {

    WMPStateManagerWebkit() {
    }

//    @Override
    public void init(WinMediaPlayerImpl player, WinMediaPlayer handler, boolean resizing) {
//        cache.put(player.getPlayerId(), new PoolingStateManager(player, handler, resizing));
    }

    @Override
    public void close(String playerId) {
//        ((PoolingStateManager) cache.get(playerId)).close();
        cache.remove(playerId);
    }

    @Override
    protected void initGlobalEventListeners(WMPStateManager impl) {
        // do nothing, provided for Global event registration in firefox browsers.
    }

    @Override
    public void registerMediaStateHandlers(WinMediaPlayerImpl player) {
        // do nothing, provided for DOM event registration in IE.
    }

    @Override
    public void stop(String playerId) {
//        ((PoolingStateManager) cache.get(playerId)).stoppedByUser = true;
    }

    /*
     * Override generic implementation to pool for WMP play state instead,
     * player not generating events as expected.
     *
    protected class PoolingStateManager extends EventProcessor {

        private Timer playStateTimer;
        private int previousState,  stateTimerPeriod = 200;
        private Timer downloadProgressTimer;
        private boolean stoppedByUser,  isBuffering;


        public PoolingStateManager(final WinMediaPlayerImpl _player,
                WinMediaPlayer _handlers, boolean _resizing) {
            super(_handlers);
            previousState = -9;
            stoppedByUser = false;
            downloadProgressTimer = new Timer() {

                @Override
                public void run() {
                    double prog = player.getDownloadProgress();
                    if (prog == 1.0) {
                        cancel();
                        debug("Media loading complete");
                    }
                    LoadingProgressEvent.fire(handlers, prog);
                }
            };

            playStateTimer = new Timer() {

                @Override
                public void run() {
                    checkPlayState();
                }
            };
            playStateTimer.scheduleRepeating(stateTimerPeriod);
        }

        @Override
        public void processPlayState(int state) {
            if (state == previousState) {
                return;
            }

            switch (state) {
                case 8:     // media ended...
                    // ensure this is not fired more than once, WMP states may be
                    // out of order. Set [stoppedByUser] to false so event can be
                    // fired in next case (just a precaution :-) ...
                    stoppedByUser = false;
                case 1:     // stopped ...
                    if (!stoppedByUser) {  // just in case, state 8 may not be captured...
                        super.processPlayState(8);  // media ended...
                    }
                    super.processPlayState(state);  // media stopped ...
                    break;
                case 3:    // playing..
                    if (isBuffering) {
                        fireBuffering(false);
                    }
                    stoppedByUser = false;
                    super.processPlayState(state);
                    break;
                case 9:     // preparing new item ...
//                    canDoMetadata = true;
                case 6:    // buffering ...
                case 7:     // waiting ...
                case 11:    // reconnecting to stream  ...
                    downloadProgressTimer.scheduleRepeating(800);
                    fireBuffering(true);
                    break;
                default:
                    super.processPlayState(state);
            }
            previousState = state;
        }

        private void fireBuffering(boolean buffering) {
            isBuffering = buffering;
            debug("Buffering " + (buffering ? " started" : " stopped"));
            PlayerStateEvent.fire(handlers,
                    buffering ? PlayerStateEvent.State.BufferingStarted : PlayerStateEvent.State.BufferingFinished);

        }

        public void close() {
            playStateTimer.cancel();
        }
    }

    */
}
