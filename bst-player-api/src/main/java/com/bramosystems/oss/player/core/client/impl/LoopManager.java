/*
 *  Copyright 2010 Sikiru.
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
package com.bramosystems.oss.player.core.client.impl;

import com.bramosystems.oss.player.core.client.PlayException;
import com.bramosystems.oss.player.core.client.RepeatMode;

/**
 * Provides programmatic loop count management for players without native support
 *
 * @author Sikiru
 * @since 1.2
 */
public class LoopManager {

    private int loopCount, _count;
    private LoopCallback callback;
    private RepeatMode repeatMode;

    /**
     *
     * @param callback the callback
     */
    public LoopManager(LoopCallback callback) {
        this.callback = callback;
        loopCount = 1;
        _count = loopCount;
        repeatMode = RepeatMode.REPEAT_OFF;
    }

    /**
     * Returns loop count
     *
     * @return loop count
     */
    public int getLoopCount() {
        return loopCount;
    }

    /**
     * Set loop count
     *
     * @param loopCount
     */
    public void setLoopCount(int loopCount) {
        this.loopCount = loopCount;
        _count = loopCount;
    }

    /**
     * notifies this manager that the player just finished current playback
     */
    public void notifyPlayFinished() {
        if (loopCount < 0) {
            repeatMode = RepeatMode.REPEAT_ALL;
        }

        switch (repeatMode) {
            case REPEAT_OFF: // one item playback finished, try another item ...
                playNextOrFinish();
                break;
            case REPEAT_ONE: // one item playback finished, play it again ...
                if (loopCount <= 1) {
                    callback.repeatPlay();
                } else {
                    _count--;
                    if (_count > 0) {
                        callback.repeatPlay();
                    } else {
                        _count = loopCount;
                        playNextOrFinish();
                    }
                }
                break;
            case REPEAT_ALL: // one item playback finished, try another and/or start over ...
                if (loopCount <= 1) {
                    playNextOrLoop();
                } else {
                    if (_count > 1) {
                        _count = playNextOrLoop() ? _count : _count - 1;
                    } else {
                        _count = playNextOrFinish() ? _count : loopCount;
                    }
                }
        }
    }

    public RepeatMode getRepeatMode() {
        return repeatMode;
    }

    public void setRepeatMode(RepeatMode repeatMode) {
        this.repeatMode = repeatMode;
    }

    /**
     *
     * @return true if next is played or false if loop is finished
     */
    private boolean playNextOrFinish() {
        try {
            callback.playNextItem();
            return true;
        } catch (PlayException ex) { // no more entries in list ....
            callback.onLoopFinished();
            return false;
        }
    }

    /**
     *
     * @return true if next is played or false otherwise if looping the list...
     */
    private boolean playNextOrLoop() {
        try {
            callback.playNextItem();
            return true;
        } catch (PlayException ex) {
            callback.playNextLoop();
            return false;
        }
    }

    public static interface LoopCallback {

        /**
         * all loop playback finished
         */
        public void onLoopFinished();

        /**
         * One loop finished, start another ...
         */
        public void playNextLoop();

        /**
         * Play next playlist item
         * @throws PlayException if playlist has no more entries
         */
        public void playNextItem() throws PlayException;

        /**
         * repeat playback of current item
         */
        public void repeatPlay();
    }
}
